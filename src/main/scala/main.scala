import heuristics.lowestDomainSizeHeuristic
import model._
import tools._

object main {

  def main(args: Array[String]): Unit = {
    val filename = "src/main/resources/ai-lab2-2020-dane/Sudoku2.csv"
    val sudokus = loadSudokus.loadSudokus(filename)


    val createVariableSudoku:(Option[Int],Option[String]) =>  Option[Int] = { (_, sudokuValue) =>
       if(sudokuValue.isDefined) sudokuValue.get.toIntOption
       else Option.empty[Int]
    }
    val heuristicFilterSudoku:((Option[Int], Int)) => Boolean = {
      case (variable:Option[Int], index:Int) => variable.isEmpty
    }
    var time = 0L
    val heuristic = lowestDomainSizeHeuristic.getNextIndexToResolve[Int](heuristicFilterSudoku) _
    val resolver = resolveProblem.resolveProblem[Int](
      heuristic,
      createVariableSudoku
    ) _

    val sudokuValidator = CSPProblemValidator [Int](
      sudokuTools.isProperlyResolved,
      sudokuTools.areAllFieldsFilled,
      sudokuTools.isProperlyFilled
    )
    val constraint:(CSPModel[Int], Int) => List[String] = domainSudoku.calculateDomainOfIndex
    sudokus.foreach(s => {
      val problem:CSPProblem[Int] = CSPProblem[Int](s, constraint)
      domainSudoku.calculateDomain(problem)
//      time += timer({
      resolver(problem,sudokuValidator)
//      })
      printProblem.printProblem(problem)
    })
    println(s"time sum:${time}")



    val quizNumber = 0
    val puzzleFile = s"src/main/resources/ai-lab2-2020-dane/Jolka/puzzle${quizNumber}"
    val wordsFile = s"src/main/resources/ai-lab2-2020-dane/Jolka/words${quizNumber}"
    val jolka:CSP[QuizVariable] = loadQuiz.loadQuiz(puzzleFile, wordsFile)


    val domainCalculator = domainPuzzle.calculateDomainOfVariableIndex[QuizVariable] _
    val quizCSP = CSPProblem[QuizVariable](jolka, domainCalculator)

    domainPuzzle.calculateDomainForEachVariables(quizCSP)
    val heuristicFilterPuzzle:((Option[QuizVariable], Int)) => Boolean = {
      case (variable:Option[QuizVariable], index:Int) => variable.get.value.isEmpty
    }

    val createQuizVariable = { (variable: Option[QuizVariable], word: Option[String]) =>
      Option(QuizVariable(
          index = variable.get.index,
          value = word,
          isVertical = variable.get.isVertical,
          size = variable.get.size
        ))
    }


    val quizValidator = getQuizValidator.getQuizValidator()
    val quizHeuristic = lowestDomainSizeHeuristic.getNextIndexToResolve[QuizVariable](heuristicFilterPuzzle) _
    val quizResolver = resolveProblem.resolveProblem[QuizVariable](quizHeuristic,
      createQuizVariable) _
    quizResolver(quizCSP, quizValidator)

    printProblem.printProblem(quizCSP)
  }







}
