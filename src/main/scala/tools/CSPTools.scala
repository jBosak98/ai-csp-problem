package tools

import model.CSPProblem

trait CSPTools {
  def isProperlyResolved(problem: CSPProblem): Boolean

  def areAllFieldsFilled(problem: CSPProblem): Boolean

  def isProperlyFilled(problem: CSPProblem): Boolean

  /*rowNumber scope = {1, ..., 9}
  columnNumber scope = {1, ..., 9}
  index scope = {0, ..., 80}
 */
}
