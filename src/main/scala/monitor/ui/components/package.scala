package monitor.ui

import japgolly.scalajs.react.vdom.all._
import monitor.styles.Default
import scalacss.ScalaCssReact._

package object components {

  def infoTable(content: (TagMod, TagMod)*) = {
    val firstRowStyle = Seq(
      padding := "15px 5px"
    )
    val otherRowsStyle = Seq(
      borderTop := "1px solid #e0e0e0"
    ) ++ firstRowStyle
    table(width := "100%", borderCollapse.collapse)(
      tbody(
        content.zipWithIndex.map { case (row, index) =>
          val style = if (index == 0) firstRowStyle else otherRowsStyle
          tr()(
            td(style: _*)(row._1),
            td(style: _*)(row._2)
          )
        }: _*
      )
    )
  }

  def box(header: TagMod, content: TagMod): TagMod = div(
    div(Default.boxHeader)(header),
    div(Default.boxContent)(content)
  )

}
