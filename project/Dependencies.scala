import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys.libraryDependencies
import sbt._

object Dependencies {

  object versions {
    val scalacss = "0.5.3"
    val scalaReact = "1.0.0"
    val scalaChrome = "0.5.0"

    val react = "15.3.2"
  }

  def addLibraries() = {
    libraryDependencies ++= Seq(
      "org.scala-js"                      %%% "scalajs-dom"     % "0.9.2"               withSources() withJavadoc(),
      "com.github.japgolly.scalajs-react" %%% "core"            % versions.scalaReact   withSources() withJavadoc(),
      "com.github.japgolly.scalajs-react" %%% "extra"           % versions.scalaReact   withSources() withJavadoc(),
      "com.github.japgolly.scalacss"      %%% "core"            % versions.scalacss     withSources() withJavadoc(),
      "com.github.japgolly.scalacss"      %%% "ext-react"       % versions.scalacss     withSources() withJavadoc(),
      "net.lullabyte"                     %%% "scala-js-chrome" % versions.scalaChrome  withSources() withJavadoc(),
      "io.monix" %% "monix" % "2.3.0"
    )
  }

  def addJsLibraries() = {
    jsDependencies ++= Seq(
      "org.webjars" % "react" % versions.react / "react-with-addons.js" commonJSName "React",
      "org.webjars" % "react" % versions.react / "react-dom.js"         commonJSName "ReactDOM"
        dependsOn           "react-with-addons.js"
        minified            "react-dom.min.js"
    )
  }
}

object MyBuildConfig {

  val scalacOptions = Seq(
    "-language:implicitConversions",
    "-language:existentials",
    "-deprecation",
    "-Xfatal-warnings",
    "-feature"
  )

}