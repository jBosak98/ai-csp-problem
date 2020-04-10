import java.util.concurrent.TimeUnit

import tools.loadSudokus.loadSudokus
import tools.printSudoku.printSudoku
import tools.{calculateDomain, resolveSudoku}

object main {
  def timer[R](block: => R): Long = {
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    val elapsedTime = TimeUnit.NANOSECONDS.toMillis(t1 - t0)
    println("Elapsed time: " + (elapsedTime) + "ms")
    elapsedTime
  }
  def main(args: Array[String]): Unit = {
    val filename = "src/main/resources/ai-lab2-2020-dane/Sudoku2.csv"
    val sudokus = loadSudokus(filename);
    println(sudokus.size)
    println(sudokus.head.domains.size)
    printSudoku(sudokus.head)
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

