package com.zedwerks.keycloak.broker.providers;

import org.keycloak.broker.oidc.OIDCIdentityProvider;
import org.keycloak.broker.oidc.OIDCIdentityProviderConfig;
import org.keycloak.broker.provider.IdentityProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.broker.provider.IdentityProvider;
import org.keycloak.broker.provider.AuthenticationRequest;
import org.keycloak.broker.provider.AuthenticationResponse;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.services.managers.AuthenticationManager;

public class SIOPv2IdentityProviderFactory implements IdentityProviderFactory<SIOPv2IdentityProvider> {
    
    public static final String PROVIDER_ID = "siopv2";

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public SIOPv2IdentityProvider create(KeycloakSession session) {
        return new SIOPv2IdentityProvider(session, new OIDCIdentityProviderConfig());
    }

    @Override
    public void init(KeycloakSessionFactory factory) {
        // Initialization logic if needed
    }
}
