package tools

import model.{Sudoku, ValueSudoku}

object calculateDomain {
  def calculateDomain(sudoku: Sudoku): Sudoku = {
    val sudokuValues = sudoku.rowSudoku

    def getDomainForEach(index:Int):ValueSudoku = {
      val sudokuValue = sudokuValues(index)
      if (sudokuValue.isConstant) return sudokuValue
      val domain = calculateDomain(sudoku, index)
       new ValueSudoku(value = sudokuValue.value, domain = domain)
    }

    val valuesWithDomain = sudokuValues.indices.map(getDomainForEach).toArray
    new Sudoku(valuesWithDomain)
  }

  def calculateDomain(sudoku: Sudoku, index: Int) = {
    val row = sudoku.getRowAtIndex(index).map(_.value)
    val column = sudoku.getColumnAtIndex(index).map(_.value)
    val box = sudoku.getBox(index).map(_.value)
    val domainComplement = (row ++ column ++ box).filter(_.isDefined).map(_.get).distinct
    val domain = (1 to 9).toList.filter(e => !domainComplement.contains(e))
    domain
  }
}
