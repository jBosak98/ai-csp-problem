package model

import model.types.DomainCalculatorFun

case class DomainCalculator[V](
                           calculateDomainOfIndex: DomainCalculatorFun[V],
                           calculateDomainOfDependents: DomainCalculatorFun[V]
                           )
