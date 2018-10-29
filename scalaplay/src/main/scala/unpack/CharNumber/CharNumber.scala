package unpack.CharNumber

case class CharNumber(a: Char, c: Int)

object UCN extends App {
  val ar = Array('a', '1', 'f', '0',  'b', '1', '2')

  val res = ar.foldLeft(List.empty[CharNumber]) {
    case (x :: xs, c: Char) if c >= '0' &&  c <= '9' =>
      CharNumber(x.a, x.c * 10 + (c - '0')) :: xs
    case (xs, c: Char) if c < '0' || c > '9' =>
      CharNumber(c, 0) :: xs
  }.reverse.filterNot(_.c < 1).map(c => c.a.toString * c.c).mkString

  println(res)
}
