package tools

import model.{CSP, QuizVariable}
import tools.domainPuzzle.getIndicesThatAreFilledByVariable

object buildPuzzle {
  def buildPuzzle(problem: CSP[QuizVariable]): Array[Option[Char]] = {
    var puzzle = Array.fill(problem.size._1 * problem.size._2)(Option('-'))
    problem.isConstant.zipWithIndex.filter(_._1).foreach {case (_:Boolean, index:Int) =>
      puzzle(index) = Option.empty[Char]
    }
    val variables = problem.variables.map(_.get)
    _buildPuzzle(
      problem,
      puzzle,
      variables
    )
  }

  private def _buildPuzzle(problem: CSP[QuizVariable],
                           puzzle: Array[Option[Char]],
                           variables: Array[QuizVariable]
                          ): Array[Option[Char]] = {
    if (variables.length == 0) puzzle
    else {
      val variable = variables.head
      val variableIndices = getIndicesThatAreFilledByVariable(problem, variable)
      variableIndices.indices.foreach { i =>
        val variableValue = variable.value.getOrElse("@")
        puzzle(variableIndices(i)) = if(variableValue != "@") Option(variableValue(i)) else puzzle(variableIndices(i))
      }
      _buildPuzzle(problem, puzzle, variables.drop(1))
    }
  }
}
