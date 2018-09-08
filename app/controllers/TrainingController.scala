package controllers

import dataset.TrainingDataset
import dataset.TrainingDataset.TypingData
import forms.TrainingData
import javax.inject.Inject
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

class TrainingController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport{

  def trainingHome() = Action { implicit request: Request[AnyContent] =>

    Ok(views.html.train("tacocat", TrainingData.trainingDataForm))

  }

  def trainingData() = Action { implicit request: Request[AnyContent] =>
    TrainingData.trainingDataForm.bindFromRequest().fold(
      _ => BadRequest,
      formData => {
        TrainingDataset.data.appendPoint(
          TypingData.fromList(formData.features)
        )
        Ok(TrainingDataset.data.data.size.toString)
      }
    )
  }
}
