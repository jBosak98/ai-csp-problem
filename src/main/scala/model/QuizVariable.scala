package model

case class QuizVariable(
                  index:Int,
                  value:Option[String],
                  isVertical:Boolean,
                  size:Int
                )