package bowlerquickstart.model

case class UserRequest(val id:String, val requestUser:String, val goalUser:String, val reqType:Int, val status:String, val date:String, val time:String)

trait RequestType{
  def id : Int
  def toString : String
}

object Friend extends RequestType{
  override def id = 1
  override def toString = "friend"
}

object Dating extends RequestType{
  override def id = 2;
  override def toString = "Dating"
}

object RequestTypes{
  
  val friend = Friend
  val dating = Dating
  
  val list = List(friend,dating);
  
  def getTypeById(id:Int) = list.filter(gt => gt.id == id).first
  
  def getTypeByString(strType:String) = {
    if(strType == "friend"){
      RequestTypes.friend
    }else if(strType == "dating"){
      RequestTypes.dating
    }else{
      RequestTypes.friend
    }
  }
}