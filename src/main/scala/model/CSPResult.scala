package model

case class CSPResult(id:Int,time:Long, iterations:Int, isSolved:Boolean){
  override def toString: String =
     s"CSPResult,${id},${time},${iterations},${isSolved}"

}


object CSPResult {

  def apply(id:Int,time:Long, result:CSPResult): CSPResult =
     CSPResult(id, time, result.iterations, result.isSolved)

}