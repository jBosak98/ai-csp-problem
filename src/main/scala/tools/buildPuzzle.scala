package tools

import model.{CSPModel, QuizVariable}
import tools.domainPuzzle.getIndicesThatAreFilledByVariable

object buildPuzzle {
  def buildPuzzle(problem: CSPModel[QuizVariable]):Array[Option[Char]] = {
    val puzzle = Array.fill(problem.size._1 * problem.size._2)(Option.empty[Char])
    val variables = problem.variables.map(_.get)
    _buildPuzzle(
      problem,
      puzzle,
      variables
    )
  }

  private def _buildPuzzle(problem: CSPModel[QuizVariable],
                  puzzle:Array[Option[Char]],
                  variables:Array[QuizVariable]
                 ):Array[Option[Char]] = {
    if(variables.length == 0) puzzle
    else {
      val variable = variables.head
      val variableIndices = getIndicesThatAreFilledByVariable(problem, variable)
//      println(variableIndices)
      variableIndices.indices.foreach { i =>
        val variableElse = (0 to i).map(_ => "_").mkString("")
        val variableValue = variable.value.getOrElse(variableElse)
//        println(s"!${variableValue}!")
        puzzle(variableIndices(i)) = if (variableValue.length > i) Option(variableValue(i)) else Option('-')
      }
      _buildPuzzle(problem, puzzle, variables.drop(1))
    }
  }
}
