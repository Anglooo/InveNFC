package com.hw.thomasfrow.invenfc;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;
import android.content.SharedPreferences;

import com.parse.ParseUser;
import com.parse.LogInCallback;
import com.parse.ParseException;


public class LoginActivity extends Activity{

    private boolean redirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        toolbar.setTitle("Login to Invenfc");

    }

    public void onClick(View view){

        switch(view.getId()){
            case R.id.loginButton:
                performLogin();
                break;

            case R.id.signUpButton:
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void performLogin(){

        EditText username = (EditText)findViewById(R.id.editTextUsername);
        EditText password = (EditText)findViewById(R.id.editTextPassword);
        String usernameTxt = username.getText().toString();
        String passwordTxt = password.getText().toString();

        if(username.getText().toString().trim().length() != 0) {

            ParseUser.logInInBackground(usernameTxt, passwordTxt,
                    new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                loginSuccess(user.getObjectId());
                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "No such user exist, please signup",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }else{
            Toast.makeText(this, "Nothing entered into name", Toast.LENGTH_SHORT);
        }



    }

    public void loginSuccess(String userID){

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("userDetails", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("userID", userID);
        editor.commit();

        if(redirect){
            int redirID = getIntent().getExtras().getInt("redirID");

            Intent intent = new Intent(this, viewItemActivity.class);
            intent.putExtra("id", redirID);
            startActivity(intent);

        }else{
            Intent intent = new Intent(this, showInventoryActivity.class);
            intent.putExtra("userID", userID);
            startActivity(intent);

        }
    }

    @Override
    public void onBackPressed(){

    }

}