package planstack.graph.core

class Edge[+V](val u:V, val v:V) {

  override def toString = "(%s, %s)".format(u, v)
}
