package com.zedwerks.keycloak.authenticators.oidc4vc;

import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import java.util.Collections;
import java.util.List;

public class OIDC4VCAuthnAuthenticatorFactory implements AuthenticatorFactory {

    public static final String PROVIDER_ID = "oidc4vc-authenticator";

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getDisplayType() {
        return "OIDC4VC Authenticator";
    }

    @Override
    public String getReferenceCategory() {
        return "OIDC4VC";
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return new AuthenticationExecutionModel.Requirement[] {
                AuthenticationExecutionModel.Requirement.REQUIRED
        };
    }

    @Override
    public Authenticator create(KeycloakSession session) {
        return new OIDC4VCAuthnAuthenticator();
    }

    @Override
    public void init(KeycloakSessionFactory factory) {
        // No initialization required
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // No post-initialization required
    }

    @Override
    public void close() {
        // No resources to close
    }

    @Override
    public List<String> getRequiredActions() {
        return Collections.emptyList();
    }
}