package models

import play.api.libs.json.{Format, Json}

case class CountrySummary(countryCode: String, totalCost: Double)

object CountrySummary {
  implicit val countrySummaryFormat: Format[CountrySummary] = Json.format[CountrySummary]
}

case class CarrierSummary(carrier: String, totalCost: Double, countrySummary: List[CountrySummary])

object CarrierSummary {
  implicit val carrierSummaryFormat: Format[CarrierSummary] = Json.format[CarrierSummary]
}

case class ShipmentSummary(shipment: Shipment, rate: Rate)

object ShipmentSummary {
  implicit val shipmentSummary: Format[ShipmentSummary] = Json.format[ShipmentSummary]
}

case class CarrierCombinationSummary(countryCode: String, carriers: Seq[String], totalCost: Double)

object CarrierCombinationSummary {
  implicit val carrierCombinationSummary: Format[CarrierCombinationSummary] = Json.format[CarrierCombinationSummary]
}

object Carriers {
  val CARRIERS: Seq[String] = List("Provider A", "Provider B", "Provider C")
}