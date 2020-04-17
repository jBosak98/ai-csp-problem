package heuristics

import model.CSP

object nextIndexHeuristicGenerator {
  def createNextIndexHeuristic[V]
  (filterFun: ((Option[V], Int)) => Boolean)
  (heuristicFunction: (CSP[V], List[Int]) => Option[Int]): CSP[V] => Option[Int] = {
    { problem: CSP[V] =>
      val filteredIndexes = problem
        .variables.zipWithIndex
        .filter(filterFun)
        .map(_._2).toList
      if (filteredIndexes.isEmpty) Option.empty[Int]
      else heuristicFunction(problem, filteredIndexes)
    }
  }
}
