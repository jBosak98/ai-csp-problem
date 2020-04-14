package model

object types {
  type ValueSudoku = Option[Int]
  type Domain = List[Int]
  type Validator[V] = CSPProblem[V] => Boolean
  //  type QuizVariable = (In)
}
