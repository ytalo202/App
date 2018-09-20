package com.example.emilia.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private TextView btnRegister;

    private ProgressBar progressBar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressBar1 = findViewById(R.id.progressBar1);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // registerUser(getEmail(), getPassword(), getName());
                new Task().execute();
            }
        });
    }
    public class Task extends AsyncTask<Void,Void,Void> {

        Message mesaje = new Message();

        private String getEmail(){ EditText editText = findViewById(R.id.edtEmail);return editText.getText().toString(); }
        private String getPassword(){ EditText editText = findViewById(R.id.edtPassword);return editText.getText().toString(); }
        private String getName(){ EditText editText = findViewById(R.id.edtName);return editText.getText().toString(); }
        @Override
        protected void onPreExecute() {
            progressBar1.setVisibility(View.VISIBLE);
            btnRegister.setEnabled(false); }
        @Override
        protected Void doInBackground(Void... voids) {
            final String url = getString(R.string.base_uri);
            //Url del servicio,sin el endpoint
            //Creamos el objeto Retrofit
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)//Indicamos la url del servicio
                    .addConverterFactory(GsonConverterFactory.create())//Agregue la fábrica del convertidor para la serialización y la deserialización de objetos.
                    .build();//Cree la instancia de Retrofit utilizando los valores configurados.
            Api service = retrofit.create(Api.class);
            Call<User> call = service.postCrear(new User(getEmail(),getName(),getPassword()));
            call.enqueue(new Callback<User>() {


                @Override
                public void onResponse(Call<User> call, Response<User> response) {



                    String text =response.body().getText();
                    Toast.makeText(RegisterActivity.this, text, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    //Elements
                    startActivity(intent);
                    finish();

                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }});



            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar1.setVisibility(View.INVISIBLE);
            btnRegister.setEnabled(true);

            // Toast.makeText(RegisterActivity.this, mesaje.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}
