package heuristics

import model.CSP

object getFirstEmptyIndexHeuristic {
  def getFirstEmptyIndex[V](problem: CSP[V], allowedIndexes:List[Int]): Option[Int] = {
    val indexToResolve = allowedIndexes.min
    Option(indexToResolve)
  }
}
