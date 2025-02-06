# keycloak-oidc4vcauthn

![Badge-License]

![Badge-Maturing]


SPI that implements custom authenticator for Self-Issued OpenID Provider v2 (SIOPv2) and OID4VCI specifications.  

A Java SPI for Keycloak that supports SIOPv2 (OpenID Connect for Verifiable Credential-based Authentication). This SPI integrates Verifiable Credential (VC) authentication into Keycloak’s authentication flow.

Steps for Integration
	1.	Implement an Authentication Provider – This will verify VCs using an external verifier.
	2.	Register the SPI in Keycloak – Deploy it as a JAR in Keycloak’s providers directory.
	3.	Configure the Authentication Flow – Use this SPI as an authentication mechanism.

## Key Features

- Accepts Verifiable Credentials from a user’s digital wallet.
- Uses an external verifier (e.g., a Verifiable Data Registry or DID resolver) to validate the credential.
- Implements Keycloak’s Authentication SPI for integration into Keycloak flows.

## Benefits

Benefits of using SIOPv2 for user authentication

- Eliminates Passwords – Reduces phishing risks and security breaches.
- Improves Privacy – Users control what they share, avoiding unnecessary data exposure.
- Strong Authentication – Uses cryptographic verification, reducing fraud risks.
- Interoperable – Works across industries and ecosystems using open standards.
- User-Centric – The user owns and controls their credentials, increasing trust.

### How SIOPv2 works

SIOPv2 follows a similar flow to traditional OpenID Connect authentication, but with VCs replacing traditional authentication methods:

- SIOPv2 integrates with Keycloak acting as a custom external Identity Provider plugin that:

 • Accept a DID (Decentralized Identifier) or JWK-based authentication.
 • Verify the SIOPv2 ID Token.
 • Verify the Verifiable Presentation (VP) using a trusted Verifiable Data Registry (e.g., blockchain, decentralized ledger, or an external
verification service).

3. Extend Keycloak to Verify Verifiable Credentials (VCs)
	•	Use Keycloak’s Custom Authentication SPI to process Verifiable Credentials within the authentication pipeline.
	•	Implement a plugin that:
	•	Extracts the Verifiable Credential (VC) from the VP.
	•	Validates the cryptographic signature of the VC.
	•	Checks the trust framework (e.g., did:web, did:ethr, W3C-based trust anchors).
	•	Maps VC claims to Keycloak user attributes or roles.

4. Configure an OIDC Client for SIOPv2
	•	Configure Keycloak’s OIDC client settings to allow authentication via SIOPv2.
	•	Adjust the authentication request to support openid:// and DID-based interactions.

5. Customize Keycloak’s Token Exchange
	•	If the RP requires JWT-based access tokens, implement token exchange within Keycloak.
	•	Convert Verifiable Credentials into OIDC claims in the issued ID Token.

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
