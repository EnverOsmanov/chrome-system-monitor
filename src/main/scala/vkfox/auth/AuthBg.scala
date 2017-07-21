package vkfox.auth

import chrome.tabs.bindings.Tab
import monix.reactive.MulticastStrategy.Publish
import monix.reactive.{Observable, Observer}
import monix.reactive.observers.Subscriber
import org.scalajs.dom.document
import org.scalajs.dom.html.IFrame
import vkfox.browser.BrowserBg

import scala.concurrent.{Future, Promise}
import scala.concurrent.duration._
import scala.scalajs.js

object AuthBg {

  var state: AuthState = AuthState.Null
  var _iframe: Option[IFrame] = None
  var authFuture: Option[Future[AuthModelI]] = None
  val AuthURI: String = ???

  private def authURIWithDate = s"$AuthURI&time=${System.currentTimeMillis}"


  def getAuthModel() = {
    authFuture.orElse {
      val tempPromise = Promise[AuthModelI]()

      Some(tempPromise.future)
    }
  }

  def login(resetToken: Boolean = false, withWindow: Boolean = false) = {
    if (state == AuthState.LockedWindow) {
      if (withWindow) loginWithWindow()
    }
    else if (state != AuthState.LockedTokenProcessing) {
      if (withWindow) {
        state = AuthState.LockedWindow
        loginWithWindow()
      }
      else {
        state = AuthState.LockedIframe
        tryLogin()
      }
    }



    BrowserBg.setIconOffline()
    authFuture =
      if (resetToken) {
        val tempPromise = Promise[AuthModelI]()

        Some(tempPromise.future)
      }
      else getAuthModel()
  }

  private def waitAndCheckIfLoggedIn() = js.timers.setTimeout(10.seconds){
    if (state == AuthState.LockedIframe) {
      state = AuthState.LockedWindow
      loginWithWindow()
    }
  }

  private def tryLogin() = {
    if (_iframe.isEmpty) {
      _iframe = Some(document.createElement("iframe").asInstanceOf[IFrame])
      _iframe.foreach{ iframe =>

        iframe.name = "vkfox-login-iframe"
        document.body.appendChild(iframe)
        iframe.src = authURIWithDate
      }

      waitAndCheckIfLoggedIn()
    }
  }

  private def loginWithWindow(): Future[Tab] = {
    if (_iframe.nonEmpty) freeLogin()

    BrowserBg.getOrCreate(AuthURI)
  }

  def freeLogin() = {
    _iframe.foreach { iframe =>
      if(document.body.contains(iframe)) document.body.removeChild(iframe)
      _iframe = None
    }
  }

}
