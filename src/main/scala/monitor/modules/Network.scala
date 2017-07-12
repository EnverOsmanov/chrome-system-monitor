package monitor.modules

import chrome.system.network._
import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.all._
import japgolly.scalajs.react.{BackendScope, Callback, CallbackTo, ScalaComponent}
import org.scalajs.dom
import org.scalajs.dom.html.Div

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object Network extends Module {

  val name = "Network"
  val iconUrl = "/assets/icons/scalable/network.svg"


  case class State(interfaces: List[Interface] = List(), online: Boolean = false)
  case class Backend(scope: BackendScope[_, State]) {

    def init(): Future[CallbackTo[Unit]] = {
      scope.modState(_.copy(online = dom.window.navigator.onLine))

      dom.window.addEventListener("online", (_: dom.Event) =>
        scope.modState(_.copy(online = dom.window.navigator.onLine))
      )

      dom.window.addEventListener("offline", (_: dom.Event) => {
        scope.modState(_.copy(online = dom.window.navigator.onLine))
      })

      chrome.system.network.Network.getNetworkInterfaces.map(ifaces => {
        scope.modState(_.copy(interfaces = ifaces))
      })
    }

  }



  def interfaceView(iface: Interface): TagOf[Div] = {
    div(
      div(
        padding := "15px 10px",
        backgroundColor := "rgb(42, 42, 42)",
        borderRadius := "5px 5px 0px 0px",
        fontSize.large
      )(iface.name),
      ul(
        padding := "10px",
        backgroundColor := "#303535",
        borderRadius := "0px 0px 5px 5px",
        border := "1px solid rgb(42, 42, 42)",
        boxShadow := "inset 0px 0px 10px 0px rgba(40, 40, 40, 0.75)",
        overflow.auto
      )(
        iface.configurations.map { configs =>
          li(s"${configs.address}/${configs.prefixLength}")
        }: _*
      )
    )
  }

  val comp = ScalaComponent.builder[Unit]("NetworkComponent")
      .initialState(State())
      .backend(Backend.apply)
      .render_S( s => {
        div(width := "100%")(
          s.interfaces.map(interfaceView): _*
        )
      })
    .componentWillMount( s => Callback.future(s.backend.init()) )
    .build

  val component: TagMod = comp()



}
