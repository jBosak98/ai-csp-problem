package tools

import model.Sudoku
import model.types.ValueSudoku


object sudokuTools {
  /*rowNumber scope = {1, ..., 9}
    columnNumber scope = {1, ..., 9}
    index scope = {0, ..., 80}
   */
  def getBox(values: Array[ValueSudoku], index: Int): Array[ValueSudoku] = {
    if (!_isProperIndex(index)) Array.empty[Option[Int]]

    val rowOfFirstElement = ((getRowNumber(index).get - 1) / 3) * 3 + 1
    val columnOfFirstElement = ((getColumnNumber(index).get - 1) / 3) * 3 + 1
    val box = for {boxColumn <- 0 to 2
                   boxRow = row(values, rowOfFirstElement + boxColumn).slice(columnOfFirstElement - 1, columnOfFirstElement + 2)
                   } yield boxRow
    box.flatten.toArray
  }

  def getRowNumber(index: Int): Option[Int] =
    if (!_isProperIndex(index)) None
    else Option((index / 9) + 1)

  def getColumnNumber(index: Int): Option[Int] =
    if (!_isProperIndex(index)) None
    else Option((index % 9) + 1)

  def areAllFieldsFilled(sudoku: Sudoku): Boolean = {
    !sudoku.values.exists(_.isEmpty)
  }

  def getRowAtIndex(values: Array[ValueSudoku], index: Int): Array[ValueSudoku] =
    row(values, (index / 9) + 1)

  def getColumnAtIndex(values: Array[ValueSudoku], index: Int): Array[ValueSudoku] =
    if (!_isProperIndex(index)) Array.empty[ValueSudoku]
    else column(values, (index % 9) + 1)

  private def _isProperIndex(index: Int): Boolean = index >= 0 && index < 81

  def column(values: Array[ValueSudoku], columnNumber: Int): Array[ValueSudoku] =
    if (columnNumber < 1 || columnNumber > 9) Array.empty[ValueSudoku]
    else values.indices.collect { case i
      if i % 9 == columnNumber - 1 => values(i)
    }.toArray

  def getFromSudoku(values: Array[ValueSudoku], rowNumber: Int, columnNumber: Int): ValueSudoku =
    if (columnNumber < 1 || columnNumber > 9 || rowNumber < 1 || rowNumber > 9) Option.empty[Int]
    else row(values, rowNumber)(columnNumber - 1)

  def row(values: Array[ValueSudoku], rowNumber: Int): Array[ValueSudoku] =
    if (rowNumber < 1 || rowNumber > 9) Array.empty[Option[Int]]
    else values.slice((rowNumber - 1) * 9, (rowNumber - 1) * 9 + 9)

  def isSudokuProperlyResolved(sudoku: Sudoku): Boolean = {
    areAllFieldsFilled(sudoku) && isSudokuProperlyFilled(sudoku)
  }

  def isSudokuProperlyFilled(sudoku: Sudoku): Boolean = {

    def isValueProperlyFilled: Int => Boolean = { index =>
      val valueSudoku = sudoku.values(index)
      val row = getRowAtIndex(sudoku.values, index).filter(_.eq(valueSudoku))
      val column = getColumnAtIndex(sudoku.values, index).filter(_.eq(valueSudoku))
      val box = getBox(sudoku.values, index).filter(_.eq(valueSudoku))
      !(row.length == 1 && column.length == 1 && box.length == 1)
    }

    !sudoku
      .values
      .indices
      .exists(isValueProperlyFilled)
  }
}