package MyOption;

sealed trait MyOption[T] {
  val t: T 
  def getOrElse(f: =>T) = {
    if(t != Nil) {
      t  
    } else { 
      f 
    }    
  }
}

case class MySome[T](t: T) extends MyOption[T];
case object MyNone extends MyOption[T];

object MySome {
  def apply(t: T): MySome[T] = MySome(t)
  def unapply(m: MySome): T = m.t
}

case class User(name: String, age: Int)

object Solution extends App { 
  val u1 = User("a", 1)
  val ou1: MyOption[User] = MySome(u1)
  ou1.getOrElse(User(56))
}
