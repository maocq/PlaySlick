package utils

import models.{User}
import play.api.libs.json.{JsPath, Json, Reads}
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

object JsonFormat {

  //implicit val userFormat = Json.format[User]

  implicit val usersWrites = Json.writes[User]
  //implicit val usersReads = Json.reads[User]

  implicit val usersReadsValidates: Reads[User] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "firstName").read[String](minLength[String](2)) and
      (JsPath \ "lastName").read[String](minLength[String](4)) and
      (JsPath \ "email").read[String]
    )(User.apply _)

}
