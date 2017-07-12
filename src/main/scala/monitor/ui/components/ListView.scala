package monitor.ui.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.builder.Lifecycle.RenderScope
import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.all._
import org.scalajs.dom.html.LI

object ListView {


  case class State[T](items: List[T], selected: Option[T] = None)
  case class Props[T](items: List[T], selected: Option[T], itemView: (T, CallbackTo[Boolean]) => TagMod, onSelect: (T) => Unit)

  class Backend[T](scope: BackendScope[Props[T], State[T]]) {

    def select(item: T): CallbackTo[Unit] = {
      scope.modState(_.copy(selected = Some(item)))
      scope.props.map(_.onSelect(item))
    }

    def isSelected(item: T): CallbackTo[Boolean] = {
      scope.state.map(_.selected.exists(_ == item))
    }

  }

    def apply[T]() = ScalaComponent.builder[Props[T]]("ListView")
      .initialStateFromProps(p => State(p.items, p.selected))
      .backend(new Backend(_))
      .render { lifecycle => {
          ul()(
            listElements(lifecycle): _*
          )
        }
      }
      .build


  def listElements[T](lifecycle: RenderScope[Props[T], State[T], Backend[T]]): List[TagOf[LI]] = {
    lifecycle.state.items.map { item =>

      li(onClick ==> ((_: ReactEvent) => lifecycle.backend.select(item)))(
        lifecycle.props.itemView(item, lifecycle.backend.isSelected(item))
      )
    }
  }
}
