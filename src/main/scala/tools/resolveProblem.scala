package tools

import model.CSPProblem

import scala.reflect.ClassTag

object resolveProblem {


  def resolveProblem[T: ClassTag]
  (indexSelectionHeuristic: CSPProblem[T] => Option[Int])
  (problem: CSPProblem[T], tools: CSPTools): Boolean = {
    val position = indexSelectionHeuristic(problem)

    val resolveField = resolveFieldGenerator[T](indexSelectionHeuristic)(tools)
    if (position.isDefined) {
      resolveField(problem, position.get)
    }

    val isSolved = tools.isProperlyResolved(problem)
    if (!isSolved) println("Problem has no solution")
    isSolved
  }

  def resolveFieldGenerator[T: ClassTag]
  (getNextIndex: CSPProblem[T] => Option[Int])
  (tools: CSPTools): (CSPProblem[T], Int) => Boolean = {

    def resolveField(problem: CSPProblem[T], index: Int): Boolean = {
      val domain = problem.constraint(problem, index)

      def isValueProper: T => Boolean = { value =>
        problem.variables(index) = Option(value)
        calculateDomain.calculateDomainOfRelatedFields(problem, index)
        if (!calculateDomain.isDomainProper(problem)) {
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

