// package day2.http

// import minitest._

// import scala.concurrent._
// import scala.concurrent.ExecutionContext.Implicits.global

// import day2.http.Round9._

// object Round9Tests extends SimpleTestSuite {

//   testAsync("match the /hello route") {
//     val req = Request(POST, Uri("/hello"), "matteo")
//     for (res <- app(req))
//       yield assertEquals(res, Response(OK, "Hello, matteo!"))
//   }

//   testAsync("match the /ciao route") {
//     val req = Request(POST, Uri("/ciao"), "matteo")
//     for (res <- app(req))
//       yield assertEquals(res, Response(OK, "Ciao, matteo!"))
//   }

//   testAsync("fallback route") {
//     val req = Request(POST, Uri("/not-hello"), "matteo")
//     for (res <- app(req))
//       yield assertEquals(res, Response(NotFound))
//   }

//   testAsync("translate on route") {
//     val req = Request(POST, Uri("/hello"), "matteo")
//     for (res <- appTranslateOnRoute(req))
//       yield assertEquals(res, Response(OK, "Ciao, matteo!"))
//   }

//   testAsync("translate on app") {
//     val req = Request(POST, Uri("/hello"), "matteo")
//     for (res <- appTranslateOnApp(req))
//       yield assertEquals(res, Response(OK, "Ciao, matteo!"))
//   }

// }
