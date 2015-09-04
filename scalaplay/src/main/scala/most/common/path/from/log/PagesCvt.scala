package most.common.path.from.log

trait PagesCvt {
  def PageSeqToPages[B <: Seq[Page]](s: B): Pages = Pages(s.mkString(","))
}
