
scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
    "org.scalatest" % "scalatest_2.11" % "2.2.5" % "test"
)

(testOptions in Test) += Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-reports")
