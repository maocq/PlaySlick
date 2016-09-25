package dal

import javax.inject.{Inject, Singleton}

import models.User
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.Future

@Singleton
class UserRepository @Inject()(dbConfigProvider: DatabaseConfigProvider){


  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class Users(tag: Tag) extends Table[User](tag, "DB_USERS") {
    def id = column[Option[Long]]("ID", O.PrimaryKey, O.AutoInc)
    def firstName = column[String]("FIRSTNAME")
    def lastname = column[String]("LASTNAME")
    def email = column[String]("EMAIL")

    def * = (id, firstName, lastname, email) <> (User.tupled, User.unapply)
  }

  private val usersdb = TableQuery[Users]


  //val setup = DBIO.seq(
  //  (usersdb.schema).create
  //)

  def list(): Future[Seq[User]] = db.run {
    usersdb.result
  }

  def get(id: Long): Future[Option[User]] = {
    db.run { usersdb.filter(_.id === id).result.headOption }
  }

  def add(user: User): Future[Option[Long]] = {
    val insert = (usersdb returning usersdb.map(_.id)) += user
    db.run(insert)
  }

  def edit(user: User): Future[Int] = {
    db.run {
      usersdb.filter(_.id === user.id).update(user)
    }
  }

  def delete(id: Long): Future[Int] = {
    db.run { usersdb.filter(_.id === id).delete }
  }

}
