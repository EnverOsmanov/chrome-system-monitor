package vkfox.app


import monitor.styles.Default
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLStyleElement
import org.scalajs.dom.window

object AppPopup {

  def run() = {
    window.onload = (_: dom.Event) => {
      val style = Default.render[HTMLStyleElement]
      window.document.head.appendChild(style)

      App.component().renderIntoDOM(window.document.body)
    }
  }

}
