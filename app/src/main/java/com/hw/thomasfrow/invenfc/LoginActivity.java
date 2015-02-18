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
import android.util.*;

public class LoginActivity extends Activity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


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

        toolbar.setNavigationIcon(R.drawable.ic_action_name);

    }

    public void onClick(View view){

        switch(view.getId()){
            case R.id.loginButton:
                performLogin();
                break;
        }

    }

    private void performLogin(){

        EditText username = (EditText)findViewById(R.id.editTextUsername);
        EditText password = (EditText)findViewById(R.id.editTextPassword);

        if(username.getText().toString().trim().length() != 0) {

            boolean loginSuccess = true;
            if (loginSuccess) {

                int userID = Integer.parseInt(username.getText().toString());

                Log.i("inputID",username.getText().toString());
                Log.i("inputID",Integer.toString(userID));
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("userDetails", getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putInt("ownerID", userID);
                editor.commit();

                Intent intent = new Intent(this, showInventoryActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);

            }

        }else{
            Toast.makeText(this,"Nothing entered into name",Toast.LENGTH_SHORT);
        }



    }

}