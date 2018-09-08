package controllers

import dataset.TrainingDataset
import dataset.TrainingDataset.{TypingData, TypingDataset}
import forms.UserData._
import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  implicit val lofDataset: TypingDataset = TrainingDataset.data


  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(userForm))
  }

  def login() = Action.async{ implicit request: Request[AnyContent] =>

    userForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest("Bad Request!")),
      userData => {
        val typingData = TypingData.fromList(userData.features)
        typingData.getLoOP(5, 1.96).map{loop=>
          if(loop <= 0.05 && userData.username == "juan" && userData.password == "tacocat"){
            Ok(loop.toString)
          } else {
            Unauthorized(loop.toString)
          }
        }
      }
    )
  }
}
