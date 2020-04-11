package tools

import model.CSPProblem
import tools.calculateDomain.calculateDomainOfIndex

class SudokuResolver(val tools:CSPTools) extends resolveCSP {

  def resolveProblem(problem:  CSPProblem): Boolean = {
    val position = getNextIndexToResolve(problem)
    if (position.isDefined) {
      resolveField(problem, position.get)
    }

    val isSolved = tools.isProperlyResolved(problem)
    if (!isSolved) println("Problem has no solution")
    isSolved
  }

  def resolveField(sudoku: CSPProblem, index: Int): Boolean = {
    val domain = calculateDomainOfIndex(sudoku, index)

    def isValueProper: Int => Boolean = { value =>
      sudoku.values(index) = Option(value)
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

    sudoku.values(index) = domain.find(isValueProper)
    true
  }

  def getNextIndexToResolve(problem: CSPProblem): Option[Int] = {

    def getOnlyEmptyValues: Int => Boolean = {
      index => !problem.isConstant(index) && problem.values(index).isEmpty
    }

    val filteredIndexes: List[Int] = problem
      .values
      .indices
      .filter(getOnlyEmptyValues).toList
    if (filteredIndexes.isEmpty) return Option.empty[Int]

    val indexToResolve = filteredIndexes.reduce((acc: Int, e: Int) => {
      problem.domains(e) = calculateDomainOfIndex(problem, e)
      if (problem.domains(acc).length > problem.domains(e).length)
        e
      else
        acc
    })
    Option(indexToResolve)
  }

}

