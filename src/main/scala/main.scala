import model.{CSP, CSPModel, CSPProblem}
import tools._

object main {

  def main(args: Array[String]): Unit = {
    val filename = "src/main/resources/ai-lab2-2020-dane/Sudoku2.csv"
    val sudokus = loadSudokus.loadSudokus(filename)

    var time = 0L
//    val heuristic = getNextIndexToResolve[Int] _
//    val resolver = resolveProblem.resolveProblem[Int](heuristic) _
    val constraint:(CSPModel[Int], Int) => List[Int] = domainSudoku.calculateDomainOfIndex
//    sudokus.foreach(s => {
//      val problem:CSPProblem[Int] = CSPProblem[Int](s, constraint)
//      calculateDomain.calculateDomain(problem)
////      time += timer({
//      resolver(problem,sudokuTools)
////      })
//      printProblem.printProblem(problem)
//    })
    println(s"time sum:${time}")
    val puzzleFile = "src/main/resources/ai-lab2-2020-dane/Jolka/puzzle0"
    val wordsFile = "src/main/resources/ai-lab2-2020-dane/Jolka/words0"
    val jolka:CSP[String] = loadJolkas.loadJolka(
      puzzleFile = puzzleFile,
      wordsFile = wordsFile
    )
    val quizCSP = CSPProblem[String](jolka, {(_:CSPModel[String],_:Int)=>Nil})


    domainPuzzle.calculateDomainOfIndex(quizCSP, 0)
    printProblem.printProblem(sudokus.head)
    printProblem.printProblem(quizCSP)
  }


}
