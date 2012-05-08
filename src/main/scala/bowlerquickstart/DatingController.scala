package bowlerquickstart
import org.bowlerframework.squeryl.SquerylController
import org.bowlerframework.model.ParameterMapper
import org.bowlerframework.model.Validations
import org.bowlerframework.view.Renderable
import org.bowlerframework.controller.FunctionNameConventionRoutes

class DatingController(appointmentStore:AppointmentStore) extends SquerylController with ParameterMapper with Validations with Renderable  with FunctionNameConventionRoutes{

}