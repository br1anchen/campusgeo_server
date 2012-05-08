package bowlerquickstart
import org.bowlerframework.squeryl.SquerylController
import org.bowlerframework.model.ParameterMapper
import org.bowlerframework.model.Validations
import org.bowlerframework.view.Renderable
import org.bowlerframework.controller.FunctionNameConventionRoutes

class UserController(userStore:UserStore) extends SquerylController with ParameterMapper with Validations with Renderable  with FunctionNameConventionRoutes{
	def `POST /user/login`(username:String,password:String) = {
	  render(userStore.checkUser(username,password))
	}
}