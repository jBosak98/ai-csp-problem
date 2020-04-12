import tools.loadJolkas

object main {

  def main(args: Array[String]): Unit = {
//    val filename = "src/main/resources/ai-lab2-2020-dane/Sudoku2.csv"
//    val sudokus = loadSudokus(filename)
//
//    var time = 0L
//    val sudokuResolver = new SudokuResolver(sudokuTools)
//    sudokus.foreach(s => {
//      calculateDomain.calculateDomain(s)
//      time += timer({
//        sudokuResolver.resolveProblem(s)
//      })
//      printSudoku(s)
//    })
//    println(s"time sum:${time}")
    val puzzleFile = "src/main/resources/ai-lab2-2020-dane/Jolka/puzzle0"
    val wordsFile = "src/main/resources/ai-lab2-2020-dane/Jolka/words0"
    val jolka = loadJolkas.loadJolka(
      puzzleFile = puzzleFile,
      wordsFile = wordsFile
    )
  }


}
