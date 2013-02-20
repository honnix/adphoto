package com.honnix.adphoto

import io.Source
import collection.JavaConversions._
import jespa.ldap.LdapSecurityProvider

object Adphoto extends App {
  val user = args(0)
  val imagePath = args(1)

  val props = Map("bindstr" -> "ldap://busicorp.local/CN=Users,DC=busicorp,DC=local")
  val provider = new LdapSecurityProvider(props)

  try {
    val account = provider.getAccount(s"CN=${user}", null)
    val image = Source.fromFile(imagePath).map(_.toByte).toArray
    account.setProperty("thumbnailphoto", image)
    account.update()
  } finally {
    provider.dispose()
  }
}
