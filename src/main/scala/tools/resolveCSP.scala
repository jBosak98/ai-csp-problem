package tools

import model.CSPProblem

import scala.reflect.ClassTag


trait resolveCSP {
  def resolveProblem[T:ClassTag](problem: CSPProblem[T]):Boolean

  def getNextIndexToResolve[T:ClassTag](problem: CSPProblem[T]): Option[Int]
}
