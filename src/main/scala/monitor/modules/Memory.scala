package monitor.modules

import chrome.system.memory.bindings.MemoryInfo
import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.all._
import japgolly.scalajs.react.{BackendScope, CallbackTo, ScalaComponent}
import monitor.Timeline.Listener
import monitor.math._
import monitor.ui.components.SVGDiagram
import monitor.{Timeline, ui}
import org.scalajs.dom.html.Table

import scala.concurrent.duration._

object Memory extends Module {

  val name = "Memory"
  val iconUrl = "/assets/icons/scalable/ram.svg"

  val memoryInfoStyle: Seq[TagMod] = Seq(
    display.flex,
    flexDirection := "column",
    flex := "1"
  )

  class Backend[T](scope: BackendScope[Timeline[T], _]) extends Listener[Timeline[T]] {

    def update(timeline: Timeline[T]): CallbackTo[Unit] =
      scope.forceUpdate

  }

  val memoryTimeline = {
    val timeline = new Timeline[MemoryInfo](60, 1.second)(
      chrome.system.memory.Memory.getInfo
    )
    timeline.start()
    timeline
  }

  val comp = ScalaComponent.builder[Timeline[MemoryInfo]]("Memory")
    .stateless
    .backend(new Backend(_))
    .render_P( props => div(memoryInfoStyle: _*)( pageContent(props): _*) )
    .componentDidMount(scope => scope.props.addListener(scope.backend) )
    .componentWillUnmount(scope => scope.props.removeListener(scope.backend) )
    .build

  override val component: TagMod = comp(memoryTimeline)


  def pageContent(props: Timeline[MemoryInfo]): Seq[TagMod] = {
    val svgDiagram = SVGDiagram.component(SVGDiagram.Props(
      for ((d, i) <- props.samples.reverse.zipWithIndex) yield {
        Point(i, (d.capacity - d.availableCapacity) / d.capacity)
      },
      (_: List[Point]) => new Rectangle(0, 0, props.sampleCount - 1, 1)
    ))


    val tables: Option[TagOf[Table]] = props.samples.headOption.map { info =>
      val used = (info.capacity - info.availableCapacity) / (1024 * 1014 * 1024)
      val capacity = info.capacity / (1024 * 1014 * 1024)
      ui.components.infoTable(
        ("Memory", f"$used%3.1fGB / $capacity%3.1fGB")
      )
    }

    svgDiagram :: tables.toList
  }

}
