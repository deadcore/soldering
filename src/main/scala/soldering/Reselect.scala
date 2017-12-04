package soldering

object Reselect {

  type Selector[T, X] = T => X

  def createSelector[State, T1, Result](t1: State => T1, mapper: T1 => Result): Selector[State, Result] = (state: State) => mapper(t1(state))

  def createSelector[State, T1, T2, Result](t1: State => T1, t2: State => T2, mapper: (T1, T2) => Result): Selector[State, Result] = (state: State) => mapper(t1(state), t2(state))

  def createSelector[State, T1, T2, T3, Result](t1: State => T1, t2: State => T2, t3: State => T3, mapper: (T1, T2, T3) => Result): Selector[State, Result] = (state: State) => mapper(t1(state), t2(state), t3(state))

  def createSelector[State, T1, T2, T3, T4, Result](t1: State => T1, t2: State => T2, t3: State => T3, t4: State => T4, mapper: (T1, T2, T3, T4) => Result): Selector[State, Result] = (state: State) => mapper(t1(state), t2(state), t3(state), t4(state))

  def createSelector[State, T1, T2, T3, T4, T5, Result](t1: State => T1, t2: State => T2, t3: State => T3, t4: State => T4, t5: State => T5, mapper: (T1, T2, T3, T4, T5) => Result): Selector[State, Result] = (state: State) => mapper(t1(state), t2(state), t3(state), t4(state), t5(state))

}
