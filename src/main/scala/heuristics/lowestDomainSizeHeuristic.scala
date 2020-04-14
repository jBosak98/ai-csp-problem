package heuristics

import model.CSPProblem

import scala.reflect.ClassTag

object lowestDomainSizeHeuristic {

  def getNextIndexToResolve[T:ClassTag,V]
  (filterFun:((Option[V], Int)) => Boolean)
  (problem: CSPProblem[T,V])
  : Option[Int] = {

    val filteredIndexes = problem
      .variables.zipWithIndex
      .filter(filterFun)
      .map(_._2)
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