package soldering

object Reducer {

  type Action = (String, Option[Any])

  type Combiner[State, Slice] = (State => Slice, Reducer[Slice], (State, Slice) => State)

  type Reducer[State] = State => PartialFunction[Action, State]

  def combineReducers[State, T1](combiner: Combiner[State, T1]): Reducer[State] = (state: State) => {
    val (selector, reducer, remapper) = combiner
    val slice = selector(state)
    val reduction = reducer(slice)

    PartialFunction(action =>
      if (reduction.isDefinedAt(action)) remapper(state, reduction(action))
      else state
    )
  }

  def combineReducers[State, T1, T2](combiner1: Combiner[State, T1],
                                     combiner2: Combiner[State, T2]): Reducer[State] = (state: State) => PartialFunction(action => Seq(
    combineReducers(combiner1),
    combineReducers(combiner2)
  ).foldLeft(state)(reduce(action)))

  def combineReducers[State, T1, T2, T3](combiner1: Combiner[State, T1],
                                         combiner2: Combiner[State, T2],
                                         combiner3: Combiner[State, T3]): Reducer[State] = (state: State) => PartialFunction(action => Seq(
    combineReducers(combiner1),
    combineReducers(combiner2),
    combineReducers(combiner3)
  ).foldLeft(state)(reduce(action)))

  def combineReducers[State, T1, T2, T3, T4](combiner1: Combiner[State, T1],
                                             combiner2: Combiner[State, T2],
                                             combiner3: Combiner[State, T3],
                                             combiner4: Combiner[State, T4]): Reducer[State] = (state: State) => PartialFunction(action => Seq(
    combineReducers(combiner1),
    combineReducers(combiner2),
    combineReducers(combiner3),
    combineReducers(combiner4)
  ).foldLeft(state)(reduce(action)))

  private def reduce[State](action: Action)(state: State, reducer: Reducer[State]): State = reducer(state)(action)

}