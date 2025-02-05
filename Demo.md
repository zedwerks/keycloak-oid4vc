4. Connecting a Digital Wallet

For testing, you can use:
	•	Microsoft Entra Verified ID (supports OIDC4VC issuance & verification)
	•	DIDKit Wallet (supports JSON-LD VCs)
	•	MetaMask / Web5 Wallets (support DID-based VCs)

Test Flow:
	1.	Issue a Verifiable Credential to a user’s digital wallet.
	2.	Have the user present the VC when authenticating with Keycloak.
	3.	Keycloak calls the verifier service to validate the credential.
	4.	If successful, the user logs in without a password.
