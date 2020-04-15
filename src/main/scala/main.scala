import heuristics.lowestDomainSizeHeuristic
import model._
import tools._

object main {

  def main(args: Array[String]): Unit = {
    val sudokuFilename = "src/main/resources/ai-lab2-2020-dane/Sudoku2.csv"
    val quizNumber = 2
    val puzzleFile = s"src/main/resources/ai-lab2-2020-dane/Jolka/puzzle${quizNumber}"
    val wordsFile = s"src/main/resources/ai-lab2-2020-dane/Jolka/words${quizNumber}"

    val sudokus = loadSudokus.loadSudokus(sudokuFilename)
    val quiz: CSP[QuizVariable] = loadQuiz.loadQuiz(puzzleFile, wordsFile)


    val createVariableSudoku: (Option[Int], Option[String]) => Option[Int] = { (_, sudokuValue) =>
      if (sudokuValue.isDefined) sudokuValue.get.toIntOption
      else Option.empty[Int]
    }
    val createQuizVariable = { (variable: Option[QuizVariable], word: Option[String]) =>
      Option(QuizVariable(
        index = variable.get.index,
        value = word,
        isVertical = variable.get.isVertical,
        size = variable.get.size
      ))
    }


    val heuristicFilterSudoku: ((Option[Int], Int)) => Boolean = {
      case (variable: Option[Int], index: Int) => variable.isEmpty
    }
    val heuristicFilterPuzzle: ((Option[QuizVariable], Int)) => Boolean = {
      case (variable: Option[QuizVariable], index: Int) => variable.get.value.isEmpty
    }

    val sudokuHeuristic = lowestDomainSizeHeuristic.getNextIndexToResolve[Int](heuristicFilterSudoku) _
    val quizHeuristic = lowestDomainSizeHeuristic.getNextIndexToResolve[QuizVariable](heuristicFilterPuzzle) _


    val sudokuDomainCalculator = DomainCalculator[Int](
      domainSudoku.calculateDomainOfIndex[Int],
      domainSudoku.calculateDomainOfRelatedFields[Int]
    )
    val quizDomainCalculator = DomainCalculator[QuizVariable](
      calculateDomainOfIndex = domainPuzzle.calculateDomainOfVariableIndex[QuizVariable] _,
      calculateDomainOfDependents = domainPuzzle.calculateDomainOfDependents[QuizVariable] _
    )

    val sudokuResolver = resolveProblem.resolveProblem[Int](
      sudokuHeuristic,
      createVariableSudoku,
      sudokuDomainCalculator
    ) _
    val quizResolver = resolveProblem.resolveProblem[QuizVariable](
      quizHeuristic,
      createQuizVariable,
      quizDomainCalculator
    ) _


    val sudokuValidator = CSPProblemValidator[Int](
      sudokuTools.isProperlyResolved,
      sudokuTools.areAllFieldsFilled,
      sudokuTools.isProperlyFilled
    )
    val quizValidator = getQuizValidator.getQuizValidator()


    sudokus.foreach(s => {
      domainSudoku.calculateDomain(s)
      sudokuResolver(s, sudokuValidator)
      printProblem.printProblem(s)
    })

    //    time += timer.timer({
    domainPuzzle.calculateDomainForEachVariables(quiz)
    quizResolver(quiz, quizValidator)
    //    })

    printProblem.printProblem(quiz)
  }


}
