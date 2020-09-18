package com.kl.doanstp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.stats.ConnectionTracker;
import com.google.gson.JsonObject;
import com.kl.doanstp.R;
import com.kl.doanstp.SessionManager;
import com.kl.doanstp.TemplateData;
import com.kl.doanstp.model.Account;
import com.kl.doanstp.service.ConnectionUtil;
import com.kl.doanstp.service.MyService;
import com.kl.doanstp.service.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText editUser, editPassWord;
    Button btnLogin, btnRegister;

    private static String baseURL = ConnectionUtil.baseURL;
//    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        SessionManager.getInstance().setContext(getBaseContext());
        initViewComponents();


    }

    public void initViewComponents(){
        editUser = findViewById(R.id.editText);
        editPassWord = findViewById(R.id.editText2);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editUser.getText().toString();
                String password = editPassWord.getText().toString();

                try {
                    JSONObject data = new JSONObject();
                    data.put("username", userName);
                    data.put("password", password);
                    Toast.makeText(LoginActivity.this, userName+" "+password, Toast.LENGTH_SHORT).show();
                    MyService myRetrofit = RetrofitClient.getInstance(baseURL).create(MyService.class);
                    Call<JsonObject> homeCallback = myRetrofit.login(data);
                    homeCallback.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            int id = response.body().get("result").getAsInt();
                            if(id != -1){
                                SessionManager.getInstance().createSession(id, userName, password);
                                Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "Invalid username or password",Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    
}