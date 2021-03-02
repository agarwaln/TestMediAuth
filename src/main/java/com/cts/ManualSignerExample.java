package com.cts;

import com.mdsol.mauth.SignerConfiguration;
import com.mdsol.mauth.apache.HttpClientRequestSigner;
import com.typesafe.config.ConfigFactory;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

public class ManualSignerExample {

    private void executeMe() throws IOException {

        BufferedReader fileReader = new BufferedReader(new FileReader("/Users/m_249913/Downloads/rsa-key"));
        String line = null;
        String privateKeyAsString = "";

        while ( (line = fileReader.readLine()) != null) {
            privateKeyAsString += line;
            privateKeyAsString += "\n";
        }


//        SignerConfiguration configuration = new SignerConfiguration(ConfigFactory.load());
        SignerConfiguration configuration = new SignerConfiguration(UUID.randomUUID(), privateKeyAsString);
        final HttpClientRequestSigner httpClientRequestSigner = new HttpClientRequestSigner(configuration);
        HttpGet request = new HttpGet("https://api.mdsol.com/v1/countries");
        httpClientRequestSigner.signRequest(request);

        CloseableHttpClient httpClient = HttpClients.custom().build();

        try(CloseableHttpResponse response = httpClient.execute(request)) {
            StatusLine status = response.getStatusLine();
            System.out.println("response code: " + status.getStatusCode()  + " (" + status.getReasonPhrase() + ")");
            System.out.println("response: " + response.getEntity().getContent().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Example how to sign requests manually
     * Set up the following environment variables:
     * APP_MAUTH_UUID - app uuid
     * APP_MAUTH_PRIVATE_KEY - the application private key itself, not the path
     *
     * @param args - no args expected
     */
    public static void main(String[] args) throws IOException {
        new ManualSignerExample().executeMe();
    }
}