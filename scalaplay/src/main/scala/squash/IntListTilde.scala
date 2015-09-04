package squash

object IntListTilde {

    import scala.language.implicitConversions

    /*List(1,2,3,5,7,8,9,10)   =>   List((1,3), (5,5), (7,10)) */
    implicit def rangify(numbers: List[Int]) = numbers.foldLeft(List.empty[(Int, Int)]) {
        case (l, b) if l.isEmpty => (b, b) :: l
        case (x :: xs, b) if b - x._2 == 1 => (x._1, b) :: xs
        case (xs, b) => (b, b) :: xs
    }.reverse

    def rangesToString(numbers: List[Int])(implicit f: List[Int] => List[(Int, Int)]): List[String] = {
        f(numbers).map {
            case (a, b) if a == b => a.toString
            case (a, b) => List(a, b).mkString("~")
        }
    }
}

object TestIntListTilde extends App {
    import IntListTilde._

    println(rangesToString(List(1,2,3,5,7,8,9,10)).mkString(", "))
}