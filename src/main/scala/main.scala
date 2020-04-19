import domainCalculations.{domainPuzzle, domainSudoku}
import heuristics.getFirstEmptyIndexHeuristic.getFirstEmptyIndex
import heuristics.getRandomIndexHeuristic.getRandomIndex
import heuristics.nextIndexHeuristicGenerator
import heuristics.shortestDomainSizeHeuristic.getIndexWithTheShortestDomainSize
import model._
import problemCreators.{loadQuiz, loadSudokus, printProblem}
import tools._
import validators.{quizValidations, sudokuValidations}

object main {

  def main(args: Array[String]): Unit = {
    val sudokuFilename = "src/main/resources/ai-lab2-2020-dane/Sudoku.csv"
    val puzzleFile = "src/main/resources/data/puzzle"
    val wordsFile = "src/main/resources/data/words"

    val sudokus = loadSudokus.loadSudokus(sudokuFilename)
    val quizes = loadQuiz.loadQuizes(puzzleFile, wordsFile, from = 0, to = 10)

    val quizHeuristicGenerator = getQuizHeuristicGenerator
    val sudokuHeuristicGenerator = getSudokuHeuristicGenerator

    val sudokuLowestDomainHeuristic = sudokuHeuristicGenerator(getFirstEmptyIndex)
    val quizLowestDomainHeuristic = quizHeuristicGenerator(getIndexWithTheShortestDomainSize)
    val quizFirstEmptyHeuristic = quizHeuristicGenerator(getFirstEmptyIndex)
    val quizRandomIndexHeuristic = quizHeuristicGenerator(getRandomIndex)

    val sudokuDomainCalculator = DomainCalculator[Int](
      domainSudoku.calculateDomainOfIndex[Int],
      domainSudoku.calculateDomainOfRelatedFields[Int],
      domainSudoku.calculateDomainForEachVariables[Int],
      domainSudoku.createVariableSudoku
    )
    val quizDomainCalculator = DomainCalculator[QuizVariable](
      domainPuzzle.calculateDomainOfVariableIndex[QuizVariable],
      domainPuzzle.calculateDomainOfDependents[QuizVariable],
      domainPuzzle.calculateDomainForEachVariables[QuizVariable],
      domainPuzzle.createQuizVariable
    )

    val sudokuResolver = resolveProblem.resolveProblem[Int](
      sudokuLowestDomainHeuristic,
      sudokuDomainCalculator
    ) _
    val quizResolver = resolveProblem.resolveProblem[QuizVariable](
      quizLowestDomainHeuristic,
      quizDomainCalculator
    ) _


    val sudokuValidator = CSPProblemValidator[Int](
      sudokuValidations.isProperlyResolved,
      sudokuValidations.areAllFieldsFilled,
      sudokuValidations.isProperlyFilled,
      sudokuValidations.isDomainProper
    )
    val quizValidator = CSPProblemValidator[QuizVariable](
      quizValidations.isProperlyResolved,
      quizValidations.areAllFieldsFilled,
      quizValidations.isProperlyFilled,
      quizValidations.isDomainProper
    )

    val results =
      runAlgorithm(quizes, quizResolver, quizValidator, forwardChecking = true)
//    runAlgorithm(sudokus, sudokuResolver, sudokuValidator, true)

    quizes.foreach{q => printProblem.printProblem(q) }
    results.foreach(println)

  }

  def runAlgorithm[V](
                       problems:List[CSP[V]],
                       resolver:(CSP[V], CSPProblemValidator[V],Boolean) => CSPResult,
                       validator:CSPProblemValidator[V],
                       forwardChecking:Boolean
                     ) = {
    val results = new Array[CSPResult](problems.length)
    problems.indices.foreach(i => {
      val (res, time) = timer.timer(
        resolver(problems(i), validator, forwardChecking)
      )
      results(i) = CSPResult.apply(i, time,  res)
    })
    results
  }


  def getQuizHeuristicGenerator = {
    val heuristicFilterPuzzle: ((Option[QuizVariable], Int)) => Boolean = {
      case (variable: Option[QuizVariable], index: Int) => variable.get.value.isEmpty
    }
    nextIndexHeuristicGenerator.createNextIndexHeuristic(heuristicFilterPuzzle) _
  }

  def getSudokuHeuristicGenerator = {
    val heuristicFilterSudoku: ((Option[Int], Int)) => Boolean = {
      case (variable: Option[Int], index: Int) =>  variable.isEmpty
    }
    nextIndexHeuristicGenerator.createNextIndexHeuristic(heuristicFilterSudoku) _
  }

}
