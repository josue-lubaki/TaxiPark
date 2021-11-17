package ca.josue.taxipark.src

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
    this.allDrivers.subtract(this.trips.map { it.driver }.toSet())

//    this.allDrivers.filter {
//        it !in trips.map { trip -> trip.driver  }
//    }.toSet()

//    this.allDrivers.filter { driver ->
//        this.trips.none { trip -> trip.driver == driver }
//    }.toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        this.allPassengers.filter {
            this.trips.count { trip -> it in trip.passengers } >= minTrips
        }.toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
    this.allPassengers.filter {
        this.trips.count { trip ->
            it in trip.passengers && trip.driver == driver
        } > 1
    }.toSet()

//    this.allPassengers.filter {
//        passager -> this.trips.filter {
//                trip -> passager in trip.passengers && trip.driver == driver
//        }.count() > 1
//    }.toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
    this.allPassengers.filter {
        this.trips.count { trip -> it in trip.passengers && trip.discount != null } >
        this.trips.count { trip -> it in trip.passengers && trip.discount == null }
    }.toSet()


/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    if(this.trips.isEmpty())
        return null

    val maxDurationAllTrip = this.trips.maxOfOrNull { it.duration } ?: 0
    val result = HashMap<Int, IntRange>()

    for (i in 0..maxDurationAllTrip step 10){
        // Actual Range
        val range = IntRange(i, i + 9)

        //count of trip by Range
        val numberTripByRange = this.trips.count{ it.duration in range }
        result[numberTripByRange] = range
    }

    return result.maxByOrNull { it.key }?.value

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
