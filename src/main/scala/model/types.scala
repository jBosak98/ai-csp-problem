package model

object types {
  type ValueSudoku = Option[Int]
  type Validator[V] = CSP[V] => Boolean
  type DomainCalculatorFun[V] = (CSP[V], Int) => List[String]
}
