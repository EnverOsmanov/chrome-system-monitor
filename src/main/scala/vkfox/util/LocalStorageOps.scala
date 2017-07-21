package vkfox.util

import org.scalajs.dom.ext.LocalStorage

import scala.scalajs.js
import scala.scalajs.js.JSON

object LocalStorageOps {

  implicit class RichLocalStorage(private val storage: LocalStorage.type) extends AnyVal {

    def getTyped[V](key: String): Option[V] = {
      storage(key)
        .map(value => JSON.parse(value)
        .asInstanceOf[V])
    }

    def setTyped(key: String, value: Boolean) = {

      val serializedValue: String = JSON.stringify(value, js.Array[js.Any]())

      storage.update(key, serializedValue)
    }

  }

}
