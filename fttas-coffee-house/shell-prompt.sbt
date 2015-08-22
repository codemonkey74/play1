/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

import scala.Console
import scala.util.matching._

shellPrompt in ThisBuild := { state =>
  val prjNbrNme = Project.extract(state).get(name)
  prjNbrNme match {
    case ("base" | "common") =>
      Console.RED + "[run the man command to get started]" + Console.RESET + s" ${prjNbrNme} > "
    case _ =>
      val prjNmeRx: Regex = """(?<=[a-zA-z]\d\d\d_).*""".r
      val prjNbrRx: Regex = """(\d\d\d)""".r
      val prjNme: String = prjNmeRx findAllIn prjNbrNme mkString
      val prjNbr: Option[String] = prjNbrRx findFirstIn prjNbrNme
      s"[${prjNbr.get}] ${prjNme} > "
  }
}
