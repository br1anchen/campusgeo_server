package bowlerquickstart
import org.bowlerframework.squeryl.SquerylController
import org.bowlerframework.model.ParameterMapper
import org.bowlerframework.model.Validations
import org.bowlerframework.view.Renderable
import org.bowlerframework.controller.FunctionNameConventionRoutes

class SocialController(socialNetworkStore:SocialNetworkStore) extends SquerylController with ParameterMapper with Validations with Renderable  with FunctionNameConventionRoutes{
	def `GET /social/friendList`(hostuser:String) = {
	  val socialList = socialNetworkStore.getAllSocialNet(hostuser)
	  render(socialList)
	}
}