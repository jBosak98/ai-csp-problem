

object main {
  def main(args: Array[String]): Unit = {
    val filename = "src/main/resources/ai-lab2-2020-dane/Sudoku2.csv"
    val sudokus = loadSudokus.loadSudokus(filename);
    println(sudokus)
    sudokus.foreach(_.rowSudoku.foreach(println))

  }


}
