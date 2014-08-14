import sbt._
import sbt.Keys._
import com.typesafe.sbt.SbtGit._


object SlickDaoBuild extends Build {

  val buildName         = "SlickDao"
  val appVersion        = "0.0.1"
  val scalaBuildOptions = Seq("-unchecked", "-deprecation", "-feature", "-language:reflectiveCalls")

  val credentials26 = Credentials(Path.userHome / ".ivy2" / ".credentials")
  val nexus = "http://build.26source.org/nexus/"
  val snapshotRepo = Some("snapshots" at nexus + "content/repositories/snapshots")
  val releaseRepo = Some("releases"  at nexus + "content/repositories/releases")

  val repos = Seq(
    "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    "Typesafe snapshots"  at "http://repo.typesafe.com/typesafe/snapshots/",
    "Sonatype snapshots"  at "https://oss.sonatype.org/content/repositories/snapshots"
  )
  
  val BuildSettings = Project.defaultSettings ++ Seq(
    scalacOptions := scalaBuildOptions,
    scalaVersion := "2.11.2",
    resolvers ++= repos,
    version := appVersion+"-"+git.gitHeadCommit.value.map{_.take(7)}.getOrElse("SNAPSHOT"),
    publishTo := {
      if (version.value.trim.endsWith("SNAPSHOT"))
        snapshotRepo
      else
        releaseRepo
    },
    credentials += credentials26
  )

  val mainDependencies = Seq(
    "com.typesafe.slick"    %%  "slick"                   % "2.1.0",
    "org.scalautils"        %%  "scalautils"              % "2.1.5",
    "org.slf4j"             %   "slf4j-api"               % "1.7.7",
    "com.h2database"        %   "h2"                      % "1.3.166",

    "org.scalatest"         %%  "scalatest"               % "2.2.1"      % "test"
  )

  lazy val root = Project(
    id = "slick-dao",
    base = file("."),
    settings = BuildSettings ++ Seq(
      libraryDependencies ++= mainDependencies
    )
  )
}
