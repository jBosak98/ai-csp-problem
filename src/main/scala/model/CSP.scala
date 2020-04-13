package model

case class CSP[T, V](variables: Array[Option[V]],
                  domains: Array[List[V]],
                  isConstant: Array[Boolean],
                  availableValues:List[T],
                  size: (Int, Int)
) extends CSPModel[T, V]

trait CSPModel[T, V]{
    val variables: Array[Option[V]]
    val domains: Array[List[V]]
    val isConstant: Array[Boolean]
    val availableValues:List[T]
    val size: (Int, Int)

}