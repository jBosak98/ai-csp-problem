package model


case class CSPProblem[V](
                          variables: Array[Option[V]],
                          domains: Array[List[String]],
                          isConstant: Array[Boolean],
                          size: (Int, Int),
//                          constraint: (CSPModel[V], Int) => List[String],
                          availableValues: List[String]
                        ) extends CSPModel[V]


//object CSPProblem {
//
//  def apply[V](csp:CSPModel[V], constraint:(CSPModel[V], Int) => List[String]):CSPProblem[V] =
//    CSPProblem[V](
//      csp.variables,
//      csp.domains,
//      csp.isConstant,
//      csp.size,
//      constraint,
//      csp.availableValues
//    )
//
//
//}
