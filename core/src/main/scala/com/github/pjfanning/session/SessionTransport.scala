package com.github.pjfanning.session

sealed trait GetSessionTransport
sealed trait SetSessionTransport extends GetSessionTransport
case object CookieST extends SetSessionTransport
case object HeaderST extends SetSessionTransport
case object CookieOrHeaderST extends GetSessionTransport
