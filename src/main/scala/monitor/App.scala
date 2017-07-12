package monitor

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.builder.Lifecycle
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.all._
import monitor.modules._
import monitor.styles.Default
import org.scalajs.dom

import scalacss.ScalaCssReact._

object App {

  val style = Default

  case class State(currentView: Option[Module])


  case class Backend(scope: BackendScope[_, State]) {

    def select(module: Module): CallbackTo[Unit] =
      scope.modState(_.copy(currentView = Some(module)))

  }

  def centered(element: TagMod) = div(
    display := "flex",
    width := "100%",
    alignItems.center,
    justifyContent.center
  )(element)

  def empty = centered("nothing selected")

  def webgl = {
    val canvas = dom.document.createElement("canvas").asInstanceOf[dom.html.Canvas]
    val gl = canvas.getContext("webgl").asInstanceOf[dom.webgl.RenderingContext]
    val extensionTags = gl.getSupportedExtensions().map(ext => div(ext))

    div(extensionTags:_*)
  }

  val modules = List(
    CPU,
    Memory,
    Network,
    Display,
    About
  )

  val component = ScalaComponent.builder[Unit]("App")
    .initialState(State(Some(CPU)))
    .backend(Backend.apply)
    .render(lifecycle => {
      div(style.app)(
        div(style.sidebar)(moduleTags(lifecycle): _*),
        div(style.viewStyle)(
          lifecycle.state.currentView.map(_.component).getOrElse(empty)
        )
      )
    }).build


  def moduleTags(lifecycle: Lifecycle.RenderScope[Unit, State, Backend]): Seq[TagMod] = modules.map { module =>
    div(
      onClick --> lifecycle.backend.select(module),
      style.menuItem(lifecycle.state.currentView.contains(module))
    )(
      img(style.menuItemIcon)(src := module.iconUrl)
    )
  }
}
