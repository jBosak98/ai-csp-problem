package model


case class CSPProblem[T](
                          variables: Array[Option[T]],
                          domains: Array[List[T]],
                          isConstant: Array[Boolean],
                          size: (Int, Int),
                          constraint: (CSP[T], Int) => List[T],
                          availableValues: List[T]
                        ) extends CSP[T]

