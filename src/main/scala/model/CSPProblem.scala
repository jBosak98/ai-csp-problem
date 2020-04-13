package model


case class CSPProblem[T](
                          variables: Array[Option[T]],
                          domains: Array[List[T]],
                          isConstant: Array[Boolean],
                          size: (Int, Int),
                          constraint: (CSPModel[T], Int) => List[T],
                          availableValues: List[T]
                        ) extends CSPModel[T]


object CSPProblem {

  def apply[T](csp:CSPModel[T], constraint:(CSPModel[T], Int) => List[T]):CSPProblem[T] =
    CSPProblem[T](
      csp.variables,
      csp.domains,
      csp.isConstant,
      csp.size,
      constraint,
      csp.availableValues
    )


}
