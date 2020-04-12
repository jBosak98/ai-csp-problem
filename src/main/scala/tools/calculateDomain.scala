package tools

import model.{CSP, CSPProblem}
import tools.sudokuTools._

import scala.reflect.ClassTag

object calculateDomain {
  def calculateDomain[T:ClassTag](sudoku: CSPProblem[T]): CSPProblem[T] = {

    def filterDefinedValues: Int => Boolean = { index => sudoku.variables(index).isEmpty && !sudoku.isConstant(index) }

    def getDomainForEach: Int => Any = { index =>
      sudoku.domains(index) =
        if(filterDefinedValues(index)) List[T]()
        else sudoku.constraint(sudoku, index)
    }

    sudoku
      .variables
      .indices
      .foreach(getDomainForEach)

    sudoku
  }

  def calculateDomainOfIndex[T:ClassTag](sudoku: CSP[T], index: Int): List[T] = {
    val row = getRowAtIndex(sudoku.variables, index)
    val column = getColumnAtIndex(sudoku.variables, index)
    val box = getBox(sudoku.variables, index)
    val domainComplement = (row ++ column ++ box).filter(_.isDefined).map(_.get).distinct
    val domain =
      sudoku
        .availableValues
        .filter(e => !domainComplement.contains(e))
    domain
  }

  def calculateDomainOfRelatedFields[T:ClassTag](sudoku: CSPProblem[T], index:Int) = {
    val indexes = sudoku.variables.indices.toArray
    val rowIndices = getRowAtIndex(indexes, index)
    val columnIndices = getColumnAtIndex(indexes, index)
    val boxIndices = getBox(indexes, index)

    val valuesToCalculate = (rowIndices ++ columnIndices ++ boxIndices).distinct
    valuesToCalculate.foreach(index => {
      sudoku.domains(index) = sudoku.constraint(sudoku, index)
    })
  }


  def isDomainProper[T:ClassTag](sudoku: CSPProblem[T]): Boolean = {

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
