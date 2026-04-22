package webtours

import io.gatling.core.Predef._
import io.gatling.core.structure._
import webtours.Feeders.users

object CommonScenario {
  def apply(): ScenarioBuilder = new CommonScenario().scn
}

class CommonScenario {

  val loginGroup: ChainBuilder = group("my login") {
    feed(users)
      .exec(Actions.getMainPage)
      .exec(Actions.getUserSession)
      .exec(Actions.postLogin)
  }

  val scn: ScenarioBuilder = scenario("Common scenario")
    .exec(loginGroup)
    .exec(Actions.getFlightsPage)
    .exec(Actions.selectCity)
    .exec(Actions.selectFlight)
    .exec(Actions.buyTickets)
    .exec(Actions.getHomePage)
    .exec(Actions.getMenu)
    .exec(Actions.getLogin)
}
