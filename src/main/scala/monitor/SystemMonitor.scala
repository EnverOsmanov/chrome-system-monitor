package monitor


import chrome.runtime.Runtime
import chrome.tabs.Tabs
import chrome.tabs.bindings.TabCreateProperties
import monitor.styles.Default
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLStyleElement
import org.scalajs.dom.window

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._

object SystemMonitor extends js.JSApp {

  def main(): Unit = {

    for {
      backgroundPage ← Runtime.getBackgroundPage
    } {
      if (window == backgroundPage) {
        val firefoxOptions = TabCreateProperties(url = "assets/html/App.html").asInstanceOf[js.Dictionary[_]]
        firefoxOptions.delete("openerTabId")
        Tabs.create(firefoxOptions.asInstanceOf[TabCreateProperties])
      } else {
        window.onload = (_: dom.Event) => {
          val style = Default.render[HTMLStyleElement]
          window.document.head.appendChild(style)

          App.component().renderIntoDOM(window.document.body)
        }
      }
    }
  }

}
