import sbtrelease.ReleasePlugin.autoImport._
import sbtrelease.ReleaseStateTransformations._
import sbtrelease.{Version, _}

name := "soldering"

organization := "io.igu"

scalaVersion := "2.12.4"

releaseNextVersion := { ver => Version(ver).map(_.bumpBugfix.string).getOrElse(versionFormatError) }

credentials += Credentials(Path.userHome / ".sbt" / "pgp.credentials")

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

releaseCrossBuild := true // true if you cross-build the project for multiple Scala versions
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommand("publishSigned"),
  setNextVersion,
  commitNextVersion,
  releaseStepCommand("sonatypeReleaseAll"),
  pushChanges
)

libraryDependencies ++= Seq(
  "io.reactivex.rxjava2" % "rxjava" % "2.1.6",
  "org.mockito" % "mockito-all" % "1.10.19" % Test,
  "org.scalatest" % "scalatest_2.12" % "3.0.1" % Test
)