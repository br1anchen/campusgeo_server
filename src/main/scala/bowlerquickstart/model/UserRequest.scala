package bowlerquickstart.model

case class UserRequest(val id:String="0", val requestUser:String, val goalUser:String, val reqType:Int, val status:String, val date:String = null, val time:String = null)

trait RequestType{
  def id : Int
  def toString : String
}

object Friendstudent extends RequestType{
  override def id = 1
  override def toString = "friendstudent"
}

object Friendteacher extends RequestType{
  override def id = 2
  override def toString = "friendteacher"
}

object Friendschool extends RequestType
{
  override def id = 3
  override def toString = "friendschool"
}
object Dating extends RequestType{
  override def id = 0;
  override def toString = "dating"
}

object RequestTypes{
  
  val friendstudent = Friendstudent
  val friendteacher = Friendteacher
  val friendschool = Friendschool
  val dating = Dating
  
  val list = List(dating,friendstudent,friendteacher,friendschool);
  
  def getTypeById(id:Int) = list.filter(gt => gt.id == id).first
  
  def getTypeByString(strType:String) = {
    if(strType == "friendstudent"){
      RequestTypes.friendstudent
    }else if(strType == "dating"){
      RequestTypes.dating
    }else if(strType == "friendteacher"){
      RequestTypes.friendteacher
    }else if(strType == "friendschool"){
      RequestTypes.friendschool
    }else{
      RequestTypes.friendstudent
    }
  }
}