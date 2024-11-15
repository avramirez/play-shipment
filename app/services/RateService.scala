package services

import models.Rate

object RateService {

  def filterRatesByWeight(rates: Seq[Rate], weight: Double, countryCode: Option[String] = None, carrier: Option[String] = None): Seq[Rate] = {
    rates.filter { rate =>
      rate.minWeight <= weight && rate.maxWeight >= weight &&
        countryCode.forall(rate.countryCode == _) &&
        carrier.forall(rate.carrier == _)
    }
  }
}