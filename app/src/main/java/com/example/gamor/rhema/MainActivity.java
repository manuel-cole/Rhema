package com.example.gamor.rhema;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gamor.rhema.model.ResObj;
import com.example.gamor.rhema.remote.ApiUtils;
import com.example.gamor.rhema.remote.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText editUsername;
    EditText editPassword;
    Button btnLogin;
    UserService userService;
    AlertDialog progressDialog;
    AlertDialog b;
    SharedPreferences sp;

    private CustomProgressDialogOne customProgressDialogOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("loginCheck",MODE_PRIVATE);

        if(sp.getBoolean("isLogged",false)){

            goToHomeActivity();
        }

        editUsername = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        userService = ApiUtils.getUserService();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialogOne = new CustomProgressDialogOne();

                View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_progress_dialog_one, null);
            progressDialog = new AlertDialog.Builder(MainActivity.this).create();
            progressDialog.setTitle("Check login credentials"); // Setting Title
            progressDialog.setCancelable(false);
            progressDialog.setView(dialogView);
            RelativeLayout mainLayout = (RelativeLayout) dialogView.findViewById(R.id.mainLayout);
            mainLayout.setBackgroundResource(R.drawable.my_progress_one);
            AnimationDrawable frameAnimation = (AnimationDrawable)mainLayout.getBackground();
            frameAnimation.start();
//            AlertDialog b = progressDialog.create();
            progressDialog.show();


//                customProgressDialogOne.showCustomDialog(MainActivity.this,"Checking Login Credentials...",true);

                String username = editUsername.getText().toString();
                String password  = editPassword.getText().toString();

                //Validate form
                if (validateLogin(username,password)){
                    //do login
                     doLogin(username,password);

                }
            }
        });
    }


    private boolean validateLogin(String username, String password) {
        if (username == null || username.trim().length() == 0){

            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password == null || password.trim().length() == 0){

            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void doLogin(final String username,final String password) {
        Call<ResObj> call = userService.login(username,password);

        call.enqueue(new Callback<ResObj>() {
            @Override
            public void onResponse(Call<ResObj> call, Response<ResObj> response) {
                if (response.isSuccessful()){
                    ResObj resObj = response.body();
                    if (resObj.getMessage().equals("Successful")){
                        //login start home activity
                        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                        intent.putExtra("username",username);
                        intent.putExtra("Email",resObj.getEmail());
                        intent.putExtra("FullName",resObj.getFullName());

                        //save user in shared preference
                        sp.edit().putString("FullName",resObj.getFullName()).apply();
                        sp.edit().putString("Email",resObj.getEmail()).apply();
                        sp.edit().putString("username",username).apply();
                        sp.edit().putBoolean("isLogged",true).apply();

                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }

                        //start home activity
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }

                        Toast.makeText(MainActivity.this, "The username or password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Toast.makeText(MainActivity.this, "Error! Please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResObj> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToHomeActivity(){
        Intent i = new Intent(MainActivity.this,HomeActivity.class);
        //fetch data from shared preference

        i.putExtra("username",sp.getString("username",""));
        i.putExtra("Email",sp.getString("Email",""));
        i.putExtra("FullName",sp.getString("FullName",""));
        startActivity(i);
        finish();
    }

}
