package com.Gem.gmll;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Configure extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);


        final EditText email = (EditText) findViewById(R.id.r_email);
        final EditText email_pass = (EditText) findViewById(R.id.r_emailpass);
        final EditText fname = (EditText) findViewById(R.id.fname);
        final EditText lname = (EditText) findViewById(R.id.lname);
        final EditText string_salt = (EditText) findViewById(R.id.string_salt);
        final EditText num_salt = (EditText) findViewById(R.id.num_salt);
        final Button to2 = (Button) findViewById(R.id.to2);
        final Button to3 = (Button) findViewById(R.id.to3);
        final Button to4 = (Button) findViewById(R.id.to4);
        final Button saveinfo = (Button) findViewById(R.id.saveinfo);
        final LinearLayout terms = (LinearLayout) findViewById(R.id.terms);

        preferences = getSharedPreferences("PREFS",0);


        email.setVisibility(View.VISIBLE);
        email_pass.setVisibility(View.VISIBLE);
        to2.setVisibility(View.VISIBLE);

        to2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    email.setVisibility(View.GONE);
                    email_pass.setVisibility(View.GONE);
                    to2.setVisibility(View.GONE);

                    fname.setVisibility(View.VISIBLE);
                    lname.setVisibility(View.VISIBLE);
                    to3.setVisibility(View.VISIBLE);


                to3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        fname.setVisibility(View.GONE);
                        lname.setVisibility(View.GONE);
                        to3.setVisibility(View.GONE);

                        string_salt.setVisibility(View.VISIBLE);
                        num_salt.setVisibility(View.VISIBLE);
                        to4.setVisibility(View.VISIBLE);

                        to4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                string_salt.setVisibility(View.GONE);
                                num_salt.setVisibility(View.GONE);
                                to4.setVisibility(View.GONE);

                                terms.setVisibility(View.VISIBLE);
                                saveinfo.setVisibility(View.VISIBLE);
                            }
                        });

                    }
                });


            }
        });


        saveinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("email", email.getText().toString());
                    editor.putString("email_pass", email_pass.getText().toString());
                    editor.putString("fname", fname.getText().toString());
                    editor.putString("lname", lname.getText().toString());
                    editor.putString("pass1", string_salt.getText().toString());
                    editor.putString("pass2", num_salt.getText().toString());
                    editor.putBoolean("intial_use",false);
                    editor.apply();

                    Toast.makeText(Configure.this, preferences.getString("Configured Successfully", ""), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Profile.class);
                    startActivity(intent);
                    finish();


            }
        });

    }

}
