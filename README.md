# Soldering
Soldering provides a mechanism to add state handling to scala in a reactive and predictable fashion

## Usage

In your `build.sbt`:

```sbt
libraryDependencies ++= Seq(
  "io.igu" %% "soldering" % "VERSION"
)
```

## Example
An example of how `soldering` works would be:

```scala
object Application extends App {

  case class State(name: String)

  val reducer = (state: State, action: Action) => action match {
    case ("SAVE_NAME", Some(name: String)) => state.copy(name = name)
    case _ => state
  }

  val store: Store[State] = Store[State](reducer, State(""))

  store.select(_.name).subscribe(name => println(s"Name: $name")) // Will print ""

  store.dispatch(("SAVE_NAME", Some("Luke"))) // Will print "Luke"
  store.dispatch(("SAVE_NAME", Some("Leia"))) // Will print "Leia"
  store.dispatch(("SAVE_NAME", Some("Vader"))) // Will print "Vader"


}
```

## Build manually

To benefit from the latest improvements and fixes, you may want to compile `soldering` from source. You will need a [Git client](http://git-scm.com/) and [SBT](http://www.scala-sbt.org).

From the shell, first checkout the source:

```
$ git clone git@github.com:deadcore/soldering.git
```

Then go to the `soldering` directory and launch the SBT build console:

```
$ sbt "release with-defaults"
```

