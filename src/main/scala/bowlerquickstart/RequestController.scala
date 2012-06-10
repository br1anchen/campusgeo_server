package bowlerquickstart
import org.bowlerframework.squeryl.SquerylController
import org.bowlerframework.model.ParameterMapper
import org.bowlerframework.model.Validations
import org.bowlerframework.view.Renderable
import org.bowlerframework.controller.FunctionNameConventionRoutes
import bowlerquickstart.model.UserRequest
import bowlerquickstart.model.SocialNetwork
import bowlerquickstart.model.UserRequest
import bowlerquickstart.model.Appointment

class RequestController(userRequestStore:UserRequestStore,socialNetworkStore:SocialNetworkStore,appointmentStore:AppointmentStore) extends SquerylController with ParameterMapper with Validations with Renderable  with FunctionNameConventionRoutes{
	def `GET /request/social`(requestUser:String,goalUser:String,reqType:Int) = 
	{
		val newRequest : UserRequest = new UserRequest("0",requestUser,goalUser,reqType,"null","null")
		render(userRequestStore.addUserRequest(newRequest))
	}
	
	def `GET /request/all`(goalUser:String) = {
	  val requests = userRequestStore.getAllRequests(goalUser)
	  render(requests)
	}
	
	def `GET /request/pass`(requestId:String) = {
	  val request = userRequestStore.getUserRequestById(requestId)
	  val newRequest = new UserRequest(request.id,request.requestUser,request.goalUser,request.reqType,"true")
	  userRequestStore.updateUserRequest(newRequest,requestId)
	  
	  if(request.reqType == 0)
	  {
	    val dating = appointmentStore.getRequestAppointmentByName(request.requestUser,request.goalUser)
	    val newdating = new Appointment(dating.id,dating.host,dating.dater,dating.date,dating.time,dating.latitude,dating.longitude,"true")
	    appointmentStore.updateAppointment(newdating,dating.id)
	  }else
	  {
	    val newSocial = new SocialNetwork("0",request.requestUser,request.goalUser,request.reqType,"true")
	    socialNetworkStore.addSocialNet(newSocial)
	    val newReturnSocial = new SocialNetwork("0",request.goalUser,request.requestUser,request.reqType,"true")
	    socialNetworkStore.addSocialNet(newReturnSocial)
	  }
	  
	  render(userRequestStore.getUserRequestById(requestId))
	}
}