package bowlerquickstart
import org.bowlerframework.squeryl.SquerylController
import org.bowlerframework.model.ParameterMapper
import org.bowlerframework.model.Validations
import org.bowlerframework.view.Renderable
import org.bowlerframework.controller.FunctionNameConventionRoutes
import bowlerquickstart.model.Appointment

class DatingController(appointmentStore:AppointmentStore) extends SquerylController with ParameterMapper with Validations with Renderable  with FunctionNameConventionRoutes{
	def `GET /dating/future`(bindUser:String) = {
	  val datings = appointmentStore.getAllAppointment(bindUser)
	  render(datings)
	}
	
	def `GET /dating/detail`(id:String) = {
	  val dating = appointmentStore.getAppointmentById(id)
	  render(dating)
	}
}