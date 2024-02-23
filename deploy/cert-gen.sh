#!/bin/bash

JKS_PASS=app123456

DIR_CA=volumes/ca
DIR_ENVOY=volumes/envoy/certs
DIR_KC=volumes/keycloak/certs

# Create directories
mkdir -p $DIR_CA
mkdir -p $DIR_ENVOY
mkdir -p $DIR_APP
mkdir -p $DIR_KC

# Generate CA private key
openssl genpkey -algorithm RSA -out $DIR_CA/ca.key

# Generate CA certificate
openssl req -x509 -new -nodes -key $DIR_CA/ca.key -sha256 -days 365 -out $DIR_CA/ca.crt -subj "/CN=my_ca"

# envoy certificates ----------------------------------------------------

# Generate envoy_sidecar private key
openssl genpkey -algorithm RSA -out $DIR_ENVOY/envoy_sidecar.key

# Generate a certificate signing request (CSR) for envoy_sidecar
openssl req -new -key $DIR_ENVOY/envoy_sidecar.key -out $DIR_ENVOY/envoy_sidecar.csr -subj "/CN=envoy_sidecar"

# Sign the CSR with the CA to get the envoy_sidecar certificate
openssl x509 -req -in $DIR_ENVOY/envoy_sidecar.csr -CA $DIR_CA/ca.crt -CAkey $DIR_CA/ca.key -CAcreateserial -out $DIR_ENVOY/envoy_sidecar.crt -days 365 -sha256

# keycloack certificates ----------------------------------------------------

# Generate Keycloak private key and certificate
openssl genpkey -algorithm RSA -out $DIR_KC/keycloak.key
openssl req -new -key $DIR_KC/keycloak.key -out $DIR_KC/keycloak.csr -subj "/CN=keycloak"
openssl x509 -req -in $DIR_KC/keycloak.csr -CA $DIR_CA/ca.crt -CAkey $DIR_CA/ca.key -CAcreateserial -out $DIR_KC/keycloak.crt -days 365 -sha256

# Convert Keycloak certificates to PKCS12 format
openssl pkcs12 -export -out $DIR_KC/keycloak.p12 -inkey $DIR_KC/keycloak.key -in $DIR_KC/keycloak.crt -name keycloak -passout pass:$JKS_PASS

# Convert PKCS12 to JKS
keytool -importkeystore -srckeystore $DIR_KC/keycloak.p12 -srcstoretype pkcs12 -destkeystore $DIR_KC/keycloak.jks -deststoretype JKS -srcstorepass $JKS_PASS -deststorepass $JKS_PASS

echo "Certificates generated successfully!"
