package model


case class CSP[V](
                          variables: Array[Option[V]],
                          domains: Array[List[String]],
                          isConstant: Array[Boolean],
                          size: (Int, Int),
                          var availableValues: List[String]
                        )


