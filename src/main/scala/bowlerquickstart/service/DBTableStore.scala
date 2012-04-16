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
import bowlerquickstart.SocialNetworkStore
import bowlerquickstart.model.SocialNetwork
import bowlerquickstart.model.SocialTypes
import bowlerquickstart.AppointmentStore
import bowlerquickstart.model.Appointment

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
  
  def checkUser(username:String,password:String) : Boolean = {
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

class DBSocialNetworkStore extends SocialNetworkStore{
  import Tables._
  implicit def db2socialnet(socialnet:DBSocialNetwork) = new SocialNetwork(socialnet.host,socialnet.friend,SocialTypes.getTypeByString(socialnet.toString()).id)
  implicit def socialnet2db(socialnet:SocialNetwork) = new DBSocialNetwork(0,socialnet.host,socialnet.friend,SocialTypes.getTypeById(socialnet.socialType).toString,new Timestamp(new DateTime().getMillis()))
  
  def addSocialNet(socialnet:SocialNetwork) : SocialNetwork ={
     inTransaction{
      val newSocialNet = Tables.socialnets.insert(socialnet2db(socialnet))
      db2socialnet(newSocialNet)
    }
  }
  
  def deleteSocialNet(host:String,friend:String) ={
    inTransaction{
      Tables.socialnets.deleteWhere(sn => (sn.host === host) and (sn.friend === friend))
    }
  }
  def updateSocialNet(socialnet:SocialNetwork) : SocialNetwork ={
    inTransaction{
      Tables.socialnets.update(sn =>
    		  					where(sn.host === socialnet.host and sn.friend === socialnet.friend)
    		  					set(sn.socialType := SocialTypes.getTypeById(socialnet.socialType).toString(),
    		  					    sn.created := new Timestamp(new Date().getTime()))
    		  				)
    }
    socialnet
  }
  def checkSocialNet(host:String,friend:String) : Boolean ={
    val socialnetResult = Tables.socialnets.where(sn => sn.host === host and sn.friend === friend)
    if(socialnetResult.nonEmpty){true}else{false}
  }
  
  def getAllSocialNet(host:String) : Seq[SocialNetwork] ={
    inTransaction{
      val socialList = from(Tables.socialnets)(sn => where(sn.host === host) select(sn) orderBy(sn.created asc))
      socialList.map(sn => db2socialnet(sn)).toSeq
    }
  }
}

class DBAppointmentStore extends AppointmentStore{
  import Tables._
  implicit def db2dating(dating:DBAppointment) = new Appointment(dating.host,
		  														dating.dater,
		  														new SimpleDateFormat("yyyy-MM-dd").format(dating.time),
		  														new SimpleDateFormat("HH:mm").format(dating.time),
		  														dating.latitude,
		  														dating.longitude)
  implicit def dating2db(dating:Appointment) = new DBAppointment(0,
		  														dating.host,
		  														dating.dater,
		  														new Timestamp(new Date().getTime()),
		  														dating.latitude,
		  														dating.longitude)
  
  def addAppointment(appointment:Appointment) : Appointment ={
    inTransaction{
      val newAppointment = Tables.appointments.insert(dating2db(appointment))
      db2dating(newAppointment)
    }
  }
  
  def deleteAppointment(host:String,dater:String,time:String) ={
    val formatter:DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
    val newDate:DateTime = formatter.parseDateTime(time)
    inTransaction{
      Tables.appointments.deleteWhere(ap => (ap.host === host) and (ap.dater === ap.dater) and (ap.time === new Timestamp(newDate.getMillis())))
    }
  }
  
  def updateAppointment(appointment:Appointment,oldTime:String) : Appointment ={
    val dateString:String = appointment.date + " " + appointment.time
    val formatter:DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
    val newDate:DateTime = formatter.parseDateTime(dateString)
    val oldDate:DateTime = formatter.parseDateTime(oldTime)
    inTransaction{
      Tables.appointments.update(ap => 
        						where(ap.host === appointment.host and ap.dater === appointment.dater and ap.time === new Timestamp(oldDate.getMillis()))
        						set(ap.time := new Timestamp(newDate.getMillis()))
        						)
    }
    appointment
  }
  
  def getAppointment(host:String,dater:String) : Seq[Appointment] ={
    inTransaction{
      val datings = from(Tables.appointments)(ap => where(ap.host === host and ap.dater === dater) select(ap) orderBy(ap.time asc)).toSeq
      val nextdatings = datings.filter(d => 
        if(d.time.compareTo(new Date()) >= 0){
        true
        }else{
        false
        })
      nextdatings.map(nd => db2dating(nd)).toSeq
    }
  }
  
  def getAllAppointment(bindUser:String) : Seq[Appointment] ={
    inTransaction{
      val datings = from(Tables.appointments)(ap => where(ap.host === bindUser or ap.dater === bindUser) select(ap) orderBy(ap.time asc)).toSeq
      val nextdatings = datings.filter(d => 
        if(d.time.compareTo(new Date()) >= 0){
        true
        }else{
        false
        })
      nextdatings.map(nd => db2dating(nd)).toSeq
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
  val socialnets = table[DBSocialNetwork]("socialnets")
  val appointments = table[DBAppointment]("appointments")
}
