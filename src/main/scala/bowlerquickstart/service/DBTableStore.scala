package bowlerquickstart.service

import org.squeryl.Schema
import org.squeryl.KeyedEntity
import java.sql.Timestamp
import java.util.Date

class DBTableStore {
  import Tables._
}
case class DBUser(
    val id:Long, val userName:String, 
    val password:String, val Role:String,
	val created:Timestamp) extends KeyedEntity[Long] {
  def this() = this(0, "", "", "",new Timestamp(new Date().getTime()))
}

case class DBGeoInformation(
    val id:Long, val latitude:String,
    val longitude:String, val bindUser:String,
    val created:Timestamp) extends KeyedEntity[Long]{
  def this() = this(0,"","","",new Timestamp(new Date().getTime()))
}

case class DBSocialNetwork(
    val id:Long, val host:String,
    val friend:String, val socialType:String,
    val created:Timestamp) extends KeyedEntity[Long]{
  def this() = this(0,"","","",new Timestamp(new Date().getTime()))
}

object Tables extends Schema{
  val users = table[DBUser]("users")
  val geoinfo = table[DBGeoInformation]("geoinfo")
  val socialnet = table[DBSocialNetwork]("socialnet")
}
