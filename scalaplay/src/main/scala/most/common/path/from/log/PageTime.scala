package most.common.path.from.log

import scala.most.common.path.from.log.Time

/**
 * Created by mcarvalho on 9/3/15.
 */
case class PageTime(page: String, time: Time = Time.zero) {
  override def toString = s"${page}"
}
