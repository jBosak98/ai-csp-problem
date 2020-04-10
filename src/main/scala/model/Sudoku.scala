package model

import model.types.{Domain, ValueSudoku}


case class Sudoku(
                   values: Array[ValueSudoku],
                   domains: Array[Domain],
                   isConstant: Array[Boolean]
                 )



