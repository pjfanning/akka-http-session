package com.github.pjfanning.session.javadsl;

import com.github.pjfanning.session.CookieST$;
import com.github.pjfanning.session.HeaderST$;
import com.github.pjfanning.session.SetSessionTransport;

/**
 * Wrapper for session transports in com.github.pjfanning.session.SetSessionTransport
 */
public final class SessionTransports {

    public static final SetSessionTransport CookieST = CookieST$.MODULE$;
    public static final SetSessionTransport HeaderST = HeaderST$.MODULE$;

    private SessionTransports() {
    }

}
