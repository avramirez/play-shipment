package services

import models.{CarrierCombinationSummary, CarrierSummary, CountrySummary, Rate, Shipment, ShipmentSummary}

import scala.concurrent.{ExecutionContext, Future}
import play.api.Logging

import scala.collection.mutable

object ShipmentService extends Logging {

  def calculateTotalCostForCarrier(
                                    shipments: Seq[Shipment],
                                    rates: Seq[Rate],
                                    carrier: String
                                  )(implicit executionContext: ExecutionContext): Future[CarrierSummary] = Future {

    val groupedShipments = shipments.groupBy(_.countryCode)

    val countrySummaries = groupedShipments.map { case (countryCode, shipments) =>

      val totalCost = shipments.flatMap { shipment =>
        calculateShipmentCost(shipment,rates, countryCode, carrier).map(_.rate.price)
      }.sum

      CountrySummary(countryCode, totalCost)
    }.toList

    val totalCarrierCost = countrySummaries.map(_.totalCost).sum

    CarrierSummary(carrier, totalCarrierCost, countrySummaries)
  }

  def calculateShipmentCost(shipment: Shipment,
                            rates: Seq[Rate],
                            countryCode: String,
                            carrier: String): Option[ShipmentSummary] = {
    (RateService.filterRatesByWeight(rates, shipment.weight, Some(countryCode), Some(carrier)).headOption)
      .map(ShipmentSummary(shipment, _))

  }

  def findMinCostCombination(shipments: Seq[Shipment],
                             rates: Seq[Rate],
                             carriers: Seq[String],
                             combinationSize: Int)(implicit executionContext: ExecutionContext): Future[Seq[CarrierCombinationSummary]] = Future {

    val rateMemo: mutable.Map[(String, String), Option[Double]] = mutable.Map()

    def getRate(shipment: Shipment, carrier: String): Option[Double] = {
      val key = (shipment.shipmentNumber, carrier)
      rateMemo.getOrElseUpdate(key, calculateShipmentCost(shipment, rates, shipment.countryCode, carrier).map(_.rate.price))
    }

    def calculateTotalCostForCombination(carrierCombination: Seq[String], countryShipments: Seq[Shipment]): Double = {
      countryShipments.flatMap { shipment =>
        carrierCombination.flatMap(carrier => getRate(shipment, carrier)).minOption
      }.sum
    }

    def getCombinationsCostForCountry(carriers: Seq[String]): Map[String, (Seq[String], Double)] = {
      carriers.combinations(combinationSize).toList.flatMap { carrierCombination =>
        shipments.groupBy(_.countryCode).map { case (countryCode, countryShipments) =>
          val totalCost = calculateTotalCostForCombination(carrierCombination, countryShipments)
          (countryCode, (carrierCombination, totalCost))
        }
      }.groupBy(_._1).view.mapValues(_.minBy(_._2._2)._2).toMap
    }


    getCombinationsCostForCountry(carriers).map { case (countryCode, (carrierCombination, totalCost)) =>
      CarrierCombinationSummary(countryCode, carrierCombination, totalCost)
    }.toSeq

  }
}