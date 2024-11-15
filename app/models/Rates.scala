package models

object Rates {

  val PROVIDER_RATES: Seq[Rate] = Seq(
    // Rates for Provider A (NL, BE, DE)
    Rate("NL", "Provider A", 50.0, 0.0, 100.0),
    Rate("NL", "Provider A", 110.0, 101.0, 250.0),
    Rate("NL", "Provider A", 170.0, 251.0, 500.0),
    Rate("NL", "Provider A", 290.0, 501.0, 750.0),
    Rate("NL", "Provider A", 650.0, 751.0, 1000.0),

    Rate("BE", "Provider A", 75.0, 0.0, 100.0),
    Rate("BE", "Provider A", 160.0, 101.0, 250.0),
    Rate("BE", "Provider A", 240.0, 251.0, 500.0),
    Rate("BE", "Provider A", 480.0, 501.0, 750.0),
    Rate("BE", "Provider A", 700.0, 751.0, 1000.0),

    Rate("DE", "Provider A", 50.0, 0.0, 100.0),
    Rate("DE", "Provider A", 150.0, 101.0, 250.0),
    Rate("DE", "Provider A", 440.0, 251.0, 500.0),
    Rate("DE", "Provider A", 780.0, 501.0, 750.0),
    Rate("DE", "Provider A", 900.0, 751.0, 1000.0),

    // Rates for Provider B (NL, BE, DE)
    Rate("NL", "Provider B", 100.0, 0.0, 250.0),
    Rate("NL", "Provider B", 190.0, 251.0, 500.0),
    Rate("NL", "Provider B", 270.0, 501.0, 750.0),
    Rate("NL", "Provider B", 350.0, 751.0, 1000.0),

    Rate("BE", "Provider B", 150.0, 0.0, 250.0),
    Rate("BE", "Provider B", 250.0, 251.0, 500.0),
    Rate("BE", "Provider B", 490.0, 501.0, 750.0),
    Rate("BE", "Provider B", 650.0, 751.0, 1000.0),

    Rate("DE", "Provider B", 100.0, 0.0, 250.0),
    Rate("DE", "Provider B", 390.0, 251.0, 500.0),
    Rate("DE", "Provider B", 680.0, 501.0, 750.0),
    Rate("DE", "Provider B", 790.0, 751.0, 1000.0),

    // Rates for Provider C (NL)
    Rate("NL", "Provider C", 50.0, 0.0, 100.0),
    Rate("NL", "Provider C", 80.0, 101.0, 200.0),
    Rate("NL", "Provider C", 120.0, 201.0, 300.0),
    Rate("NL", "Provider C", 200.0, 301.0, 400.0),
    Rate("NL", "Provider C", 300.0, 401.0, 500.0),
    Rate("NL", "Provider C", 500.0, 501.0, 600.0),
    Rate("NL", "Provider C", 600.0, 601.0, 700.0),
    Rate("NL", "Provider C", 800.0, 701.0, 800.0),
    Rate("NL", "Provider C", 950.0, 801.0, 900.0),
    Rate("NL", "Provider C", 950.0, 901.0, 1000.0),

    // Rates for Provider C (BE)
    Rate("BE", "Provider C", 100.0, 0.0, 100.0),
    Rate("BE", "Provider C", 120.0, 101.0, 200.0),
    Rate("BE", "Provider C", 300.0, 201.0, 300.0),
    Rate("BE", "Provider C", 330.0, 301.0, 400.0),
    Rate("BE", "Provider C", 360.0, 401.0, 500.0),
    Rate("BE", "Provider C", 390.0, 501.0, 600.0),
    Rate("BE", "Provider C", 500.0, 601.0, 700.0),
    Rate("BE", "Provider C", 550.0, 701.0, 800.0),
    Rate("BE", "Provider C", 600.0, 801.0, 900.0),
    Rate("BE", "Provider C", 800.0, 901.0, 1000.0),

    // Rates for Provider C (DE)
    Rate("DE", "Provider C", 40.0, 0.0, 100.0),
    Rate("DE", "Provider C", 80.0, 101.0, 200.0),
    Rate("DE", "Provider C", 130.0, 201.0, 300.0),
    Rate("DE", "Provider C", 400.0, 301.0, 400.0),
    Rate("DE", "Provider C", 450.0, 401.0, 500.0),
    Rate("DE", "Provider C", 500.0, 501.0, 600.0),
    Rate("DE", "Provider C", 550.0, 601.0, 700.0),
    Rate("DE", "Provider C", 600.0, 701.0, 800.0),
    Rate("DE", "Provider C", 700.0, 801.0, 900.0),
    Rate("DE", "Provider C", 850.0, 901.0, 1000.0)
  )

}
