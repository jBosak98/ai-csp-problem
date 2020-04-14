package tools

import model.{CSPModel, CSPProblem, QuizVariable}
import tools.domainPuzzle.getIndicesThatAreFilledByVariable

import scala.reflect.runtime.universe._


object printProblem {


  def printProblem[T:TypeTag,V](problem: CSPModel[T,V]) =
    typeOf[T].toString match {
      case "Int" => _printSudoku(problem)
      case "String" => _printPuzzle(problem.asInstanceOf[CSPModel[String,QuizVariable]])
    }


  private def _printPuzzle(problem: CSPModel[String,QuizVariable] with CSPModel[String,QuizVariable]) = {
      val (column, row) = problem.size
    val puzzle = buildPuzzle(problem, problem.variables.map(_.get))

    (0 until row).foreach { rowNumber =>
      val rowToPrint = puzzle
        .slice(rowNumber * column, rowNumber * column + column)
        .map(_.getOrElse("#")).mkString("").toString
      println(rowToPrint)
    }
  }

  def buildPuzzle(problem: CSPModel[String,QuizVariable], variables:Array[QuizVariable]):Array[Option[Char]] =
    buildPuzzle(problem, Array.fill(problem.size._1 * problem.size._2)(Option.empty[Char]), variables)

  def buildPuzzle(problem: CSPModel[String,QuizVariable],
                    puzzle:Array[Option[Char]],
                    variables:Array[QuizVariable]
                  ):Array[Option[Char]] = {
    if(variables.length == 0) puzzle
    else {
      val variable = variables.head
      val variableIndices = getIndicesThatAreFilledByVariable(problem, variable)
      variableIndices.indices.foreach { i =>
        val variableElse = (0 to variable.size).map(_ => "_").mkString("")
        val variableValue = variable.value.getOrElse(variableElse)
        puzzle(variableIndices(i)) = Option(variableValue(i))
      }
      buildPuzzle(problem, puzzle, variables.drop(1))
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
