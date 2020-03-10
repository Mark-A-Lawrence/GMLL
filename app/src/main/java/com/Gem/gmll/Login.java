package com.Gem.gmll;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("PREFS",0);
        final String pass_str = preferences.getString("pass1","");
        final String pass_pin = preferences.getString("pass2","");
        Intent in =  getIntent();
        final String access_c = in.getStringExtra("access_c");

        Button unlock = (Button)findViewById(R.id.unlock);
        final EditText password = (EditText)findViewById(R.id.pwd);
        final EditText pin = (EditText)findViewById(R.id.pin);
        final EditText acesscode =(EditText)findViewById(R.id.access_code);

        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().equals(pass_str)&&pin.getText().toString().equals(pass_pin)&&access_c.equals(acesscode.getText().toString())){
                    startActivity(new Intent(getApplicationContext(),Profile.class));
                }
                else{
                    Toast.makeText(Login.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
      // Toast.makeText(this, "Loading Data", Toast.LENGTH_LONG).show();
        System.out.println("Successful");
    }
}
