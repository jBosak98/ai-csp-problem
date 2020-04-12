package heuristics

import model.CSPProblem

import scala.reflect.ClassTag

object lowestDomainSizeHeuristic {
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
