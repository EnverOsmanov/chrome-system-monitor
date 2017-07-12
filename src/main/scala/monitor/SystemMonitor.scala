package monitor

import chrome.app.runtime.bindings.LaunchData
import chrome.app.window._
import chrome.app.window.bindings.CreateWindowOptions
import chrome.utils.ChromeApp
import monitor.styles.Default
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLStyleElement

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._

object SystemMonitor extends ChromeApp {

  override def onLaunched(launchData: LaunchData): Unit = {
    val options = CreateWindowOptions(id = "MainWindow")

    Window.create("assets/html/App.html", options).foreach { window =>
      window.contentWindow.onload = (_: dom.Event) => {
        val style = Default.render[HTMLStyleElement]
        window.contentWindow.document.head.appendChild(style)

        App.component().renderIntoDOM(window.contentWindow.document.body)
      }
    }
  }

}
