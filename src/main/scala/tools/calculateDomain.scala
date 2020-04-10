package tools

import model.Sudoku
import model.types.Domain
import sudokuTools._

object calculateDomain {
  def calculateDomain(sudoku: Sudoku, without: Int = -1): Sudoku = {

    def getDomainForEach: Int => Any = { index =>
      val shouldNotBeCalculated = index == without || sudoku.isConstant(index) || sudoku.values(index).isDefined
      sudoku.domains(index) =
        if(shouldNotBeCalculated) List[Int]()
        else calculateDomainOfIndex(sudoku, index)
    }

    sudoku
      .values
      .indices
      .foreach(getDomainForEach)

    sudoku
  }

  def calculateDomainOfIndex(sudoku: Sudoku, index: Int): Domain = {
    val row = getRowAtIndex(sudoku.values, index)
    val column = getColumnAtIndex(sudoku.values, index)
    val box = getBox(sudoku.values, index)
    val domainComplement = (row ++ column ++ box).filter(_.isDefined).map(_.get).distinct
    val domain = (1 to 9).toList.filter(e => !domainComplement.contains(e))
    domain
  }


  def isDomainProper(sudoku: Sudoku): Boolean = {

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
