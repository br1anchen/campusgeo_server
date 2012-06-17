package bowlerquickstart
import org.bowlerframework.squeryl.SquerylController
import org.bowlerframework.model.ParameterMapper
import org.bowlerframework.model.Validations
import org.bowlerframework.view.Renderable
import org.bowlerframework.controller.FunctionNameConventionRoutes
import model.GeoInformation
import java.text.SimpleDateFormat
import java.sql.Timestamp
import org.joda.time.DateTime

class GeoController(geoInformationStore:GeoInformationStore) extends SquerylController with ParameterMapper with Validations with Renderable  with FunctionNameConventionRoutes{
	def`GET /geo/current`(bindUser:String) = {
	  geoInformationStore.checkGeoInfo(bindUser) match
	  {
	    case Some(g) => render(g)
	    case None => render("null")
	  }
	}
	
	def `GET /geo/update`(username:String,latitude:String,longitude:String,geoType:Int,area:String) = {
	  geoInformationStore.checkGeoInfo(username) match
	  {
	    case Some(g) => geoInformationStore.updateGeoInfoFromApp(username,latitude,longitude,geoType,area)
	    				render("update")
	    case None => geoInformationStore.addGeoInfo(new GeoInformation("0",latitude,longitude,username,new SimpleDateFormat("yyyy-MM-dd").format(new Timestamp(new DateTime().getMillis())),new SimpleDateFormat("HH:mm:ss").format(new Timestamp(new DateTime().getMillis())),geoType,area))
		 				render("create")
	  }
	}
	
	def `GET /geo/all`(key:String) = 
	{
	  if(key == "08124146")
	  {
	    val geos = geoInformationStore.getAllGeos();
	    render(geos)
	  }else{
	    render(null)
	  }
	}
}