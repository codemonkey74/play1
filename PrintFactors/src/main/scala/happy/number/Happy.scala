package happy.number


object Happy extends App {
  import scala.annotation.tailrec

  /**
    * the square of any digit is < 100.
    * so the top of size of the sum of squares is #dig * 100.
    *  for numbers with #digs       top is      #dig of top
    *      1                         100            3
    *      2                         200            3
    *      3                         300            3
    *      4                         400            3
    *      5                         500            3
    *      10                       1000            4
    *      ...
    *
    *  if we apply the recursion, all numbers with #dig >= 4, it will always go down to 3.
    *  for #dig <= 3, it will remain at 3.
    *  So the loop ONLY occurs for numbers with up to 3 digits, with the sum up to 300.
    *  the largest sum of square (9 * 9) for those 3 digits number is  9 * 9 * 3
    */
  val maxCacheable: BigInt = BigInt(9 * 9 * 3) //max square of single digit * #digits = 9^2 * 3
  var cost: BigInt = BigInt(0)

  /** pre-calculation of all happy numbers < maxCacheable. Could be even hardcoded */
  val happies: Set[BigInt] = PreCalcHappy.preCalc
  /* Set[BigInt] (
      1, 31, 82, 103, 133, 32, 176, 97, 230, 129,
      19, 70, 49, 100, 203, 94, 226, 44, 23, 167,
      219, 139, 190, 68, 192, 10, 91, 193, 236, 13,
      208, 86, 188, 239, 28, 7, 130, 79, 109)

    */

  def calculateHappiness(n: BigInt): BigInt = {
    @tailrec
    def calcHappyRec(n: BigInt, acc: BigInt): BigInt = {
      (n <= 0, n < 10) match {
        case (true, _) => acc
        case (false, true) => acc + n * n
        case _ =>
          val dig = n % 10
          if (dig > 0) { cost += 1 }
          calcHappyRec(n / 10, acc + dig * dig)
      }
    }
    val r = calcHappyRec(n, 0)
    println(s"Happiness of ($n) is $r.")
    r
  }

  @tailrec
  def isHappy(n: BigInt): Boolean = n <= maxCacheable match {
    case false => isHappy(calculateHappiness(n)) //not on the stable zone yet.
    case true => happies.contains(n)
  }

  args.map(BigInt.apply).foreach { n =>
    if (isHappy(n)) println(s"$n is a Happy number.")
    else println(s"$n is NOT a happy number.")
    println(s"Sizes: happies ${happies.size}     cost: $cost")
  }

  object PreCalcHappy {
      import scala.collection.mutable.{Set => MutSet}
      val hap: MutSet[BigInt] = MutSet[BigInt](1)
      val unhap: MutSet[BigInt] = MutSet[BigInt](0)

      @tailrec
      def isHappySeenRec(n: BigInt, seen: Seq[BigInt] = Seq.empty[BigInt]): Boolean =
        (n > maxCacheable,
          n <= 1 || hap.contains(n),
          seen.contains(n) || unhap.contains(n)) match {
        case (true, _, _) => isHappySeenRec(calculateHappiness(n), seen)
        case (_, true, _) =>
          hap ++= seen
          true
        case (_, _, true) =>
          unhap ++= seen
          false
        case _ => isHappySeenRec(calculateHappiness(n), seen :+ n)
      }

    def isHappySeen(n: BigInt) = {
      val r = isHappySeenRec(n, Seq.empty[BigInt])
      println(s"Result for $n is $r")
      r
    }

    def preCalc: Set[BigInt] = {
      (BigInt(1) to maxCacheable).foreach (isHappySeen)
      println(s"$hap")
      hap.toSet
    }
  }

}