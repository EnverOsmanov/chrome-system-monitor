import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys.libraryDependencies
import sbt._

object Dependencies {

  object versions {
    val scalacss = "0.5.6"
    val scalaReact = "1.4.2"

    val react = "16.7.0"
  }

  def addLibraries() = {
    libraryDependencies ++= Seq(
      "org.scala-js"                      %%% "scalajs-dom"     % "0.9.2"               withSources() withJavadoc(),
      "com.github.japgolly.scalajs-react" %%% "core"            % versions.scalaReact   withSources() withJavadoc(),
      "com.github.japgolly.scalajs-react" %%% "extra"           % versions.scalaReact   withSources() withJavadoc(),
      "com.github.japgolly.scalacss"      %%% "core"            % versions.scalacss     withSources() withJavadoc(),
      "com.github.japgolly.scalacss"      %%% "ext-react"       % versions.scalacss     withSources() withJavadoc(),
      "net.lullabyte"                     %%% "scala-js-chrome" % "0.5.0"               withSources() withJavadoc()
    )
  }

  def addJsLibraries() = {
    jsDependencies ++= Seq(
      "org.webjars.npm" % "react" % versions.react
        /        "umd/react.development.js"
        minified "umd/react.production.min.js"
        commonJSName "React",

      "org.webjars.npm" % "react-dom" % versions.react
        /         "umd/react-dom.development.js"
        minified  "umd/react-dom.production.min.js"
        dependsOn "umd/react.development.js"
        commonJSName "ReactDOM"
    )
  }
}

object MyBuildConfig {

  val scalacOptions = Seq(
    "-language:implicitConversions",
    "-language:existentials",
    "-Xlint",
    "-deprecation",
    "-Xfatal-warnings",
    "-feature"
  )

}