# keycloak-oidc4vcauthn

![Badge-License]

![Badge-Maturing]


SPI that implements custom authenticator for OIDC4VCAuthN protocol  

A Java SPI for Keycloak that supports OIDC4VCAuthn (OpenID Connect for Verifiable Credential Authentication). This SPI integrates Verifiable Credential (VC) authentication into Keycloak’s authentication flow.

Steps for Integration
	1.	Implement an Authentication Provider – This will verify VCs using an external verifier.
	2.	Register the SPI in Keycloak – Deploy it as a JAR in Keycloak’s providers directory.
	3.	Configure the Authentication Flow – Use this SPI as an authentication mechanism.

## Key Features

- ✅ Accepts Verifiable Credentials from a user’s digital wallet.
- ✅ Uses an external verifier (e.g., a Verifiable Data Registry or DID resolver) to validate the credential.
- ✅ Implements Keycloak’s Authentication SPI for integration into Keycloak flows.

## Benefits

Benefits of using OIDC4VCAuthn for user authentication

- ✅ Eliminates Passwords – Reduces phishing risks and security breaches.
- ✅ Improves Privacy – Users control what they share, avoiding unnecessary data exposure.
- ✅ Strong Authentication – Uses cryptographic verification, reducing fraud risks.
- ✅ Interoperable – Works across industries and ecosystems using open standards.
- ✅ User-Centric – The user owns and controls their credentials, increasing trust.

### How OIDC4VCAuthN works

OIDC4VCAuthn follows a similar flow to traditional OpenID Connect authentication, but with VCs replacing traditional authentication methods:

1. Authentication Request
- A Relying Party (RP) (e.g., a website or application) requests authentication from the user.
- Instead of username/password, it asks for a Verifiable Credential that meets certain criteria.
2.	Presentation of Verifiable Credential
- The user selects an appropriate Verifiable Credential stored in their digital wallet (mobile app, browser extension, etc.).
- The credential is sent to the RP following an OIDC-based authorization flow.

3.	Credential Verification
- The RP (or an external verifier) validates the credential:
- Checking issuer signatures (to verify authenticity).
- Ensuring it hasn’t been revoked.
- Confirming it meets policy requirements.
4.	Authentication Success & Token Issuance
- If valid, the RP issues an OIDC ID Token confirming authentication.
- The user is granted access to the system.

## How to Use the SPI

```bash
cp target/zedwerks-oidc4vcauthn.jar /opt/keycloak/providers/
```
Then restart keycloak:

```bash
bin/kc.sh build
bin/kc.sh start
```
### Configure the Keycloak Realm to use OIDC4VCAuthN authenticator

1. Go to Keycloak Admin Console → Select your realm.
2. Navigate to Authentication → Flows.
3. Create a new authentication flow or edit an existing one (browser flow)
4. Add an execution step → Select OIDC4VC Authenticator as the provider.
5. Set the requirement to REQUIRED.
6. Save the flow and apply it to a client.

Consider using keycloak terraform scripting to create your own custom browser-flow 
that uses this authenticator.

#### Testing it out with Curl

```bash
curl -X POST \
  -d "client_id=my-client" \
  -d "grant_type=password" \
  -d "vc_token=<verifiable_credential_base64_encoded>" \
  https://keycloak.example.com/auth/realms/myrealm/protocol/openid-connect/token
```

[Badge-License]: https://img.shields.io/badge/license-apache%202.0-60C060.svg
[Badge-Maturing]: https://img.shields.io/badge/Lifecycle-Maturing-007EC6
[Badge-Stable]: https://img.shields.io/badge/status-Stable-brightgreen
