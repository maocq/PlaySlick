package controllers

import javax.inject.Inject

import play.api.mvc.{Action, Controller}
import play.api.libs.json._
import play.api.libs.functional.syntax._


class JsonController @Inject() extends Controller{

  /**
    * Contruir un objeto Json
    */
  def index = Action {
    val json: JsValue = Json.parse("""
    {
      "name" : "Watership Down",
      "location" : {
        "lat" : 51.235685,
        "long" : -1.309197
      }
    }
    """)
    Ok(json)
  }

  /**
    * Construcción de clase
    */
  def classConstruction = Action {
    val json: JsValue = JsObject(
      Seq(
      "name" -> JsString("Watership Down"),
      "location" -> JsObject(
          Seq(
            "lat" -> JsNumber(51.235685),
            "long" -> JsNumber(-1.309197)
          )
        )
      )
    )
    Ok(json)
  }

  /**
    * Json obj
    */
  def jsonObj = Action {
    val json: JsValue = Json.obj(
      "name" -> "Watership Down",
      "location" -> Json.obj(
        "lat" -> 51.235685,
        "long" -> -1.309197
      )
    )
    Ok(json)
  }

  /**
    * writes
    */
  def writes = Action {

    case class Location(lat: Double, long: Double)
    case class Place(name: String, location: Location)

    implicit val locationWrites = new Writes[Location] {
      def writes(location: Location) = Json.obj(
        "lat" -> location.lat,
        "long" -> location.long
      )
    }

    implicit val placeWrites = new Writes[Place] {
      def writes(place: Place) = Json.obj(
        "name" -> place.name,
        "location" -> place.location
      )
    }

    val place = Place(
      "Watership Down",
      Location(51.235685, -1.309197)
    )

    val res: JsValue = Json.toJson(place)
    Ok(res)
  }

  /**
    * writes combinator
    */
  def writesCombinator = Action {
    //Requiere import play.api.libs.functional.syntax._

    case class Location(lat: Double, long: Double)
    case class Place(name: String, location: Location)

    implicit val locationWrites: Writes[Location] = (
      (JsPath \ "lat").write[Double] and
        (JsPath \ "long").write[Double]
      )(unlift(Location.unapply))

    implicit val placeWrites: Writes[Place] = (
      (JsPath \ "name").write[String] and
        (JsPath \ "location").write[Location]
      )(unlift(Place.unapply))

    val place = Place(
      "Watership Down",
      Location(51.235685, -1.309197)
    )

    Ok(Json.toJson(place))
  }

  /**
    * Recorrer un objeto Json
    */
  def traversing = Action {
    val json: JsValue = Json.parse("""
    {
      "name" : "Watership Down",
      "location" : {
        "lat" : 51.235685,
        "long" : -1.309197
      }
    }
    """)

    // Obtener latitud
    val lat = (json \ "location" \ "lat").get
    Ok(lat)

    //Recursivo val names = json \\ "name"
    //Indice val bigwig = (json \ "residents")(1)
  }

  /**
    * conversión de un JsValue
    */
  def converting = Action {
    val json: JsValue = Json.obj(
      "name" -> "Watership Down",
      "location" -> Json.obj("lat" -> 51.235685, "long" -> -1.309197)
    )

    val minifiedString: String = Json.stringify(json)
    //val readableString: String = Json.prettyPrint(json)

    Ok(minifiedString)
  }

  /**
    * as
    */
  def as = Action {
    val json: JsValue = Json.obj(
      "name" -> "Watership Down",
      "location" -> Json.obj("lat" -> 51.235685, "long" -> -1.309197)
    )

    //val name = (json \ "name").as[String]
    //val names = (json \\ "name").map(_.as[String])
    val nameOption = (json \ "nam").asOpt[String]
    Ok(nameOption.getOrElse("=("))
  }

  /**
    * validation
    */
  def validation = Action {
    val json: JsValue = Json.obj(
      "name" -> "Watership Down",
      "location" -> Json.obj("lat" -> 51.235685, "long" -> -1.309197)
    )

    val nameResult: JsResult[String] = (json \ "nam").validate[String]

    val nameOption: Option[String] = nameResult.fold(
      invalid = {
        fieldErrors => fieldErrors.foreach(x => {
          println("field: " + x._1 + ", errors: " + x._2)
        })
          None
      },
      valid = {
        name => Some(name)
      }
    )
    Ok(nameOption.getOrElse("ERROR"))
  }

  /**
    * JsValue to a model
    */
  def jsValueToModel = Action {
    val json: JsValue = Json.obj(
      "name" -> "mao",
      "location" -> Json.obj("lat" -> 51.235685, "long" -> -1.309197)
    )

    case class Location(lat: Double, long: Double)
    case class Place(name: String, location: Location)

    implicit val locationReads: Reads[Location] = (
      (JsPath \ "lat").read[Double] and
        (JsPath \ "long").read[Double]
      )(Location.apply _)

    implicit val placeReads: Reads[Place] = (
      (JsPath \ "name").read[String] and
        (JsPath \ "location").read[Location]
      )(Place.apply _)

    val placeResult: JsResult[Place] = json.validate[Place]
    val response = placeResult.fold(
      error => error.toString,
      place => place.toString
    )

    Ok(response)
  }

}
