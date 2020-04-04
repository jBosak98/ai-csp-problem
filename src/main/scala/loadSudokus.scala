import model.{Sudoku, ValueSudoku}

import scala.io.Source.fromFile

object loadSudokus {
  def loadSudokus(filename:String) = {
    val bufferedSource = fromFile(filename)
    val sudokusIterator = for { fileRow <- bufferedSource.getLines
                        values = for {sudokuNumber <- fileRow.split(';')(2)
                                      singleValue = _toInt(sudokuNumber)
                                      } yield new ValueSudoku(singleValue, singleValue.isDefined)
                        sudoku = Sudoku(values.toArray)
                        if(fileRow.exists(_.isDigit) || fileRow.exists(_ == '.'))
                        } yield sudoku
    val sudokus = sudokusIterator.toList
    bufferedSource.close()
    sudokus

  }
  def _toInt(i: Char): Option[Int] = i match {
    case i if i.isDigit => Option(i.asDigit)
    case _ => Option.empty[Int]
  }
}
