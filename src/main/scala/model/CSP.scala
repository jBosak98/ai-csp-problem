package model

case class CSP[T](variables: Array[Option[T]],
                  domains: Array[List[T]],
                  isConstant: Array[Boolean],
                  availableValues:List[T],
                  size: (Int, Int)
) extends CSPModel[T]

trait CSPModel[T]{
    val variables: Array[Option[T]]
    val domains: Array[List[T]]
    val isConstant: Array[Boolean]
    val availableValues:List[T]
    val size: (Int, Int)

}