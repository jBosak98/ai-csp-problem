package tools

import model.CSPProblem

import scala.reflect.ClassTag

class SudokuResolver(val tools:CSPTools) extends resolveCSP {

  def resolveProblem[T:ClassTag](problem:  CSPProblem[T]): Boolean = {
    val position = getNextIndexToResolve(problem)
    if (position.isDefined) {
      resolveField(problem, position.get)
    }

    val isSolved = tools.isProperlyResolved(problem)
    if (!isSolved) println("Problem has no solution")
    isSolved
  }

  def resolveField[T:ClassTag](sudoku: CSPProblem[T], index: Int): Boolean = {
    val domain = sudoku.constraint(sudoku, index)


    def isValueProper: T => Boolean = { value =>
      sudoku.variables(index) = Option(value)
      calculateDomain.calculateDomainOfRelatedFields(sudoku, index)
      if (!calculateDomain.isDomainProper(sudoku)) {
        false
      } else {
        val indexToResolve = getNextIndexToResolve(sudoku)
        if (indexToResolve.isEmpty) {
          true
        } else {
          resolveField(sudoku, indexToResolve.get)
          tools.isProperlyFilled(sudoku)
        }
      }


    }

    sudoku.variables(index) = domain.find(isValueProper)
    true
  }

  def getNextIndexToResolve[T:ClassTag](problem: CSPProblem[T]): Option[Int] = {

    def getOnlyEmptyValues: Int => Boolean = {
      index => !problem.isConstant(index) && problem.variables(index).isEmpty
    }

    val filteredIndexes: List[Int] = problem
      .variables
      .indices
      .filter(getOnlyEmptyValues).toList
    if (filteredIndexes.isEmpty) return Option.empty[Int]

    val indexToResolve = filteredIndexes.reduce((acc: Int, e: Int) => {
      problem.domains(e) = problem.constraint(problem, e)
      if (problem.domains(acc).length > problem.domains(e).length)
        e
      else
        acc
    })
    Option(indexToResolve)
  }
}

