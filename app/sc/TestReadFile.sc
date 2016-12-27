import scala.io.Source
import scala.util.Try

/**
  * COMPILAR ANTES DE PROBAR
  */

/*
def readFile(file: String): Try[List[String]] = Try {
  Source.fromFile(s"${file}").getLines()toList
}
val listNumber = readFile("/home/mauricio/mao/mao/info/list.txt")
*/

case class TasaFraccionamiento(formaPago: String, factor: BigDecimal)

def fileName( archivo: String ) = s"/factor-fraccionamiento-$archivo.csv"

val stream = getClass.getResourceAsStream( fileName( "2016" ) )

def tasasFraccionamiento() = Try[ List[TasaFraccionamiento] ] {
  println(stream)
  Source.fromInputStream( stream ).getLines().toList
    .map( _.split(",") )
    .map { vals =>
      TasaFraccionamiento( vals( 0 ), BigDecimal( vals( 1 ) ) )
    }
}

val tasas = tasasFraccionamiento()

tasas.map { x=>
  println(x)
}

