package pku.lesson_evaluator;

import android.content.Intent;
import android.os.StrictMode;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.lang.Thread;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class Login extends AppCompatActivity {
    public String user__name;
    public String pass__word;
    public class Check_login extends Thread {
        public void run()
        {
            String connectURL = "http://59.110.216.106/login.php/";
            String result = null;
            HttpPost httpRequest = new HttpPost(connectURL);
            httpRequest.addHeader(HTTP.CONTENT_TYPE,"application/json");
            JSONObject jo = new JSONObject();
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                jo.put("usr_name", user__name);
                jo.put("password", pass__word);
                JSONArray json = new JSONArray();
                json.put(jo);
                String jsonstr = json.toString();
                StringEntity se = new StringEntity(jsonstr);
                se.setContentType("text/json");
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httpRequest.setEntity(se);

                HttpResponse response = httpClient.execute(httpRequest);
                if(response != null && response.getStatusLine().getStatusCode() == 200) {
                    String result_1 = EntityUtils.toString(response.getEntity());
                    if (result_1 == "accept")
                    {
                        Intent intent=new Intent(Login.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(result_1 == "deny")
                    {
                        hints();
                    }
                    else if(result_1 == "error")
                    {
                        hints();
                    }
                }
            }catch(JSONException e){
            }catch(UnsupportedEncodingException e1){
            }catch(IOException e2){}
        }
    }

    private void hints(){
        new AlertDialog.Builder(this)
                .setTitle("提醒")
                .setMessage("用户名或密码错误")
                .setPositiveButton("确定", null)
                .show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        android.support.v7.widget.Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("登录");
        setSupportActionBar(toolbar);
        Button signin=(Button)findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,SignIn.class);
                startActivity(intent);
                finish();
            }
        });
        Button login=(Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText user_name=(EditText)findViewById(R.id.account);
                EditText pass_word=(EditText)findViewById(R.id.password);
                user__name = user_name.getText().toString();
                pass__word = pass_word.getText().toString();
                Thread t = new Check_login();
                t.start();
            }
        });
    }
}
