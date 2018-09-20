package com.example.emilia.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class LoginActivity extends AbstractAsyncActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initiate the request to the protected service
        final TextView submitButton = findViewById(R.id.btnLogin);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new FetchSecuredResourceTask().execute();
            }
        });

        final TextView registerOption = findViewById(R.id.txtRegister);
        registerOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                //Elements
                startActivity(intent);
            }
        });
    }
    private void displayResponse(Message response) {
        Toast.makeText(this, response.getText(), Toast.LENGTH_LONG).show();
    }

    // ***************************************
    // Private classes
    // ***************************************
    private class FetchSecuredResourceTask extends AsyncTask<Void, Void, Message> {

        private String username;

        private String password;

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();

            // build the message object
            EditText editText = (EditText) findViewById(R.id.username);
            this.username = editText.getText().toString();

            editText = (EditText) findViewById(R.id.password);
            this.password = editText.getText().toString();
        }

        @Override
        protected Message doInBackground(Void... params) {
            //This going to be URL
            final String url = getString(R.string.base_uri) + "/getmessage";

            // Populate the HTTP Basic Authentitcation header with the username and password
            HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            try {
                // Make the network request
                Log.d(TAG, url);
                ResponseEntity<Message> response = restTemplate.exchange(url,
                        HttpMethod.GET, new HttpEntity<Object>(requestHeaders), Message.class);
                return response.getBody();
            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                //Using Message class that i have created
                return new Message(0, e.getStatusText(), e.getLocalizedMessage());
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new Message(0, e.getClass().getSimpleName(), e.getLocalizedMessage());
            }
        }

        @Override
        protected void onPostExecute(Message result) {
            dismissProgressDialog();
            displayResponse(result);
        }

    }




}

