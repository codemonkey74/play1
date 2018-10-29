package most.common.path.from.log

import scala.most.common.path.from.log.Time


case class Pages(pages: Seq[PageTime]) {
  lazy val time: Time = Time(pages.map(_.time.t).sum)
  lazy val asPath: String = pages.map(_.page).mkString(",")
}

