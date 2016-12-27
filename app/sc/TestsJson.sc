
import play.api.libs.json._
import play.api.libs.functional.syntax._


val json: JsValue = Json.obj(
  "name" -> "mao",
  "location" -> "uiop",
  "other" -> "MY OTHER"
)

case class Location(lat: Double, long: Double)
case class Place(name: String, location: String = "med", other: String = "other for default")

implicit val locationReads: Reads[Location] = (
(JsPath \ "lat").read[Double] and
(JsPath \ "long").read[Double]
)(Location.apply _)

implicit val placeReads: Reads[Place] = (
(JsPath \ "name").read[String] and
(JsPath \ "location").read[String]
)((a, b) => Place(a, b))

/*
implicit val placeReadsOthers: Reads[Place] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "location").read[String] and
    (JsPath \ "other").read[String]
  )(Place.apply _)
*/

val placeResult: JsResult[Place] = json.validate[Place]
val response = placeResult.fold(
error => error.toString,
place => place.toString
)

// WRITES

val pw = Place("mao", "mm")

implicit val placeWrites: Writes[Place] = (
  (JsPath \ "name").write[String] and
    (JsPath \ "location").write[String] and
    (JsPath \ "other").write[String]
  )(unlift(Place.unapply))

val placeJsValue = Json.toJson(pw)

val conAdicion = placeJsValue.as[JsObject] + ("adicion" -> Json.toJson(3))