package flat.map.that

object FMTS extends App {
  def flattenIt(m: Map[String, Any]): Seq[Seq[String]] = m.flatMap {
    case (k, v: Map[String, Any]) => flattenIt(v).map(k +: _)
    case (k, v) => Seq(Seq(k, v.toString))
  }.toSeq

  def toCSV(m: Seq[Seq[String]]): String = m.map(_.mkString(",")).mkString("\n")
  val m = Map("a" -> Map("x" -> 1, "y" -> Map("m" -> 32.2, "n" -> 33)), "b" -> 2 , "c" -> 3)
  println(toCSV(flattenIt(m)))

}
