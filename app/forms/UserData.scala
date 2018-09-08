package forms

import play.api.data.Forms._
import play.api.data._

case class UserData(username: String, password: String, features: List[BigDecimal])

object UserData{
  val userForm = Form(
    mapping(
      "name" -> text,
      "password" -> text,
      "features" -> list(bigDecimal)
    )(UserData.apply)(UserData.unapply)
  )
}

case class TrainingData(typedWord: String, requiredWord: String, features: List[BigDecimal])

object TrainingData{
  val trainingDataForm = Form(
    mapping(
      "required_word" -> text,
      "typed_word" -> text,
      "features" -> list(bigDecimal)
    )(TrainingData.apply)(TrainingData.unapply)
  )

}
