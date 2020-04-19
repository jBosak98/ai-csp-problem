package tools

import model.{CSP, CSPProblemValidator, CSPResult, DomainCalculator, QuizVariable}

import scala.reflect.runtime.universe._

object resolveProblem {

  var steps = 0

  def resolveProblem[V: TypeTag]
  (indexSelectionHeuristic: CSP[V] => Option[Int], domainCalculator: DomainCalculator[V])
  (problem: CSP[V], validatorCSP: CSPProblemValidator[V], forwardChecking:Boolean): CSPResult = {
    val position = indexSelectionHeuristic(problem)

    val domainCalc = if (forwardChecking) domainCalculator else DomainCalculator(
      calculateDomainOfIndex = domainCalculator.calculateDomainOfIndex,
      calculateDomainForEachVariables = {(_:CSP[V], _:Int) => List.empty[String]},
      calculateDomainOfDependents = {(_:CSP[V], _:Int) => List.empty[String]},
      createVariableFromDomainValue = domainCalculator.createVariableFromDomainValue
    )

    domainCalculator.calculateDomainForEachVariables(problem, 0)
    val resolveField = resolveFieldGenerator[V](indexSelectionHeuristic, domainCalc)(validatorCSP)
    if (position.isDefined) {
      resolveField(problem, position.get)
    }
    println(s"steps: ${steps}")

    val isSolved = validatorCSP.isProperlyResolved(problem)
    if (!isSolved) println("Problem has no solution")
    val result = CSPResult(0,0L,steps, isSolved)
    steps = 0
    result
  }

  def resolveFieldGenerator[V: TypeTag]
  (getNextIndex: CSP[V] => Option[Int], domainCalculator: DomainCalculator[V])
  (validatorCSP: CSPProblemValidator[V]): (CSP[V], Int) => Boolean = {
    val calculateDomainOfIndex = domainCalculator.calculateDomainOfIndex
    val createVariableFromDomainValue = domainCalculator.createVariableFromDomainValue
    val calculateDomainOfDependents = domainCalculator.calculateDomainOfDependents

    val isDomainProper = validatorCSP.isDomainProper
    val isProperlyFilled = validatorCSP.isProperlyFilled


    def resetVariableAndRefreshDomain(problem: CSP[V], index: Int) = {
      problem.variables(index) = domainCalculator.createVariableFromDomainValue(problem.variables(index), Option.empty)
      calculateDomainOfDependents(problem, index)
      false
    }

    def resolveField(problem: CSP[V], index: Int): Boolean = {

      val domain = calculateDomainOfIndex(problem, index)

      //printProblem.printProblem[V](problem.asInstanceOf[CSPProblem[V]])

      def isValueProper: String => Boolean = { value =>
        problem.variables(index) = createVariableFromDomainValue(problem.variables(index), Option(value))
        domainCalculator.calculateDomainOfDependents(problem, index)
        steps = steps + 1

        if (isDomainProper(problem)) {
          val indexToResolve = getNextIndex(problem)
          if (indexToResolve.isEmpty) true

          else {
            resolveField(problem, indexToResolve.get)
            if (isProperlyFilled(problem)) true
            else resetVariableAndRefreshDomain(problem, index)

          }
        } else resetVariableAndRefreshDomain(problem, index)


      }

      val properValue = domain.find(isValueProper)

      problem.variables(index) = createVariableFromDomainValue(problem.variables(index), properValue)
      true
    }

    resolveField
  }


}

