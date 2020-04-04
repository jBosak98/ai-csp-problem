package model

import org.junit.jupiter.api.{BeforeEach, Test}
import org.junit.jupiter.api.Assertions._
import tools.printSudoku

class SudokuTest {

  var s1:Sudoku = _


  @BeforeEach private[model] def setUp() = {
    s1 = new Sudoku((1 to 81).map(a =>{new ValueSudoku(value = Option(a)) }).toArray)
  }
  @Test private[model] def get() = {
    printSudoku.printSudoku(s1)
    assertEquals(1, s1.get(rowNumber = 1,columnNumber = 1).get.value.get)
    assertEquals(81, s1.get(rowNumber = 9,columnNumber = 9).get.value.get)
    assertTrue(s1.get(rowNumber = 0,columnNumber = 9).isEmpty)
    assertEquals(9, s1.get(rowNumber = 1,columnNumber = 9).get.value.get)
    assertTrue(s1.get(rowNumber = 9,columnNumber = 0).isEmpty)
    assertEquals(73, s1.get(rowNumber = 9,columnNumber = 1).get.value.get)
    assertEquals(39, s1.get(rowNumber = 5,columnNumber = 3).get.value.get)
  }

  @Test private[model] def row() = {
    assertEquals(0, s1.row(0).length)
    assertEquals(0, s1.row(10).length)
    assertEquals(9, s1.row(1).length)
    assertEquals(Option(1),s1.row(1)(0).value)
    assertEquals(Option(9),s1.row(1)(8).value)
    assertEquals(Option(40),s1.row(5)(3).value)
    assertEquals(Option(73),s1.row(9)(0).value)
    assertEquals(Option(81),s1.row(9)(8).value)

  }

  @Test private[model] def column() = {
    assertEquals(0, s1.column(0).length)
    assertEquals(0, s1.column(10).length)
    assertEquals(9, s1.column(1).length)
    assertEquals(9, s1.column(9).length)
    assertEquals(9, s1.column(5).length)
    assertEquals(Option(1),s1.column(1)(0).value)
    assertEquals(Option(73),s1.column(1)(8).value)
    assertEquals(Option(32),s1.column(5)(3).value)
    assertEquals(Option(9),s1.column(9)(0).value)
    assertEquals(Option(81),s1.column(9)(8).value)
  }
}