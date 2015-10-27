package planstack.constraints.experimental

import planstack.constraints.experimental.DCMorris.Proj

import scala.collection.mutable.{HashMap => MMap}
import scala.collection.mutable.{ArrayBuffer => Buff}
import scala.collection.mutable.{PriorityQueue => Queue}
import scala.collection.mutable.{Set => MSet}

object DCMorris {
  type Node = Int

  sealed abstract class Proj(val n: Node)
  case class MaxProj(override val n: Node) extends Proj(n)
  case class MinProj(override val n: Node) extends Proj(n)

  sealed abstract class Edge(val from: Node, val to: Node, val d: Int, val proj:Set[Proj])
  case class Req(override val from: Node, override val to: Node, override val d: Int, override val proj:Set[Proj])
    extends Edge(from, to, d, proj)
  case class Upper(override val from: Node, override val to: Node, override val d: Int, lbl: Node, override val proj:Set[Proj]) extends Edge(from, to, d, proj)
  case class Lower(override val from: Node, override val to: Node, override val d: Int, lbl: Node, override val proj:Set[Proj]) extends Edge(from, to, d, proj)

}

class NotDC(val involvedProjections: Set[Proj]) extends Exception

class DCMorris {
  import DCMorris._
  val infty = 999999999



  val edges = Buff[Edge]()
  val inEdges = MMap[Node, Buff[Edge]]()
  val outEdges = MMap[Node, Buff[Edge]]()

  private def ensureSpaceForNode(n: Node): Unit = {
    if(!inEdges.contains(n))
      inEdges += ((n, Buff()))
    if(!outEdges.contains(n))
      outEdges += ((n, Buff()))
  }

  def addEdge(e: Edge): Unit = {
    ensureSpaceForNode(e.from)
    ensureSpaceForNode(e.to)
    if (!dominated(e)) {
      edges += e
      inEdges(e.to) += e
      outEdges(e.from) += e
      println("    adding edge: " + e)
    }
  }

  private def dominated(e:Edge): Boolean = {
    e.isInstanceOf[Req] && outEdges(e.from)
      .filter(e2 => e2.to == e.to && e.isInstanceOf[Req])
      .exists(e2 => e2.d <= e.d)
  }

  private def extractNeedObs(projs: Set[Proj]) : Set[Node] = {
    val maxProjs = projs.filter(p => p.isInstanceOf[MaxProj]).map(p => p.n)
    projs.filter(p => p.isInstanceOf[MinProj] && maxProjs.contains(p.n)).map(p => p.n)
  }

  def determineDC(): (Boolean, Option[Set[Node]]) = {
    try {
      val propagated = Buff[Node]()
      for (n <- nodes; if isNegative(n))
        dcBackprop(n, propagated, Nil)
    } catch {
      case e:NotDC => return (false, Some(extractNeedObs(e.involvedProjections)))
    }
    (true, None)
  }

  @throws[NotDC]
  def dcBackprop(source: Node, propagated: Buff[Node], callHistory: List[Node]): Unit = {
    println("current: "+source)
    val negReq = inEdges(source).filter(e => e.isInstanceOf[Req] && e.d < 0).map(_.asInstanceOf[Req]).toList
    dcBackprop(source, propagated, callHistory, Right(negReq))
    for(e <- inEdges(source) if e.d < 0 && e.isInstanceOf[Upper])
      dcBackprop(source, propagated, callHistory, Left(e.asInstanceOf[Upper]))
    propagated += source
    println("end: "+source)
  }

  @throws[NotDC]
  def dcBackprop(source: Node, propagated: Buff[Node], callHistory: List[Node], curEdges: Either[Upper, List[Req]]) : Unit = {
    case class QueueElem(n: Node, dist: Int, projs: Set[Proj])
    def suitable(e: Edge) = (curEdges, e) match  {
      case (Left(Upper(_, _, _, upLbl, _)) ,Lower(_, _, _, downLbl, _)) if upLbl == downLbl => false
      case _ => true
    }

    val visited = MSet[Node]()
    assert(!callHistory.contains(source), "This should have been caught earlier")

    if(propagated contains source)
      return ;
    val queue = Queue[QueueElem]()(Ordering.by(- _.dist))

    val toProp = curEdges match {
      case Left(up) => List(up)
      case Right(reqs) => reqs
    }
    for(e <- toProp)
      queue += QueueElem(e.from, e.d, e.proj)

    while(queue.nonEmpty) {
      val QueueElem(cur, dist, projs) = queue.dequeue()
      println(s"  dequeue: $cur ($dist) $projs")
      if(!visited.contains(cur)) {
        visited += cur

        if(dist >= 0) {
          addEdge(new Req(cur, source, dist, projs))
        } else {
          if(isNegative(cur)) {
            if(source == cur || callHistory.contains(cur))
              throw new NotDC(projs)
            dcBackprop(cur, propagated, source :: callHistory)
          }
          for(e <- inEdges(cur) ; if e.d >= 0 && suitable(e))
            queue += QueueElem(e.from, dist + e.d, projs ++ e.proj)
        }
      }
    }
  }




  private def nodes = inEdges.keys
  private def isNegative(n: Node) : Boolean = inEdges(n).exists(e => e.d < 0)



}

object DCMorrisTest extends App {
  import DCMorris._
  val A = 1
  val B = 2
  val C = 3
  val D = 4
  val E: Node = 5
  val F = 6

  val notDCs = List(
    List(Req(A,B,5, Set()), Req(B,A,-6, Set())),
    List(Req(C, B, 3, Set()), Req(B, C, -1, Set()), Upper(B,A,-5,B, Set()), Lower(A,B,1,B, Set())),
    List(Req(C, B, 3, Set()), Req(B, C, -1, Set()), Upper(B,A,-5,B, Set()), Lower(A,B,1,B, Set()))
  )

  val DCs = List(
    List(Req(B,C, 5, Set()), Req(C,B, -5, Set()), Upper(B,A,-5,B, Set()), Lower(A,B,1,B, Set()))
  )

  def assertNotDC(edges: List[Edge]): Unit = {
    val stnu = new DCMorris()
    for(e <- edges)
      stnu.addEdge(e)
    val (dc,obs) = stnu.determineDC()
    assert(!dc)
    println(s"You should observe at least one of events: $obs")
  }
  def assertDC(edges: List[Edge]): Unit = {
    val stnu = new DCMorris()
    for(e <- edges)
      stnu.addEdge(e)
    val (dc,_) = stnu.determineDC()
    assert(dc)
  }
//  for(notDC <- notDCs) {
//    assertNotDC(notDC)
//  }
//  for(dc <- DCs) {
//    assertDC(dc)
//  }


//  val stnu = new DCMorris()
//
//  val edges = List(
//    Req(E,B,4),
//    Upper(B, A, -2, B),
//    Lower(A, B, 0, B),
//    Req(B, D, 1),
//    Upper(D, C, -3, D),
//    Lower(C, D, 0, D),
//    Req(D, B, 3),
//    Upper(B, A, -2, B),
//    Lower(A, B, 0, B),
//    Req(B, E, -2)
//  )
//  for(e <- edges)
//    stnu.addEdge(e)
//
//  println()
//
//  val props = Buff[Node]()
//  try {
//    stnu.dcBackprop(E, props, Nil)
//    println("DC")
//  } catch {
//    case e:NotDC => println("Not DC")
//  }


  def makeNonObservable(edges: List[Edge], nodes: List[Node]) : List[Edge] = nodes match {
    case Nil => edges
    case h::tail => makeNonObservable(makeNonObservable(edges, h), tail)
  }

  def makeNonObservable(edges: List[Edge], node: Node) : List[Edge] = {
    val upper = edges.find(e => e.isInstanceOf[Upper] && e.asInstanceOf[Upper].lbl == node).get
    val lower = edges.find(e => e.isInstanceOf[Lower] && e.asInstanceOf[Lower].lbl == node).get

    val l = lower.d
    val lowerProjs = lower.proj + MinProj(node)
    val u = upper.d
    val upperProjs = upper.proj + MaxProj(node)
    val src = lower.from

    edges.filter(e => e != upper && e != lower).map {
      case Req(`node`,y,d, projs) => Req(src, y, l+d, projs ++ lowerProjs)
      case Req(x,`node`,d, projs) => Req(x, src, d+u, projs ++ upperProjs)
      case Lower(`node`, y, d, lbl, projs) => Lower(src, y, l+d, lbl, projs ++ lowerProjs)
      case Upper(x, `node`, d, lbl, projs) => Upper(x, src, u+d, lbl, projs ++ upperProjs)
      case e => assert(e.from != node && e.to != node); e
    }
  }

  def getMinimalObservationSets(edges: List[Edge]) : Iterable[Set[Node]] = {
    val ctgs = edges.filter(_.isInstanceOf[Upper]).map(_.asInstanceOf[Upper].lbl).toSet
    val solutions = MSet[Set[Node]]()
    var queue = MSet[Set[Node]]()
    queue += Set()

    while(queue.nonEmpty) {
      val candidate = queue.head
      queue -= candidate

      val nonObs = ctgs -- candidate
      val allObsEdges = makeNonObservable(edges, nonObs.toList)

      val stnu = new DCMorris()
      for(e <- allObsEdges)
        stnu.addEdge(e)

      println(s"Candidate: $candidate")
      stnu.determineDC() match {
        case (true, None) =>
          println("  Valid")
          solutions.add(candidate)
        case (false, Some(possiblyObservable)) =>
          for(n <- possiblyObservable) {
            println(s"new: ${candidate+n}")
            queue += candidate + n
          }
      }
    }
    val minSols = solutions.filterNot(s => solutions.exists(s2 => s2 != s && s2.subsetOf(s)))
    println(s"Solutions: $minSols, all: $solutions")

    minSols
  }

  val ex1 = List(
    cont(A,B,0,5), reqs(B,C,20,25), reqs(B,D,20,25), cont(F,E,0,5), reqs(E,C,1,6), reqs(E,D,0,5)
  ).flatten
  val ex2 = List(
    cont(A,B,0,2), cont(B,C,0,2), reqs(C,D,0,2)
  ).flatten

//  println("\nObserve example:")
//  assertDC(ex1)

//  println("\nTwo non-obs:")
//  assertNotDC(makeNonObservable(ex1, List(B,E)))

//  println("\nOne non-obs:")
//  assertDC(makeNonObservable(ex1, List(E)))
//  assertNotDC(makeNonObservable(ex1, List(B)))
//
//  getMinimalObservationSets(ex1)

  getMinimalObservationSets(ex2)

  def cont(src:Node, dst: Node, minDur:Int, maxDur:Int) = {
    List(Upper(dst, src, -maxDur, dst, Set()), Lower(src, dst, minDur, dst, Set()))
  }
  def reqs(src:Node, dst:Node, min:Int, max:Int) = {
    List(Req(src, dst, max, Set()), Req(dst, src, -min, Set()))
  }
}