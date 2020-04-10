import tools.loadSudokus.loadSudokus
import tools.printSudoku.printSudoku
import tools.timer.timer
import tools.{calculateDomain, resolveSudoku}

object main {

  def main(args: Array[String]): Unit = {
    val filename = "src/main/resources/ai-lab2-2020-dane/Sudoku2.csv"
    val sudokus = loadSudokus(filename)

//    printSudoku(sudokus.head)
    var time = 0L
    sudokus.foreach(s => {
      calculateDomain.calculateDomain(s)
      time += timer({
        resolveSudoku.resolveSudoku(s)
      })
      printSudoku(s)
    })
    println(s"time sum:${time}")

  }


}

