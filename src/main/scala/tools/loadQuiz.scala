package tools

import model.{CSP, CSPProblem, QuizVariable}

import scala.io.Source.fromFile

object loadQuiz {
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
      isConstant = puzzle.map(_.equals("#")),
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


    val verticalWords = (1 to numberOfColumn).flatMap { columnNumber =>
      val indices = sudokuTools.getIndicesOfColumn(columnNumber, size)
      val words = sudokuTools.column(puzzle, size, columnNumber).map(_.getOrElse("_")).mkString("").replace("#", "#!#").split("#")
      createQuizVariable(Array.empty[QuizVariable], words, indices, isVertical = true)
    }
    val horizontalWords: IndexedSeq[QuizVariable] = (1 to numberOfRows) flatMap { rowNumber =>
      val indices = sudokuTools.getIndicesOfRow(rowNumber, size)
      val words = sudokuTools.row(puzzle, size, rowNumber).map(_.getOrElse("_")).mkString("").replace("#", "#!#").split("#")
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
