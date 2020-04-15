package tools

import model.{CSP, CSPProblemValidator, QuizVariable}

object QuizValidations {

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
    isFilled
  }

  def areAllFieldsFilled(problem: CSP[QuizVariable]) = {
    val arePuzzleFilled = buildPuzzle.buildPuzzle(problem).exists { char =>
      char.isEmpty || !(char.get.isLetter && char.get.equals("#"))
    }
    val areVariablesFilled = problem.variables.map(_.get).exists(_.value.isEmpty)

    arePuzzleFilled //&& areVariablesFilled
  }
  def isProperlyResolved(problem: CSP[QuizVariable]) = {
    areAllFieldsFilled(problem) && isProperlyFilled(problem)
  }






}
