package bowlerquickstart
import org.bowlerframework.squeryl.SquerylController
import org.bowlerframework.model.ParameterMapper
import org.bowlerframework.model.Validations
import org.bowlerframework.view.Renderable
import org.bowlerframework.controller.FunctionNameConventionRoutes

class SocialController(socialNetworkStore:SocialNetworkStore) extends SquerylController with ParameterMapper with Validations with Renderable  with FunctionNameConventionRoutes{
	def `GET /social/user`(username:String) = {
	  val socialList = socialNetworkStore.getAllSocialNet(username)
	  render(socialList)
	}
}