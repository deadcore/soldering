sonatypeProfileName := "io.igu"

publishMavenStyle := true

// License of your choice
licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
homepage := Some(url("https://soldering.igu.io"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/deadcore/soldering"),
    "scm:git@github.com:deadcore/soldering.git"
  )
)
developers := List(
  Developer(id="deadcore", name="Jack", email="admin@deadcore.co.uk", url=url("https://igu.io"))
)