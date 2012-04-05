package bowlerquickstart.service

import org.squeryl.Schema

class DBTableStore {
  import Tables._
}
case class DBUser{}

case class DBGeoInformation{}

case class DBSocialNetwork{}

object Tables extends Schema{
  val users = table[DBUser]("users")
  val geoinfo = table[DBGeoInformation]("geoinfo")
  val socialnet = table[DBSocialNetwork]("socialnet")
}
