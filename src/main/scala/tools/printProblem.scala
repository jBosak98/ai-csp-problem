package tools

import model.CSPProblem
import reflect.runtime.universe._


object printProblem {


  def printProblem[T:TypeTag](problem: CSPProblem[T]) =
    typeOf[T].toString match {
      case "Int" => _printSudoku(problem)
      case "String" => _printPuzzle(problem)
    }


  private def _printPuzzle[String](problem: CSPProblem[String] with CSPProblem[String]) = {
      val (column, row) = problem.size

    (0 until row).foreach { rowNumber =>
      val rowToPrint = problem
        .variables
        .slice(rowNumber * column, rowNumber * column + column)
        .map(_.getOrElse("_").toString).mkString("").toString
      println(rowToPrint)
    }
  }



  private def _printSudoku[T](problem: CSPProblem[T]) = {
    val (column, row) = problem.size
    (0 until column)
      .foreach(rowNumber => {
        (0 until row).foreach(columnNumber => {
          val singleValue = problem.variables(columnNumber + rowNumber * column).getOrElse(' ')
          print(s"[${singleValue}]")
          if (columnNumber % 3 == 2) print(" ")
        })
        if (rowNumber % 3 == 2) print('\n')
        print('\n')
      })
  }
}
