#!/bin/bash

echo "Waiting for SQL Server..."

until /opt/mssql-tools18/bin/sqlcmd \
    -S mssql \
    -U sa \
    -P "$MSSQL_SA_PASSWORD" \
    -C \
    -Q "SELECT 1"
do
    sleep 5
done


echo "SQL Server is ready."


echo "Creating database..."


/opt/mssql-tools18/bin/sqlcmd \
    -S mssql \
    -U sa \
    -P "$MSSQL_SA_PASSWORD" \
    -C \
    -i /scripts/init.sql


echo "Database initialized successfully."