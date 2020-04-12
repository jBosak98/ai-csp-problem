import heuristics.lowestDomainSizeHeuristic._
import model.CSPProblem
import tools._
object main {

  def main(args: Array[String]): Unit = {
    val filename = "src/main/resources/ai-lab2-2020-dane/Sudoku2.csv"
    val sudokus = loadSudokus.loadSudokus(filename)

    var time = 0L
    val heuristic = getNextIndexToResolve[Int] _
    val resolver = resolveProblem.resolveProblem[Int](heuristic) _
    sudokus.foreach(s => {
      calculateDomain.calculateDomain(s)
//      time += timer({
      resolver(s,sudokuTools)
//      })
      printSudoku.printSudoku(s)
    })
//    println(s"time sum:${time}")
//    val puzzleFile = "src/main/resources/ai-lab2-2020-dane/Jolka/puzzle0"
//    val wordsFile = "src/main/resources/ai-lab2-2020-dane/Jolka/words0"
//    val jolka = loadJolkas.loadJolka(
//      puzzleFile = puzzleFile,
//      wordsFile = wordsFile
//    )
  }


}
