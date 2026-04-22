package webtours

import io.gatling.core.Predef._

class MaxCapacitySimulationTest extends Simulation {
  setUp(CommonScenario()
    .inject(incrementConcurrentUsers(2)
      .times(20)
      .eachLevelLasting(60)
      .separatedByRampsLasting(10)
      .startingFrom(20)))
    .protocols(httpProtocol)
}
