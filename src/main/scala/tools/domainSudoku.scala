package tools

import model.{CSP, CSPModel, CSPProblem, QuizVariable}
import tools.sudokuTools._

import scala.reflect.ClassTag

object domainSudoku {
  def calculateDomain[T: ClassTag, V](sudoku: CSP[Int]): CSP[Int] = {

    def filterDefinedValues: Int => Boolean = { index => sudoku.variables(index).isEmpty && !sudoku.isConstant(index) }

    def getDomainForEach: Int => Any = { index =>
      sudoku.domains(index) =
        if (filterDefinedValues(index)) List[String]()
        else calculateDomainOfIndex(sudoku, index)
    }

    sudoku
      .variables
      .indices
      .foreach(getDomainForEach)

    sudoku
  }

  def calculateDomainOfIndex[V <: Int:ClassTag](sudoku: CSP[V], index: Int): List[String] = {
    val row = getRowAtIndex(sudoku.variables, sudoku.size, index)
    val column = getColumnAtIndex(sudoku.variables, sudoku.size, index)
    val box = getBox(sudoku.variables, sudoku.size, index)
    val domainComplement = (row ++ column ++ box).filter(_.isDefined).map(_.get).distinct
    val domain =
      sudoku
        .availableValues
        .filter(e => !domainComplement.map(_.toString).contains(e))
    domain.map(_.toString)
  }

  def calculateDomainOfRelatedFields[V <: Int:ClassTag](sudoku: CSP[V], index: Int) = {
    val indexes = sudoku.variables.indices.toArray
    val rowIndices = getRowAtIndex(indexes, sudoku.size, index)
    val columnIndices = getColumnAtIndex(indexes, sudoku.size, index)
    val boxIndices = getBox(indexes, sudoku.size, index)

    val valuesToCalculate = (rowIndices ++ columnIndices ++ boxIndices).distinct
    valuesToCalculate.foreach(index => {
      sudoku.domains(index) = calculateDomainOfIndex[V](sudoku, index)
    })
    sudoku.domains(index)
  }


  def isDomainProper[V](sudoku: CSP[V]): Boolean = {

    def filterDefinedValues: Int => Boolean = { index => sudoku.variables(index).isDefined }

    def mapIsAnyDomainEmpty: Int => Boolean = { index =>false

    }

    !sudoku
      .variables
      .indices
      .filter(filterDefinedValues)
      .exists(mapIsAnyDomainEmpty)
  }
}
