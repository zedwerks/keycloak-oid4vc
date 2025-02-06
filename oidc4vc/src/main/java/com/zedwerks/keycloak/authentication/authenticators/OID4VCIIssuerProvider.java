package com.zedwerks.keycloak.authentication.authenticators;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.authenticators.directgrant.AbstractDirectGrantAuthenticator;
import org.keycloak.models.UserModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.services.messages.Messages;
import org.jboss.logging.Logger;

import javax.ws.rs.core.Response;
import org.json.JSONObject;

public class OID4VCIIssuerProvider extends AbstractDirectGrantAuthenticator {

    private static final Logger LOG = Logger.getLogger(OID4VCIIssuerProvider.class);

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        UserModel user = context.getUser();

        if (user == null) {
            context.failure(AuthenticationFlowError.UNKNOWN_USER);
            return;
        }

        // Generate a Verifiable Credential JSON-LD
        String vc = generateVerifiableCredential(user);

        if (vc == null) {
            context.failure(AuthenticationFlowError.INVALID_CREDENTIALS,
                    Response.status(Response.Status.UNAUTHORIZED).entity("VC issuance failed").build());
            return;
        }

        // Return VC in token response
        context.getAuthenticationSession().setUserSessionNote("vc", vc);
        context.success();
    }

    private String generateVerifiableCredential(UserModel user) {
        try {
            JSONObject vc = new JSONObject();
            vc.put("@context", "https://www.w3.org/2018/credentials/v1");
            vc.put("type", new String[]{"VerifiableCredential", "IDCard"});
            vc.put("issuer", "https://keycloak.example.com");
            vc.put("credentialSubject", new JSONObject()
                    .put("id", user.getId())
                    .put("name", user.getUsername())
                    .put("email", user.getEmail()));

            // TODO: Sign the VC using a private key (e.g., using a JWT or DID method)
            return vc.toString();
        } catch (Exception e) {
            LOG.error("Error generating Verifiable Credential", e);
            return null;
        }
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
    }

    @Override
    public void close() {
    }
}