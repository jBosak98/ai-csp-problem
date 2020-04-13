package tools

import model.CSPModel
import tools.sudokuTools._
import scala.reflect.ClassTag

object domainPuzzle {
  def calculateDomainOfIndex[T:ClassTag](problem: CSPModel[T], index: Int): List[T] = {
    println(problem.size)
    val row = getRowAtIndex(problem.variables, problem.size,index)
    val column = getColumnAtIndex(problem.variables, problem.size, 2)
    println(row.map(_.getOrElse("_")).mkString(""), column.map(_.getOrElse("-")).mkString(""))
    Nil
  }
}
