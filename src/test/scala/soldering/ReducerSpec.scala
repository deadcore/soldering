package soldering

import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import soldering.Reducer.Reducer

class ReducerSpec extends WordSpec with MustMatchers with OptionValues {

  "Reducer" should {
    "accept and mutate a valid action" in {
      case class State(name: String)

      val reducer: Reducer[State] = state => {
        case ("SAVE_NAME", Some(name: String)) => state.copy(name = name)
      }

      val action = ("SAVE_NAME", Some("Jack"))
      val reduction = reducer(State(""))

      reduction(action) must be(State("Jack"))
    }
  }

  "combineReducers" should {
    "combine 2 reducers together" in {
      case class State(name: String)

      val reducer1: Reducer[State] = state => {
        case ("SAVE_NAME", Some(name: String)) => state.copy(name = name)
      }

      val reducer2: Reducer[State] = state => {
        case ("SAVE_NAME", Some(name: String)) => state.copy(name = name)
      }
    }
  }

}
