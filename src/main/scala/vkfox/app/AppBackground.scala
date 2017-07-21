package vkfox.app

import chrome.tabs.Tabs
import chrome.tabs.bindings.TabCreateProperties
import org.scalajs.dom.ext.LocalStorage
import vkfox.auth.AuthBg
import vkfox.browser.BrowserBg
import vkfox.util.LocalStorageOps.RichLocalStorage

import scala.scalajs.js
import scala.scalajs.js.|

object AppBackground {


  val BadgeColor = |.from[js.Array[Int], String, js.Array[Int]](js.Array(231, 76, 60, 255))

  def run() = {
    val firefoxOptions = TabCreateProperties(url = "assets/html/App.html").asInstanceOf[js.Dictionary[_]]
    firefoxOptions.delete("openerTabId")
    Tabs.create(firefoxOptions.asInstanceOf[TabCreateProperties])


    // Browser
    //chrome.browserAction.BrowserAction.setBadgeBackgroundColor(|.from(BadgeColor))

    // yandex
    if (LocalStorage.getTyped[Boolean]("dialog").getOrElse(false)) {
      LocalStorage.setTyped("dialog", value = false)
      BrowserBg.createTab("/pages/install.html")
    }
    else {
      AuthBg.login()
    }

  }


}
