package bowlerquickstart.model

case class GeoInformation(val id:String="0", val latitude:String, val longitude:String, val bindUser:String, val date:String, val time:String, val geoType:Int)

trait GeoType{
  def id : Int
  def toString : String
}

object Start extends GeoType{
  override def id = 1
  override def toString = "start"
}

object Process extends GeoType{
  override def id = 2
  override def toString = "process"
}

object End extends GeoType{
  override def id = 3
  override def toString = "end"
}

object Single extends GeoType{
  override def id = 4
  override def toString = "single"
}

object GeoTypes{
  val start = Start
  val process = Process
  val end = End
  val single = Single
  
  val list = List(start,process,end,single)
  
  def getTypeById(id:Int) = list.filter(gt => gt.id == id).first
  
  def getTypeByString(strType:String) = {
    if(strType == "start"){
      GeoTypes.start
    }else if(strType == "process"){
      GeoTypes.process
    }else if(strType == "end"){
      GeoTypes.end
    }else{
      GeoTypes.single
    }
  }
}