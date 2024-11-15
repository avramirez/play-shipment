package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.Files.TemporaryFile

import scala.io.Source
import scala.util.Using
import models.{ Rates, Shipment}
import models.Carriers._
import play.api.libs.json.Json
import services.ShipmentService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import HomeController._

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
  def upload(): Action[MultipartFormData[TemporaryFile]] = Action.async(parse.multipartFormData) { implicit request: Request[MultipartFormData[TemporaryFile]] =>

    for {
      fileShipments <- Future {
        request.body.file(FormFields.SHIPMENT_FILE).map(readShipmentFile)
      }
      result <- fileShipments match {
        case Some(shipments) => {
          for {
           calculateCostPerCarrier <- Future.sequence(CARRIERS.map( carrier => ShipmentService.calculateTotalCostForCarrier(shipments, Rates.PROVIDER_RATES, carrier)))
          } yield(Ok(Json.toJson(calculateCostPerCarrier)))
        }
        case None => Future { BadRequest(ErrorMessages.FILE_MISSING_BAD_CONTENT) }
      }
    } yield(result)

  }

  def uploadMinimum(): Action[MultipartFormData[TemporaryFile]] = Action.async(parse.multipartFormData) { implicit request: Request[MultipartFormData[TemporaryFile]] =>

    for {
      fileShipments <- Future {
        request.body.file(FormFields.SHIPMENT_FILE).map(readShipmentFile)
      }
      result <- fileShipments match {
        case Some(shipments) => {

          val combinationSize = request.body.asFormUrlEncoded.get(FormFields.CARRIER_COMBINATION).flatMap(_.headOption).map(_.toInt).getOrElse(2) //TODO: PROPER Validation, for now just default to 2 if nothing is found.

          for {
            minimumCost <- ShipmentService.findMinCostCombination(shipments, Rates.PROVIDER_RATES, CARRIERS, combinationSize = combinationSize)
          } yield(Ok(Json.toJson(minimumCost)))
        }
        case None => Future { BadRequest(ErrorMessages.FILE_MISSING_BAD_CONTENT) }
      }
    } yield(result)

  }

  private def readShipmentFile(file: MultipartFormData.FilePart[TemporaryFile]): Seq[Shipment] = {
    Using(Source.fromFile(file.ref.path.toFile)) { source =>
      source.getLines().drop(1).map { line => Shipment(line) }.toList
    }.getOrElse {
      throw new RuntimeException(ErrorMessages.INVALID_FILE_CONTENT)
    }
  }
}

object HomeController {
  private object FormFields {
    val SHIPMENT_FILE = "shipmentFile"
    val CARRIER_COMBINATION = "carrierCombination"
  }

  object ErrorMessages {
    val INVALID_FILE_CONTENT = "Failed to read file content"
    val FILE_MISSING_BAD_CONTENT = "File missing or Bad File Content"
  }
}