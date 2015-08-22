package MyOption

sealed trait MyOption[T] {
  def isEmpty: Boolean

  def get: T

  def getOrElse(f: => T): T = doOrElse(f, get)

  def doOrElse[U](d: => U, e: => U): U = {
    if (isEmpty) {
      d
    } else {
      e
    }
  }
}

case object EmptyOptionException extends Exception

final case class MySome[T](t: T) extends MyOption[T] {
  def get = t

  val isEmpty = false
}

case object MyNone extends MyOption[Nothing] {
  def get = throw EmptyOptionException

  def isEmpty = true
}
