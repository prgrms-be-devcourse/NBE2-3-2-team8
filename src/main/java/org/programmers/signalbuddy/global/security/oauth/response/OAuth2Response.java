package org.programmers.signalbuddy.global.security.oauth.response;

import java.io.Serializable;

public interface OAuth2Response extends Serializable {

    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();
}
