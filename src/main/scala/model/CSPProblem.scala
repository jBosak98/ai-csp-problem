package model


case class CSPProblem[T, V](
                          variables: Array[Option[V]],
                          domains: Array[List[T]],
                          isConstant: Array[Boolean],
                          size: (Int, Int),
                          constraint: (CSPModel[T,V], Int) => List[T],
                          availableValues: List[T]
                        ) extends CSPModel[T,V]


object CSPProblem {

  def apply[T,V](csp:CSPModel[T,V], constraint:(CSPModel[T,V], Int) => List[T]):CSPProblem[T,V] =
    CSPProblem[T,V](
      csp.variables,
      csp.domains,
      csp.isConstant,
      csp.size,
      constraint,
      csp.availableValues
    )


}
