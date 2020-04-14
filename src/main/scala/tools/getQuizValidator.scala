package tools

import model.{CSPProblem, CSPProblemValidator, QuizVariable}

object getQuizValidator {

  def isProperlyFilled(problem: CSPProblem[QuizVariable]) = {
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

  def areAllFieldsFilled(problem: CSPProblem[QuizVariable]) = {
    val arePuzzleFilled = buildPuzzle.buildPuzzle(problem).exists { char =>
      char.isEmpty || !(char.get.isLetter && char.get.equals("#"))
    }
    val areVariablesFilled = problem.variables.map(_.get).exists(_.value.isEmpty)

    arePuzzleFilled //&& areVariablesFilled
  }

  def getQuizValidator() = {

    def isProperlyResolved(problem: CSPProblem[QuizVariable]) = {
      areAllFieldsFilled(problem) && isProperlyFilled(problem)
    }

    CSPProblemValidator[QuizVariable](
      isProperlyResolved,
      areAllFieldsFilled,
      isProperlyFilled
    )
  }

  def createQuizVariable = { (variable: QuizVariable, word: Option[String]) =>
    Option(QuizVariable(
      index = variable.index,
      value = word,
      isVertical = variable.isVertical,
      size = variable.size
    ))

  }
}
