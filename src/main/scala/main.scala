import tools.loadSudokus.loadSudokus
import tools.printSudoku.printSudoku
import tools.timer.timer
import tools.{SudokuResolver, calculateDomain, sudokuTools}

object main {

  def main(args: Array[String]): Unit = {
    val filename = "src/main/resources/ai-lab2-2020-dane/Sudoku2.csv"
    val sudokus = loadSudokus(filename)

//    printSudoku(sudokus.head)
    var time = 0L
    val sudokuResolver = new SudokuResolver(sudokuTools)
    sudokus.foreach(s => {
      calculateDomain.calculateDomain(s)
      time += timer({
        sudokuResolver.resolveProblem(s)
      })
      printSudoku(s)
    })
    println(s"time sum:${time}")

  }


}

