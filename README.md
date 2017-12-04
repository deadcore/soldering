# Soldering
Soldering provides a mechanism to add state handling to scala in a reactive and predictable fashion

## Usage

In your `build.sbt`:

```sbt
libraryDependencies ++= Seq(
  "io.igu" %% "soldering" % "VERSION"
)
```

## Example - Single State
An example of how `soldering` can work with a single state:

```scala
import soldering.Reducer.Reducer
import soldering.Reselect.{Selector, createSelector}
import soldering.{Reducer, Store}

object Application extends App {

  case class State(name: String)

  val reducer: Reducer[State] = state => {
      case ("SAVE_NAME", Some(name: String)) => state.copy(name = name)
    }

  val store: Store[State] = Store[State](reducer, State(""))

  store.select(_.name).subscribe(name => println(s"Name: $name")) // Will print ""

  store.dispatch(("SAVE_NAME", Some("Luke"))) // Will print "Luke"
  store.dispatch(("SAVE_NAME", Some("Vader"))) // Will print "Vader"
}
```

## Example - Multiple States
An example of how `soldering` can work using multiple states and selectors:

```scala
import soldering.Reducer.Reducer
import soldering.Reselect.{Selector, createSelector}
import soldering.{Reducer, Store}

object Application extends App {

  case class Jedi(name: String, alliance: String)
  case class State(jedies: JediState = JediState(), spaceShips: SpaceShipState = SpaceShipState())
  case class JediState(jedies: Seq[Jedi] = Seq.empty)
  case class SpaceShipState(available: Seq[String] = Seq.empty)

  val jediStateReducer: Reducer[JediState] = state => {
    case ("NEW_JEDI", Some(name: Jedi)) => state.copy(jedies = state.jedies :+ name)
  }

  val spaceShipReducer: Reducer[SpaceShipState] = state => {
    case ("NEW_SPACESHIP", Some(name: String)) => state.copy(available = state.available :+ name)
  }

  lazy val rootReducer: Reducer[State] = Reducer.combineReducers(
    ((_: State).spaceShips, spaceShipReducer, (state: State, slice: SpaceShipState) => state.copy(spaceShips = slice)),
    ((_: State).jedies, jediStateReducer, (state: State, slice: JediState) => state.copy(jedies = slice))
  )

  val store: Store[State] = Store(rootReducer, State())

  type StateSelector[T] = Selector[State, T] // Handy for larger states

  val jediState: StateSelector[JediState] = _.jedies
  val spaceState: StateSelector[SpaceShipState] = _.spaceShips

  val jediAlliance: Selector[String, Selector[JediState, Seq[Jedi]]] = (alliance: String) => _.jedies.filter(_.alliance == alliance)

  val rebels: Selector[State, Seq[Jedi]] = createSelector(jediState, jediAlliance("Rebel Alliance"))
  val empire: Selector[State, Seq[Jedi]] = createSelector(jediState, jediAlliance("The Empire"))

  store.select(_.spaceShips).subscribe(name => println(s"Spaceship: $name")) // Will print "Spaceship: Seq()"
  store.select(jediState.andThen(_.jedies)).subscribe(name => println(s"Jedies: $name")) // Will print "Jedies: Seq()"
  store.select(rebels).subscribe(rebels => println(s"Rebels: $rebels")) // Will print "Rebels: Seq()"
  store.select(empire).subscribe(rebels => println(s"Empire: $rebels")) // Will print "Empire: Seq()"

  store.dispatch(("NEW_JEDI", Some(Jedi("Luke", "Rebel Alliance")))) // Will print "Jedies: List(Jedi(Luke,Rebel Alliance))" & "Rebels: List(Jedi(Luke,Rebel Alliance))"
  store.dispatch(("NEW_JEDI", Some(Jedi("Vader", "The Empire")))) // Will print "Jedies: List(Jedi(Luke,Rebel Alliance), Jedi(Vader,The Empire))" & "Empire: List(Jedi(Vader,The Empire))"
  store.dispatch(("NEW_JEDI", Some(Jedi("Yoda", "Rebel Alliance")))) // Will print "Jedies: List(Jedi(Luke,Rebel Alliance), Jedi(Vader,The Empire), Jedi(Yoda,Rebel Alliance))" & Rebels: "List(Jedi(Luke,Rebel Alliance), Jedi(Yoda,Rebel Alliance))"

  store.dispatch(("NEW_JEDI", Some("Kylo ren"))) // Will print nothing. No loss really if I'm honest

  store.dispatch(("NEW_SPACESHIP", Some("X-Wing"))) // Will print "Spaceship: List(X-Wing)"
  store.dispatch(("NEW_SPACESHIP", Some("Y-Wing"))) // Will print "Spaceship: List(X-Wing, Y-Wing)"
  store.dispatch(("NEW_SPACESHIP", Some("Tie Fighter"))) // Will print "Spaceship: List(X-Wing, Y-Wing, Tie Fighter)"

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

