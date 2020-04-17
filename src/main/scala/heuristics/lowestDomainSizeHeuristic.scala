package heuristics

import model.CSP


object lowestDomainSizeHeuristic {

  def getIndexWithTheLowestDomainSize[V](problem: CSP[V], allowedIndexes: List[Int]): Option[Int] = {
    val indexToResolve = allowedIndexes.reduce((acc: Int, e: Int) => {
      val isDomainSizeBigger = problem.domains(acc).length > problem.domains(e).length
      if (isDomainSizeBigger) e else acc
    })

    Option(indexToResolve)
  }
}