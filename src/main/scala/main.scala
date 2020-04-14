import heuristics.lowestDomainSizeHeuristic
import model._
import tools._
import tools.domainPuzzle._
import tools.getQuizValidator.{createQuizVariable, getQuizValidator}
import tools.loadQuiz.loadQuiz

object main {

  def main(args: Array[String]): Unit = {
    val filename = "src/main/resources/ai-lab2-2020-dane/Sudoku2.csv"
    val sudokus = loadSudokus.loadSudokus(filename)
    val heuristicFilterSudoku:(Option[Int], Int) => Boolean = {
      case (variable:Option[Int], index:Int) => variable.isEmpty
    }
//    var time = 0L
//    val heuristic = lowestDomainSizeHeuristic.getNextIndexToResolve[Int,Int] _
//    val resolver = resolveProblem.resolveProblem[Int,Int](heuristic) _
//    val constraint:(CSPModel[Int,Int], Int) => List[Int] = domainSudoku.calculateDomainOfIndex
//    sudokus.foreach(s => {
//      val problem:CSPProblem[Int,Int] = CSPProblem[Int,Int](s, constraint)
//      domainSudoku.calculateDomain(problem)
////      time += timer({
//      resolver(problem,sudokuTools)
////      })
//      printProblem.printProblem(problem)
//    })
//    println(s"time sum:${time}")
    val quizNumber = 1
    val puzzleFile = s"src/main/resources/ai-lab2-2020-dane/Jolka/puzzle${quizNumber}"
    val wordsFile = s"src/main/resources/ai-lab2-2020-dane/Jolka/words${quizNumber}"
    val jolka:CSP[QuizVariable] = loadQuiz(puzzleFile, wordsFile)


    val domainCalculator = domainPuzzle.calculateDomainOfVariableIndex[QuizVariable] _
    val quizCSP = CSPProblem[QuizVariable](jolka, domainCalculator)

    calculateDomainForEachVariables(quizCSP)

    val heuristicFilterPuzzle:((Option[QuizVariable], Int)) => Boolean = {
      case (variable:Option[QuizVariable], index:Int) => variable.get.value.isEmpty
    }

    val quizValidator = getQuizValidator()
    val createVariable = createQuizVariable
    val heuristic = lowestDomainSizeHeuristic.getNextIndexToResolve[QuizVariable](heuristicFilterPuzzle) _
    val resolver = resolveProblem.resolveProblem[QuizVariable](heuristic, createVariable) _
    resolver(quizCSP, quizValidator)

    printProblem.printProblem(quizCSP)
  }







}
