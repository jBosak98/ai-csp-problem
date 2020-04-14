package model

case class CSP[T, V](variables: Array[Option[V]],
                  domains: Array[List[T]],
                  isConstant: Array[Boolean],
                  availableValues:List[T],
                  size: (Int, Int)
) extends CSPModel[T, V]

trait CSPModel[T, V]{
    val variables: Array[Option[V]]
    val domains: Array[List[T]]
    val isConstant: Array[Boolean]
    val availableValues:List[T]
    val size: (Int, Int)

}