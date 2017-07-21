package vkfox

import chrome.runtime.Runtime
import org.scalajs.dom.window
import scala.concurrent.ExecutionContext.Implicits.global
import vkfox.app.{AppBackground, AppPopup}

import scala.scalajs.js

object Main  {

  def main(): Unit = {
    println("hello, world")


    for {
      backgroundPage ← Runtime.getBackgroundPage
    } {
      if (window == backgroundPage) {
        AppBackground.run()
      } else {
        AppPopup.run()
      }
    }
  }

}