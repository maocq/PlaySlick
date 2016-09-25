package models

/**
  * Usuario
  * @param id
  * @param firstName
  * @param lastName
  * @param email
  */

case class User(id: Option[Long], firstName: String, lastName: String, email: String)