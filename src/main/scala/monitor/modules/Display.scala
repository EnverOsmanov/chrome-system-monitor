package monitor.modules

import chrome.system.display.bindings.DisplayInfo
import japgolly.scalajs.react.vdom.all._
import japgolly.scalajs.react.{BackendScope, Callback, CallbackTo, ScalaComponent}
import monitor.ui

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object Display extends Module {

  val name = "Display"
  val iconUrl = "/assets/icons/scalable/display.svg"

  def displayView(display: DisplayInfo) = ui.components.box(s"ID ${display.id}",
    ui.components.infoTable(
      ("Name", display.name),
      ("Enabled", display.isEnabled.toString),
      ("Internal", display.isInternal.toString),
      ("Primary", display.isPrimary.toString),
      ("Work Area", s"${display.workArea.top}/${display.workArea.left}/${display.workArea.width}/${display.workArea.height}"),
      ("Bounds", s"${display.bounds.top}/${display.bounds.left}/${display.bounds.width}/${display.bounds.height}")
    )
  )


  case class State(displays: List[DisplayInfo] = List())

  case class Backend(scope: BackendScope[_, State]) {

    def init(): Future[CallbackTo[Unit]] = {
      chrome.system.display.Display.getInfo.map(displays => {
        scope.modState(_.copy(displays = displays))
      })
    }

  }


  val comp = ScalaComponent.builder[Unit]("Displays")
    .initialState(State())
    .backend(Backend.apply)
    .render_S( s =>
      div(width := "100%")(s.displays.map(displayView): _*)
    )
    .componentWillMount((s) => Callback.future(s.backend.init()))
    .build

  val component: TagMod = comp()

}
