package bowlerquickstart.service

import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.KeyedEntity
import java.sql.Timestamp
import java.util.Date
import bowlerquickstart.UserStore
import bowlerquickstart.model.User
import org.joda.time.DateTime
import bowlerquickstart.GeoInformationStore
import bowlerquickstart.model.GeoInformation
import bowlerquickstart.model.GeoInformation
import bowlerquickstart.model.GeoTypes
import java.text.SimpleDateFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormat

class DBTableStore {
  import Tables._
}

class DBUserStore extends UserStore{
  import Tables._
  implicit def db2user(user:DBUser) = 
    new User(user.userName,
    		user.password,
    		user.role,
    		user.status)
  implicit def user2db(user:User) = 
    new DBUser(0,
    		user.userName,
    		user.password,
    		user.role,
    		new Timestamp(new DateTime().getMillis()),
    		true)
  
  def addUser(user:User) : User = {
    inTransaction{
      val newUser = Tables.users.insert(user2db(user))
      db2user(newUser)
    }
  }
  
  def deleteUser(username:String) = {
    inTransaction{
      Tables.users.deleteWhere(user => user.userName === username)
    }
  }
  
  def updateUser(user:User) = {
    inTransaction{
      Tables.users.update(u => 
        				where(u.userName === user.userName) 
    		  			set(u.userName := user.userName,
    		  			    u.password := user.password,
    		  			    u.role := user.role,
    		  			    u.status := user.status)
    		  )
    }
     user
  }
  
  def checkUser(username:String,password:String) = {
    inTransaction{
      val usernameResult = Tables.users.where(u => u.userName === username)
      if(usernameResult.nonEmpty)
      {
        if(usernameResult.first.password == password){true}else{false}
      }
      else{
        false
      }
    }
  }
}

class DBGeoInformationStore extends GeoInformationStore{
  import Tables._
  implicit def db2geoinfo(geoinfo:DBGeoInformation) =
    new GeoInformation(geoinfo.latitude,
    				geoinfo.longitude,
    				geoinfo.bindUser,
    				new SimpleDateFormat("yyyy-MM-dd").format(geoinfo.created),
    				new SimpleDateFormat("HH:mm").format(geoinfo.created),
    				GeoTypes.getTypeByString(geoinfo.geoType).id)
  implicit def geoinfo2db(geoinfo:GeoInformation) = 
    new DBGeoInformation(0,
    				geoinfo.latitude,
    				geoinfo.longitude,
    				geoinfo.bindUser,
    				new Timestamp(new DateTime().getMillis()),
    				GeoTypes.getTypeById(geoinfo.geoType).toString())
  
  def addGeoInfo(geoinfo:GeoInformation) : GeoInformation ={
    inTransaction{
      val newGeoInfo = Tables.geoinfos.insert(geoinfo2db(geoinfo))
      db2geoinfo(newGeoInfo)
    }
  }
  
  def updateGeoInfo(geoinfo:GeoInformation) : GeoInformation ={
    val dateString:String = geoinfo.date + " " + geoinfo.time
    val formatter:DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
    val newDate:DateTime = formatter.parseDateTime(dateString)
    inTransaction{
      Tables.geoinfos.update(g => 
        					where(g.bindUser === geoinfo.bindUser and g.geoType === GeoTypes.getTypeById(4).toString())
    		  				set(g.latitude := geoinfo.latitude,
    		  				    g.longitude := geoinfo.longitude,
    		  				    g.created := new Timestamp(newDate.getMillis()))
    		  				    )
    }
    geoinfo
  }
  
  def checkGeoInfo(bindUser:String) : GeoInformation ={
    inTransaction{
      val geoinfo = from(geoinfos)(g => 
        						where(g.bindUser === bindUser and g.geoType === GeoTypes.getTypeById(4).toString())
        						select(g)
        						orderBy(g.created desc)).first
      db2geoinfo(geoinfo)
    }
  }
}

case class DBUser(
    val id:Long, val userName:String, 
    val password:String, val role:String,
	val created:Timestamp, val status:Boolean) extends KeyedEntity[Long] {
  def this() = this(0, "", "", "",new Timestamp(new Date().getTime()),true)
}

case class DBGeoInformation(
    val id:Long, val latitude:String,
    val longitude:String, val bindUser:String,
    val created:Timestamp, val geoType:String) extends KeyedEntity[Long]{
  def this() = this(0,"","","",new Timestamp(new Date().getTime()),"")
}

case class DBSocialNetwork(
    val id:Long, val host:String,
    val friend:String, val socialType:String,
    val created:Timestamp) extends KeyedEntity[Long]{
  def this() = this(0,"","","",new Timestamp(new Date().getTime()))
}

case class DBAppointment(
    val id:Long, val host:String,
    val dater:String, val time:Timestamp,
    val latitude:String, val longitude:String) extends KeyedEntity[Long]{
  def this() = this(0,"","",new Timestamp(new Date().getTime()),"","")
}

object Tables extends Schema{
  val users = table[DBUser]("users")
  val geoinfos = table[DBGeoInformation]("geoinfos")
  val socialnet = table[DBSocialNetwork]("socialnet")
  val appointments = table[DBAppointment]("appointments")
}
