package webtours

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Actions {

  val getMainPage: HttpRequestBuilder = http("getMainPage")
    .get("webtours/")
    .check(status is 200)

  val getUserSession: HttpRequestBuilder = http("getUserSession")
    .get("/cgi-bin/nav.pl")
    .queryParam("in", "home")
    .check(css("input[name='userSession']", "value").saveAs("userSession"))
    .check(status is 200)

  val postLogin: HttpRequestBuilder = http("postLogin")
    .post("cgi-bin/login.pl")
    .formParam("email", "#{login}")
    .formParam("password", "#{password}")
    .formParam("userSession", "#{userSession}")
    .formParam("login.x", "0")
    .formParam("login.y", "0")
    .formParam("JSFormSubmit", "off")
    .check(status is 200)


  val getFlightsPage: HttpRequestBuilder = http("getFlightsPage")
    .get("cgi-bin/reservations.pl")
    .queryParam("page", "welcome")
    .check(status is 200)
    .check(regex("<option[^>]*value=\"(.+)\"").findRandom.saveAs("depart"))
    .check(regex("<option[^>]*value=\"(.+)\"").findRandom.saveAs("arrive"))

  val selectCity: HttpRequestBuilder = http("selectCity")
    .post("cgi-bin/reservations.pl")
    .formParam("depart", "#{depart}")
    .formParam("arrive", "#{arrive}")
    .formParam("departDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")))
    .formParam("returnDate", LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("MM/dd/yyyy")))
    .formParam("roundtrip", "on")
    .formParam("advanceDiscount", 0)
    .formParam("numPassengers", 1)
    .formParam("seatPref", "None")
    .formParam("seatType", "Coach")
    .formParam("findFlights.x", "61")
    .formParam("findFlights.y", "8")
    .formParam(".cgifields", "roundtrip")
    .formParam(".cgifields", "seatType")
    .formParam(".cgifields", "seatPref")
    .check(regex("""outboundFlight" value="([^"]+)"""").findRandom.saveAs("outboundFlight"))
    .check(regex("""returnFlight" value="([^"]+)"""").findRandom.saveAs("returnFlight"))
    .check(regex("""Flight departing from""").exists)

  val selectFlight: HttpRequestBuilder = http("selectFlight")
    .post("cgi-bin/reservations.pl")
    .formParam("outboundFlight", "#{outboundFlight}")
    .formParam("returnFlight", "#{returnFlight}")
    .formParam("advanceDiscount", 0)
    .formParam("numPassengers", 1)
    .formParam("seatPref", "None")
    .formParam("seatType", "Coach")
    .formParam("reserveFlights.x", "32")
    .formParam("reserveFlights.y", "10")
    .check(status is 200)

  val buyTickets: HttpRequestBuilder = http("buyTickets")
    .post("cgi-bin/reservations.pl")
    .formParam("outboundFlight", "#{outboundFlight}")
    .formParam("returnFlight", "#{returnFlight}")
    .formParam("advanceDiscount", 0)
    .formParam("numPassengers", 1)
    .formParam("seatPref", "None")
    .formParam("seatType", "Coach")
    .formParam("firstName", "")
    .formParam("lastName", "")
    .formParam("pass1", "")
    .formParam("JSFormSubmit", "off")
    .formParam("buyFlights.x", "61")
    .formParam("buyFlights.y", "10")
    .formParam(".cgifields", "saveCC")
    .formParam("address1", "")
    .formParam("address2", "")
    .formParam("creditCard", "")
    .formParam("expDate", "")
    .formParam("oldCCOption", "")
    .check(status is 200)
    .check(regex("""Thank you for booking through Web Tours""").exists)

  val getHomePage: HttpRequestBuilder = http("getHomePage")
    .get("cgi-bin/welcome.pl")
    .queryParam("page", "menus")
    .check(status is 200)

  val getMenu: HttpRequestBuilder = http("getMenu")
    .get("cgi-bin/nav.pl")
    .queryParam("page", "menu")
    .queryParam("in", "home")
    .check(status is 200)

  val getLogin: HttpRequestBuilder = http("getLogin")
    .get("cgi-bin/login.pl")
    .queryParam("intro", "true")
    .check(status is 200)
    .check(regex("""Using the menu to the left""").exists)
}
