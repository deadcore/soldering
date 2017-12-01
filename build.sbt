name := "soldering"

version := "0.1"

scalaVersion := "2.12.4"

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

libraryDependencies ++= Seq(
  "io.reactivex.rxjava2" % "rxjava" % "2.1.6",
  "org.mockito" % "mockito-all" % "1.10.19" % Test,
  "org.scalatest" % "scalatest_2.12" % "3.0.1" % Test
)