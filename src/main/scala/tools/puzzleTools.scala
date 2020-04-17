package tools


import model.{CSP, QuizVariable}
import tools.sudokuTools.{column, getColumnNumber, getIndicesOfRow, getRowNumber}
import scala.reflect.runtime.universe._

import scala.reflect.ClassTag

object puzzleTools {

  def getIndicesOfColumn(columnNumber: Int, size: (Int, Int)): Array[Int] = {
    val indices = (0 until size._1 * size._2).toArray
    column(indices, size, columnNumber)
  }

  def getLineByPuzzleIndex[V <: QuizVariable : ClassTag](problem: CSP[V], index: Int, isVertical: Boolean) = {
    if (isVertical) {
      getIndicesOfColumn(getColumnNumber(index, problem.size).get, problem.size)
    } else
      getIndicesOfRow(getRowNumber(index, problem.size).get, problem.size)
  }

  def getWholeLineOfVariable[V <: QuizVariable : ClassTag](problem: CSP[V], variable: V) =
    getLineByPuzzleIndex(problem, variable.index, variable.isVertical)

  def getLineOfVariableByPuzzleIndex[V <: QuizVariable : TypeTag : ClassTag](problem: CSP[V], index: Int, getVertical: Option[Boolean]) = {
    if (getVertical.isEmpty) List[Int]().toArray
    else {
      val variable = getVariable(problem, index, getVertical.get)
      if (variable.isEmpty) List[Int]().toArray
      else getWholeLineOfVariable(problem, variable.get
      )
    }
  }

  def getIndicesThatAreFilledByVariable[V <: QuizVariable : TypeTag : ClassTag](problem: CSP[V], variable: V) = {
    val longLine = getWholeLineOfVariable(problem, variable)
    val indexInLongLine = longLine.indexOf(variable.index)
    longLine.slice(indexInLongLine, indexInLongLine + variable.size).toList

    val wholeLine = getLineOfVariableByPuzzleIndex(problem, variable.index, Option(variable.isVertical))
    val start = wholeLine.indexOf(variable.index)
    wholeLine.slice(start, start + variable.size)
  }


  def valueOfVariableAtIndex[V <: QuizVariable : TypeTag : ClassTag]
  (problem: CSP[V], variable: V, index: Int) = {
    val indicesOfVariable = getIndicesThatAreFilledByVariable[V](problem, variable)
    val indexInsideVariable = indicesOfVariable.indexOf(index)

    if (indexInsideVariable == -1 || variable.value.getOrElse("") == "") Option.empty[Char]
    else Option(variable.value.get.charAt(indexInsideVariable))
  }

  def getVariablesThatConflictWithIndex[V <: QuizVariable : TypeTag : ClassTag]
  (problem: CSP[V], index: Int) = {
    problem
      .variables
      .filter(_.isDefined)
      .map(_.get)
      .filter(variable =>
        getIndicesThatAreFilledByVariable(problem, variable).exists(_.equals(index))
      )
  }


  def getVariablesThatConflictWithIndexes[V <: QuizVariable : TypeTag : ClassTag]
  (problem: CSP[V], indexes: List[Int]): List[V] =
    indexes.flatMap { index =>
      getVariablesThatConflictWithIndex(problem, index)
    }.distinct


  def getVariable[V <: QuizVariable : TypeTag : ClassTag]
  (problem: CSP[V], index: Int, isVertical: Boolean) =
    problem
      .variables
      .filter(_.isDefined)
      .map(_.get)
      .find { variable =>
        index == variable.index && isVertical == variable.isVertical
      }

  def getIndexOfVariable[V <: QuizVariable : TypeTag : ClassTag]
  (problem: CSP[V], puzzleIndex: Int, isVertical: Boolean) =
    problem
      .variables
      .zipWithIndex
      .filter(_._1.isDefined)
      .find { case (variable: Option[V], _: Int) =>
        puzzleIndex == variable.get.index && isVertical == variable.get.isVertical
      }
      .get
      ._2


  def getDependentVariables[V <: QuizVariable : TypeTag : ClassTag](problem: CSP[V], index: Int) = {
    val variable = problem.variables(index).get
    val indexesInPuzzle = getIndicesThatAreFilledByVariable(problem, variable).toList
    val indexesOfDependentVariables = getVariablesThatConflictWithIndexes(problem, indexesInPuzzle)
      .map(v =>
        getIndexOfVariable(problem, v.index, v.isVertical)
      )
    indexesOfDependentVariables
  }
}
