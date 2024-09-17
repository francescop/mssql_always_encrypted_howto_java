# Always Encrypted SQL Server Setup

This project demonstrates how to set up and use Always Encrypted with SQL Server using Java.

## Prerequisites

- Docker
- Java Development Kit (JDK) 11 or later

## Setup

1. Start the SQL Server container:
   ```
   docker-compose up -d
   ```

2. Create the Java keystore:
   ```
   keytool -genkeypair -alias cmk1 -keyalg RSA -keysize 2048 -storetype JKS -keystore ./cmk-keystore.jks -validity 360 -storepass YourStrongPassword
   ```

3. Run the always encrypted setup script (creates the column master key):
   NOTE: You need to use the password you set for the SQL Server SA user in the docker-compose.yml file.
   NOTE: on linux (my case) you need to download the sqlcmd utility.
   ```
   sqlcmd -S 127.0.0.1 -C -U sa -P 'YourStrongPassword123!' -i always_encrypted_setup.sql
   ```

4. Compile and run the Java program that creates the column encryption key:
   ```
   export CLASSPATH=/path/of/mssql-jdbc-12.2.2.jre8/mssql-jdbc-12.2.2.jre8.jar
   java AlwaysEncrypted.java
   ```

5. Create the table:
    ```
    sqlcmd -S 127.0.0.1 -C -U sa -P 'YourStrongPassword123!' -i table_creation.sql
    ```

6. Compile and run the Java program that inserts the encrypted data:
   ```
   java App.java
   ```

## Notes

- Ensure all file paths in the Java code match your local setup.
- The `YourStrongPassword` and `YourStrongPassword123!` should be replaced with secure passwords in a production environment.
- The `AlwaysEncrypted.java` creates the Column Encryption Key (CEK).
- The `App.java` demonstrates inserting encrypted data into the table.
- Reference the correct alias for the column master key (CMK) in the `always_encrypted_setup.sql` file.
- The `CLASSPATH` should be set to the path of the mssql-jdbc-12.2.2.jre8/mssql-jdbc-12.2.2.jre8.jar file.