package tools

import model.CSPProblem


trait resolveCSP {
  def resolveProblem(problem: CSPProblem):Boolean

  def getNextIndexToResolve(problem: CSPProblem): Option[Int]
}
