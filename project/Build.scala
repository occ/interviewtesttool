import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "InterviewTool"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.webjars" %% "webjars-play" % "2.1.0-3",
    "org.webjars" % "bootstrap" % "3.0.0-rc1",
    "org.webjars" % "codemirror" % "3.14",
    "org.webjars" % "jquery" % "2.0.3",
    jdbc,
    anorm
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
