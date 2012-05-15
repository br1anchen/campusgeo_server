package bowlerquickstart

import org.bowlerframework.controller.{Controller,FunctionNameConventionRoutes}
import org.bowlerframework.model.{ ParameterMapper, Validations}
import org.bowlerframework.view.{Renderable}
import org.bowlerframework.squeryl.SquerylController
import bowlerquickstart.model.User
import org.bowlerframework.RequestScope
import bowlerquickstart.model.SocialNetwork
import bowlerquickstart.model.SocialNetwork

/**
 * 
 * extends:
 * - Controller: used to construct routes and deal with them by providing functions that respond to routes.
 * - ParameterMapper: takes a request and maps any values into beans or other objects.
 * - Validations: validation enables the Controller
 * - Renderable: allows you to render View Model objects.
 */

class AdminController(userStore:UserStore,geoInformationStore:GeoInformationStore,socialNetworkStore:SocialNetworkStore,appointmentStore:AppointmentStore,userRequestStore:UserRequestStore) extends SquerylController with ParameterMapper with Validations with Renderable  with FunctionNameConventionRoutes {


  // simple, no args render, just renders the root view of /views/GET/index (or http 204 for JSON)
 // views are resolved by view-root ("/view" on the classpath by default) + HTTP Method + path,
// in this case /views/GET/index. The ending of the template file (mustache, ssp, jade or scaml) will be auto-resolved in the order mentioned here.
// for named params, the ":" of the Scalatra route definition will be replaced by "_" when looking up on the classpath.
  def `GET /admin` = render
  
  def `GET /admin/user` = {
    val users = userStore.getAllUsers();
    render(users)
  }
  
  def `GET /admin/user/new` = render
  
  def `POST /admin/user/new`(user:User) = {
    userStore.addUser(user)
    RequestScope.response.sendRedirect("/admin/user")
  }
  
  def `GET /admin/user/:username/factory`(username:String) = {
    val user = userStore.getUser(username)
    render(user)
  }
  
  def `POST /admin/user/:username/factory`(user:User) = {
    userStore.updateUser(user)
    RequestScope.response.sendRedirect("/admin/user")
  }
  
  def `GET /admin/user/:username/trash`(username:String) = {
    userStore.deleteUser(username)
    RequestScope.response.sendRedirect("/admin/user")
  }
  
  def `GET /admin/social` = {
    val socialNets = socialNetworkStore.getAllSocials()
    render(socialNets)
  }
  
  def `GET /admin/social/new`= render
  
  def `POST /admin/social/new`(socialNetwork:SocialNetwork) ={
    socialNetworkStore.addSocialNet(socialNetwork)
    RequestScope.response.sendRedirect("/admin/social")
  }
  
  def `GET /admin/social/:socialId/factory`(socialId:String)  = {
    val socialNet = socialNetworkStore.getSocial(socialId)
    render(socialNet)
  }
  
  def `POST /admin/social/:socialId/factory`(socialNetwork:SocialNetwork) = {
    socialNetworkStore.updateSocialNet(socialNetwork)
    RequestScope.response.sendRedirect("/admin/social")
  }
}