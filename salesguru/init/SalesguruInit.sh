# These values would be securely stored on the server, not in plaintext

export SALESGURU_KEYSTORE_PATH=keystore.p12
export SALESGURU_KEYSTORE_PASSWORD=cs600

export SALESGURU_DB_URL=jdbc:postgresql://localhost:5432/salesguru
export SALESGURU_DB_USER=cs600
export SALESGURU_DB_PASSWORD=cs600

export SALESGURU_AUTH_DB_URL=jdbc:postgresql://localhost:5432/salesguru_auth
export SALESGURU_AUTH_DB_USER=cs600
export SALESGURU_AUTH_DB_PASSWORD=cs600

export SALESGURU_SEED_USERNAME=cs600
export SALESGURU_SEED_PASSWORD=cs600

cd ..
mvn clean salesguru:run
