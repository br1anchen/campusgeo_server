package bowlerquickstart
import org.bowlerframework.squeryl.SquerylController
import org.bowlerframework.model.ParameterMapper
import org.bowlerframework.model.Validations
import org.bowlerframework.view.Renderable
import org.bowlerframework.controller.FunctionNameConventionRoutes
import bowlerquickstart.model.UserRequest

class RequestController(userRequestStore:UserRequestStore) extends SquerylController with ParameterMapper with Validations with Renderable  with FunctionNameConventionRoutes{
	def `GET /request/social`(requestUser:String,goalUser:String,reqType:Int) = 
	{
		val newRequest : UserRequest = new UserRequest("0",requestUser,goalUser,reqType,"null","null")
		render(userRequestStore.addUserRequest(newRequest))
	}
	
	def `GET /request/all`(goalUser:String) = {
	  val requests = userRequestStore.getAllRequests(goalUser)
	  render(requests)
	}
}