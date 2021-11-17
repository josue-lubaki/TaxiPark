package ca.josue.solution.src

import ca.josue.solution.test.passenger

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
    allDrivers - trips.map{ it.driver}.toSet()
// this.allDrivers.filter { d -> trips.none{ it.driver == d }}.toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
    allPassengers.filter { p ->
        trips.count { p in it.passengers } >= minTrips
    }.toSet()

//    trips.flatMap (Trip::passengers)
//        .groupBy { passenger -> passenger }
//        .filterValues { group -> group.size >= minTrips }
//        .keys


/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
    trips
        .filter { trip -> trip.driver == driver }
        .flatMap(Trip::passengers)
        .groupBy { passenger -> passenger }
        .filterValues{ group -> group.size > 1}
        .keys

//    this.allPassengers.filter {
//        this.trips.count { trip ->
//            it in trip.passengers && trip.driver == driver
//        } > 1
//    }.toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger>
//{
//    val (tripsWithDiscount, tripsWithoutDiscount) = trips.partition { it.discount != null }
//    return allPassengers
//        .filter { passenger ->
//            tripsWithDiscount.count { passenger in it.passengers } >
//                    tripsWithoutDiscount.count { passenger in it.passengers }
//        }
//        .toSet()
//}

//= allPassengers.associateWith { p -> trips.filter { t -> p in t.passengers } }
//    .filterValues { group ->
//        val (withDiscount, withoutDiscount) = group
//            .partition { it.discount != null }
//        withDiscount.size > withoutDiscount.size
//    }
//    .keys

= allPassengers.filter { p ->
    val withDiscount = trips.count { t -> p in t.passengers && t.discount != null }
    val withoutDiscount = trips.count { t-> p in t.passengers && t.discount == null }
    withDiscount > withoutDiscount
}.toSet()


/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
  return trips
      .groupBy {
          val start = it.duration / 10 * 10
          val end = start + 9
          start..end
      }
      .maxByOrNull { (_, group) -> group.size }
      ?.key

}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    return if (this.trips.isNotEmpty())
        this.trips
            .map{it.driver to it.cost}
            .groupBy{it.first}
            .mapValues { it.value.sumOf { pair -> pair.second } }
            .values
            .sortedDescending()
            .take((this.allDrivers.size * 0.2).toInt())
            .sum() >= this.trips.map { it.cost }.sum() * 0.8
    else false
}
