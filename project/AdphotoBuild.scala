import sbt._
import Keys._

object Resolvers {
  val typesafeReleases = "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
  val mavenLocal = "Local Maven Repository" at "file://" + Path.userHome + "/.m2/repository"

  val myResolvers = Seq(typesafeReleases, mavenLocal)
}

object BuildSettings {

  import Resolvers._

  val buildOrganization = "com.honnix.adphoto"
  val buildVersion = "0.1.0-SNAPSHOT"
  val buildScalaVersion = "2.10.0"

  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := buildOrganization,
    version := buildVersion,
    scalaVersion := buildScalaVersion,
    shellPrompt := ShellPrompt.buildShellPrompt,
    // publish to maven repository
    // publishTo := Some(Resolver.file("file", new File(Path.userHome.absolutePath + "/.m2/repository"))),
    resolvers ++= myResolvers
  ) ++ net.virtualvoid.sbt.graph.Plugin.graphSettings
}

object ShellPrompt {

  object devnull extends ProcessLogger {
    def info(s: => String) {}

    def error(s: => String) {}

    def buffer[T](f: => T): T = f
  }

  def currBranch = (
    ("git status -sb" lines_! devnull headOption)
      getOrElse "-" stripPrefix "## "
    )

  val buildShellPrompt = {
    (state: State) => {
      val currProject = Project.extract(state).currentProject.id
      "%s:%s:%s> ".format(
        currProject, currBranch, BuildSettings.buildVersion
      )
    }
  }
}

object Dependencies {
}

object AdphotoBuild extends Build {

  import Dependencies._
  import BuildSettings._

  val commonDeps = Seq(
  )

  lazy val adphoto = Project(
    id = "adphoto",
    base = file("."),
    settings = buildSettings ++ Seq(
      libraryDependencies ++= commonDeps
    ))
}
