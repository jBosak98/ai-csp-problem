package model

trait CSP[T]{
  val variables: Array[Option[T]]
  val domains: Array[List[T]]
  val isConstant: Array[Boolean]
  val availableValues:List[T]
  val size: (Int, Int)
}