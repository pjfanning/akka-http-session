package com.github.pjfanning.session.example.session

import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.http.scaladsl.server.Directives._
import org.apache.pekko.stream.ActorMaterializer
import com.github.pjfanning.session.SessionDirectives._
import com.github.pjfanning.session.{InMemoryRefreshTokenStorage, SessionConfig, SessionManager}
import com.github.pjfanning.session.SessionOptions._
import com.github.pjfanning.session.SessionResult._
import com.typesafe.scalalogging.StrictLogging

import scala.io.StdIn

object VariousSessionsScala extends App with StrictLogging {
  implicit val system: ActorSystem = ActorSystem("example")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  import system.dispatcher

  val sessionConfig = SessionConfig.default(
    "c05ll3lesrinf39t7mc5h6un6r0c69lgfno69dsak3vabeqamouq4328cuaekros401ajdpkh60rrtpd8ro24rbuqmgtnd1ebag6ljnb65i8a55d482ok7o0nch0bfbe")
  implicit val sessionManager: SessionManager[MyScalaSession] = new SessionManager[MyScalaSession](sessionConfig)
  implicit val refreshTokenStorage: InMemoryRefreshTokenStorage[MyScalaSession] = new InMemoryRefreshTokenStorage[MyScalaSession] {
    def log(msg: String) = logger.info(msg)
  }

  def mySetSession(v: MyScalaSession) = setSession(refreshable, usingCookies, v)

  val myRequiredSession = requiredSession(refreshable, usingCookies)
  val myInvalidateSession = invalidateSession(refreshable, usingCookies)

  val routes =
    path("secret") {
      get {
        // type: Long, or whatever the T parameter is
        requiredSession(oneOff, usingCookies) { session =>
          complete {
            "treasure"
          }
        }
      }
    } ~
      path("open") {
        get {
          // type: Option[Long] (Option[T])
          optionalSession(oneOff, usingCookies) { session =>
            complete {
              "small treasure"
            }
          }
        }
      } ~
      path("detail") {
        get {
          // type: SessionResult[Long] (SessionResult[T])
          // which can be: Decoded, DecodedLegacy, CreatedFromToken, Expired, Corrupt, NoSession
          session(oneOff, usingCookies) { sessionResult =>
            sessionResult match {
              case Decoded(session) =>
                complete {
                  "decoded"
                }
              case DecodedLegacy(session) =>
                complete {
                  "decoded legacy"
                }
              case CreatedFromToken(session) =>
                complete {
                  "created from token"
                }
              case NoSession =>
                complete {
                  "no session"
                }
              case TokenNotFound =>
                complete {
                  "token not found"
                }
              case Expired =>
                complete {
                  "expired"
                }
              case Corrupt(exc) =>
                complete {
                  "corrupt"
                }
            }
          }
        }
      }

  val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)

  println("Server started, press enter to stop. Visit http://localhost:8080 to see the demo.")
  StdIn.readLine()

  import system.dispatcher

  bindingFuture
    .flatMap(_.unbind())
    .onComplete { _ =>
      system.terminate()
      println("Server stopped")
    }
}
