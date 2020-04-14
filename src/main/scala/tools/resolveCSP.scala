package tools

import model.CSPProblem


trait resolveCSP {
  def resolveProblem[V](problem: CSPProblem[V]):Boolean

  def getNextIndexToResolve[V](problem: CSPProblem[V]): Option[Int]
}
