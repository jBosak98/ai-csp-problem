import model.{CSP, CSPModel, CSPProblem, QuizVariable}
import tools._

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
    val puzzleFile = "src/main/resources/ai-lab2-2020-dane/Jolka/puzzle1"
    val wordsFile = "src/main/resources/ai-lab2-2020-dane/Jolka/words1"
    val jolka:CSP[String, QuizVariable] = loadJolkas.loadJolka(
      puzzleFile = puzzleFile,
      wordsFile = wordsFile
    )
    val domainCalculator = domainPuzzle.calculateDomainOfVariableIndex[QuizVariable] _
    val quizCSP = CSPProblem[String, QuizVariable](jolka, domainCalculator)
//    println(quizCSP.variables.toList)

//    quizCSP.variables.indices.foreach { i =>
//      domainPuzzle.calculateDomainOfVariableIndex(quizCSP, i)
//    }
//
//    def createVariable = { (variable:QuizVariable, word:String) =>
//      Option(QuizVariable(
//        index = variable.index,
//        value = Option(word),
//        isVertical = variable.isVertical,
//        size = variable.size
//      ))
//
//    }
//    val heuristicFilterPuzzle:((Option[QuizVariable], Int)) => Boolean = {
//      case (variable:Option[QuizVariable], index:Int) => variable.get.value.isEmpty
//    }
//
//    val heuristic = lowestDomainSizeHeuristic.getNextIndexToResolve[String,QuizVariable](heuristicFilterPuzzle) _
//    val resolver = resolveProblem.resolveProblem[String,QuizVariable](heuristic, createVariable) _
//
//    resolver(quizCSP, sudokuTools)

//    printProblem.printProblem(sudokus.head)
    quizCSP.variables.toList.foreach(println)
    printProblem.printProblem(quizCSP)
  }


}
