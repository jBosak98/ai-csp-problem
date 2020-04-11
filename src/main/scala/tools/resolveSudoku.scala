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
      calculateDomain.calculateDomainOfRelatedFields(sudoku, index)
      if (!calculateDomain.isDomainProper(sudoku)) {
        false
      } else {
        val indexToResolve = getNextIndexToResolve(sudoku)
        if (indexToResolve.isEmpty) {
          true
        } else {
          resolveField(sudoku, indexToResolve.get)
          isSudokuProperlyFilled(sudoku)
        }
      }


    }

    sudoku.values(index) = domain.find(isValueProper)
    true
  }

  def getNextIndexToResolve(sudoku: Sudoku): Option[Int] = {

    def getOnlyEmptyValues: Int => Boolean = {
      index => !sudoku.isConstant(index) && sudoku.values(index).isEmpty
    }

    val filteredIndexes: List[Int] = sudoku
      .values
      .indices
      .filter(getOnlyEmptyValues).toList
    if (filteredIndexes.isEmpty) return Option.empty[Int]

    val indexToResolve = filteredIndexes.reduce((acc: Int, e: Int) => {
      sudoku.domains(e) = calculateDomainOfIndex(sudoku, e)
      if (sudoku.domains(acc).length > sudoku.domains(e).length)
        e
      else
        acc
    })
    Option(indexToResolve)
  }

}

