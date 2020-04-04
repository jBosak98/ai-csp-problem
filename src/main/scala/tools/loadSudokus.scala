package tools

import model.{Sudoku, ValueSudoku}

import scala.io.Source.fromFile

object loadSudokus {
  def loadSudokus(filename: String) = {
    val bufferedSource = fromFile(filename)
    val sudokusIterator = for {fileRow <- bufferedSource.getLines
                               sudoku = _loadSudoku(fileRow)
                               if fileRow.exists(_.isDigit) || fileRow.exists(_ == '.')
                               } yield sudoku
    val sudokus = sudokusIterator.toList
    bufferedSource.close()
    sudokus

  }

  private def _loadSudoku(line: String): Sudoku = {
    val values = line.split(';')(2)
    val sudokuValues = for {sudokuNumber <- values
                            singleValue = _toInt(sudokuNumber)
                            valueSudoku = new ValueSudoku(singleValue, singleValue.isDefined)
                            } yield valueSudoku
    new Sudoku(sudokuValues.toArray)
  }

  private def _toInt(i: Char): Option[Int] = i match {
    case i if i.isDigit => Option(i.asDigit)
    case _ => Option.empty[Int]
  }
}
