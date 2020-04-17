package domainCalculations

import model.{CSP, QuizVariable}
import tools.sudokuTools.{column, getColumnNumber, getIndicesOfRow, getRowNumber}

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

object domainPuzzle {


  def createQuizVariable(problem:CSP[QuizVariable])(variable:Option[QuizVariable], word:Option[String]): Option[QuizVariable] = {
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

  def getDependentVariables[V<: QuizVariable:TypeTag:ClassTag](problem: CSP[V], index: Int) = {
    val variable = problem.variables(index).get
    val indexesInPuzzle = getIndicesThatAreFilledByVariable(problem, variable).toList
//    problem.domains(index) = calculateDomainOfVariableIndex(problem, index)

    val indexesOfDependentVariables = getVariablesThatConflictWithIndexes(problem, indexesInPuzzle)
      .map{ v =>
      getIndexOfVariable(problem, v.index, v.isVertical)
    }
    indexesOfDependentVariables
//    val variablesIndexes = problem
//      .variables
//      .zipWithIndex
//      .filter(_._1.isDefined)
//      .filter { case (variable:Option[V], _:Int) =>
//        getIndicesThatAreFilledByVariable(problem.asInstanceOf[CSP[QuizVariable]], variable.get)
//          .exists(i => indexesInPuzzle.contains(i))
//      }.map(_._2)
//    variablesIndexes
  }


  def calculateDomainOfDependents[V<: QuizVariable:TypeTag:ClassTag](problem: CSP[V], index: Int) = {

    val dependentVariablesIndexes = getDependentVariables(problem, index)
    dependentVariablesIndexes
      .filter(i => problem.variables(i).get.value.isEmpty)
      .foreach { variableIndex:Int =>
//      println(problem.variables(index))
//      println(problem.variables(variableIndex))
//      println(problem.domains(variableIndex))
      problem.domains(variableIndex) = calculateDomainOfVariableIndex(problem, variableIndex)
//      println(problem.domains(variableIndex))
//        printProblem.printProblem(problem)
//      println()
//      println()
    }
    problem.domains(index)
  }



  def calculateDomainForEachVariables[V <: QuizVariable](problem: CSP[V]) = {
    problem.variables.indices.foreach { i =>
      problem.domains(i) =  domainPuzzle.calculateDomainOfVariableIndex(problem, i)
    }

  }

  def getVariable[V <: QuizVariable: TypeTag:ClassTag]
  (problem:CSP[V], index:Int, isVertical:Boolean) =
    problem
      .variables
      .filter(_.isDefined)
      .map(_.get)
      .find { variable =>
      index == variable.index && isVertical == variable.isVertical
    }

  def getIndexOfVariable[V <: QuizVariable: TypeTag:ClassTag]
  (problem:CSP[V], puzzleIndex:Int, isVertical:Boolean) =
    problem
      .variables
      .zipWithIndex
      .filter(_._1.isDefined)
      .find { case (variable:Option[V], _:Int) =>
        puzzleIndex == variable.get.index && isVertical == variable.get.isVertical
      }
      .get
      ._2


  def getVariablesThatConflictWithIndexes[V <: QuizVariable:TypeTag:ClassTag]
  (problem: CSP[V], indexes: List[Int]): List[V] =
    indexes.flatMap { index =>
      getVariablesThatConflictWithIndex(problem, index)
    }.distinct


  def getVariablesThatConflictWithIndex[V <: QuizVariable: TypeTag:ClassTag]
  (problem: CSP[V], index: Int) = {
//    val indices = getLineOfVariableByIndex(problem, index, getVertical)
    problem
      .variables
      .filter(_.isDefined)
      .map(_.get)
      .filter ( variable =>
        getIndicesThatAreFilledByVariable(problem, variable).exists(_.equals(index))
    )
  }

  def calculateDomainForVariable[V <: QuizVariable:TypeTag : ClassTag]
  (problem: CSP[V], variable: V) = {

    val insertedWords = problem.variables
      .collect( word => if(word.isDefined && word.get.value.isDefined) word.get.value.get)

    val availableValues
    = problem
      .availableValues
      .filter(word => word.length == variable.size && !insertedWords.contains(word))

    val filledIndices = getIndicesThatAreFilledByVariable(problem, variable).toList
    val variablesToCheck = getVariablesThatConflictWithIndexes(problem, filledIndices)
    defineAvailableValues(problem, filledIndices, availableValues, variablesToCheck)
  }



  def defineAvailableValues[V <: QuizVariable:TypeTag:ClassTag](
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

  def defineAvailableValue[V <: QuizVariable:TypeTag:ClassTag](
                                                                problem:CSP[V],
                                                                filledIndicesByVariable:List[Int],
                                                                availableValue:String,
                                                                variablesToCheck:List[V]
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

  def isCharacterInCharacterAtIndex[V <: QuizVariable:TypeTag:ClassTag]
  (problem:CSP[V]) = {

  }


  def valueOfVariableAtIndex[V <: QuizVariable:TypeTag:ClassTag]
  (problem: CSP[V], variable: V, index: Int) = {
    val indicesOfVariable = getIndicesThatAreFilledByVariable[V](problem, variable)
    val indexInsideVariable = indicesOfVariable.indexOf(index)

    if (indexInsideVariable == -1 || variable.value.getOrElse("") == "") Option.empty[Char]
    else Option(variable.value.get.charAt(indexInsideVariable))

  }

  def getWholeLineOfVariable[V <: QuizVariable:ClassTag](problem: CSP[V], variable: V) =
    getLineByPuzzleIndex(problem, variable.index, variable.isVertical)

  def getLineOfVariableByPuzzleIndex[V <: QuizVariable:TypeTag:ClassTag](problem: CSP[V], index: Int, getVertical: Option[Boolean]) = {
    if(getVertical.isEmpty) List[Int]().toArray
    else {
      val variable = getVariable(problem, index, getVertical.get)
      if(variable.isEmpty) List[Int]().toArray
      else getWholeLineOfVariable(problem, variable.get
      )
    }
  }

  def getLineByPuzzleIndex[V <: QuizVariable:ClassTag](problem:CSP[V], index:Int, isVertical:Boolean) = {
    if (isVertical) {
      getIndicesOfColumn(getColumnNumber(index, problem.size).get, problem.size)
    } else
      getIndicesOfRow(getRowNumber(index, problem.size).get, problem.size)
  }


  def getIndicesThatAreFilledByVariable[V <: QuizVariable:TypeTag:ClassTag](problem: CSP[V], variable: V) = {
    val longLine = getWholeLineOfVariable(problem, variable)
    val indexInLongLine = longLine.indexOf(variable.index)
    longLine.slice(indexInLongLine, indexInLongLine + variable.size).toList

    val wholeLine = getLineOfVariableByPuzzleIndex(problem, variable.index, Option(variable.isVertical))
    val start = wholeLine.indexOf(variable.index)
    wholeLine.slice(start, start + variable.size)
  }

  def getIndicesOfColumn(columnNumber: Int, size: (Int, Int)): Array[Int] = {
    val indices = (0 until size._1 * size._2).toArray
    column(indices, size, columnNumber)
  }
}
