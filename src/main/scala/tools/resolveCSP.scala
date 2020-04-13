package tools

import model.CSPProblem

import scala.reflect.ClassTag


trait resolveCSP {
  def resolveProblem[T:ClassTag,V](problem: CSPProblem[T,V]):Boolean

  def getNextIndexToResolve[T:ClassTag,V](problem: CSPProblem[T,V]): Option[Int]
}
