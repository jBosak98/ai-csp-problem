package domainCalculations

import model.{CSP, QuizVariable}
import tools.puzzleTools._

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

object domainPuzzle {

  def createQuizVariable(variable: Option[QuizVariable], word: Option[String]): Option[QuizVariable] = {
    Option(QuizVariable(
      index = variable.get.index,
      value = word,
      isVertical = variable.get.isVertical,
      size = variable.get.size
    ))
  }

  def calculateDomainOfDependents[V <: QuizVariable : TypeTag : ClassTag](problem: CSP[V], index: Int) = {

    val dependentVariablesIndexes = getDependentVariables(problem, index)
    dependentVariablesIndexes
      .filter(i => problem.variables(i).get.value.isEmpty)
      .foreach { variableIndex: Int =>
        problem.domains(variableIndex) = calculateDomainOfVariableIndex(problem, variableIndex)

      }
    problem.domains(index)
  }

  def calculateDomainOfVariableIndex[V <: QuizVariable](problem: CSP[V], index: Int) = {
    val variable = problem.variables(index).get

    problem.domains(index) = calculateDomainForVariable(
      problem.asInstanceOf[CSP[QuizVariable]],
      variable)
      .asInstanceOf[List[String]]
    problem.domains(index)
  }

  def calculateDomainForVariable[V <: QuizVariable : TypeTag : ClassTag]
  (problem: CSP[V], variable: V) = {

    val insertedWords = problem.variables
      .collect(word => if (word.isDefined && word.get.value.isDefined) word.get.value.get)

    val availableValues
    = problem
      .availableValues
      .filter(word => word.length == variable.size && !insertedWords.contains(word))

    val filledIndices = getIndicesThatAreFilledByVariable(problem, variable).toList
    val variablesToCheck = getVariablesThatConflictWithIndexes(problem, filledIndices)
    defineAvailableValues(problem, filledIndices, availableValues, variablesToCheck)
  }

  def defineAvailableValues[V <: QuizVariable : TypeTag : ClassTag](
                                                                     problem: CSP[V],
                                                                     filledIndicesByVariable: List[Int],
                                                                     availableValues: List[String],
                                                                     variablesToCheck: List[V]
                                                                   ): List[String] =
    availableValues
      .map { availableValue =>
        defineAvailableValue(problem, filledIndicesByVariable, availableValue, variablesToCheck)
      }
      .filter(_.isDefined)
      .map(_.get)

  def defineAvailableValue[V <: QuizVariable : TypeTag : ClassTag](
                                                                    problem: CSP[V],
                                                                    filledIndicesByVariable: List[Int],
                                                                    availableValue: String,
                                                                    variablesToCheck: List[V]
                                                                  ) = {
    val IsWordProperForVariable = !filledIndicesByVariable
      .indices
      .zip(availableValue)
      .exists { case (filledIndice: Int, char: Char) =>
        variablesToCheck.exists { variableToCheck =>
          val valueInCheckedVariable = valueOfVariableAtIndex(problem, variableToCheck, filledIndicesByVariable(filledIndice))
          valueInCheckedVariable.isDefined && valueInCheckedVariable.get != char
        }
      }
    if (IsWordProperForVariable) Option(availableValue) else Option.empty[String]
  }

  def calculateDomainForEachVariables[V <: QuizVariable](problem: CSP[V], index:Int) = {
    problem.variables.indices.foreach { i =>
      problem.domains(i) = domainPuzzle.calculateDomainOfVariableIndex(problem, i)
    }
    problem.domains(index)

  }

}
