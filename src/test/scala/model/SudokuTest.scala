package model

import org.junit.jupiter.api.{BeforeEach, Test}
import org.junit.jupiter.api.Assertions._
import tools.printSudoku

class SudokuTest {

  var s1: Sudoku = _


  @BeforeEach private[model] def setUp() = {
    s1 = new Sudoku((1 to 81).map(a => {
      new ValueSudoku(value = Option(a))
    }).toArray)
  }

  @Test private[model] def get() = {
    printSudoku.printSudoku(s1)
    assertEquals(1, s1.get(rowNumber = 1, columnNumber = 1).get.value.get)
    assertEquals(81, s1.get(rowNumber = 9, columnNumber = 9).get.value.get)
    assertTrue(s1.get(rowNumber = 0, columnNumber = 9).isEmpty)
    assertEquals(9, s1.get(rowNumber = 1, columnNumber = 9).get.value.get)
    assertTrue(s1.get(rowNumber = 9, columnNumber = 0).isEmpty)
    assertEquals(73, s1.get(rowNumber = 9, columnNumber = 1).get.value.get)
    assertEquals(39, s1.get(rowNumber = 5, columnNumber = 3).get.value.get)
  }

  @Test private[model] def row() = {
    assertEquals(0, s1.row(0).length)
    assertEquals(0, s1.row(10).length)
    assertEquals(9, s1.row(1).length)
    assertEquals(Option(1), s1.row(1)(0).value)
    assertEquals(Option(9), s1.row(1)(8).value)
    assertEquals(Option(40), s1.row(5)(3).value)
    assertEquals(Option(73), s1.row(9)(0).value)
    assertEquals(Option(81), s1.row(9)(8).value)

  }

  @Test private[model] def column() = {
    assertEquals(0, s1.column(0).length)
    assertEquals(0, s1.column(10).length)
    assertEquals(9, s1.column(1).length)
    assertEquals(9, s1.column(9).length)
    assertEquals(9, s1.column(5).length)
    assertEquals(Option(1), s1.column(1)(0).value)
    assertEquals(Option(73), s1.column(1)(8).value)
    assertEquals(Option(32), s1.column(5)(3).value)
    assertEquals(Option(9), s1.column(9)(0).value)
    assertEquals(Option(81), s1.column(9)(8).value)
  }

  @Test private[model] def getRowAtIndex() = {
    assertEquals(9, s1.getRowAtIndex(80).length)
    assertEquals(s1.row(1)(0), s1.getRowAtIndex(5)(0))
    assertEquals(s1.row(9)(8), s1.getRowAtIndex(80)(8))
    assertEquals(0, s1.getRowAtIndex(81).length)
  }

  @Test private[model] def getColumnAtIndex() = {
    assertEquals(9, s1.getColumnAtIndex(80).length)
    assertEquals(0, s1.getColumnAtIndex(81).length)
    assertEquals(s1.row(1)(5), s1.getColumnAtIndex(5)(0))
    assertEquals(s1.row(9)(8), s1.getColumnAtIndex(80)(8))
  }

  @Test private[model] def getBox(): Unit = {
    val firstBox = s1.getBox(0)
    assertEquals(9, firstBox.length)
    assertEquals(1, firstBox(0).value.get)
    assertEquals(2, firstBox(1).value.get)
    assertEquals(3, firstBox(2).value.get)
    assertEquals(10, firstBox(3).value.get)
    assertEquals(19, firstBox(6).value.get)
    assertEquals(21, firstBox(8).value.get)

    val fourthBox = s1.getBox(41)
    assertEquals(9, fourthBox.length)
    assertEquals(31, fourthBox(0).value.get)
    assertEquals(32, fourthBox(1).value.get)
    assertEquals(33, fourthBox(2).value.get)
    assertEquals(40, fourthBox(3).value.get)
    assertEquals(49, fourthBox(6).value.get)
    assertEquals(51, fourthBox(8).value.get)
    val fifthBox = s1.getBox(44)
    assertEquals(9, fifthBox.length)
    assertEquals(34, fifthBox(0).value.get)
    assertEquals(35, fifthBox(1).value.get)
    assertEquals(36, fifthBox(2).value.get)
    assertEquals(43, fifthBox(3).value.get)
    assertEquals(52, fifthBox(6).value.get)
    assertEquals(54, fifthBox(8).value.get)
  }

  @Test private[model] def getColumnNumber(): Unit = {

    assertEquals(9, s1.getColumnNumber(44).get)
    assertEquals(1, s1.getColumnNumber(0).get)
    assertEquals(2, s1.getColumnNumber(1).get)
    assertEquals(3, s1.getColumnNumber(2).get)
    assertEquals(4, s1.getColumnNumber(3).get)
    assertEquals(5, s1.getColumnNumber(4).get)
    assertEquals(6, s1.getColumnNumber(5).get)
    assertEquals(7, s1.getColumnNumber(6).get)
    assertEquals(8, s1.getColumnNumber(7).get)
    assertEquals(9, s1.getColumnNumber(8).get)
    assertEquals(1, s1.getColumnNumber(9).get)
    assertEquals(2, s1.getColumnNumber(10).get)
  }
}