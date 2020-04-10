package tools

import java.util.concurrent.TimeUnit

object timer {
  def timer[R](block: => R): Long = {
    val t0 = System.nanoTime()
    val result = block
    val t1 = System.nanoTime()
    val elapsedTime = TimeUnit.NANOSECONDS.toMillis(t1 - t0)
    println("Elapsed time: " + (elapsedTime) + "ms")
    elapsedTime
  }
}
