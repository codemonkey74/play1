package unpack.CharNumber

case class CharNumber(a: Char, c: Int)

object CharNumber extends {
    def apply(cn: CharNumber, counter: Char) = new CharNumber(cn.a, cn.c * 10 + counter - '0')
}

object UCN extends App {
  val ar = Array('a', '1', 'f', '0',  'b', '1', '2')

  val res = ar.foldLeft(List.empty[CharNumber]) {
      case (x :: xs, c: Char) if c >= '0' &&  c <= '9' =>
          CharNumber(x, c) :: xs
      case (xs, c: Char) if c < '0' || c > '9' =>
          CharNumber(c, 0) :: xs
  }.reverse.filterNot(_.c < 1).map(c => c.a.toString * c.c).mkString

  println(res)
}
