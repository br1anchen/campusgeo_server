package bowlerquickstart

import org.bowlerframework.view.scalate._
import org.bowlerframework.Request
import org.squeryl.SessionFactory
import org.squeryl.Session
import org.squeryl.adapters.PostgreSqlAdapter
import com.mchange.v2.c3p0.ComboPooledDataSource
import bowlerquickstart.service.Tables
import bowlerquickstart.model.User
import bowlerquickstart.model.GeoInformation
import bowlerquickstart.model.GeoInformation
import bowlerquickstart.model.GeoInformation
import bowlerquickstart.model.SocialNetwork

/**
 * This class acts as the starting point and bootstrap point for our application
 */
class Bootstrap{
  // parent layout
  val parentLayout = DefaultLayout("default", "doLayout", None, None)

  def resolver(request: Request): Option[DefaultLayout] = Option(parentLayout)
  TemplateRegistry.defaultLayout = resolver(_)


  // I think we're ready to start and instantiate our Controller.
  val adminController = new AdminController with BasicAuth {
    val user :String = "campusgeo"
    val password :String = "admintest" 
  }
	
	
  // allow template reload during development - remove these lines in production for better performance
  org.bowlerframework.view.scalate.RenderEngine.getEngine.allowCaching = false
  org.bowlerframework.view.scalate.RenderEngine.getEngine.allowReload = true
  
  val cpds = setupC3p0
  SessionFactory.concreteFactory = Some(() => connection)

  def connection = {
    Session.create(cpds.getConnection, new PostgreSqlAdapter())
  }

  makeTables
  
  def makeTables = {
    val session = SessionFactory.newSession
    session.bindToCurrentThread
    try{
    Tables.create
    println("Database created")
    }catch{
      case e: Exception => {
        println("database exists!" + e.getMessage)
      }
    }finally{
      session.close
      session.unbindFromCurrentThread
    }
    
  }
  
  def setupC3p0 = {
    val cpds = new ComboPooledDataSource
    cpds.setDriverClass("org.postgresql.Driver");

    setupConnectionUrl(cpds)

    cpds.setMinPoolSize(5)
    cpds.setAcquireIncrement(1)
    cpds.setMaxPoolSize(10)

    def setupConnectionUrl(cpds: ComboPooledDataSource) {
      val db_env = System.getenv("DATABASE_URL")
      if (db_env == null || db_env.length() == 0)
        throw new Exception("Remember to set DATABASE_URL with the jdbc url")

      val JdbcUrl = "(.*//)([^:]+):([^:]+)@(.*)".r
      val JdbcUrl(pre, username, password, url) = db_env

     
      cpds.setJdbcUrl("jdbc:postgresql://"+ url)
      cpds.setUser(username)
      cpds.setPassword(password)
    }

    cpds
  }
}

trait userStore
{
  def addUser(user:User) : User
  def deleteUser(username:String)
  def updateUser(user:User) : User
  def checkUser(username:String,password:String) : Boolean
}

trait geoInformationStore
{
  def addGeoInfo(geoinfo:GeoInformation) : GeoInformation
  def updateGeoInfo(geoinfo:GeoInformation) : GeoInformation
  def checkGeoInfo(bindUser:String) : GeoInformation
}

trait socialNetworkStore
{
  def addSocialNet(socialnet:SocialNetwork) : SocialNetwork
  def deleteSocialNet(host:String,friend:String)
  def updateSocialNet(socialnet:SocialNetwork) : SocialNetwork
  def checkSocialNet(host:String,friend:String) : Boolean
  def getAllSocialNet(host:String) : Seq[SocialNetwork]
}