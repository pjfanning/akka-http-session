package com.github.pjfanning.session.javadsl

import com.github.pjfanning.session

/**
 * Can't use the trait com.github.pjfanning.session.InMemoryRefreshTokenStorage in Java code, hence this wrapper
 * http://stackoverflow.com/questions/7637752/using-scala-traits-with-implemented-methods-in-java
 */
abstract class InMemoryRefreshTokenStorage[T]() extends session.InMemoryRefreshTokenStorage[T]
