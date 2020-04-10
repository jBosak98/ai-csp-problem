package tools

import model.Sudoku
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions._
import tools.sudokuTools._

class sudokuToolsTest {
      var s1: Sudoku = _
  @BeforeEach private[tools] def setUp() = {
            s1 = new Sudoku(
              values = (1 to 81).map(a => Option(a)).toArray,
              domains = Array.empty[List[Int]],
              isConstant = Array.fill(80)(false)
            )
  }

  @Test private[tools] def getRowNumber() = {
  }



  @Test private[tools] def getFromSudokuTest() = {
//    printSudoku.printSudoku(s1)
//    getFromSudoku(s1.values,1,1)
    assertEquals(1, getFromSudoku(values = s1.values, rowNumber = 1, columnNumber = 1).get)
    assertEquals(81, getFromSudoku(values = s1.values, rowNumber = 9, columnNumber = 9).get)
    assertTrue(getFromSudoku(s1.values, rowNumber = 0, columnNumber = 9).isEmpty)
    assertEquals(9, getFromSudoku(s1.values, rowNumber = 1, columnNumber = 9).get)
    assertTrue(getFromSudoku(s1.values, rowNumber = 9, columnNumber = 0).isEmpty)
    assertEquals(73, getFromSudoku(s1.values, rowNumber = 9, columnNumber = 1).get)
    assertEquals(39, getFromSudoku(s1.values, rowNumber = 5, columnNumber = 3).get)
  }

  @Test private[tools] def getColumnAtIndexTest() = {
    assertEquals(9, getColumnAtIndex(s1.values, 80).length)
    assertEquals(0, getColumnAtIndex(s1.values, 81).length)
    assertEquals(row(s1.values,1)(5), getColumnAtIndex(s1.values, 5)(0))
    assertEquals(row(s1.values, 9)(8), getColumnAtIndex(s1.values, 80)(8))

  }

  @Test private[tools] def rowTest() = {
    assertEquals(0, row(s1.values, 0).length)
    assertEquals(0, row(s1.values, 10).length)
    assertEquals(9, row(s1.values, 1).length)
    assertEquals(Option(1), row(s1.values, 1)(0))
    assertEquals(Option(9), row(s1.values, 1)(8))
    assertEquals(Option(40), row(s1.values, 5)(3))
    assertEquals(Option(73), row(s1.values, 9)(0))
    assertEquals(Option(81), row(s1.values, 9)(8))
  }

  @Test private[tools] def getBoxTest() = {
    val firstBox = getBox(s1.values, 0)
    assertEquals(9, firstBox.length)
    assertEquals(1, firstBox(0).get)
    assertEquals(2, firstBox(1).get)
    assertEquals(3, firstBox(2).get)
    assertEquals(10, firstBox(3).get)
    assertEquals(19, firstBox(6).get)
    assertEquals(21, firstBox(8).get)

    val fourthBox = getBox(s1.values,41)
    assertEquals(9, fourthBox.length)
    assertEquals(31, fourthBox(0).get)
    assertEquals(32, fourthBox(1).get)
    assertEquals(33, fourthBox(2).get)
    assertEquals(40, fourthBox(3).get)
    assertEquals(49, fourthBox(6).get)
    assertEquals(51, fourthBox(8).get)
    val fifthBox = getBox(s1.values, 44)
    assertEquals(9, fifthBox.length)
    assertEquals(34, fifthBox(0).get)
    assertEquals(35, fifthBox(1).get)
    assertEquals(36, fifthBox(2).get)
    assertEquals(43, fifthBox(3).get)
    assertEquals(52, fifthBox(6).get)
    assertEquals(54, fifthBox(8).get)
  }

  @Test private[tools] def getRowAtIndexTest() = {
    assertEquals(9, getRowAtIndex(s1.values, 80).length)
    assertEquals(row(s1.values, 1)(0), getRowAtIndex(s1.values,5)(0))
    assertEquals(row(s1.values, 9)(8), getRowAtIndex(s1.values, 80)(8))
    assertEquals(0, getRowAtIndex(s1.values, 81).length)
  }

  @Test private[tools] def getColumnNumberTest() = {

    assertEquals(9, getColumnNumber(44).get)
    assertEquals(1, getColumnNumber(0).get)
    assertEquals(2, getColumnNumber(1).get)
    assertEquals(3, getColumnNumber(2).get)
    assertEquals(4, getColumnNumber(3).get)
    assertEquals(5, getColumnNumber(4).get)
    assertEquals(6, getColumnNumber(5).get)
    assertEquals(7, getColumnNumber(6).get)
    assertEquals(8, getColumnNumber(7).get)
    assertEquals(9, getColumnNumber(8).get)
    assertEquals(1, getColumnNumber(9).get)
    assertEquals(2, getColumnNumber(10).get)
  }

  @Test private[tools] def columnTest() = {
    assertEquals(0, column(s1.values,0).length)
    assertEquals(0, column(s1.values,10).length)
    assertEquals(9, column(s1.values,1).length)
    assertEquals(9, column(s1.values,9).length)
    assertEquals(9, column(s1.values,5).length)
    assertEquals(Option(1), column(s1.values,1)(0))
    assertEquals(Option(73), column(s1.values,1)(8))
    assertEquals(Option(32), column(s1.values,5)(3))
    assertEquals(Option(9), column(s1.values,9)(0))
    assertEquals(Option(81), column(s1.values,9)(8))
  }
}