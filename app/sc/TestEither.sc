/**
  * Either
  */

val in = "45"
val result: Either[String,Int] = try {
  Right(in.toInt)
} catch {
  case e: Exception =>
    Left(in)
}

println( result match {
  case Right(x) => "You passed me the Int: " + x + ", which I will increment. " + x + " + 1 = " + (x+1)
  case Left(x) => "You passed me the String: " + x
})

val l: Either[String, Int] = Left("flower")
val r: Either[String, Int] = Right(12)
l.left.map(_.size): Either[Int, Int] // Left(6)
r.left.map(_.size): Either[Int, Int] // Right(12)
l.right.map(_.toDouble): Either[String, Double] // Left("flower")
r.right.map(_.toDouble): Either[String, Double] // Right(12.0)