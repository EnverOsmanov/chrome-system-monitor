package monitor

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import chrome.events.EventSource
import japgolly.scalajs.react.{Callback, CallbackTo}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class TickSource[T](val sampleInterval: FiniteDuration, fun: => Future[T]) extends EventSource[T] {

  class Subscription(fn: T => Unit) extends chrome.events.Subscription {
    def cancel(): Unit = {

    }
  }

  def listen(fn: T => Unit): Subscription = {
    ???
  }
}

object Timeline {

  trait Listener[T] {
    def update(value: T): CallbackTo[Unit]
  }

}

class Timeline[T](val sampleCount: Int, val sampleInterval: FiniteDuration)(fun: => Future[T]) {

  import Timeline._

  private var _samples: List[T] = List.empty
  private val listeners = mutable.ListBuffer[Listener[Timeline[T]]]()
  private var intervalHandler: Option[js.timers.SetIntervalHandle] = None

  private def addSample(sample: T): ListBuffer[CallbackTo[Unit]] = {
    _samples = (sample :: _samples).take(sampleCount)
    listeners.map(_.update(this))
  }

  def samples = _samples

  def addListener(listener: Listener[Timeline[T]]) = Callback {
    listeners += listener
  }

  def removeListener(listener: Listener[Timeline[T]]) = Callback {
    listeners -= listener
  }

  private def tick(): Future[Unit] = for {
      sample <- fun
      _ <- Future.traverse(addSample(sample))(_.asAsyncCallback.unsafeToFuture())
  } yield ()


  def start() = {
    if (intervalHandler.isEmpty)
      intervalHandler = Some(js.timers.setInterval(sampleInterval)(tick()))

    this
  }

  def stop() = intervalHandler foreach js.timers.clearInterval

}
