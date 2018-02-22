package soldering

object Reducer {

  type Action = Any

  type Combiner[State, Slice] = (State => Slice, Reducer[Slice], (State, Slice) => State)

  type Reducer[State] = State => PartialFunction[Action, State]

  private def performReduction[State, T1](combiner: Combiner[State, T1]): Reducer[State] = {
    val (selector, reducer, remapper) = combiner
    val reduction = reducer.compose(selector)

    (state: State) =>
      PartialFunction(action => reduction(state).
        lift(action).
        map(remapper(state, _)).
        getOrElse(state)
      )
  }

  def combineReducers[State](other: Combiner[State, _]*): Reducer[State] = {
    val reducers = other.map(combiner => performReduction(combiner))
    (state: State) => PartialFunction(action => reducers.foldLeft(state)((state, reducer) => reducer(state)(action)))
  }
}