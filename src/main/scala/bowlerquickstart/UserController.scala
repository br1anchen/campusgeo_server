package bowlerquickstart
import org.bowlerframework.squeryl.SquerylController
import org.bowlerframework.model.ParameterMapper
import org.bowlerframework.model.Validations
import org.bowlerframework.view.Renderable
import org.bowlerframework.controller.FunctionNameConventionRoutes
import bowlerquickstart.model.User

class UserController(userStore:UserStore) extends SquerylController with ParameterMapper with Validations with Renderable  with FunctionNameConventionRoutes{
	def `GET /user/login`(username:String,password:String) = {
	  val result:Boolean = userStore.checkUser(username,password)
	  render(result)
	}
	
	def `GET /user/new`(username:String,password:String,role:String) = {
	  val newUser = userStore.addUser(new User(username,password,role,"true"))
	  render(newUser)
	}
}