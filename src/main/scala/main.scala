import domainCalculations.{domainPuzzle, domainSudoku}
import heuristics.getFirstEmptyIndexHeuristic.getFirstEmptyIndex
import heuristics.getRandomIndexHeuristic.getRandomIndex
import heuristics.lowestDomainSizeHeuristic.getIndexWithTheLowestDomainSize
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

    val sudokuLowestDomainHeuristic = sudokuHeuristicGenerator(getIndexWithTheLowestDomainSize)
    val quizLowestDomainHeuristic = quizHeuristicGenerator(getIndexWithTheLowestDomainSize)
    val quizFirstEmptyHeuristic = quizHeuristicGenerator(getFirstEmptyIndex)
    val quizRandomIndexHeuristic = quizHeuristicGenerator(getRandomIndex)

    val sudokuDomainCalculator = DomainCalculator[Int](
      domainSudoku.calculateDomainOfIndex[Int],
      domainSudoku.calculateDomainOfRelatedFields[Int],
      domainSudoku.createVariableSudoku
    )
    val quizDomainCalculator = DomainCalculator[QuizVariable](
      domainPuzzle.calculateDomainOfVariableIndex[QuizVariable],
      domainPuzzle.calculateDomainOfDependents[QuizVariable],
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

//    sudokus.foreach(s => {
//      domainSudoku.calculateDomain(s)
//    timer.timer({
//        sudokus.head.domains.foreach(println)
//        sudokuResolver(s, sudokuValidator)

//      })
//      printProblem.printProblem(s)
//    })

//
    timer.timer({

      domainPuzzle.calculateDomainForEachVariables(quiz)

      quizResolver(quiz, quizValidator)
//      printProblem.printProblem(quiz)
//      ""
    })


  }


}
