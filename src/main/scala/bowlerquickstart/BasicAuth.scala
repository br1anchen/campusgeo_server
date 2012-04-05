package bowlerquickstart

import org.bowlerframework.controller.InterceptingController
import org.bowlerframework.Response
import org.bowlerframework.Request
import org.apache.commons.codec.binary.Base64

trait BasicAuth extends InterceptingController{
	val BasicAuth = "Basic (.*)".r
	val UsernamePassword = "([^:]+):(.+)".r
	
	val user:String
	val password:String
	val realm:String = "bowlerframework"
	
	override def around(request: Request, response: Response)(controller: (Request, Response) => Unit) = {

    request.getHeader("Authorization") match {
      case None => {
        unauthorized(response)
      }
      case Some(value) => {
        val BasicAuth(code) = value
        decodeBase64(code) match {
          case UsernamePassword(user, password) => controller(request, response)
          case _ => unauthorized(response)
        }
      }
    }
  }
	
	def decodeBase64(base64:String)= new String(Base64.decodeBase64(base64.getBytes()))
	def unauthorized(response:Response) {
	  response.setHeader("WWW-Authenticate", "Basic realm=\"%s\"".format(realm))
	  response.setStatus(401)
	}
}


