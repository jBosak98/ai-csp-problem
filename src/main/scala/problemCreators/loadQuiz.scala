package problemCreators

import model.{CSP, QuizVariable}
import tools.puzzleTools._
import tools.sudokuTools

import scala.io.Source.fromFile

object loadQuiz {

  def loadQuizes(puzzleFile: String, wordsFile: String, from:Int, to:Int) =
    (0 to 10).map { quizNumber =>
      val puzzleFileWithIndex = puzzleFile + quizNumber.toString
      val wordsFileWithIndex = wordsFile + quizNumber.toString
      loadQuiz(puzzleFileWithIndex, wordsFileWithIndex)
    }.toList


  def loadQuiz(puzzleFile: String, wordsFile: String): CSP[QuizVariable] = {
    val bufferedSource = fromFile(puzzleFile)("UTF-8")
    val lines = bufferedSource.getLines().toList
    bufferedSource.close()

    val puzzle: Array[Option[String]] = (lines flatMap { line =>
      line map {
        case '_' => Option.empty[String]
        case '#' => Option("#")
      }
    }).toArray
    val size = (lines.head.length, lines.size)

    val variables = createVariables(puzzle, size).map(Option(_))
    CSP[QuizVariable](
      variables = variables,
      domains = Array.fill(variables.length)(List.empty[String]),
      isConstant = puzzle.map(_.isDefined),
      availableValues = loadAvailableValues(wordsFile),
      size = size
    )
  }

  def loadAvailableValues(wordsFile: String) = {
    val bufferedSource = fromFile(wordsFile)("UTF-8")
    val lines = bufferedSource.getLines().toList
    bufferedSource.close()
    lines
  }

  def createVariables(puzzle: Array[Option[String]], size: (Int, Int)): Array[QuizVariable] = {
    val (numberOfColumn, numberOfRows) = size

    val getWordsFromLine = {line:Array[Option[String]] =>
      line.map(_.getOrElse("_")).mkString("").replace("#", "#!#").split("#")
    }

    val verticalWords = (1 to numberOfColumn).flatMap { columnNumber =>
      val indices = getIndicesOfColumn(columnNumber, size)
      val words = getWordsFromLine(sudokuTools.column(puzzle, size, columnNumber))

      createQuizVariable(Array.empty[QuizVariable], words, indices, isVertical = true)
    }
    val horizontalWords: IndexedSeq[QuizVariable] = (1 to numberOfRows) flatMap { rowNumber =>
      val indices = sudokuTools.getIndicesOfRow(rowNumber, size)
      val words = getWordsFromLine(sudokuTools.row(puzzle, size, rowNumber))

      createQuizVariable(Array.empty[QuizVariable], words, indices, isVertical = false)
    }

    verticalWords.toArray ++ horizontalWords.toArray
  }

  def createQuizVariable(
                          quizVariables: Array[QuizVariable],
                          words: Array[String],
                          indices: Array[Int],
                          isVertical: Boolean
                        ): Array[QuizVariable] = {
    if (words.isEmpty) return quizVariables
    val word = words.head
    if (word == "") return createQuizVariable(quizVariables, words.drop(1), indices, isVertical)
    if (word.length < 2) return createQuizVariable(quizVariables, words.drop(1), indices.drop(1), isVertical)
    val variable = QuizVariable(
      index = indices(0),
      value = Option.empty[String],
      isVertical = isVertical,
      size = word.length
    )
    createQuizVariable(quizVariables :+ variable, words.drop(1), indices.drop(word.length), isVertical)
  }
}
