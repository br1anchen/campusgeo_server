package bowlerquickstart.service

import org.squeryl.Schema
import org.squeryl.KeyedEntity
import java.sql.Timestamp
import java.util.Date

class DBTableStore {
  import Tables._
}
case class DBUsers(
    val id:Long, val userName:String, 
    val password:String, val role:String,
	val created:Timestamp, val status:Boolean) extends KeyedEntity[Long] {
  def this() = this(0, "", "", "",new Timestamp(new Date().getTime()),true)
}

case class DBGeoInformations(
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

case class DBAppointments(
    val id:Long, val host:String,
    val dater:String, val time:Timestamp,
    val latitude:String, val longitude:String) extends KeyedEntity[Long]{
  def this() = this(0,"","",new Timestamp(new Date().getTime()),"","")
}

object Tables extends Schema{
  val users = table[DBUsers]("users")
  val geoinfos = table[DBGeoInformations]("geoinfos")
  val socialnet = table[DBSocialNetwork]("socialnet")
  val appointments = table[DBAppointments]("appointments")
}
