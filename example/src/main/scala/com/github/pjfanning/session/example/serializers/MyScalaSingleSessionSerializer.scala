package com.github.pjfanning.session.example.serializers

import com.github.pjfanning.session.SessionSerializer
import com.github.pjfanning.session.example.SomeScalaComplexObject

import scala.util.Try

class MyScalaSingleSessionSerializer extends SessionSerializer[String, SomeScalaComplexObject] {

  override def serialize(value: String): SomeScalaComplexObject = new SomeScalaComplexObject(value)

  override def deserialize(sco: SomeScalaComplexObject): Try[String] = Try(sco.value)

}
