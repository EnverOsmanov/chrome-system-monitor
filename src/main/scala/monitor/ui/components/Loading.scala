package monitor.ui.components

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.all._

import scala.concurrent.Future
import scala.util.{Failure, Success}


object Loading {

  case class Props(future: Future[VdomElement], loading: VdomElement)

  def loading = ScalaComponent.builder[Props]("AsyncComponent")
    .render_P(props => {
      props.future.value match {
        case Some(Success(vdomElement)) => vdomElement
        case Some(Failure(_)) => div("failed")
        case None => props.loading
      }
    })


}
