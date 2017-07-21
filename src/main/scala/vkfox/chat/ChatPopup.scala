package vkfox.chat

import japgolly.scalajs.react.{CtorType, _ }
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import vkfox.popup.PageSelector
import org.scalajs.dom
import japgolly.scalajs.react.extra._
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.html
import org.scalajs.dom.html.Element

object ChatPopup {

  case class Props(
                    ctl: RouterCtl[PageSelector],
                    model: ChatModel,
                    currentFilter: PageSelector
                  )


  case class State()
  class Backend() extends OnUnmount


  private val component: Component[Props, State, Backend, CtorType.Props[_, _]] = ???

  def apply(model: ChatModel, currentFilter: PageSelector)(ctl: RouterCtl[PageSelector]): VdomElement =
    vdomElementFromComponent(component(ChatPopup.Props(ctl, model, currentFilter)))
}

class ChatModel(storage: Storage) extends Broadcaster[Seq[Chat]]
case class Chat()
case class Storage(storage: dom.ext.Storage, namespace: String)