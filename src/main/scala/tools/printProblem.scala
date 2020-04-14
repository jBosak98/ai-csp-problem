package tools

import model.{CSPModel, CSPProblem, QuizVariable}

import scala.reflect.runtime.universe._


object printProblem {


  def printProblem[V:TypeTag](problem: CSPModel[V]) =
    typeOf[V].toString match {
      case "Int" => _printSudoku(problem)
      case "model.QuizVariable" => _printPuzzle(problem.asInstanceOf[CSPModel[QuizVariable]])
    }


  private def _printPuzzle(problem: CSPModel[QuizVariable] with CSPModel[QuizVariable]) = {
      val (column, row) = problem.size
    val puzzle = buildPuzzle.buildPuzzle(problem)

    (0 until row).foreach { rowNumber =>
      val rowToPrint = puzzle
        .slice(rowNumber * column, rowNumber * column + column)
        .map(_.getOrElse("#")).mkString("").toString
      println(rowToPrint)
    }
  }


  private def _printSudoku[V](problem: CSPModel[V]) = {
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
