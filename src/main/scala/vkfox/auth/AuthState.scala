package vkfox.auth


sealed trait AuthState

object AuthState {

  case object Null extends AuthState
  case object LockedIframe extends AuthState
  case object LockedWindow extends AuthState
  case object LockedTokenProcessing extends AuthState
  case object Ready extends AuthState

}


trait AuthModelI {



}
