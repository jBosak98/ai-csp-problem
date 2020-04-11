package model

import model.types.{Domain, ValueSudoku}


//class CSPProblem
//                   values: Array[ValueSudoku],
//                   domains: Array[Domain],
//                   isConstant: Array[Boolean]
//                  extends CSPProblem


case class CSPProblem(                   values: Array[ValueSudoku],
                                         domains: Array[Domain],
                                         isConstant: Array[Boolean],
                                         size:(Int,Int)
                     )


//sealed trait CSPProblem
//
//object CSPProblem {
//  class Sudoku(
//           values: Array[ValueSudoku],
//           domains: Array[Domain],
//           isConstant: Array[Boolean]
//         ) extends CSPProblem {}
//  class Quiz(
//              values: Array[String],
//              domains: Array[Domain],
//              isConstant: Array[Boolean]
//            ) extends CSPProblem {}
//}