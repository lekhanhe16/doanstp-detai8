package com.kl.doanstp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kl.doanstp.R;
import com.kl.doanstp.SessionManager;
import com.kl.doanstp.service.MyService;
import com.kl.doanstp.service.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText username, password, confPass, name, birthYear, phone;
    private Spinner position;
    private Button regButton;
    private String user, pass;
    private int id;
    private static String baseUrl = "http://192.168.0.107:5000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.edit_reg_user);
        password = findViewById(R.id.edit_reg_pwd);
        confPass = findViewById(R.id.edit_reg_confirm);
        name = findViewById(R.id.edit_reg_name);
        birthYear = findViewById(R.id.edit_reg_by);
        phone = findViewById(R.id.edit_reg_sdt);
        position = findViewById(R.id.spn_reg_pos);
        regButton = findViewById(R.id.btn_reg);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();
                String confp = confPass.getText().toString();
                String pname = name.getText().toString();

                String tphone = phone.getText().toString();
                String pos = position.getSelectedItem().toString();
                try {
                    int birth = Integer.parseInt(birthYear.getText().toString());
                    if(!user.isEmpty()
                            && !pass.isEmpty()
                            && !confp.isEmpty()
                            && !pname.isEmpty()
                            && !tphone.isEmpty()){
                        if(pass.equals(confp)){
                            JSONObject data = new JSONObject();
                            data.put("username", user);
                            data.put("password", pass);
                            data.put("name", pname);
                            data.put("phone", tphone);
                            data.put("birthyear", birth);
                            data.put("position", pos);

                            MyService myRetrofit = RetrofitClient.getInstance(baseUrl).create(MyService.class);
                            Call<JsonObject> homeCallback = myRetrofit.register(data);
                            homeCallback.enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    id = response.body().get("result").getAsInt();
                                    if(id != -1){
                                        SessionManager.getInstance().createSession(id, user, pass);
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity2.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {

                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {

                                }
                            });

                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Mat khau xac nhan khong dung"
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Ban phai dien day du thong tin"
                                , Toast.LENGTH_LONG).show();
                    }
                }
                catch (NumberFormatException | JSONException e){

                }

            }
        });
    }
}