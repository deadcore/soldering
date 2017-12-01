package soldering


import io.reactivex.Flowable
import io.reactivex.processors.{BehaviorProcessor, FlowableProcessor}
import org.reactivestreams.Subscriber
import soldering.Reducer.{Action, Reducer}


class Store[T](dispatcher: Dispatcher,
               reducer: Reducer[T],
               initialState: T) extends Flowable[T] {

  private val state$: FlowableProcessor[T] = BehaviorProcessor.createDefault(initialState)

  private val eventStream: Unit = dispatcher.
    scan(initialState, reducer(_, _)).
    subscribe(state$ onNext _)

  def select[X](selector: T => X): Flowable[X] = {
    val slice$: Flowable[X] = state$.map(selector(_))
    slice$.distinctUntilChanged()
  }

  def dispatch(value: Action): Unit = dispatcher.dispatch(value)

  def subscribeActual(s: Subscriber[_ >: T]): Unit = state$.subscribe(s)
}

object Store {

  def apply[T](reducer: Reducer[T], initialState: T): Store[T] = new Store(new Dispatcher, reducer, initialState)

}
