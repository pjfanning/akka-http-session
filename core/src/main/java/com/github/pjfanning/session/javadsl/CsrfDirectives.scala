package com.github.pjfanning.session.javadsl

import com.github.pjfanning.session
import com.github.pjfanning.session.CsrfCheckMode

import java.util.function.Supplier
import org.apache.pekko.http.javadsl.server.Route
import org.apache.pekko.http.javadsl.server.directives.RouteAdapter

/**
 * Java alternative for com.github.pjfanning.session.CsrfDirectives
 */
trait CsrfDirectives {

  def hmacTokenCsrfProtection[T](checkMode: CsrfCheckMode[T], inner: Supplier[Route]): Route = RouteAdapter {
    session.CsrfDirectives.hmacTokenCsrfProtection(checkMode) {
      inner.get.asInstanceOf[RouteAdapter].delegate
    }
  }

  /**
    * @deprecated as of release 0.6.1, replaced by {@link #hmacTokensCsrfProtection()}
    */
  def randomTokenCsrfProtection[T](checkMode: CsrfCheckMode[T], inner: Supplier[Route]): Route =
    hmacTokenCsrfProtection(checkMode, inner)

  def setNewCsrfToken[T](checkMode: CsrfCheckMode[T], inner: Supplier[Route]): Route = RouteAdapter {
    session.CsrfDirectives.setNewCsrfToken(checkMode) {
      inner.get.asInstanceOf[RouteAdapter].delegate
    }
  }

}

object CsrfDirectives extends CsrfDirectives
