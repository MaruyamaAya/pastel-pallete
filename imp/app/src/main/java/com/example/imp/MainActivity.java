package com.example.imp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//}
public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {
    //声明控件对象
    private EditText et_name, et_pass;
    private Button mLoginButton, mLoginError, mRegister, ONLYTEST;
    int slectIndx = 1;
    int tempSelect = slectIndx;
    private boolean flase;
    boolean isReLogin = flase;
    private int SERVER_FLAG = 0;
    private RelativeLayout countryselsct;
    private TextView county_phone_sn, countryName;

    private final static int LOGIN_ENABLE = 0x01;
    private final static int LOGIN_UNABLF = 0x02;
    private final static int PASS_ERROR = 0x03;
    private final static int NAME_ERROR = 0x04;  //上面是消息的常量值

    final Handler UiMangerHandler = new Handler() {        //处理UI的操作的

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_ENABLE:
                    mLoginButton.setClickable(true);
                    break;
                case LOGIN_UNABLF:
                    mLoginButton.setClickable(false);
                    break;
                case PASS_ERROR:

                    break;
                case NAME_ERROR:
                    break;

            }
            super.handleMessage(msg);
        }
    };

    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private Button bt_pwd_eye;
    private TextWatcher username_watcher;
    private TextWatcher password_watcher;  //文本监视器


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_name = (EditText) findViewById(R.id.usename);
        et_pass = (EditText) findViewById(R.id.password);
        bt_pwd_clear = (Button) findViewById(R.id.bt_usename_clear);
        bt_pwd_clear = (Button) findViewById(R.id.bt_pwd_clear);
        bt_pwd_eye = (Button) findViewById(R.id.bt_pwd_eyes);

//        bt_username_clear.setOnClickListener(this);
//        bt_pwd_eye.setOnClickListener(this);
//        bt_pwd_clear.setOnClickListener(this);
        initWatcher();
        et_name.addTextChangedListener(username_watcher);
        et_pass.addTextChangedListener(password_watcher);

        mLoginButton = (Button) findViewById(R.id.login);
        mLoginError = (Button) findViewById(R.id.login_error);
        mRegister = (Button) findViewById(R.id.register);
        //ONLYTEST     = (Button) findViewById(R.id.registfer);
//        ONLYTEST.setOnClickListener(this);
//        ONLYTEST.setOnLongClickListener((View.OnLongClickListener) this);
//        mLoginButton.setOnClickListener(this);
//        mLoginError.setOnClickListener(this);
//        mRegister.setOnClickListener(this);
        et_name.addTextChangedListener(username_watcher);
        et_pass.addTextChangedListener(password_watcher);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initWatcher() {
        username_watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_pass.setText("");
                if (editable.toString().length() > 0) {
                    bt_username_clear.setVisibility(View.VISIBLE);
                } else {
                    bt_username_clear.setVisibility(View.INVISIBLE);
                }

            }
        };
        password_watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() > 0) {
                    bt_pwd_clear.setVisibility(View.VISIBLE);
                } else {
                    bt_pwd_clear.setVisibility(View.INVISIBLE);
                }

            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //登陆activity
            case R.id.login:
                //login();
                break;
            //忘记密码
            case R.id.login_error:
                break;
            //注册
            case R.id.register:
                break;

        }

    }

    private void login() {

    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                if (SERVER_FLAG > 9) {

                    break;
                }
        }
        return true;
    }

    /**
     * 监听back的那块
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isReLogin) {

            }
        }
        return super.onKeyDown(keyCode, event);
    }

    TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {//表示最终内容

        }


        @Override
        public void beforeTextChanged(CharSequence s, int start/*开始的位置*/, int count/*被改变的旧内容数*/, int after/*改变后的内容数量*/) {
            //这里的s表示改变之前的内容，通常start和count组合，可以在s中读取本次改变字段中被改变的内容。而after表示改变后新的内容的数量。
        }


        @Override
        public void onTextChanged(CharSequence s, int start/*开始位置*/, int before/*改变前的内容数量*/, int count/*新增数*/) {
            //这里的s表示改变之后的内容，通常start和count组合，可以在s中读取本次改变字段中新的内容。而before表示被改变的内容的数量。
        }
    };
};

