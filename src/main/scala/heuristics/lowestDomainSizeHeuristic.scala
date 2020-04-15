package heuristics

import model.CSP


object lowestDomainSizeHeuristic {

  def getNextIndexToResolve[V]
  (filterFun:((Option[V], Int)) => Boolean)
  (problem: CSP[V])
  : Option[Int] = {

    val filteredIndexes = problem
      .variables.zipWithIndex
      .filter(filterFun)
      .map(_._2)
    if (filteredIndexes.isEmpty) return Option.empty[Int]

    val indexToResolve = filteredIndexes.reduce((acc: Int, e: Int) => {
      if (problem.domains(acc).length > problem.domains(e).length)
        e
      else
        acc
    })

    Option(indexToResolve)
  }
}