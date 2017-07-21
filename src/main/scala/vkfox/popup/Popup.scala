package vkfox.popup

import japgolly.scalajs.react.extra.router.{BaseUrl, Redirect, Router, RouterConfigDsl}

class Popup {

  private val baseUrl = BaseUrl.until_#

  private val routerConfig = RouterConfigDsl[PageSelector].buildConfig { dsl =>
    import dsl._

    def filterRoute(s: PageSelector): Rule = staticRoute("#/" + s.link, s) ~> renderR(TodoList(model, s))

    val filterRoutes: Rule = PageSelector.values.map(filterRoute).reduce(_ | _)

    filterRoutes.notFound(redirectToPage(PageSelector.Buddies))
  }

  val router = Router(baseUrl, routerConfig)()
}

sealed abstract class PageSelector(val link: String, val title: String)


object PageSelector {
  object Chat extends PageSelector("", "All")
  object News extends PageSelector("active", "Active")
  object Buddies extends PageSelector("buddies", "Buddies")

  val values = List(Chat, News, Buddies)
}
