package com.hw.thomasfrow.invenfc;


import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;
import android.content.SharedPreferences;

import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.ParseException;


public class SignUpActivity extends Activity{

    private boolean redirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("redirect")) {

                redirect = getIntent().getExtras().getBoolean("redirect");

            }
        }


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        toolbar.inflateMenu(R.menu.menu_main);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_about:

                        //addItemInterface();

                        return true;
                }

                return false;
            }


        });

        toolbar.setTitle("Sign up");

    }

    public void onClick(View view){

        switch(view.getId()){
            case R.id.signUpButton:
                performSignUp();
                break;
        }

    }

    private void performSignUp(){

        EditText username = (EditText)findViewById(R.id.editTextUsername);
        EditText password = (EditText)findViewById(R.id.editTextPassword);
        EditText email = (EditText)findViewById(R.id.editTextEmail);
        String usernameTxt = username.getText().toString();
        String passwordTxt = password.getText().toString();
        String emailTxt =  email.getText().toString();

        boolean signUpNameError = false;

        if(username.getText().toString().trim().length() != 0) {
            if(passwordTxt.toString().trim().length() != 0){
                if(emailTxt.toString().trim().length() != 0){
                    ParseUser user = new ParseUser();

                    user.setUsername(usernameTxt);
                    user.setPassword(passwordTxt);
                    user.setEmail(emailTxt);

                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                Intent intent = new Intent(getApplicationContext(), showInventoryActivity.class);
                                intent.putExtra("firstLogin",true);
                                startActivity(intent);
                            } else {
                                Log.i("parse", e.toString());
                            }
                        }
                    });

                }else{
                    signUpNameError = true;
                }
            }else{
                signUpNameError = true;
            }

        }else{
            signUpNameError = true;
        }

        if(signUpNameError == true){

            Toast.makeText(this, "You have not filled in all appropriate fields.",Toast.LENGTH_LONG).show();
        }


    }


}