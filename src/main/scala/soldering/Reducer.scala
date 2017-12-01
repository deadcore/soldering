package soldering

object Reducer {

  type Action = (String, Option[Any])

  type Combiner[State, Slice] = (State => Slice, Reducer[Slice], (State, Slice) => State)

  type Reducer[State] = State => PartialFunction[Action, State]

  def combineReducers[State, T1](combiner: Combiner[State, T1]): Reducer[State] = (state: State) => {
    val (selector, reducer: Reducer[T1], remapper) = combiner
    val slice = selector(state)
    val reduction: PartialFunction[Action, T1] = reducer(slice)

    PartialFunction(action => remapper(state, reduction(action)))
  }

  def combineReducers[State, T1, T2](combiner1: Combiner[State, T1],
                                     combiner2: Combiner[State, T2]): Reducer[State] = (state: State) => PartialFunction(action => Seq(
    combineReducers(combiner1),
    combineReducers(combiner2)
  ).foldLeft(state) { (acc: State, reducer: Reducer[State]) =>
    reducer(acc)(action)
  })

}