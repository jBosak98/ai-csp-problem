package model

import model.ValueSudoku

 class Sudoku(val rowSudoku:Array[ValueSudoku]) {

  def row(rowNumber:Int):Array[ValueSudoku] =
    if (rowNumber < 1 || rowNumber > 9) Array.empty[ValueSudoku]
    else rowSudoku.slice((rowNumber-1)*9,(rowNumber-1)*9 + 9)

  def column(columnNumber:Int):Array[ValueSudoku] =
   if (columnNumber < 1 || columnNumber > 9) Array.empty[ValueSudoku]
   else rowSudoku.indices.collect { case i
       if i % 9 == columnNumber - 1 =>  rowSudoku(i)
     }.toArray


  def get(rowNumber:Int, columnNumber:Int):Option[ValueSudoku] =
    if (columnNumber < 1 || columnNumber > 9 || rowNumber < 1 || rowNumber > 9) Option.empty[ValueSudoku]
    else Option(row(rowNumber)(columnNumber - 1))


}
