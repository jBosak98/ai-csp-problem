package tools

import model.{CSPProblem, CSPProblemValidator, QuizVariable}

object resolveProblem {


  def resolveProblem[V]
  (indexSelectionHeuristic: CSPProblem[V] => Option[Int], createVariable: (V, Option[String]) => Option[V])
  (problem: CSPProblem[V], validatorCSP: CSPProblemValidator[V]): Boolean = {
    val position = indexSelectionHeuristic(problem)
    val resolveField = resolveFieldGenerator[V](indexSelectionHeuristic, createVariable)(validatorCSP)
    if (position.isDefined) {
      resolveField(problem, position.get)
    }
    val isSolved = validatorCSP.isProperlyResolved(problem)
    if (!isSolved) println("Problem has no solution")
    isSolved
  }

  def resolveFieldGenerator[V]
  (getNextIndex: CSPProblem[V] => Option[Int], createVariable: (V, Option[String]) => Option[V])
  (validatorCSP: CSPProblemValidator[V]): (CSPProblem[V], Int) => Boolean = {

    def resolveField(problem: CSPProblem[V], index: Int): Boolean = {

      val domain = problem.constraint(problem, index)
      printProblem.printProblem[QuizVariable](problem.asInstanceOf[CSPProblem[QuizVariable]])

      def isValueProper: String => Boolean = { value =>
        problem.variables(index) = createVariable(problem.variables(index).get, Option(value))
        //        problem.domains.indices.map { index => //domainSudoku.calculateDomainOfRelatedFields(problem, index)
        //          problem.domains(index) = problem.constraint(problem,index)
        //        }.toArray
        //        if (!domainSudoku.isDomainProper(problem)) {
        //          false
        //        } else {
        val indexToResolve = getNextIndex(problem)
        if (indexToResolve.isEmpty) {
          problem.variables.foreach(println)
          println("indexToResolve.isEmpty")
          true
        } else {
          resolveField(problem, indexToResolve.get)
          validatorCSP.isProperlyFilled(problem)
        }
        //        }


      }

      val x = domain.find(isValueProper)

      problem.variables(index) = createVariable(
        problem.variables(index).get,
        x
      )
      true
    }

    resolveField
  }


}

