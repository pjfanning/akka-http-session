package com.github.pjfanning.session.example.session.manager;

import com.github.pjfanning.session.BasicSessionEncoder;
import com.github.pjfanning.session.SessionConfig;
import com.github.pjfanning.session.SessionEncoder;
import com.github.pjfanning.session.SessionManager;

import static com.github.pjfanning.session.javadsl.SessionSerializers.LongToStringSessionSerializer;

public class MyJavaSessionManager {

    static final SessionEncoder<Long> BASIC_ENCODER = new BasicSessionEncoder<>(LongToStringSessionSerializer);
    static final SessionConfig SESSION_CONFIG = SessionConfig.defaultConfig("some very long unusual string");
    static final SessionManager<Long> SESSION_MANAGER = new SessionManager<>(SESSION_CONFIG, BASIC_ENCODER);

}
