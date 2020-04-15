package validators

import model.CSP
import tools.sudokuTools.{getBox, getColumnAtIndex, getRowAtIndex}

object sudokuValidations {

  def isProperlyResolved[V](sudoku: CSP[V]): Boolean = {
    areAllFieldsFilled(sudoku) && isProperlyFilled(sudoku)
  }

  def areAllFieldsFilled[V](sudoku: CSP[V]): Boolean = {
    !sudoku.variables.exists(_.isEmpty)
  }

  def isProperlyFilled[V](sudoku: CSP[V]): Boolean = {

    def isValueProperlyFilled: Int => Boolean = { index =>
      val valueSudoku = sudoku.variables(index)
      val row = getRowAtIndex(sudoku.variables, sudoku.size, index).filter(_.eq(valueSudoku))
      val column = getColumnAtIndex(sudoku.variables, sudoku.size, index).filter(_.eq(valueSudoku))
      val box = getBox(sudoku.variables, sudoku.size, index).filter(_.eq(valueSudoku))
      !(row.length == 1 && column.length == 1 && box.length == 1)
    }

    !sudoku
      .variables
      .indices
      .exists(isValueProperlyFilled)
  }
}
