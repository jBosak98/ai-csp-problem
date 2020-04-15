import domainCalculations.{domainPuzzle, domainSudoku}
import heuristics.lowestDomainSizeHeuristic
import model._
import problemCreators.{loadQuiz, loadSudokus, printProblem}
import tools._
import validators.quizValidations.{areAllFieldsFilled, isProperlyFilled, isProperlyResolved}
import validators.sudokuValidations

object main {

  def main(args: Array[String]): Unit = {
    val sudokuFilename = "src/main/resources/ai-lab2-2020-dane/Sudoku2.csv"
    val quizNumber = 2
    val puzzleFile = s"src/main/resources/ai-lab2-2020-dane/Jolka/puzzle${quizNumber}"
    val wordsFile = s"src/main/resources/ai-lab2-2020-dane/Jolka/words${quizNumber}"

    val sudokus = loadSudokus.loadSudokus(sudokuFilename)
    val quiz: CSP[QuizVariable] = loadQuiz.loadQuiz(puzzleFile, wordsFile)


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
      domainSudoku.calculateDomainOfRelatedFields[Int],
      domainSudoku.createVariableSudoku
    )
    val quizDomainCalculator = DomainCalculator[QuizVariable](
      domainPuzzle.calculateDomainOfVariableIndex[QuizVariable],
      domainPuzzle.calculateDomainOfDependents[QuizVariable],
      domainPuzzle.createQuizVariable
    )

    val sudokuResolver = resolveProblem.resolveProblem[Int](
      sudokuHeuristic,
      sudokuDomainCalculator
    ) _
    val quizResolver = resolveProblem.resolveProblem[QuizVariable](
      quizHeuristic,
      quizDomainCalculator
    ) _


    val sudokuValidator = CSPProblemValidator[Int](
      sudokuValidations.isProperlyResolved,
      sudokuValidations.areAllFieldsFilled,
      sudokuValidations.isProperlyFilled
    )
    val quizValidator = CSPProblemValidator[QuizVariable](
      isProperlyResolved,
      areAllFieldsFilled,
      isProperlyFilled
    )


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
