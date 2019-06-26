package com.softwaremill.session

import java.security.{KeyPairGenerator, PrivateKey}
import java.util.Base64

import com.softwaremill.session.JwsAlgorithm.HmacSHA256
import com.typesafe.config.ConfigValueFactory.fromAnyRef
import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest.{FlatSpec, Matchers}

class SessionConfigTest extends FlatSpec with Matchers {

  val fakeServerSecret = s"f4k3S3rv3rS3cr37-${"x" * 64}"

  def referenceConfWithSecret(serverSecret: String): Config =
    ConfigFactory
      .load("reference")
      .withValue("akka.http.session.server-secret", fromAnyRef(serverSecret))

  def configWith(stringValue: String): Config =
    ConfigFactory
      .parseString(stringValue)
      .withFallback(referenceConfWithSecret(fakeServerSecret))

  it should "load and parse default (HS256) JWS config" in {
    val fakeConfig = referenceConfWithSecret(fakeServerSecret)
    fakeConfig.getString("akka.http.session.jws.alg") should equal("HS256")

    val config = SessionConfig.fromConfig(fakeConfig)
    config.jws.alg should equal(HmacSHA256(fakeServerSecret))
  }

  it should "load and parse HS256 JWS config" in {
    val fakeConfig = referenceConfWithSecret(fakeServerSecret)

    val config = SessionConfig.fromConfig(fakeConfig)
    config.jws.alg should equal(HmacSHA256(fakeServerSecret))
  }

  it should "load and parse RS256 JWS config" in {
    val privateKey: PrivateKey = {
      val keyPairGen = KeyPairGenerator.getInstance("RSA")
      keyPairGen.initialize(4096)
      val kp = keyPairGen.generateKeyPair()
      kp.getPrivate
    }
    val encodedPrivateKey: String = Base64.getEncoder.encodeToString(privateKey.getEncoded)
    val fakeConfig = configWith(s"""
        |akka.http.session.jws {
        |  alg = "RS256"
        |  rsa-private-key = "$encodedPrivateKey"
        |}
      """.stripMargin)

    val config = SessionConfig.fromConfig(fakeConfig)
    config.jws.alg should equal(JwsAlgorithm.Rsa(privateKey))
  }

  it should "fail to load config due to missing RSA private key (alg = RS256)" in {
    val fakeConfig = configWith(s"""
         |akka.http.session.jws {
         |  alg = "RS256"
         |}
      """.stripMargin)
    val ex = intercept[IllegalArgumentException] {
      SessionConfig.fromConfig(fakeConfig)
    }
    ex.getMessage should equal("akka.http.session.jws.rsa-private-key must be defined in order to use alg = RS256")
  }

  it should "fail to load config due to empty RSA private key (alg = RS256)" in {
    val fakeConfig = configWith(s"""
         |akka.http.session.jws {
         |  alg = "RS256"
         |  rsa-private-key = ""
         |}
      """.stripMargin)
    val ex = intercept[IllegalArgumentException] {
      SessionConfig.fromConfig(fakeConfig)
    }
    ex.getMessage should equal("akka.http.session.jws.rsa-private-key must be defined in order to use alg = RS256")
  }

  it should "fail to load config due to invalid RSA private key (alg = RS256)" in {
    val fakeConfig = configWith(s"""
         |akka.http.session.jws {
         |  alg = "RS256"
         |  rsa-private-key = "an invalid RSA key"
         |}
      """.stripMargin)
    val ex = intercept[IllegalArgumentException] {
      SessionConfig.fromConfig(fakeConfig)
    }
    ex.getMessage should equal("Invalid RSA private key")
  }

  it should "fail to load config due to unsupported JWS alg" in {
    val fakeConfig = configWith("""akka.http.session.jws.alg = "UNSUPPORTED1" """)
    val ex = intercept[IllegalArgumentException] {
      SessionConfig.fromConfig(fakeConfig)
    }
    ex.getMessage should equal(s"Unsupported JWS alg 'UNSUPPORTED1'. Supported algorithms are: HS256, RS256")
  }

}
