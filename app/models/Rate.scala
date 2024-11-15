package models

import play.api.libs.json.{Format, Json}

case class Rate(countryCode: String, carrier: String, price: Double, minWeight: Double, maxWeight: Double)

object Rate {
  implicit val rateFormat: Format[Rate] = Json.format[Rate]
}