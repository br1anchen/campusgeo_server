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
import bowlerquickstart.UserRequestStore
import bowlerquickstart.model.UserRequest
import bowlerquickstart.model.UserRequest
import bowlerquickstart.model.RequestTypes

class DBTableStore {
  import Tables._
}

class DBUserStore extends UserStore{
  import Tables._
  implicit def db2user(user:DBUser) = 
    new User(user.username,
    		user.password,
    		user.role,
    		user.status.toString())
  implicit def user2db(user:User) = 
    new DBUser(0,
    		user.username,
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
      Tables.users.deleteWhere(user => user.username === username)
    }
  }
  
  def updateUser(user:User) = {
    val userStatus:Boolean = if(user.status == "true"){true}else{false}
    inTransaction{
      Tables.users.update(u => 
        				where(u.username === user.username) 
    		  			set(u.username := user.username,
    		  			    u.password := user.password,
    		  			    u.role := user.role,
    		  			    u.status := userStatus)
    		  )
    }
     user
  }
  
  def checkUser(username:String,password:String) : Boolean = {
    inTransaction{
      val usernameResult = Tables.users.where(u => u.username === username)
      if(usernameResult.nonEmpty)
      {
        if(usernameResult.first.password == password){true}else{false}
      }
      else{
        false
      }
    }
  }
  
  def getUser(username:String) : User=
  {
    inTransaction{
      val newUser = from(Tables.users)(u =>where(u.username === username) select(u)).first
      db2user(newUser)
    }
  }
  
  def getAllUsers() : Seq[User] = {
    inTransaction{
      val userList = from(Tables.users)(u => select(u) orderBy(u.created desc))
      userList.map(u => db2user(u)).toSeq
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
  
  def getAllGeos() : Seq[GeoInformation] = {
    inTransaction{
      val geoList = from(Tables.geoinfos)(g => select(g) orderBy(g.created desc))
      geoList.map(g => db2geoinfo(g)).toSeq
    }
  }
}

class DBSocialNetworkStore extends SocialNetworkStore{
  import Tables._
  implicit def db2socialnet(socialnet:DBSocialNetwork) = new SocialNetwork(socialnet.id.toString(),
		  																socialnet.host,
		  																socialnet.friend,
		  																SocialTypes.getTypeByString(socialnet.toString()).id,
		  																socialnet.status.toString())
  implicit def socialnet2db(socialnet:SocialNetwork) = new DBSocialNetwork(0,
		  																socialnet.host,
		  																socialnet.friend,
		  																SocialTypes.getTypeById(socialnet.socialType).toString,
		  																if(socialnet.status == "true"){true}else{false},
		  																new Timestamp(new DateTime().getMillis()))
  
  def addSocialNet(socialnet:SocialNetwork) : SocialNetwork ={
     inTransaction{
      val newSocialNet = Tables.socialnets.insert(socialnet2db(socialnet))
      db2socialnet(newSocialNet)
    }
  }
  
  def getSocial(id : String) : SocialNetwork = {
    inTransaction{
      val social = from(Tables.socialnets)(sn => where(sn.id.toString() === id) select(sn)).first
      db2socialnet(social)
    }
  }
  
  def deleteSocialNet(host:String,friend:String) ={
    inTransaction{
      Tables.socialnets.deleteWhere(sn => (sn.host === host) and (sn.friend === friend))
    }
  }
  def updateSocialNet(socialnet:SocialNetwork) : SocialNetwork ={
    val socialStatus : Boolean = if(socialnet.status == "true"){true}else{false}
    inTransaction{
      Tables.socialnets.update(sn =>
    		  					where(sn.host === socialnet.host and sn.friend === socialnet.friend)
    		  					set(sn.socialType := SocialTypes.getTypeById(socialnet.socialType).toString(),
    		  					    sn.created := new Timestamp(new Date().getTime()),
    		  					    sn.status := socialStatus)
    		  				)
    }
    socialnet
  }
  def checkSocialNet(host:String,friend:String) : Boolean ={
    val socialnetResult = Tables.socialnets.where(sn => sn.host === host and sn.friend === friend and sn.status === true)
    if(socialnetResult.nonEmpty){true}else{false}
  }
  
  def getAllSocialNet(host:String) : Seq[SocialNetwork] ={
    inTransaction{
      val socialList = from(Tables.socialnets)(sn => where(sn.host === host and sn.status === true) select(sn) orderBy(sn.created asc))
      socialList.map(sn => db2socialnet(sn)).toSeq
    }
  }
  
  def getAllSocials() : Seq[SocialNetwork] = {
    inTransaction{
      val socialList = from(Tables.socialnets)(sn => where(sn.status === true) select(sn) orderBy(sn.created asc))
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
		  														dating.longitude,
		  														dating.status.toString())
  implicit def dating2db(dating:Appointment) = new DBAppointment(0,
		  														dating.host,
		  														dating.dater,
		  														new Timestamp(new Date().getTime()),
		  														dating.latitude,
		  														dating.longitude,
		  														if(dating.status == "true"){true}else{false})
  
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
    val datingStatus : Boolean = if(appointment.status == "true"){true}else{false}
    val dateString:String = appointment.date + " " + appointment.time
    val formatter:DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
    val newDate:DateTime = formatter.parseDateTime(dateString)
    val oldDate:DateTime = formatter.parseDateTime(oldTime)
    inTransaction{
      Tables.appointments.update(ap => 
        						where(ap.host === appointment.host and ap.dater === appointment.dater and ap.time === new Timestamp(oldDate.getMillis()))
        						set(ap.time := new Timestamp(newDate.getMillis()),
        						    ap.status := datingStatus)
        						)
    }
    appointment
  }
  
  def getAppointment(host:String,dater:String) : Seq[Appointment] ={
    inTransaction{
      val datings = from(Tables.appointments)(ap => where(ap.host === host and ap.dater === dater and ap.status === true) select(ap) orderBy(ap.time asc)).toSeq
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

class DBUserRequestStore extends UserRequestStore{
	import Tables._
	
	implicit def db2UserRequest(request : DBUserRequest) = new UserRequest(request.id.toString(),
																		request.requestUser,
																		request.goalUser,
																		RequestTypes.getTypeByString(request.reqType).id,
																		request.status.toString(),
																		new SimpleDateFormat("yyyy-MM-dd").format(request.created),
																		new SimpleDateFormat("HH:mm").format(request.created))
	implicit def userRequest2db(request : UserRequest) = new DBUserRequest(0,
																		request.requestUser,
																		request.goalUser,
																		RequestTypes.getTypeById(request.reqType).toString(),
																		if(request.status == "true"){true}else{false},
																		new Timestamp(new Date().getTime()))
	
  def addUserRequest(request:UserRequest) : UserRequest = {
	  inTransaction{
      val newRequest = Tables.userrequests.insert(userRequest2db(request))
      db2UserRequest(newRequest)
	  }
	}
	
  def deleteUserRequest(requestId:String) = {
    inTransaction{
      Tables.userrequests.deleteWhere(req => req.id.toString() === requestId)
    }
  }
  
  def updateUserRequest(request:UserRequest) : UserRequest = {
    val requestStatus : Boolean = if(request.status == "true"){true}else{false}
    inTransaction{
      Tables.userrequests.update(req => 
        						where(req.id.toString() === request.id)
        						set(req.requestUser := request.requestUser,
        						    req.goalUser := request.goalUser,
        						    req.reqType := RequestTypes.getTypeById(request.reqType).toString(),
        						    req.status := requestStatus,
        						    req.created := new Timestamp(new Date().getTime()))
        						)
    }
    request
  }
  def getAllRequests(goalUser:String) : Seq[UserRequest] = {
    inTransaction{
      val requestList = from(Tables.userrequests)(req => where(req.goalUser === goalUser and req.status === false) select(req) orderBy(req.created asc))
      requestList.map(req => db2UserRequest(req)).toSeq
    }
  }
}

case class DBUser(
    val id:Long, val username:String, 
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
    val status:Boolean, val created:Timestamp) extends KeyedEntity[Long]{
  def this() = this(0,"","","",false,new Timestamp(new Date().getTime()))
}

case class DBAppointment(
    val id:Long, val host:String,
    val dater:String, val time:Timestamp,
    val latitude:String, val longitude:String, val status:Boolean) extends KeyedEntity[Long]{
  def this() = this(0,"","",new Timestamp(new Date().getTime()),"","",false)
}

case class DBUserRequest(
    val id:Long, val requestUser:String,
    val goalUser:String, val reqType:String,
    val status:Boolean, val created:Timestamp) extends KeyedEntity[Long]{
  def this() = this(0,"","","",true,new Timestamp(new Date().getTime()))
}
object Tables extends Schema{
  val users = table[DBUser]("users")
  val geoinfos = table[DBGeoInformation]("geoinfos")
  val socialnets = table[DBSocialNetwork]("socialnets")
  val appointments = table[DBAppointment]("appointments")
  val userrequests = table[DBUserRequest]("userrequests")
}
