package soldering

object Reducer {

  type Action = (String, Option[Any])

  type Combiner[State, Slice] = (State => Slice, Reducer[Slice], (State, Slice) => State)

  type Reducer[State] = (State, Action) => State

  def combineReducers[State, T1](combiner: Combiner[State, T1]): Reducer[State] = (state: State, action: Action) => {
    val (selector, reducer, remapper) = combiner
    val slice = selector(state)

    val reduced = reducer(slice, action)
    remapper(state, reduced)
  }

  def combineReducers[State, T1, T2](combiner1: Combiner[State, T1],
                                     combiner2: Combiner[State, T2]): Reducer[State] = (state: State, action: Action) => Seq(
    combineReducers(combiner1),
    combineReducers(combiner2)
  ).foldLeft(state) { (acc, reducer) =>
    reducer(acc, action)
  }

}