package com.zedwerks.keycloak.authentication.authenticators;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.models.UserModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.services.messages.Messages;
import org.keycloak.authentication.authenticators.directgrant.AbstractDirectGrantAuthenticator;
import org.jboss.logging.Logger;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class OIDC4VCAuthnAuthenticator extends AbstractDirectGrantAuthenticator {

    private static final Logger LOG = Logger.getLogger(OIDC4VCAuthnAuthenticator.class);

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        String vcToken = formData.getFirst("vc_token"); // Expecting a VC token in request

        if (vcToken == null || vcToken.isEmpty()) {
            context.failure(AuthenticationFlowError.INVALID_CREDENTIALS,
                    Response.status(Response.Status.BAD_REQUEST).entity("Missing Verifiable Credential Token").build());
            return;
        }

        // Validate the Verifiable Credential
        boolean isValid = verifyVC(vcToken);

        if (!isValid) {
            context.failure(AuthenticationFlowError.INVALID_CREDENTIALS,
                    Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Verifiable Credential").build());
            return;
        }

        // Find or create the user in Keycloak
        UserModel user = context.getSession().users().getUserByUsername(context.getRealm(), extractSubject(vcToken));

        if (user == null) {
            context.failure(AuthenticationFlowError.UNKNOWN_USER);
            return;
        }

        context.setUser(user);
        context.success();
    }

    private boolean verifyVC(String vcToken) {
        // TODO: Implement external VC verification logic (e.g., call a VC verifier service)
        // For now, assume it's always valid.
        LOG.info("Validating Verifiable Credential Token: " + vcToken);
        return true;
    }

    private String extractSubject(String vcToken) {
        // TODO: Extract subject (DID or user ID) from the VC token
        // Assuming the subject is hardcoded for now
        return "test-user";
    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        // No required actions
    }

    @Override
    public void close() {
        // Clean up resources if needed
    }
}