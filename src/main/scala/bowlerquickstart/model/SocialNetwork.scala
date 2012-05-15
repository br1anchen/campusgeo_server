package bowlerquickstart.model

case class SocialNetwork(val id:String="0", val host:String,val friend:String, val socialType:Int, val status:String)

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

object TeacherStudent extends SocialType{
  override def id = 3
  override def toString = "teacherstudent"
}

object SocialTypes{
  val studentmate = StudentMate
  val teachermate = TeacherMate
  val teacherstudent = TeacherStudent
  
  val list = List(studentmate,teachermate,teacherstudent)
  
  def getTypeById(id:Int) = list.filter(gt => gt.id == id).first
  
  def getTypeByString(strType:String) = {
    if(strType == "studentmate"){
      SocialTypes.studentmate
    }else if(strType == "teachermate"){
      SocialTypes.teachermate
    }else if(strType == "teacherstudent"){
      SocialTypes.teacherstudent
    }else{
      SocialTypes.teacherstudent
    }
  }
}