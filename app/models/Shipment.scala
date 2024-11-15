package models

import play.api.libs.json.{Format, Json }

case class Shipment(shipmentNumber: String,countryCode: String,weight: Double)

object Shipment {
  def apply(csvString: String): Shipment = {
    val shipment = csvString.split(",").map(_.trim)

    val shipmentNumber = shipment(0)
    val countryCode = shipment(1)
    val weight = shipment(2)

    new Shipment(shipmentNumber, countryCode, weight.toDouble)
  }

  implicit val shipmentFormat: Format[Shipment] = Json.format[Shipment]
}