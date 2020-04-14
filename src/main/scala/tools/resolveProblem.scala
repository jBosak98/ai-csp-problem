package tools

import model.{CSPProblem, QuizVariable}

import scala.reflect.ClassTag

object resolveProblem {


  def resolveProblem[T: ClassTag,V]
  (indexSelectionHeuristic: CSPProblem[T,V] => Option[Int], createVariable:(V,T) => Option[V])
  (problem: CSPProblem[T,V], tools: CSPTools): Boolean = {
    val position = indexSelectionHeuristic(problem)
    val resolveField = resolveFieldGenerator[T,V](indexSelectionHeuristic, createVariable)(tools)
    println("pos", position)//TODO
    if (position.isDefined) {
      resolveField(problem, position.get)
    }

    val isSolved = tools.isProperlyResolved(problem)
    if (!isSolved) println("Problem has no solution")
    isSolved
  }

  def resolveFieldGenerator[T: ClassTag,V]
  (getNextIndex: CSPProblem[T,V] => Option[Int], createVariable:(V,T) => Option[V])
  (tools: CSPTools): (CSPProblem[T,V], Int) => Boolean = {

    def resolveField(problem: CSPProblem[T,V], index: Int): Boolean = {

      val domain = problem.constraint(problem, index)
      println("resolveField",index, domain)
//      printProblem.printProblem[String, QuizVariable](problem.asInstanceOf[CSPProblem[String,QuizVariable]])
      def isValueProper: T => Boolean = { value =>
        println("isValueProper", value)
        problem.variables(index) = createVariable(problem.variables(index).get, value)
//        domainSudoku.calculateDomainOfRelatedFields(problem, index)
//        if (!domainSudoku.isDomainProper(problem)) {
          false
//        } else {
          val indexToResolve = getNextIndex(problem)
          if (indexToResolve.isEmpty) {
            println("indexToResolve.isEmpty")
            true
          } else {
            resolveField(problem, indexToResolve.get)
//            tools.isProperlyFilled(problem)
          }
//        }


      }
//      println(index, problem.variables(index))
      val x = domain.find(isValueProper)
      println(x,domain)
      problem.variables(index) = createVariable(
        problem.variables(index).get,
        x.get
      )
      true
    }

    resolveField
  }


}

