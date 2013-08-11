import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {
  val appName         = "InterviewTool"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "junit" % "junit" % "4.11",
    "org.reflections" % "reflections" % "0.9.9-RC1",
    "org.webjars" %% "webjars-play" % "2.1.0-3",
    "org.webjars" % "ace" % "04.09.2013",
    "org.webjars" % "bootstrap" % "3.0.0-rc1",
    "org.webjars" % "jquery" % "2.0.3",
    jdbc,
    anorm
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    templatesImport += "interfaces._"
  )
}
