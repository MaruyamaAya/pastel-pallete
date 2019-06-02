package com.project.pastel_pallete.our_live2d;

import com.project.utils.android.FileManager;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.project.pastel_pallete.R;


public class sampleapplication extends Activity
{

    private live2dmanager live2DMgr ;
    static private Activity instance;

    public sampleapplication( )
    {
        instance=this;
        if(live2ddefine.DEBUG_LOG)
        {
            Log.d( "", "==============================================\n" ) ;
            Log.d( "", "   Live2D Sample  \n" ) ;
            Log.d( "", "==============================================\n" ) ;
        }

        live2DMgr = new live2dmanager() ;
    }


    static public void exit()
    {
        instance.finish();
    }



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setupGUI();



        FileManager.init(this.getApplicationContext());
    }



    void setupGUI()
    {
        setContentView(R.layout.activity_sampleapplication);


        live2dview view = live2DMgr.createView(this,3) ;


        FrameLayout layout=(FrameLayout) findViewById(R.id.live2DLayout);
        layout.addView(view, 0, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

    }


    @Override
    protected void onResume()
    {
        //live2DMgr.onResume() ;
        super.onResume();
    }



    @Override
    protected void onPause()
    {
        live2DMgr.onPause() ;
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.live2d_main, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId()){
            case R.id.menu_settings:
                Toast.makeText(getApplicationContext(), "change model", Toast.LENGTH_SHORT).show();
                live2DMgr.changeModel();//Live2D Event
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
