package flat.map.that


object FMTS extends App {
  def flattenIt(m: Map[String, Any]): Seq[Seq[String]] = m.flatMap {
    case (k, v: Map[String, Any]) => flattenIt(v).map(k +: _)
    case (k, v) => Seq(Seq(k, v.toString))
  }.toSeq

  def toCSV(m: Seq[Seq[String]]): String = m.map(_.mkString(",")).mkString("\n")

  println(toCSV(flattenIt(Map("a"->1, "b"->Map("c"->3, "d"->4), "e"->5))))
}
