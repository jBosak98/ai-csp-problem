import domainCalculations.{domainPuzzle, domainSudoku}
import heuristics.getFirstEmptyIndexHeuristic.getFirstEmptyIndex
import heuristics.getRandomIndexHeuristic.getRandomIndex
import heuristics.shortestDomainSizeHeuristic.getIndexWithTheShortestDomainSize
import heuristics.nextIndexHeuristicGenerator
import model._
import problemCreators.{loadQuiz, loadSudokus}
import tools._
import validators.{quizValidations, sudokuValidations}

object main {

  def main(args: Array[String]): Unit = {
    val sudokuFilename = "src/main/resources/ai-lab2-2020-dane/Sudoku.csv"
    val quizNumber = 1
    val puzzleFile = s"src/main/resources/data/puzzle${quizNumber}"
    val wordsFile = s"src/main/resources/data/words${quizNumber}"

    val sudokus = loadSudokus.loadSudokus(sudokuFilename)
    val quiz: CSP[QuizVariable] = loadQuiz.loadQuiz(puzzleFile, wordsFile)


    val heuristicFilterSudoku: ((Option[Int], Int)) => Boolean = {
      case (variable: Option[Int], index: Int) =>  variable.isEmpty
    }
    val heuristicFilterPuzzle: ((Option[QuizVariable], Int)) => Boolean = {
      case (variable: Option[QuizVariable], index: Int) => variable.get.value.isEmpty
    }
    val sudokuHeuristicGenerator = nextIndexHeuristicGenerator.createNextIndexHeuristic(heuristicFilterSudoku) _
    val quizHeuristicGenerator = nextIndexHeuristicGenerator.createNextIndexHeuristic(heuristicFilterPuzzle) _

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
      domainPuzzle.createQuizVariable(quiz)
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

    val results = new Array[CSPResult](sudokus.length + 1)

    sudokus.indices.foreach(i => {
//      domainSudoku.calculateDomain(s)
//      println(i+1)
      val (res, time) = timer.timer({
        //        sudokus.head.domains.foreach(println)
        sudokuResolver(sudokus(i), sudokuValidator, false)
      })
      results(i) = CSPResult.apply(i + 1, time,  res)
//      printProblem.printProblem(sudokus(i))

//      printProblem.printProblem(sudokus.head)
    })
    results.foreach(println)
//
////
//    timer.timer({
//      quizResolver(quiz, quizValidator, false)
//      printProblem.printProblem(quiz)
//      ""
//    })


  }


}
