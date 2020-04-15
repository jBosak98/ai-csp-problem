package tools

import domainCalculations.domainSudoku

import scala.reflect.runtime.universe._
import model.{CSP, CSPProblemValidator, DomainCalculator, QuizVariable}

object resolveProblem {

  var steps = 0

  def resolveProblem[V: TypeTag]
  (indexSelectionHeuristic: CSP[V] => Option[Int], domainCalculator: DomainCalculator[V])
  (problem: CSP[V], validatorCSP: CSPProblemValidator[V]): Boolean = {
    val position = indexSelectionHeuristic(problem)
    val resolveField = resolveFieldGenerator[V](indexSelectionHeuristic, domainCalculator)(validatorCSP)
    if (position.isDefined) {
      resolveField(problem, position.get)
    }
    println(s"steps: ${steps}")
    val isSolved = validatorCSP.isProperlyResolved(problem)
    if (!isSolved) println("Problem has no solution")
    isSolved
  }

  def resolveFieldGenerator[V: TypeTag]
  (getNextIndex: CSP[V] => Option[Int], domainCalculator: DomainCalculator[V])
  (validatorCSP: CSPProblemValidator[V]): (CSP[V], Int) => Boolean = {

    def resolveField(problem: CSP[V], index: Int): Boolean = {

      val domain = domainCalculator.calculateDomainOfIndex(problem, index)
      //      printProblem.printProblem[V](problem.asInstanceOf[CSPProblem[V]])

      def isValueProper: String => Boolean = { value =>
        problem.variables(index) = domainCalculator.createVariableFromDomainValue(problem.variables(index), Option(value))
        domainCalculator.calculateDomainOfDependents(problem, index)
        steps = steps + 1

        if (!domainSudoku.isDomainProper(problem)) {
          false
        } else {
          val indexToResolve = getNextIndex(problem)
          if (indexToResolve.isEmpty) {
            true
          } else {
            resolveField(problem, indexToResolve.get)
            validatorCSP.isProperlyFilled(problem)
          }
        }
      }

      problem.variables(index) = domainCalculator.createVariableFromDomainValue(
        problem.variables(index),
        domain.find(isValueProper)
      )
      true
    }

    resolveField
  }


}

