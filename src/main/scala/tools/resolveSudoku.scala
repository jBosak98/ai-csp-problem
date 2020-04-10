package tools

import model.Sudoku
import tools.calculateDomain.calculateDomainOfIndex
import tools.sudokuTools._

object resolveSudoku {

  def resolveSudoku(sudoku: Sudoku): Boolean = {
    val position = getNextIndexToResolve(sudoku)
    if (position.isDefined) {
      resolveField(sudoku, position.get)
    }

    val isSolved = isSudokuProperlyResolved(sudoku)
    if (!isSolved) println("Sudoku has no solution")
    isSolved
  }

  def resolveField(sudoku: Sudoku, index: Int): Boolean = {
    val domain = calculateDomainOfIndex(sudoku, index)

    def isValueProper: Int => Boolean = { value =>
      sudoku.values(index) = Option(value)
      val indexToResolve = getNextIndexToResolve(sudoku)
      if (indexToResolve.isEmpty) {
        true
      } else {
        resolveField(sudoku, indexToResolve.get)
        isSudokuProperlyFilled(sudoku)
      }

    }
    sudoku.values(index) = domain.find(isValueProper)
    true
  }

  def getNextIndexToResolve(sudoku: Sudoku): Option[Int] = {

    def getOnlyEmptyValues: Int => Boolean = {
      index => !sudoku.isConstant(index) && sudoku.values(index).isEmpty
    }

    sudoku
      .values
      .indices
      .find(getOnlyEmptyValues)
  }

}

