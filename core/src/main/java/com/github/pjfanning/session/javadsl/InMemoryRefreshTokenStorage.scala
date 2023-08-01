package com.github.pjfanning.session.javadsl

import com.github.pjfanning.session
import com.github.pjfanning.session.InMemoryRefreshTokenStorage

/**
 * Can't use the trait com.softwaremill.session.InMemoryRefreshTokenStorage in Java code, hence this wrapper
 * http://stackoverflow.com/questions/7637752/using-scala-traits-with-implemented-methods-in-java
 */
abstract class InMemoryRefreshTokenStorage[T]() extends session.InMemoryRefreshTokenStorage[T]
