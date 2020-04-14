package tools

import model.{CSPModel, QuizVariable}
import tools.sudokuTools._

object domainPuzzle {
  def calculateDomainOfVariableIndex[V <: QuizVariable](problem: CSPModel[String,V], index: Int) = {
    val variable = problem.variables(index).get

    problem.domains(index) = calculateDomainForVariable(
      problem.asInstanceOf[CSPModel[String, QuizVariable]],
      variable)
      .asInstanceOf[List[String]]
    println("calculateDomainOfVariableIndex",index, problem.domains(index))
    printProblem.printProblem(problem)
    println("\n\n")
    problem.domains(index)
  }



  def getVariablesThatConflictWithIndexes(
                                           problem:CSPModel[String, QuizVariable],
                                           indexes:List[Int],
                                           getVertical:Option[Boolean]):List[QuizVariable] =
    indexes.flatMap { index =>
      getVariablesThatConflictWithIndex(problem, index, getVertical)
    }.distinct


  def getVariablesThatConflictWithIndex(problem:CSPModel[String, QuizVariable], index:Int, getVertical:Option[Boolean]) = {
    val indices = if (getVertical.isEmpty) {
      val indicesOfColumn = getIndicesOfColumn(getColumnNumber(index, problem.size).get, problem.size)
      val indicesOfRow = getIndicesOfRow(getRowNumber(index, problem.size).get, problem.size)
      (indicesOfColumn ++ indicesOfRow).distinct
    } else if (getVertical.get) {
      getIndicesOfColumn(getColumnNumber(index, problem.size).get, problem.size)
    } else {
      getIndicesOfRow(getRowNumber(index, problem.size).get, problem.size)
    }
    problem.variables.filter(_.isDefined).map(_.get).filter { variable =>
      getIndicesThatAreFilledByVariable(problem, variable).exists(i => indices.contains(i))
    }
  }
  def calculateDomainForVariable(problem:CSPModel[String, QuizVariable], variable: QuizVariable) = {
    val availableValues = problem.availableValues.filter(_.length == variable.size)
    val filledIndices = getIndicesThatAreFilledByVariable(problem, variable)
    val variablesToCheck = getVariablesThatConflictWithIndexes(problem, filledIndices, Option(!variable.isVertical))
    println("filledIndices", filledIndices)
    println("availableValues", availableValues)
    defineAvailableValues(problem, filledIndices, availableValues, variablesToCheck)
  }

//  boat
//  ___#
//  ____

  def defineAvailableValues(
         problem: CSPModel[String, QuizVariable],
         filledIndicesByVariable:List[Int],
         availableValues:List[String],
         variablesToCheck:List[QuizVariable]
       ):List[String] =
    availableValues.map { availableValue =>
      val IsWordProperForVariable = !filledIndicesByVariable
        .indices
        .zip(availableValue)
        .exists {case (filledIndice: Int, char: Char) =>
        variablesToCheck.exists { variableToCheck =>
          val valueInCheckedVariable = valueOfVariableAtIndex(problem,variableToCheck,filledIndicesByVariable(filledIndice))
//          println(variableToCheck, valueInCheckedVariable, char, filledIndicesByVariable(filledIndice))
         valueInCheckedVariable.isDefined && valueInCheckedVariable.get != char
        }
      }
//      println(availableValue, IsWordProperForVariable)
      if(IsWordProperForVariable) Option(availableValue) else Option.empty[String]
    }.filter(_.isDefined).map(_.get)


  def valueOfVariableAtIndex(problem:CSPModel[String, QuizVariable], variable: QuizVariable, index:Int) = {
    val indicesOfVariable = getIndicesThatAreFilledByVariable(problem, variable)
    val indexInsideVariable = indicesOfVariable.indexOf(index)
    if(indexInsideVariable == -1 || variable.value.isEmpty) Option.empty[Char]
    else Option(variable.value.get.charAt(indexInsideVariable))

  }
  def getWholeLineIndicesThatAreFilledByVariable(problem:CSPModel[String, QuizVariable], variable: QuizVariable) =
    if(variable.isVertical)
      getIndicesOfColumn(getColumnNumber(variable.index, problem.size).get, problem.size)
    else
      getIndicesOfRow(getRowNumber(variable.index, problem.size).get, problem.size)


  def getIndicesThatAreFilledByVariable(problem:CSPModel[String, QuizVariable], variable: QuizVariable) = {
    val longLine = getWholeLineIndicesThatAreFilledByVariable(problem, variable)
    val indexInLongLine = longLine.indexOf(variable.index)
    longLine.slice(indexInLongLine, indexInLongLine + variable.size).toList
  }
}
