package com.example.keycloak.oidc4vc;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.authenticators.directgrant.AbstractDirectGrantAuthenticator;
import org.keycloak.models.UserModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.services.messages.Messages;
import org.jboss.logging.Logger;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import java.util.Scanner;

public class OIDC4VCAuthnAuthenticator extends AbstractDirectGrantAuthenticator {

    private static final Logger LOG = Logger.getLogger(OIDC4VCAuthnAuthenticator.class);
    private static final String VC_VERIFIER_URL = "https://verifier.example.com/verify"; // Replace with actual VC verification service URL

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        String vcToken = formData.getFirst("vc_token");

        if (vcToken == null || vcToken.isEmpty()) {
            context.failure(AuthenticationFlowError.INVALID_CREDENTIALS,
                    Response.status(Response.Status.BAD_REQUEST).entity("Missing Verifiable Credential Token").build());
            return;
        }

        // Send VC to external verifier
        String subject = verifyVC(vcToken);

        if (subject == null) {
            context.failure(AuthenticationFlowError.INVALID_CREDENTIALS,
                    Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Verifiable Credential").build());
            return;
        }

        // Find or create the user in Keycloak
        UserModel user = context.getSession().users().getUserByUsername(context.getRealm(), subject);

        if (user == null) {
            user = context.getSession().users().addUser(context.getRealm(), subject);
            user.setEnabled(true);
        }

        context.setUser(user);
        context.success();
    }

    private String verifyVC(String vcToken) {
        try {
            URL url = new URL(VC_VERIFIER_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject requestJson = new JSONObject();
            requestJson.put("vc", vcToken);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestJson.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                LOG.error("VC verification failed with response code: " + responseCode);
                return null;
            }

            InputStream responseStream = conn.getInputStream();
            String responseBody;
            try (Scanner scanner = new Scanner(responseStream, StandardCharsets.UTF_8.name())) {
                responseBody = scanner.useDelimiter("\\A").next();
            }

            JSONObject responseJson = new JSONObject(responseBody);
            boolean isValid = responseJson.getBoolean("valid");
            if (!isValid) return null;

            return responseJson.getString("subject");

        } catch (Exception e) {
            LOG.error("Error verifying Verifiable Credential", e);
            return null;
        }
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
        // No cleanup needed
    }
}
