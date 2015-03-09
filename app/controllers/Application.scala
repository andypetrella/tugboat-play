package controllers

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.duration._

import play.api._
import play.api.mvc._
import play.api.libs.json._


object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }


  import scala.concurrent.ExecutionContext.Implicits.global
  val docker:tugboat.Docker = tugboat.Docker()

  def dockerList = Action.async { request =>
    import scala.concurrent.ExecutionContext.Implicits.global
    Logger.info(docker.toString)
    Logger.info(Await.result(docker.info(), 1 second).toString)
    val images = docker.images
    images.list().map { list =>
      Ok(Json.toJson(
        list.map { i =>
          i.repoTags
        }
      ))
    }
  }
}