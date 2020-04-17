package domainCalculations

import tools.puzzleTools._
import model.{CSP, QuizVariable}
import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.{BeforeEach, Test}
import problemCreators.{loadQuiz, printProblem}
import tools.sudokuTools.getColumnNumber

class domainPuzzleTest {
  var quiz: CSP[QuizVariable] = _


  @BeforeEach private[domainCalculations] def setUp() = {
    quiz = loadQuiz.loadQuiz(
      "src/main/resources/data/puzzle15",
      "src/main/resources/data/words15"
    )
  }

  @Test private[domainCalculations] def checkQuiz = {
    printProblem.printProblem(quiz)
    quiz.variables.foreach(println)
    assertEquals((8, 8), quiz.size)
    assertEquals(64, quiz.isConstant.length)
    assertEquals(25, quiz.isConstant.count(_.equals(true)))
    assertEquals(34, quiz.variables(2).get.index)
    assertEquals(true, quiz.variables(2).get.isVertical)
    assertNotEquals(34, quiz.variables(3).get.index)
    assertNotEquals(false, quiz.variables(3).get.isVertical)
  }


  @Test private[domainCalculations] def getWholeLineIndicesThatAreFilledByVariableTest() = {

    assertArrayEquals(
      List(1, 9, 17, 25, 33, 41, 49, 57).toArray,
      getWholeLineOfVariable(quiz, quiz.variables(0).get)
    )

    assertArrayEquals(
      List(2, 10, 18, 26, 34, 42, 50, 58).toArray,
      getWholeLineOfVariable(quiz, quiz.variables(2).get)
    )

  }

  @Test private[domainCalculations] def getIndicesOfColumnTest() = {
    assertArrayEquals(
      List(1, 9, 17, 25, 33, 41, 49, 57).toArray,
      getIndicesOfColumn(2, (8, 8))
    )
    assertArrayEquals(
      List(2, 10, 18, 26, 34, 42, 50, 58).toArray,
      getIndicesOfColumn(3, (8, 8))
    )
    assertArrayEquals(
      List(2, 10, 18, 26, 34, 42, 50, 58).toArray,
      getIndicesOfColumn(
        getColumnNumber(34, (8, 8)).get, (8, 8)
      )
    )
    assertArrayEquals(
      List(1, 9, 17, 25, 33, 41, 49, 57).toArray,
      getIndicesOfColumn(
        getColumnNumber(1, (8, 8)).get, (8, 8)
      )
    )
  }

  @Test private[domainCalculations] def getIndicesThatAreFilledByVariableTest() = {
    assertArrayEquals(
      List(1, 9, 17, 25, 33, 41, 49, 57).toArray,
      getIndicesThatAreFilledByVariable(quiz, quiz.variables(0).get).toArray
    )

    assertArrayEquals(
      List(10, 18).toArray,
      getIndicesThatAreFilledByVariable(quiz, quiz.variables(1).get).toArray
    )
    assertArrayEquals(
      List(34, 42, 50).toArray,
      getIndicesThatAreFilledByVariable(quiz, quiz.variables(2).get).toArray
    )
    assertArrayEquals(
      List(20, 28).toArray,
      getIndicesThatAreFilledByVariable(quiz, quiz.variables(3).get).toArray
    )
    assertArrayEquals(
      List(52, 60).toArray,
      getIndicesThatAreFilledByVariable(quiz, quiz.variables(4).get).toArray
    )
    assertArrayEquals(
      List(31, 39, 47, 55, 63).toArray,
      getIndicesThatAreFilledByVariable(quiz, quiz.variables(8).get).toArray
    )
    assertArrayEquals(
      List(0, 1).toArray,
      getIndicesThatAreFilledByVariable(quiz, quiz.variables(9).get).toArray
    )
    assertArrayEquals(
      List(3, 4, 5, 6, 7).toArray,
      getIndicesThatAreFilledByVariable(quiz, quiz.variables(10).get).toArray
    )
    assertArrayEquals(
      List(9, 10).toArray,
      getIndicesThatAreFilledByVariable(quiz, quiz.variables(11).get).toArray
    )
    assertArrayEquals(
      List(17, 18).toArray,
      getIndicesThatAreFilledByVariable(quiz, quiz.variables(12).get).toArray
    )
    assertArrayEquals(
      List(20, 21).toArray,
      getIndicesThatAreFilledByVariable(quiz, quiz.variables(13).get).toArray
    )
    assertArrayEquals(
      List(28, 29, 30, 31).toArray,
      getIndicesThatAreFilledByVariable(quiz, quiz.variables(14).get).toArray
    )
    assertArrayEquals(
      List(60, 61).toArray,
      getIndicesThatAreFilledByVariable(quiz, quiz.variables.last.get).toArray
    )
  }

  @Test private[domainCalculations] def defineAvailableValues() = {
  }

  @Test private[domainCalculations] def getLineByPuzzleIndexTest() = {
    assertArrayEquals(
      (0 to 7).toArray,
      getLineByPuzzleIndex(quiz, 0, false)
    )
    assertArrayEquals(
      List(0, 8, 16, 24, 32, 40, 48, 56).toArray,
      getLineByPuzzleIndex(quiz, 0, true)
    )
    assertArrayEquals(
      List(7, 15, 23, 31, 39, 47, 55, 63).toArray,
      getLineByPuzzleIndex(quiz, 31, true)
    )
    assertArrayEquals(
      (24 to 31).toArray,
      getLineByPuzzleIndex(quiz, 31, false)
    )

  }

  @Test private[domainCalculations] def getVariablesThatConflictWithIndexesTest() = {
    val indexes = getIndicesThatAreFilledByVariable(quiz, quiz.variables(0).get).toList
    assertEquals(
      8,
      getVariablesThatConflictWithIndexes(quiz, indexes).length
    )
    assertArrayEquals(
      List(0, 1, 9, 17, 33, 40, 49, 56).toArray,
      getVariablesThatConflictWithIndexes(quiz, indexes).map(_.index).toArray.sorted
    )
    assertArrayEquals(
      List(0).toArray,
      getVariablesThatConflictWithIndexes(quiz, List(0)).map(_.index).toArray
    )
    val indexes2 = getIndicesThatAreFilledByVariable(quiz, quiz.variables(14).get).toList
    assertArrayEquals(
      List(5, 20, 28, 30, 31).toArray,
      getVariablesThatConflictWithIndexes(quiz, indexes2).map(_.index).toArray.sorted
    )

  }

  @Test private[domainCalculations] def calculateDomainForVariableTest() = {
  }

  @Test private[domainCalculations] def getVariablesThatConflictWithIndexTest() = {


    assertEquals(
      1,
      getVariablesThatConflictWithIndex(quiz, 0).length
    )
    assertArrayEquals(
      List(0).toArray,
      getVariablesThatConflictWithIndex(quiz, 0).map(_.index)
    )
    assertArrayEquals(
      List(1, 9).toArray,
      getVariablesThatConflictWithIndex(quiz, 9).map(_.index)
    )
    assertArrayEquals(
      List(1, 0).toArray,
      getVariablesThatConflictWithIndex(quiz, 1).map(_.index)
    )

  }


}