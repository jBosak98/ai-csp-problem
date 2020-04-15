package tools


import model.{CSP}

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

  private def _loadSudoku(line: String): CSP[Int] = {
    val values = line.split(';')(2)
    val loadedSudoku = for {sudokuNumber <- values
                            singleValue = _toInt(sudokuNumber)
                            } yield (singleValue, singleValue.isDefined)
    val (sudokuValues, isConstant) = loadedSudoku.toArray.unzip
    val domains = Array.fill[List[String]](sudokuValues.length)(List[String]())
    CSP[Int](
      variables = sudokuValues,
      domains = domains,
      isConstant = isConstant,
      size = (9, 9),
      availableValues = List.range(1, 10).map(_.toString)
    )
  }

  private def _toInt(i: Char): Option[Int] = i match {
    case i if i.isDigit => Option(i.asDigit)
    case _ => Option.empty[Int]
  }
}
