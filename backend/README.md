### Setting Up Keystore (SSL)

To generate a keystore for SSL/TLS, you'll use the keytool utility that comes with Java.

```bash
keytool -genkeypair -alias ${yourappkeyalias} -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore ${keystore}.p12 -validity 3650 -storepass ${your_keystore_password} -keypass ${your_key_password}
```

When you run this command, you'll be prompted to enter additional information:


> 1. What is your first and last name?
    [Unknown]:  David Enikuomehin
>
> 
> 2. What is the name of your organizational unit?
[Unknown]:  Software Director
> 
> 
> 3. What is the name of your organization?
[Unknown]:  NACOS FUNAAB
>
> 4. What is the name of your City or Locality?
[Unknown]:  Abeokuta
>
> 5. What is the name of your State or Province?
[Unknown]:  Ogun
>
>6. What is the two-letter country code for this unit?
[Unknown]:  NG
>
>Is CN=David Enikuomehin, OU=Software Director, O=NACOS FUNAAB, L=Abeokuta, ST=Ogun, C=NG correct?
[no]:  yes


After confirming, the keystore will be created. You can then use this keystore in your Spring Boot application by
updating your application.properties:

```properties
server.ssl.key-store-type=PKCS12
server.ssl.key-store=classpath:nacosfunaabpay.p12
server.ssl.key-store-password=securepassword123
server.ssl.key-alias=nacosfunaabpay
```

Remember to place the `nacosfunaabpay.p12` file in your `src/main/resources` directory so that Spring Boot can find it.

> In prod, obtain a certificate from a trusted Certificate Authority (CA) rather than using a self-signed certificate.