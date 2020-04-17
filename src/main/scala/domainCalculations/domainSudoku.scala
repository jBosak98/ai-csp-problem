package domainCalculations

import model.{CSP, QuizVariable}
import tools.sudokuTools.{getBox, getColumnAtIndex, getRowAtIndex}

import scala.reflect.ClassTag

object domainSudoku {

  def createVariableSudoku(a:Option[Int], sudokuValue:Option[String]): Option[Int] = {
    if (sudokuValue.isDefined) sudokuValue.get.toIntOption
    else Option.empty[Int]
  }

  def calculateDomain[T: ClassTag, V](sudoku: CSP[Int]): CSP[Int] = {

    def filterDefinedValues: Int => Boolean = { index => sudoku.variables(index).isDefined && sudoku.isConstant(index) }

    def getDomainForEach: Int => Any = { index =>
      sudoku.domains(index) =
        if (filterDefinedValues(index)) List[String]()
        else
          calculateDomainOfIndex(sudoku, index)
    }

    sudoku
      .variables
      .indices
      .foreach(getDomainForEach)

    sudoku
  }

  def calculateDomainOfIndex[V <: Int:ClassTag](sudoku: CSP[V], index: Int): List[String] = {
    val row = getRowAtIndex(sudoku.variables, sudoku.size, index)
    val column = getColumnAtIndex(sudoku.variables, sudoku.size, index)
    val box = getBox(sudoku.variables, sudoku.size, index)
    val domainComplement = (row ++ column ++ box).filter(_.isDefined).map(_.get).distinct
    val domain =
      sudoku
        .availableValues
        .filter(e => !domainComplement.map(_.toString).contains(e))
    domain.map(_.toString)
  }

  def calculateDomainOfRelatedFields[V <: Int:ClassTag](sudoku: CSP[V], index: Int) = {
    val indexes = sudoku.variables.indices.toArray
    val rowIndices = getRowAtIndex(indexes, sudoku.size, index)
    val columnIndices = getColumnAtIndex(indexes, sudoku.size, index)
    val boxIndices = getBox(indexes, sudoku.size, index)
    if(index == 40 || index == 27){
      sudoku.domains(16) = calculateDomainOfIndex[V](sudoku, 16)
      sudoku.domains(9) = calculateDomainOfIndex[V](sudoku, 9)
    }
    val valuesToCalculate = ((rowIndices ++ columnIndices ++ boxIndices ))
      .distinct
      .filter(i => sudoku.variables(i).isEmpty)

    valuesToCalculate
      .foreach(i => {
      sudoku.domains(i) = calculateDomainOfIndex[V](sudoku, i)
    })
    sudoku.domains(index)
  }



}
