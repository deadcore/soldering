package soldering

import io.reactivex.Flowable
import io.reactivex.processors.{FlowableProcessor, PublishProcessor}
import org.reactivestreams.Subscriber
import soldering.Reducer.Action

class Dispatcher extends Flowable[Action] {

  private val stream: FlowableProcessor[Action] = PublishProcessor.create()

  def dispatch(value: Action) : Unit = stream.onNext(value)

  def subscribeActual(s: Subscriber[_ >: Action]): Unit = stream.subscribe(s)

}