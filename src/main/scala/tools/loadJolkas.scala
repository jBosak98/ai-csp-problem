package tools

import model.{CSP, CSPProblem}

import scala.io.Source.fromFile

object loadJolkas {
  def loadJolka(puzzleFile:String, wordsFile:String):CSPProblem[String] = {
    val bufferedSource = fromFile(puzzleFile)("UTF-8")
    val lines = bufferedSource.getLines().toList
    bufferedSource.close()

    println(lines.size)
    val variables: Array[Option[String]] = (lines flatMap { line =>
      line map {
        case '_' => Option.empty[String]
        case '#' => Option("#")
      }
    }).toArray
    val size = (lines.head.length,lines.size)


    CSPProblem[String](
      variables = variables,
      domains = Array.empty[List[String]],
      isConstant = variables.map(_.equals("#")),
      availableValues = Nil,
      constraint = {(_,_)=> Nil},
      size = size
    )
    }





}
