package MyOption

sealed trait MyOption[+T] {
  def isEmpty: Boolean

  def get: T

  def getOrElse[B >: T](f: => B): B = doOrElse(f, get)

  def doOrElse[U](d: => U, e: => U): U = {
    if (isEmpty) {
      d
    } else {
      e
    }
  }
}



final case class MySome[+T](t: T) extends MyOption[T] {
  def get = t

  val isEmpty = false
}

case object MyNone extends MyOption[Nothing] {
  def get = throw EmptyOptionException

  def isEmpty = true
}

object EmptyOptionException extends Exception
