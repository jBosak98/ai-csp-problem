package tools

import model.CSPProblem


trait resolveCSP {
  def resolveProblem(problem: CSPProblem, tools:CSPTools):Boolean

  def getNextIndexToResolve(problem: CSPProblem, tools:CSPTools): Option[Int]
}
