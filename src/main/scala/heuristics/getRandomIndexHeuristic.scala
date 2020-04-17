package heuristics

import model.CSP

object getRandomIndexHeuristic {

  def getRandomIndex[V](problem: CSP[V], allowedIndexes: List[Int]): Option[Int] = {

    val indexToResolve = getRandomElement(allowedIndexes)
    Option(indexToResolve)
  }

  def getRandomElement[A](seq: Seq[A]): A = seq((new util.Random).nextInt(seq.length))
}
