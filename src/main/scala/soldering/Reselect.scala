package soldering

object Reselect {

  type Selector[T, X] = T => X

  def createSelector[State, T1, Result](t1: State => T1, mapper: T1 => Result): Selector[State, Result] = (state: State) => mapper(t1(state))

  def createSelector2[State, T1, T2, Result](t1: State => T1, t2: State => T2, mapper: (T1, T2) => Result): Selector[State, Result] = (state: State) => mapper(t1(state), t2(state))
}
