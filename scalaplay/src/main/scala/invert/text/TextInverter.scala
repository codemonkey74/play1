package invert.text

object TextInverter {
    implicit class TextInverterOp(var s: String) {
        def invertText: String = s.split(" ").reverse.mkString(" ")
    }
}
object InvertTextWords extends App {
    import TextInverter._
    println("there you Hi".invertText)
}
