package tools

import model.CSPProblem

trait CSPTools {
  def isProperlyResolved[T](problem: CSPProblem[T]): Boolean

  def areAllFieldsFilled[T](problem: CSPProblem[T]): Boolean

  def isProperlyFilled[T](problem: CSPProblem[T]): Boolean

  /*rowNumber scope = {1, ..., 9}
  columnNumber scope = {1, ..., 9}
  index scope = {0, ..., 80}
 */
}
