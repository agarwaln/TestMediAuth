package com.cts;

import com.mdsol.mauth.exceptions.MAuthKeyException;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;

import java.io.*;
import java.security.PrivateKey;
import java.security.SecureRandom;

import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;



public class TestPK {

    private static final JcaPEMKeyConverter KEY_CONVERTER =
            new JcaPEMKeyConverter().setProvider("BC");

    public static PrivateKey getPrivateKeyFromString(final String privateKeyAsString) {
        PrivateKey pk;
        try (PEMParser parser = new PEMParser(new StringReader(privateKeyAsString))) {
            PEMKeyPair keyPair = (PEMKeyPair) parser.readObject();
            if (keyPair != null) {
                pk = KEY_CONVERTER.getPrivateKey(keyPair.getPrivateKeyInfo());
            } else {
                throw new MAuthKeyException("Unable to process private key string");
            }
        } catch (IOException ex) {
            throw new MAuthKeyException("Unable to process private key string", ex);
        }
        return pk;
    }
    public static void main(String[] args) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader("/Users/m_249913/Downloads/rsa-key"));
        String line = null;
        String privateKeyAsString = "";

        while ( (line = fileReader.readLine()) != null) {
            privateKeyAsString += line;
            privateKeyAsString += "\n";
        }
        System.out.println(privateKeyAsString);
        PrivateKey privateKey = getPrivateKeyFromString(privateKeyAsString);
        System.out.println(privateKey);
    }
}
