package tools

import model.{CSP, QuizVariable}
import tools.sudokuTools._

object domainPuzzle {


  def createQuizVariable(variable:Option[QuizVariable], word:Option[String]): Option[QuizVariable] = {
    Option(QuizVariable(
      index = variable.get.index,
      value = word,
      isVertical = variable.get.isVertical,
      size = variable.get.size
    ))
  }

  def calculateDomainOfVariableIndex[V <: QuizVariable](problem: CSP[V], index: Int) = {
    val variable = problem.variables(index).get

    problem.domains(index) = calculateDomainForVariable(
      problem.asInstanceOf[CSP[QuizVariable]],
      variable)
      .asInstanceOf[List[String]]
    problem.domains(index)
  }


  def calculateDomainOfDependents[V<: QuizVariable](problem: CSP[V], index: Int) = {
    val variable = problem.variables(index)
    val indexesInPuzzle = getLineOfVariable(problem,index, Option(variable.get.isVertical))

    val variablesIndexes = problem
      .variables
      .zipWithIndex
      .filter(_._1.isDefined)
      .filter { case (variable:Option[V], _:Int) =>
        getIndicesThatAreFilledByVariable(problem.asInstanceOf[CSP[QuizVariable]], variable.get)
          .exists(i => indexesInPuzzle.contains(i))
      }.map(_._2)

    variablesIndexes.foreach { variableIndex:Int =>
      problem.domains(variableIndex) = calculateDomainOfVariableIndex(problem, variableIndex)
    }
    problem.domains(index)
  }

  def getLineOfVariable[V](problem: CSP[V], index: Int, getVertical: Option[Boolean]) =
    getVertical match {
      case isVertical if isVertical.isEmpty => {
        val indicesOfColumn = getIndicesOfColumn(getColumnNumber(index, problem.size).get, problem.size)
        val indicesOfRow = getIndicesOfRow(getRowNumber(index, problem.size).get, problem.size)
        (indicesOfColumn ++ indicesOfRow).distinct
      }
      case isVertical if isVertical.get => getIndicesOfColumn(getColumnNumber(index, problem.size).get, problem.size)
      case isVertical if !isVertical.get => getIndicesOfRow(getRowNumber(index, problem.size).get, problem.size)
    }

  def calculateDomainForEachVariables[V <: QuizVariable](problem: CSP[V]) = {
    problem.variables.indices.foreach { i =>
      domainPuzzle.calculateDomainOfVariableIndex(problem, i)
    }

  }

  def getVariablesThatConflictWithIndexes(
                                           problem: CSP[QuizVariable],
                                           indexes: List[Int],
                                           getVertical: Option[Boolean]): List[QuizVariable] =
    indexes.flatMap { index =>
      getVariablesThatConflictWithIndex(problem, index, getVertical)
    }.distinct


  def getVariablesThatConflictWithIndex(problem: CSP[QuizVariable], index: Int, getVertical: Option[Boolean]) = {
    val indices = getLineOfVariable(problem,index,getVertical)
    problem
      .variables
      .filter(_.isDefined)
      .map(_.get)
      .filter { variable =>
        getIndicesThatAreFilledByVariable(problem, variable).exists(i => indices.contains(i))
    }
  }

  def calculateDomainForVariable(problem: CSP[QuizVariable], variable: QuizVariable) = {
    val availableValues = problem.availableValues.filter(_.length == variable.size)
    val filledIndices = getIndicesThatAreFilledByVariable(problem, variable)
    val variablesToCheck = getVariablesThatConflictWithIndexes(problem, filledIndices, Option(!variable.isVertical))
    defineAvailableValues(problem, filledIndices, availableValues, variablesToCheck)
  }


  def defineAvailableValues(
                             problem: CSP[QuizVariable],
                             filledIndicesByVariable: List[Int],
                             availableValues: List[String],
                             variablesToCheck: List[QuizVariable]
                           ): List[String] =
    availableValues.map { availableValue =>
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
    }.filter(_.isDefined).map(_.get)


  def valueOfVariableAtIndex(problem: CSP[QuizVariable], variable: QuizVariable, index: Int) = {
    val indicesOfVariable = getIndicesThatAreFilledByVariable(problem, variable)
    val indexInsideVariable = indicesOfVariable.indexOf(index)

    if (indexInsideVariable == -1 || variable.value.getOrElse("") == "") Option.empty[Char]
    else Option(variable.value.get.charAt(indexInsideVariable))

  }

  def getWholeLineIndicesThatAreFilledByVariable(problem: CSP[QuizVariable], variable: QuizVariable) =
    if (variable.isVertical)
      getIndicesOfColumn(getColumnNumber(variable.index, problem.size).get, problem.size)
    else
      getIndicesOfRow(getRowNumber(variable.index, problem.size).get, problem.size)


  def getIndicesThatAreFilledByVariable(problem: CSP[QuizVariable], variable: QuizVariable) = {
    val longLine = getWholeLineIndicesThatAreFilledByVariable(problem, variable)
    val indexInLongLine = longLine.indexOf(variable.index)
    longLine.slice(indexInLongLine, indexInLongLine + variable.size).toList
  }
}
