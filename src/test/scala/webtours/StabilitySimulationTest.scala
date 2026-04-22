package webtours

import io.gatling.core.Predef._

class StabilitySimulationTest extends Simulation {
  setUp(CommonScenario()
    .inject(
      constantConcurrentUsers(34).during(3600)
    ))
    .protocols(httpProtocol)
}
