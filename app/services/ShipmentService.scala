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

    val combinationsCostPerCountry = carriers.combinations(combinationSize).toList.flatMap { carrierCombination =>
      val totalCostByCountry = shipments.groupBy(_.countryCode).map { case (countryCode, countryShipments) =>
        val totalCost = countryShipments.map { shipment =>
          // Calculate the minimum cost for the shipment across all carriers in the combination
          carrierCombination.flatMap { carrier =>

            //Use the cached rate of the shipment
            val rateMemoKey = (shipment.shipmentNumber, carrier)
            rateMemo.get(rateMemoKey) match {
              case Some(rate) => rate
              case None => {
                val rate = calculateShipmentCost(shipment, rates, countryCode, carrier).map(_.rate.price)
                rateMemo(rateMemoKey) = rate
                rate
              }
            }

          }.min
        }.sum

        (countryCode, (carrierCombination, totalCost))
      }
      totalCostByCountry
    }.groupBy { case (countryCode, _) =>
      countryCode // Group by country code (._1)
    }.mapValues { countryCosts =>

      // Find the minimum cost combination for each country
      countryCosts.minBy { case (_, (_, totalCost)) =>
        totalCost // Extract the total cost from (carrierCombination, totalCost) tuple (._2._2)
      }._2 // Get the (carrierCombination, totalCost) tuple (._2)
    }

    // Map results to CarrierCombinationSummary case class instances
    combinationsCostPerCountry.map { case (countryCode, (carrierCombination, totalCost)) =>
      CarrierCombinationSummary(countryCode, carrierCombination, totalCost)
    }.toSeq
  }
}