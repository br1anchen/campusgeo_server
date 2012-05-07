package bowlerquickstart.model

case class SocialNetwork(val host:String,val friend:String, val socialType:Int, val status:String)

trait SocialType{
  def id : Int
  def toString : String
}

object StudentMate extends SocialType{
  override def id = 1
  override def toString = "studentmate"
}

object TeacherMate extends SocialType{
  override def id = 2
  override def toString = "teachermate"
}

object SocialTypes{
  val studentmate = StudentMate
  val teachermate = TeacherMate
  
  val list = List(studentmate,teachermate)
  
  def getTypeById(id:Int) = list.filter(gt => gt.id == id).first
  
  def getTypeByString(strType:String) = {
    if(strType == "studentmate"){
      SocialTypes.studentmate
    }else if(strType == "teachermate"){
      SocialTypes.teachermate
    }else{
      SocialTypes.teachermate
    }
  }
}