package soldering


import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.{BehaviorProcessor, FlowableProcessor}
import org.reactivestreams.Subscriber
import soldering.Reducer.{Action, Reducer}
import soldering.Reselect.Selector


class Store[State](dispatcher: Dispatcher,
                   reducer: Reducer[State],
                   initialState: State) extends Flowable[State] {

  private val state$: FlowableProcessor[State] = BehaviorProcessor.createDefault(initialState)

  private val eventStream: Disposable = dispatcher.
    scan(initialState, safeReduce).
    subscribe(state$ onNext _)

  private def safeReduce(state: State, action: Action) = {
    val reduction = reducer(state)
    val newStats: State = if (reduction.isDefinedAt(action)) {
      reduction(action)
    } else {
      state
    }

    newStats
  }

  def select[Slice](selector: Selector[State, Slice]): Flowable[Slice] = {
    val slice$: Flowable[Slice] = state$.map(selector(_))
    slice$.distinctUntilChanged()
  }

  def dispatch(value: Action): Unit = dispatcher.dispatch(value)

  def subscribeActual(s: Subscriber[_ >: State]): Unit = state$.subscribe(s)
}

object Store {

  def apply[T](reducer: Reducer[T], initialState: T): Store[T] = new Store(new Dispatcher, reducer, initialState)

}
