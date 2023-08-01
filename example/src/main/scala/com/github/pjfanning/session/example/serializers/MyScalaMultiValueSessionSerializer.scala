package com.github.pjfanning.session.example.serializers

import com.github.pjfanning.session.MultiValueSessionSerializer
import com.github.pjfanning.session.example.SomeScalaComplexObject

import scala.util.Try

class MyScalaMultiValueSessionSerializer
    extends MultiValueSessionSerializer[SomeScalaComplexObject](
      (sco: SomeScalaComplexObject) => Map("value" -> sco.value),
      (m: Map[String, String]) => Try(new SomeScalaComplexObject(m("value"))))
