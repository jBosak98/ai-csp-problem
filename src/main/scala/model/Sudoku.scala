package model

import model.ValueSudoku

class Sudoku(val rowSudoku: Array[ValueSudoku]) {
  /*rowNumber scope = {1, ..., 9}
    columnNumber scope = {1, ..., 9}
    index scope = {0, ..., 80}
   */

  def getBox(index: Int) = {
    if (!_isProperIndex(index)) Array.empty[ValueSudoku]
    val rowOfFirstElement = ((getRowNumber(index).get - 1) / 3) * 3 + 1
    val columnOfFirstElement = ((getColumnNumber(index).get - 1) / 3) * 3 + 1
    val box = for {boxColumn <- 0 to 2
                   boxRow = row(rowOfFirstElement + boxColumn).slice(columnOfFirstElement - 1, columnOfFirstElement + 2)
                   } yield boxRow
    box.flatten.toArray
  }

  def getRowNumber(index: Int): Option[Int] =
    if (!_isProperIndex(index)) None
    else Option((index / 9) + 1)

  def getColumnNumber(index: Int): Option[Int] =
    if (!_isProperIndex(index)) None
    else Option((index % 9) + 1)

  def getRowAtIndex(index: Int) =
    row((index / 9) + 1)

  def getColumnAtIndex(index: Int) =
    if (!_isProperIndex(index)) Array.empty[ValueSudoku]
    else column((index % 9) + 1)

  private def _isProperIndex(index: Int) = index >= 0 && index < 81

  def column(columnNumber: Int): Array[ValueSudoku] =
    if (columnNumber < 1 || columnNumber > 9) Array.empty[ValueSudoku]
    else rowSudoku.indices.collect { case i
      if i % 9 == columnNumber - 1 => rowSudoku(i)
    }.toArray

  def get(rowNumber: Int, columnNumber: Int): Option[ValueSudoku] =
    if (columnNumber < 1 || columnNumber > 9 || rowNumber < 1 || rowNumber > 9) Option.empty[ValueSudoku]
    else Option(row(rowNumber)(columnNumber - 1))

  def row(rowNumber: Int): Array[ValueSudoku] =
    if (rowNumber < 1 || rowNumber > 9) Array.empty[ValueSudoku]
    else rowSudoku.slice((rowNumber - 1) * 9, (rowNumber - 1) * 9 + 9)


}
