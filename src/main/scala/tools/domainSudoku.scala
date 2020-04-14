package tools

import model.{CSP, CSPModel, CSPProblem}
import tools.sudokuTools._

import scala.reflect.ClassTag

object domainSudoku {
  def calculateDomain[T:ClassTag,V](sudoku: CSPProblem[Int]): CSPProblem[Int] = {

    def filterDefinedValues: Int => Boolean = { index => sudoku.variables(index).isEmpty && !sudoku.isConstant(index) }

    def getDomainForEach: Int => Any = { index =>
      sudoku.domains(index) =
        if(filterDefinedValues(index)) List[String]()
        else sudoku.constraint(sudoku, index)
    }

    sudoku
      .variables
      .indices
      .foreach(getDomainForEach)

    sudoku
  }

  def calculateDomainOfIndex(sudoku: CSPModel[Int], index: Int): List[String] = {
    val row = getRowAtIndex(sudoku.variables, sudoku.size, index)
    val column = getColumnAtIndex(sudoku.variables, sudoku.size, index)
    val box = getBox(sudoku.variables, sudoku.size, index)
    val domainComplement = (row ++ column ++ box).filter(_.isDefined).map(_.get).distinct
    val domain =
      sudoku
        .availableValues
        .filter(e => !domainComplement.map(_.toString).contains(e))
    domain
  }

  def calculateDomainOfRelatedFields[V](sudoku: CSPProblem[V], index:Int) = {
    val indexes = sudoku.variables.indices.toArray
    val rowIndices = getRowAtIndex(indexes, sudoku.size, index)
    val columnIndices = getColumnAtIndex(indexes, sudoku.size, index)
    val boxIndices = getBox(indexes, sudoku.size, index)

    val valuesToCalculate = (rowIndices ++ columnIndices ++ boxIndices).distinct
    valuesToCalculate.foreach(index => {
      sudoku.domains(index) = sudoku.constraint(sudoku, index)
    })
  }


  def isDomainProper[V](sudoku: CSPProblem[V]): Boolean = {

    def filterDefinedValues: Int => Boolean = { index => sudoku.variables(index).isEmpty }

    def mapIsAnyDomainEmpty: Int => Boolean = { index =>
      sudoku.constraint(sudoku, index).isEmpty
    }

    !sudoku
      .variables
      .indices
      .filter(filterDefinedValues)
      .map(mapIsAnyDomainEmpty)
      .exists(_.equals(true))
  }
}
