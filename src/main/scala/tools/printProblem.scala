package tools

import model.{CSPModel, CSPProblem, QuizVariable}

import scala.reflect.runtime.universe._


object printProblem {


  def printProblem[T:TypeTag,V](problem: CSPModel[T,V]) =
    typeOf[T].toString match {
      case "Int" => _printSudoku(problem)
      case "String" => _printPuzzle(problem.asInstanceOf[CSPModel[String,QuizVariable]])
    }


  private def _printPuzzle(problem: CSPModel[String,QuizVariable] with CSPModel[String,QuizVariable]) = {
      val (column, row) = problem.size

    (0 until row).foreach { rowNumber =>
      val rowToPrint = problem
        .variables
        .slice(rowNumber * column, rowNumber * column + column)
        .map(_.getOrElse("_").toString).mkString("").toString
      println(rowToPrint)
    }
  }



  private def _printSudoku[T,V](problem: CSPModel[T,V]) = {
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
