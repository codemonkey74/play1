package flat.map.that

object FMTS extends App {
  trait Tree
  case class Leaf[T <: Any](key: String, v: T) extends Tree
  case class Branch(key:String, child: Seq[Tree]) extends Tree

  type KeyChain = Seq[String]
  val emptyKeyChain = Seq.empty[String]

  case class Row[T](k: KeyChain, v: T) {
    override def toString = (k :+ v).mkString(",")
  }

  def flattenItRec(t: Seq[Tree], current: KeyChain = emptyKeyChain, result: Seq[Row[Any]] = Seq.empty[Row[Any]]
  ): Seq[Row[Any]] = t.flatMap {
    case Leaf(k, v) => result :+ Row(current :+ k, v)
    case Branch(k, tree) => flattenItRec(tree, current :+ k, result)
  }

  case class X(m: Int = 1)
  val m = Seq(
    Branch(
      "a", Seq(
        Leaf("x",1),
        Branch("y",
          Seq(
            Leaf("m", 32.2),
            Leaf("n", 33)
          )
        )
      )
    ),
    Leaf("b", 2),
    Leaf("c", 3),
    Leaf("e", null),
    Branch(
      "d", Seq(
        Branch(
          "d1", Seq(
            Leaf("f", "abc"),
            Leaf("g", X())
          )
        )
      )
    )
  )
  val n = Seq(Branch("n", m))

  flattenItRec(n) foreach println
}
