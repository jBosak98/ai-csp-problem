package tools

import model.CSPProblem
import scala.reflect.ClassTag

object sudokuTools extends CSPTools {
  /*rowNumber scope = {1, ..., 9}
  columnNumber scope = {1, ..., 9}
  index scope = {0, ..., 80}
 */

  def isProperlyResolved[T](sudoku: CSPProblem[T]): Boolean = {
    areAllFieldsFilled(sudoku) && isProperlyFilled(sudoku)
  }

  def areAllFieldsFilled[T](sudoku: CSPProblem[T]): Boolean = {
    !sudoku.variables.exists(_.isEmpty)
  }

  def isProperlyFilled[T](sudoku: CSPProblem[T]): Boolean = {

    def isValueProperlyFilled: Int => Boolean = { index =>
      val valueSudoku = sudoku.variables(index)
      val row = getRowAtIndex(sudoku.variables, index).filter(_.eq(valueSudoku))
      val column = getColumnAtIndex(sudoku.variables, index).filter(_.eq(valueSudoku))
      val box = getBox(sudoku.variables, index).filter(_.eq(valueSudoku))
      !(row.length == 1 && column.length == 1 && box.length == 1)
    }

    !sudoku
      .variables
      .indices
      .exists(isValueProperlyFilled)
  }

  def getBox[T: ClassTag](values: Array[T], index: Int): Array[T] = {
    if (!_isProperIndex(index)) Array.empty[T]

    val rowOfFirstElement = ((getRowNumber(index).get - 1) / 3) * 3 + 1
    val columnOfFirstElement = ((getColumnNumber(index).get - 1) / 3) * 3 + 1
    val box = for {boxColumn <- 0 to 2
                   rowNumber = rowOfFirstElement + boxColumn
                   boxRow = row[T](values, rowNumber)
                     .slice(columnOfFirstElement - 1, columnOfFirstElement + 2)
                   } yield boxRow
    box.flatten.toArray
  }

  def getRowNumber(index: Int): Option[Int] =
    if (!_isProperIndex(index)) None
    else Option((index / 9) + 1)

  private def _isProperIndex(index: Int): Boolean = index >= 0 && index < 81

  def getColumnNumber(index: Int): Option[Int] =
    if (!_isProperIndex(index)) None
    else Option((index % 9) + 1)

  def getRowAtIndex[T: ClassTag](values: Array[T], index: Int): Array[T] =
    row[T](values, (index / 9) + 1)

  //  def getFromSudoku[T:ClassTag](values: Array[T], rowNumber: Int, columnNumber: Int): Option[T] =
  //    if (columnNumber < 1 || columnNumber > 9 || rowNumber < 1 || rowNumber > 9) Option.empty[T]
  //    else row[T](values, rowNumber)(columnNumber - 1)

  def row[T: ClassTag](values: Array[T], rowNumber: Int): Array[T] =
    if (rowNumber < 1 || rowNumber > 9) Array.empty[T]
    else values.slice((rowNumber - 1) * 9, (rowNumber - 1) * 9 + 9)

  def getColumnAtIndex[T: ClassTag](values: Array[T], index: Int): Array[T] =
    if (!_isProperIndex(index)) Array.empty[T]
    else column(values, (index % 9) + 1)

  def column[T: ClassTag](values: Array[T], columnNumber: Int): Array[T] =
    if (columnNumber < 1 || columnNumber > 9) Array.empty[T]
    else values.indices.collect { case i
      if i % 9 == columnNumber - 1 => values(i)
    }.toArray


}
