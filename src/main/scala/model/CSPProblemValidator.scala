package model

import model.types.Validator

case class CSPProblemValidator[V] (
                                      isProperlyResolved: Validator[V],
                                      areAllFieldsFilled: Validator[V],
                                      isProperlyFilled: Validator[V]
                                    )