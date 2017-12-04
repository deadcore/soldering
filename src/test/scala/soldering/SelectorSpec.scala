package soldering

import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import soldering.Reselect.Selector

class SelectorSpec extends WordSpec with MustMatchers with OptionValues {

  "Selector" should {
    "select a value" in {
      case class State(name: String)

      val selector: Selector[State, String] = _.name

      selector(State("Jack")) must be("Jack")
    }
  }

  "createSelector(selector)" should {
    "select a value" in {
      case class State(name: String)

      val selector1: Selector[State, String] = _.name
      val selector2: Selector[String, Int] = _.length
      val selector = Reselect.createSelector(selector1, selector2)

      selector(State("Jack")) must be(4)
    }
  }

  "createSelector2(Selector, Selector)" should {
    "select a value" in {
      case class State(name: String, age: Int)

      val selector1: Selector[State, String] = _.name
      val selector2: Selector[State, Int] = _.age
      val combiner = (name: String, age: Int) => name + age
      val selector = Reselect.createSelector(selector1, selector2, combiner)

      selector(State("Jack", 32)) must be("Jack32")
    }
  }

}
