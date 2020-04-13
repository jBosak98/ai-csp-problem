package tools

import model.CSPProblem

trait CSPTools {
  def isProperlyResolved[T,V](problem: CSPProblem[T,V]): Boolean

  def areAllFieldsFilled[T,V](problem: CSPProblem[T,V]): Boolean

  def isProperlyFilled[T,V](problem: CSPProblem[T,V]): Boolean

  /*rowNumber scope = {1, ..., 9}
  columnNumber scope = {1, ..., 9}
  index scope = {0, ..., 80}
 */
}
