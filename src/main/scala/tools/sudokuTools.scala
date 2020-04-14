package tools

import model.CSPProblem
import scala.reflect.ClassTag

object sudokuTools extends CSPTools {
  /*rowNumber scope = {1, ..., 9}
  columnNumber scope = {1, ..., 9}
  index scope = {0, ..., 80}
 */

  def isProperlyResolved[T,V](sudoku: CSPProblem[T,V]): Boolean = {
    areAllFieldsFilled(sudoku) && isProperlyFilled(sudoku)
  }

  def areAllFieldsFilled[T,V](sudoku: CSPProblem[T,V]): Boolean = {
    !sudoku.variables.exists(_.isEmpty)
  }

  def isProperlyFilled[T,V](sudoku: CSPProblem[T,V]): Boolean = {

    def isValueProperlyFilled: Int => Boolean = { index =>
      val valueSudoku = sudoku.variables(index)
      val row = getRowAtIndex(sudoku.variables,sudoku.size, index).filter(_.eq(valueSudoku))
      val column = getColumnAtIndex(sudoku.variables, sudoku.size, index).filter(_.eq(valueSudoku))
      val box = getBox(sudoku.variables,sudoku.size, index).filter(_.eq(valueSudoku))
      !(row.length == 1 && column.length == 1 && box.length == 1)
    }

    !sudoku
      .variables
      .indices
      .exists(isValueProperlyFilled)
  }

  def getBox[T: ClassTag](values: Array[T], size:(Int,Int), index: Int): Array[T] = {
    if (!_isProperIndex(index)) Array.empty[T]

    val rowOfFirstElement = ((getRowNumber(index,size).get - 1) / 3) * 3 + 1
    val columnOfFirstElement = ((getColumnNumber(index,size).get - 1) / 3) * 3 + 1
    val box = for {boxColumn <- 0 to 2
                   rowNumber = rowOfFirstElement + boxColumn
                   boxRow = row[T](values, size, rowNumber)
                     .slice(columnOfFirstElement - 1, columnOfFirstElement + 2)
                   } yield boxRow
    box.flatten.toArray
  }

  def getRowNumber(index: Int, size:(Int,Int)): Option[Int] =
    if (!_isProperIndex(index)) None
    else Option((index / size._1) + 1)

  private def _isProperIndex(index: Int): Boolean = index >= 0 && index < 81

  def getColumnNumber(index: Int, size:(Int, Int)): Option[Int] =
    if (!_isProperIndex(index)) None
    else Option((index % size._1) + 1)

  def getIndicesOfRow(rowNumber:Int, size:(Int, Int)): Array[Int] = {
    val indices = (0 until size._1 * size._2).toArray
    row(indices, size, rowNumber)
  }

  def getIndicesOfColumn(columnNumber:Int, size:(Int, Int)): Array[Int] = {
    val indices = (0 until size._1 * size._2).toArray
    column(indices, size, columnNumber)
  }

  def getRowAtIndex[T: ClassTag](values: Array[T],size:(Int,Int), index: Int): Array[T] =
    row[T](values,size, (index / size._1) + 1)

  //  def getFromSudoku[T:ClassTag](values: Array[T], rowNumber: Int, columnNumber: Int): Option[T] =
  //    if (columnNumber < 1 || columnNumber > 9 || rowNumber < 1 || rowNumber > 9) Option.empty[T]
  //    else row[T](values, rowNumber)(columnNumber - 1)

  def row[T: ClassTag](values: Array[T],size:(Int,Int), rowNumber: Int): Array[T] =
    if (rowNumber < 1 || rowNumber > size._2) Array.empty[T]
    else values.slice((rowNumber - 1) * size._1, (rowNumber - 1) * size._1 + size._1)

  def getColumnAtIndex[T: ClassTag](values: Array[T],size:(Int,Int), index: Int): Array[T] =
    if (!_isProperIndex(index)) Array.empty[T]
    else column(values, size, (index % size._2) + 1)

  def column[T: ClassTag](values: Array[T], size:(Int,Int), columnNumber: Int): Array[T] =
    if (columnNumber < 1 || columnNumber > size._1) Array.empty[T]
    else values.indices.collect { case i
      if i % size._1 == columnNumber - 1 => values(i)
    }.toArray


}
