package validators

import model.{CSP, QuizVariable}
import problemCreators.buildPuzzle
import tools.sudokuTools

import scala.reflect.ClassTag

object quizValidations {

  def isProperlyFilled(problem: CSP[QuizVariable]) = {
    val (numberOfColumn, numberOfRows) = problem.size
    val puzzle = buildPuzzle.buildPuzzle(problem)
    val columnLines: String = (1 to numberOfColumn).map { columnNumber =>
      sudokuTools.column(puzzle, problem.size, columnNumber).map(_.getOrElse(' ')).mkString("")
    }.mkString(" ")
    val rowLines = (1 to numberOfRows).map { rowNumber =>
      sudokuTools.row(puzzle, problem.size, rowNumber).map(_.getOrElse(' ')).mkString("")
    }.mkString(" ")
    val allLines = columnLines + rowLines
    val isFilled = problem.availableValues.forall(word => allLines.contains(word))


    areAllFieldsFilled(problem) && isFilled
  }

  def areAllFieldsFilled(problem: CSP[QuizVariable]) = {
    val arePuzzleFilled = buildPuzzle.buildPuzzle(problem).exists(_.isEmpty)

    val areAllVariablesFilled = problem
      .variables
      .exists(variable => variable.isEmpty || (variable.isDefined && variable.get.value.isDefined))

    arePuzzleFilled && areAllVariablesFilled
  }


  def isProperlyResolved(problem: CSP[QuizVariable]) = {
    areAllFieldsFilled(problem) && isProperlyFilled(problem)
  }

  def isDomainProper[V <:QuizVariable:ClassTag](sudoku: CSP[V]): Boolean = {

    def filterDefinedValues: ((Option[V], Int)) => Boolean = {
      case (variable, _) => variable.get.value.isEmpty
    }

    def mapIsAnyDomainEmpty: ((Option[V], Int)) => Boolean = {
      case (_, index) =>false
      //          sudoku.domains(index).isEmpty
    }

    !sudoku
      .variables
      .zipWithIndex
      .filter(filterDefinedValues)
      .exists(mapIsAnyDomainEmpty)
  }




}
