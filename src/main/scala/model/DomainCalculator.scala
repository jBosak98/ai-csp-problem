package model

import model.types.DomainCalculatorFun

case class DomainCalculator[V](
                                calculateDomainOfIndex: DomainCalculatorFun[V],
                                calculateDomainOfDependents: DomainCalculatorFun[V],
                                createVariableFromDomainValue: (Option[V], Option[String]) => Option[V]
                              )
