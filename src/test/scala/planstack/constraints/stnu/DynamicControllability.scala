package planstack.constraints.stnu

import org.scalatest.FunSuite
import planstack.graph.printers.NodeEdgePrinter
import planstack.graph.core.LabeledEdge

class DynamicControllability extends FunSuite {

  for(idc <- List[ISTNU](new FastIDC)) {

    // example from `Incremental Dynamic Controllability Revisited` fig. 2
    test("DC violation, cycle of negative edges: "+idc.getClass.getName) {
      val A = idc.addVar()
      val B = idc.addVar()
      val C = idc.addVar()
      val D = idc.addVar()
      val E = idc.addVar()
      val U = idc.addVar()

      idc.enforceInterval(A, B, 5, 10)
      idc.enforceInterval(B, C, 5, 10)
      idc.enforceInterval(C, D, 5, 10)
      idc.enforceInterval(E, A, 5, 7)

      idc.addContingent(A, U, 50)
      idc.addContingent(U, A, -5)

      assert(idc.consistent)

      idc.addConstraint(U, D, -15)

      assert(!idc.consistent)
    }


  }

  for(idc <- List[ISTNU](new FastIDC)) {

    // example from `Incremental Dynamic Controllability Revisited` fig. 2
    test("DC on the cooking dinner example: "+idc.getClass.getName) {
      val WifeStore = idc.addVar()
      val StartDriving = idc.addVar()
      val WifeHome = idc.addVar()
      val StartCooking = idc.addVar()
      val DinnerReady = idc.addVar()

      val printer = new NodeEdgePrinter[Int, STNULabel, LabeledEdge[Int,STNULabel]] {
        override def printNode(n:Int) = n match {
          case WifeStore => "Wife Store"
          case StartDriving => "Start Driving"
          case WifeHome => "Wife Home"
          case StartCooking => "Start Cooking"
          case DinnerReady => "Dinner Ready"
          case 0 => "start"
          case 1 => "end"
          case _ => n.toString
        }
        override def printEdge(el : STNULabel) = {
          if(el.cond) "<%s, %d>".format(printNode(el.node), el.value)
          else el.toString
        }
        override def excludeNode(n:Int) = n == 1 || n == 0
      }

      idc.addContingent(WifeStore, StartDriving, 60)
      idc.addContingent(StartDriving, WifeStore, -30)

      idc.addContingent(StartDriving, WifeHome, 40)
      idc.addContingent(WifeHome, StartDriving, -35)

      idc.addRequirement(WifeHome, DinnerReady, 5)
      idc.addRequirement(DinnerReady, WifeHome, 5)

      idc.addContingent(StartCooking, DinnerReady, 30)
      idc.addContingent(DinnerReady, StartCooking, -25)

      assert(idc.consistent)

      // make sure the cooking starts at the right time
      assert(idc.hasRequirement(StartDriving, StartCooking, 10))
      assert(idc.hasRequirement(StartCooking, StartDriving, -10))
    }
  }

}
