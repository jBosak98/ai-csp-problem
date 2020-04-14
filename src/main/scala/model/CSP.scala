package model

case class CSP[V](variables: Array[Option[V]],
                  domains: Array[List[String]],
                  isConstant: Array[Boolean],
                  availableValues:List[String],
                  size: (Int, Int)
) extends CSPModel[V]

trait CSPModel[V]{
    val variables: Array[Option[V]]
    val domains: Array[List[String]]
    val isConstant: Array[Boolean]
    val availableValues:List[String]
    val size: (Int, Int)

}