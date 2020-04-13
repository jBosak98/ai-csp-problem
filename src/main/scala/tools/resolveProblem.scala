package tools

import model.CSPProblem

import scala.reflect.ClassTag

object resolveProblem {


  def resolveProblem[T: ClassTag,V]
  (indexSelectionHeuristic: CSPProblem[T,V] => Option[Int])
  (problem: CSPProblem[T,V], tools: CSPTools): Boolean = {
    val position = indexSelectionHeuristic(problem)

    val resolveField = resolveFieldGenerator[T,V](indexSelectionHeuristic)(tools)
    if (position.isDefined) {
      resolveField(problem, position.get)
    }

    val isSolved = tools.isProperlyResolved(problem)
    if (!isSolved) println("Problem has no solution")
    isSolved
  }

  def resolveFieldGenerator[T: ClassTag,V]
  (getNextIndex: CSPProblem[T,V] => Option[Int])
  (tools: CSPTools): (CSPProblem[T,V], Int) => Boolean = {

    def resolveField(problem: CSPProblem[T,V], index: Int): Boolean = {
      val domain = problem.constraint(problem, index)

      def isValueProper: V => Boolean = { value =>
        problem.variables(index) = Option(value)
        domainSudoku.calculateDomainOfRelatedFields(problem, index)
        if (!domainSudoku.isDomainProper(problem)) {
          false
        } else {
          val indexToResolve = getNextIndex(problem)
          if (indexToResolve.isEmpty) {
            true
          } else {
            resolveField(problem, indexToResolve.get)
            tools.isProperlyFilled(problem)
          }
        }


      }

      problem.variables(index) = domain.find(isValueProper)
      true
    }

    resolveField
  }


}

