package pku.lesson_evaluator;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Talking extends AppCompatActivity implements View.OnClickListener{

    private List<Talking_item> talkingItemList=new ArrayList<>();
    private TextView ClassName;
    private TextView ClassTeacher;
    private EditText addWord;
    private Button sendWord;
    private String uid;
    private String className;
    private String classTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talking);
        android.support.v7.widget.Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("课程讨论板");
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getUid();
        ClassName=findViewById(R.id.class_name);
        ClassTeacher=findViewById(R.id.class_teacher);
        ClassName.setText(className);
        ClassTeacher.setText(classTeacher);
        initTalking();
        RecyclerView recyclerView=findViewById(R.id.talking_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        TalkingAdapter adapter=new TalkingAdapter(talkingItemList);
        recyclerView.setAdapter(adapter);
        addWord=findViewById(R.id.add_word);
        sendWord=findViewById(R.id.send_word);
        sendWord.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.send_word:
                //发送信息
        }
    }

    private void initTalking(){
        //根据uid在服务器上请求信息
        String[] a=new String[]{"hym","lzm"};
        String[] b=new String[]{"2018/01/19 12:20","2018/06/04 13:00"};
        String[] c=new String[]{"我真是爱死java程序设计了","黄骏老师真好"};
        for(int i=0;i<a.length;++i){
            talkingItemList.add(new Talking_item(a[i],b[i],c[i]));
        }
    }

    private void getUid(){
        Intent intent=getIntent();
        //获得uid
        uid=intent.getStringExtra("UID");
        String [] temp=uid.split("/");
        className=temp[0];
        classTeacher=temp[1];
    }
}
