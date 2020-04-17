package validators

import model.{CSP, QuizVariable}
import tools.sudokuTools.{getBox, getColumnAtIndex, getRowAtIndex}

import scala.reflect.ClassTag

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

  def isDomainProper[V <: Int : ClassTag](sudoku: CSP[V]): Boolean = {

    def filterDefinedValues: ((Option[V], Int)) => Boolean = {
      case (variable, _) => variable.isEmpty
    }

    def mapIsAnyDomainEmpty: ((Option[V], Int)) => Boolean = {
      case (_, index) =>
        sudoku.domains(index).isEmpty
    }

    !sudoku
      .variables
      .zipWithIndex
      .filter(filterDefinedValues)
      .exists(mapIsAnyDomainEmpty)
  }
}
