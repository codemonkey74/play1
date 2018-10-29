package flat.map.that


object FMTS1 extends App {
  def flattenIt(m: Map[String, Any]): Seq[Seq[String]] = m.flatMap {
    case (k, v: Map[String, Any]) => flattenIt(v).map(k +: _)
    case (k, v) => Seq(Seq(k, v.toString))
  }.toSeq

  def toCSV(m: Seq[Seq[String]]): String = m.map(_.mkString(",")).mkString("\n")

  println(toCSV(flattenIt(Map("a"->1, "b"->Map("c"->3, "d"->4), "e"->5))))
}

object FMTS2 extends App {
  sealed trait Value

  type multiValue = Map[String, Value]

  case class Leaf(i: Int) extends Value
  case class Branch(m: multiValue) extends Value

  def flattenIt(x: Value): Seq[Seq[String]] = x match {
    case Leaf(v) => Seq(Seq(v.toString))
    case Branch(m) => m.foldLeft(Seq.empty[Seq[String]]){
      case (p, (k, v)) => p ++ flattenIt(v).map(k +: _)
    }
  }

  def toCSVFormat(m: Seq[Seq[String]]): String = m.map(_.mkString(",")).mkString("\n")


  println(toCSVFormat(flattenIt(Branch(Map( "a" -> Leaf(1), "b" -> Branch(Map("c" -> Leaf(3), "d" -> Leaf(4))), "e" -> Leaf(5))))))
}

object FMTS3 extends App {
  sealed trait Value { def key: String }

  case class Leaf(key: String, v: Int) extends Value
  case class NonLeaf(key: String, v: Seq[Value]) extends Value
  case class Root(v: Seq[Value]) extends Value { override val key: String = ""}

  def flattenIt(m: Value): Seq[Seq[String]] = m match {
    case NonLeaf(k, vs) => vs.flatMap(flattenIt).map(k +: _)
    case Root(vs) => vs.flatMap(flattenIt)
    case Leaf(k, v) => Seq(Seq(k, v.toString))
  }

  def toCSVFormat(m: Seq[Seq[String]]): String = m.map(_.mkString(",")).mkString("\n")


  println(toCSVFormat(flattenIt(Root(Seq(Leaf("a", 1), NonLeaf("b", Seq(Leaf("c",3), Leaf("d", 4))), Leaf("e", 5))))))

}

object FMTS4 extends App {
  sealed trait Value { def key: String }

  case class Leaf(key: String, v: Int) extends Value
  case class NonLeaf(key: String, v: Seq[Value]) extends Value
  case class Root(v: Seq[Value]) extends Value { override val key: String = ""}

  def flattenIt(m: Value, keypath: Seq[String] = Seq.empty[String]): Seq[Seq[String]] = m match {
    case NonLeaf(k, vs) => vs.flatMap(v => flattenIt(v, keypath :+ k))
    case Root(vs) => vs.flatMap(v => flattenIt(v))
    case Leaf(k, v) => Seq(keypath ++ Seq(k, v.toString))
  }

  def toCSVFormat(m: Seq[Seq[String]]): String = m.map(_.mkString(",")).mkString("\n")


  println(toCSVFormat(flattenIt(Root(Seq(Leaf("a", 1), NonLeaf("b", Seq(Leaf("c",3), Leaf("d", 4))), Leaf("e", 5))))))

}


object FMTS5 extends App {
  sealed trait Value {
    def key: String
    def asCSV: Seq[Seq[String]]
  }

  case class Leaf(key: String, v: Int) extends Value {

    override def asCSV: Seq[Seq[String]] = Seq(Seq(key, v.toString))
  }
  case class NonLeaf(key: String, v: Seq[Value]) extends Value {

    override def asCSV: Seq[Seq[String]] = v.flatMap(i => i.asCSV.map(key +: _))
  }

  type Root = NonLeaf
  def root(v: Seq[Value]): Root = new NonLeaf("", v) {
    override def asCSV: Seq[Seq[String]] = v.flatMap(i => i.asCSV)
  }

  def flattenIt(m: Root): Seq[Seq[String]] = m.asCSV

  def toCSVFormat(m: Seq[Seq[String]]): String = m.map(_.mkString(",")).mkString("\n")


  println(toCSVFormat(flattenIt(root(Seq(Leaf("a", 1), NonLeaf("b", Seq(Leaf("c",3), Leaf("d", 4))), Leaf("e", 5))))))

}


object FMTS6 extends App {
  sealed trait Json

  case class Leaf(v: Int) extends Json

  case class NonLeaf(v: Seq[(String, Json)]) extends Json

  @inline def appendRec(prefix: Seq[String], key: String): Seq[String] = prefix :+ key


  def flattenIt(m: Json, prefix: Seq[String] = Seq.empty[String]): Seq[Seq[String]] = m match {
    case Leaf(v) => Seq(appendRec(prefix, v.toString))
    case NonLeaf(s) => s.flatMap {
      case (k, j) => flattenIt(j, appendRec(prefix, k))
    }
  }

  def toCSVFormat(m: Seq[Seq[String]]): String = m.map(_.mkString(",")).mkString("\n")


  println(toCSVFormat(flattenIt(
    NonLeaf(Seq(
      "x" -> NonLeaf(Seq(
        "a" -> Leaf(2),
        "b" -> NonLeaf(Seq(
          "d" -> Leaf(3),
          "e" -> Leaf(4)
        )),
        "c" -> Leaf(2)
      )),
      "y" -> Leaf(1)
    ))))
  )

}
