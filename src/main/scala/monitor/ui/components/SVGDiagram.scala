package monitor.ui.components

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits.{vdomAttrVtKey, vdomAttrVtString}
import japgolly.scalajs.react.vdom.all.{backgroundColor, borderRadius, flex, svg, vdomElementFromTag}
import japgolly.scalajs.react.vdom.{TagMod, TagOf, VdomElement}
import monitor.math.{Point, Rectangle}
import org.scalajs.dom.svg.G



object SVGDiagram {

  case class Props(data: List[Point], view: (List[Point] => Rectangle), style: Style = defaultStyle)

  case class State(bounds: Rectangle)

  trait Style {

    import svg.{shapeRendering, stroke, strokeOpacity, strokeWidth, vectorEffect}

    def gridLine: Seq[TagMod] = Seq(
      vectorEffect.nonScalingStroke,
      strokeWidth := "2px"
    )

    def grid: Seq[TagMod] = Seq(
      strokeOpacity := 0.1,
      stroke := "#2A2A2A"
    )

    def path: Seq[TagMod] = Seq(
      vectorEffect.nonScalingStroke,
      strokeWidth := "2px"
    )

    def root: Seq[TagMod] = Seq(
      flex := "1",
      backgroundColor := "#ffffff",
      shapeRendering := "geometricPrecision",
      borderRadius := "5px"
    )

  }


  def fit(data: List[Point]): Rectangle = {
    data.foldLeft(Rectangle.ZERO)(Rectangle.union)
  }

  object defaultStyle extends Style

  //def barChart(p: Props): ReactTag = {
  //  val bounds = p.data.foldLeft(Rectangle.ZERO)(Rectangle.union)
  //  import svg._
  //  svg(width := "100%", height := "100%", viewBox := s"0 0 ${bounds.max.x} ${bounds.max.y}", preserveAspectRatio := "none")(
  //    g(transform := "translate(0, 1)", fill := "black", stroke := "none", strokeWidth := 0)(
  //      g(transform := "scale(1,-1)", fill := "#bada55")(
  //        for ((data, index) <- p.data.zipWithIndex) yield {
  //          rect(x := index, y := 0, width := 1, height := data)
  //        }
  //      )
  //    )
  //  )
  //}

  def grid(p: Props, bounds: Rectangle): TagOf[G] = {
    import svg._
    g(p.style.grid: _*)(
      (0 to 5).map { index => line(p.style.gridLine: _*)(x1 := 0, y1 := index / 5.0, x2 := bounds.max.x, y2 := index / 5.0) }: _*
    )
  }

  def lineChart(p: Props): VdomElement = {
    import svg.{d, fill, fillOpacity, g, height, path, preserveAspectRatio, stroke, strokeWidth, transform, viewBox, width}

    val bounds = p.view(p.data)

    val pathData = p.data.foldLeft(new StringBuilder("M0 0")) { case (acc, data) =>
      acc.append(s" ${data.x} ${data.y}")
    }.append(s"V0 ").toString

    svg.svg(p.style.root: _*)(width := "100%", height := "100%", viewBox := s"0 0 ${bounds.max.x} ${bounds.max.y}", preserveAspectRatio := "none")(
      g(transform := "translate(0, 1)", fill := "black", stroke := "#bada55", strokeWidth := 0.01)(
        g(transform := "scale(1,-1)", fill := "#bada55", fillOpacity := 0.3)(
          path(p.style.path: _*)(d := pathData),
          grid(p, bounds)
        )
      )
    )

  }

  val component = ScalaComponent.builder[Props]("SVGDiagram")
    .render_P(lineChart)
    .build

}
