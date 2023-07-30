package com.softwaremill.example.session.manager

import com.softwaremill.session.{JValueSessionSerializer, JwtSessionEncoder, SessionConfig, SessionManager, SessionSerializer}
import org.json4s.JValue

class JWTSessionManagerScala {

  case class SessionData(value: String)

  implicit val serializer: SessionSerializer[SessionData, JValue] = JValueSessionSerializer.caseClass[SessionData]
  implicit val encoder: JwtSessionEncoder[SessionData] = new JwtSessionEncoder[SessionData]
  implicit val manager: SessionManager[SessionData] = new SessionManager(SessionConfig.fromConfig())

}
