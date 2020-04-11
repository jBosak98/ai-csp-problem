package tools

import model.CSPProblem
import model.types.Domain
import sudokuTools._

object calculateDomain {
  def calculateDomain(sudoku: CSPProblem): CSPProblem = {

    def filterDefinedValues: Int => Boolean = { index => sudoku.values(index).isEmpty && !sudoku.isConstant(index) }

    def getDomainForEach: Int => Any = { index =>
      sudoku.domains(index) =
        if(filterDefinedValues(index)) List[Int]()
        else calculateDomainOfIndex(sudoku, index)
    }

    sudoku
      .values
      .indices
      .foreach(getDomainForEach)

    sudoku
  }

  def calculateDomainOfIndex(sudoku: CSPProblem, index: Int): Domain = {
    val row = getRowAtIndex(sudoku.values, index)
    val column = getColumnAtIndex(sudoku.values, index)
    val box = getBox(sudoku.values, index)
    val domainComplement = (row ++ column ++ box).filter(_.isDefined).map(_.get).distinct
    val domain = (1 to 9).toList.filter(e => !domainComplement.contains(e))
    domain
  }

  def calculateDomainOfRelatedFields(sudoku: CSPProblem, index:Int) = {
    val indexes = sudoku.values.indices.toArray
    val rowIndices = getRowAtIndex(indexes, index)
    val columnIndices = getColumnAtIndex(indexes, index)
    val boxIndices = getBox(indexes, index)

    val valuesToCalculate = (rowIndices ++ columnIndices ++ boxIndices).distinct
    valuesToCalculate.foreach(index => {
      sudoku.domains(index) = calculateDomainOfIndex(sudoku, index)
    })
  }


  def isDomainProper(sudoku: CSPProblem): Boolean = {

    def filterDefinedValues: Int => Boolean = { index => sudoku.values(index).isEmpty }

    def mapIsAnyDomainEmpty: Int => Boolean = { index =>
      calculateDomainOfIndex(sudoku, index).isEmpty
    }

    !sudoku
      .values
      .indices
      .filter(filterDefinedValues)
      .map(mapIsAnyDomainEmpty)
      .exists(_.equals(true))
  }
}
