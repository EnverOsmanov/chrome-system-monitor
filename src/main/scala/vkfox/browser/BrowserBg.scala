package vkfox.browser

import chrome.tabs.bindings.{Tab, TabCreateProperties, TabQuery, UpdateProperties}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.{Dictionary, |}

object BrowserBg {

  private val iconOnline = Dictionary(
    "19" -> "../../assets/logo19_offline.png",
    "38" -> "../../assets/logo38_offline.png"
  )

  private val IconOnlinePath: String | Dictionary[String] =
    |.from[js.Dictionary[String], String, js.Dictionary[String]](iconOnline)

  def setIconOffline() =
    chrome.browserAction.BrowserAction.setIcon(pathData = IconOnlinePath, imageData = js.undefined, tabId = js.undefined)

  def getOrCreate(url: String): Future[Tab] = {
    def findOrCreate(tabs: js.Array[Tab]): Future[Tab] = tabs.find(_.url.contains(url)) match {
      case None => createTab(url)
      case Some(tab) if tab.highlighted => Future.successful(tab)
      case Some(tab) => chrome.tabs.Tabs.update(tab.id, UpdateProperties(active = true)).map(_.get)
    }

    chrome.tabs.Tabs.query(TabQuery())
      .flatMap(findOrCreate)
  }


  def createTab(url: String): Future[Tab] =
    chrome.tabs.Tabs.create(tabCreateProperties(url))


  def tabCreateProperties(url: String): TabCreateProperties = {
    val firefoxOptions = TabCreateProperties(url = url).asInstanceOf[js.Dictionary[_]]
    firefoxOptions.delete("openerTabId")
    firefoxOptions.asInstanceOf[TabCreateProperties]
  }

}
