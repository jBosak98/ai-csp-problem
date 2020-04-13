package tools

import model.{CSPModel, QuizVariable}
import tools.sudokuTools._

object domainPuzzle {
  def calculateDomainOfIndex[T, V <: QuizVariable](problem: CSPModel[T,V], index: Int): List[T] = {
    println(problem.size)
    val row = getRowAtIndex(problem.variables, problem.size,index)
    val column = getColumnAtIndex(problem.variables, problem.size, 2)
    println(row.map(_.getOrElse("_")).mkString(""), column.map(_.getOrElse("-")).mkString(""))

//    val rowDomain = getMatchedDomain(problem[String], row.asInstanceOf[Array[Option[String]]]).map(_=> "-".addString(_))
//    val columnDomain = getMatchedDomain(problem[String], column.asInstanceOf[Array[Option[String]]]).map(_=> "|".addString(_))

    Nil
  }



  def getMatchedDomain(problem: CSPModel[String, QuizVariable], line:Array[Option[QuizVariable]]) =
    problem.availableValues.map { availableValue:String =>
      if(availableValue.length != line.length) Option.empty[String]
      else {
        val isProper = line.indices.exists { index =>
          line(index).isDefined && line(index).get == availableValue(index)
        }
        if(isProper) Option[String]("-" + availableValue)
        else Option.empty[String]
      }
      ""
    }

}
