import Dependencies.{addLibraries, addJsLibraries}
import chrome.permissions.Permission
import chrome.permissions.Permission.API
import chrome.{App, AppManifest, Background}
import net.lullabyte.{Chrome, ChromeSbtPlugin}

enablePlugins(ChromeSbtPlugin)

name          := "System Monitor"
version       := "0.2.0"
scalaVersion  := "2.12.2"


addLibraries()
addJsLibraries()


scalaJSUseMainModuleInitializer         := true
scalaJSUseMainModuleInitializer in Test := false
relativeSourceMaps                      := true
skip in packageJSDependencies           := false


scalacOptions ++= MyBuildConfig.scalacOptions


chromeManifest := new AppManifest {
  val name = Keys.name.value
  val version = Keys.version.value
  val app = App(
    background = Background(
      scripts = List("dependencies.js", "main.js")
    )
  )

  override val defaultLocale = Some("en")

  override val icons = Chrome.icons(
    "assets/icons",
    "app.png",
    Set(16, 32, 48, 64, 96, 128, 256, 512)
  )

  override val permissions: Set[Permission] = Set(
    API.System.CPU,
    API.System.Display,
    API.System.Memory,
    API.System.Network,
    API.Storage
  )
}



