# keycloak-oidc4vcauthn
SPI that implements custom authenticator for OIDC4VCAuthN protocol  

A Java SPI for Keycloak that supports OIDC4VCAuthn (OpenID Connect for Verifiable Credential Authentication). This SPI integrates Verifiable Credential (VC) authentication into Keycloak’s authentication flow.

Steps for Integration
	1.	Implement an Authentication Provider – This will verify VCs using an external verifier.
	2.	Register the SPI in Keycloak – Deploy it as a JAR in Keycloak’s providers directory.
	3.	Configure the Authentication Flow – Use this SPI as an authentication mechanism.

## Key Features

✅ Accepts Verifiable Credentials from a user’s digital wallet.
✅ Uses an external verifier (e.g., a Verifiable Data Registry or DID resolver) to validate the credential.
✅ Implements Keycloak’s Authentication SPI for integration into Keycloak flows.

## Benefits

Benefits of using OIDC4VCAuthn for user authentication

✅ Eliminates Passwords – Reduces phishing risks and security breaches.
✅ Improves Privacy – Users control what they share, avoiding unnecessary data exposure.
✅ Strong Authentication – Uses cryptographic verification, reducing fraud risks.
✅ Interoperable – Works across industries and ecosystems using open standards.
✅ User-Centric – The user owns and controls their credentials, increasing trust.

### How OIDC4VCAuthN works

OIDC4VCAuthn follows a similar flow to traditional OpenID Connect authentication, but with VCs replacing traditional authentication methods:
	1.	Authentication Request
	•	A Relying Party (RP) (e.g., a website or application) requests authentication from the user.
	•	Instead of username/password, it asks for a Verifiable Credential that meets certain criteria.
	2.	Presentation of Verifiable Credential
	•	The user selects an appropriate Verifiable Credential stored in their digital wallet (mobile app, browser extension, etc.).
	•	The credential is sent to the RP following an OIDC-based authorization flow.
	3.	Credential Verification
	•	The RP (or an external verifier) validates the credential:
	•	Checking issuer signatures (to verify authenticity).
	•	Ensuring it hasn’t been revoked.
	•	Confirming it meets policy requirements.
	4.	Authentication Success & Token Issuance
	•	If valid, the RP issues an OIDC ID Token confirming authentication.
	•	The user is granted access to the system.