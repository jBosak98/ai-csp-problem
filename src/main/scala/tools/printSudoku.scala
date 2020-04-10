package tools

import model.Sudoku

object printSudoku {

  def printSudoku(s: Sudoku) =
    (0 to 8)
      .foreach(row => {
        (0 to 8).foreach(column => {
          val singleValue = s.values(column + row * 9).getOrElse(' ')
          print(s"[${singleValue}]")
          if (column % 3 == 2) print(" ")
        })
        if (row % 3 == 2) print('\n')
        print('\n')
      })
}
