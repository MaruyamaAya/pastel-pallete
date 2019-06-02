package com.project.pastel_pallete.our_live2d;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import android.app.Activity;
import android.util.Log;
import com.project.framework.L2DViewMatrix;
import jp.live2d.Live2D;


public class live2dmanager
{

    static public final String 	TAG = "SampleLive2DManager";

    private live2dview				view;


    private ArrayList<live2dmodel>	models;



    private int 					modelCount		=4;
    private boolean 				reloadFlg;



    public live2dmanager()
    {
        Live2D.init();
        models = new ArrayList<live2dmodel>();
    }


    public void releaseModel()
    {
        for(int i=0;i<models.size();i++)
        {
            models.get(i).release();
        }
        models.clear();
    }



    public void update(GL10 gl)
    {
        view.update();
        if(reloadFlg)
        {

            reloadFlg=false;

            int no = modelCount % 4;

            try {
                switch (no) {
                    case 0:
                        releaseModel();

                        models.add(new live2dmodel());
                        models.get(0).load(gl, live2ddefine.MODEL_MIKU);
                        models.get(0).feedIn();
                        break;
                    case 1:
                        releaseModel();

                        models.add(new live2dmodel());
                        models.get(0).load(gl, live2ddefine.MODEL_SHIZUKU);
                        models.get(0).feedIn();
                        break;
                    case 2:
                        releaseModel();

                        models.add(new live2dmodel());
                        models.get(0).load(gl, live2ddefine.MODEL_WANKO);
                        models.get(0).feedIn();
                        break;
                    case 3:
                        releaseModel();

                        models.add(new live2dmodel());
                        models.get(0).load(gl, live2ddefine.MODEL_HARU_A);
                        models.get(0).feedIn();

                        models.add(new live2dmodel());
                        models.get(1).load(gl, live2ddefine.MODEL_HARU_B);
                        models.get(1).feedIn();
                        break;
                    default:

                        break;
                }
            } catch (Exception e) {

                Log.e(TAG,"モデルの読み込みに失敗しました。アプリケーションを終了します。");
                sampleapplication.exit();
            }
        }
    }



    public live2dmodel getModel(int no)
    {
        if(no>=models.size())return null;
        return models.get(no);
    }


    public int getModelNum()
    {
        return models.size();
    }


    //=========================================================

    //=========================================================

    public live2dview createView(Activity act,int model_num)
    {
        modelCount = model_num;
        view = new live2dview( act ) ;
        view.setLive2DManager(this);
        view.startAccel(act);
        return view ;
    }



    public void onResume()
    {
        if(live2ddefine.DEBUG_LOG)Log.d(TAG, "onResume");
        view.onResume();
    }



    public void onPause()
    {
        if(live2ddefine.DEBUG_LOG)Log.d(TAG, "onPause");
        view.onPause();
    }



    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        if(live2ddefine.DEBUG_LOG)Log.d(TAG, "onSurfaceChanged "+width+" "+height);
        view.setupView(width,height);

        if(getModelNum()==0)
        {

            changeModel();

        }
    }


    //=========================================================

    //=========================================================

    public void changeModel()
    {
        reloadFlg=true;
        modelCount++;
    }


    //=========================================================

    //=========================================================

    public boolean tapEvent(float x,float y)
        {
        if(live2ddefine.DEBUG_LOG)Log.d(TAG, "tapEvent view x:"+x+" y:"+y);

        for (int i=0; i<models.size(); i++)
        {
            if(models.get(i).hitTest(  live2ddefine.HIT_AREA_HEAD,x, y ))
            {

                if(live2ddefine.DEBUG_LOG)Log.d(TAG, "顔をタップした");
                models.get(i).setRandomExpression();
            }
            else if(models.get(i).hitTest( live2ddefine.HIT_AREA_BODY,x, y))
            {
                if(live2ddefine.DEBUG_LOG)Log.d(TAG, "体をタップした");
                models.get(i).startRandomMotion(live2ddefine.MOTION_GROUP_TAP_BODY, live2ddefine.PRIORITY_NORMAL );
            }
        }
        return true;
    }

    public void listenEvent(int no)
    {
        if(live2ddefine.DEBUG_LOG)Log.d(TAG, "收到对方通讯");

        for (int i=0; i<models.size(); i++)
        {
            models.get(i).startMotion(live2ddefine.MOTION_GROUP_NONE,no,live2ddefine.PRIORITY_FORCE);
        }
    }

    public void flickEvent(float x,float y)
    {
        if(live2ddefine.DEBUG_LOG)Log.d(TAG, "flick x:"+x+" y:"+y);

        for (int i=0; i<models.size(); i++)
        {
            if(models.get(i).hitTest( live2ddefine.HIT_AREA_HEAD, x, y ))
            {
                if(live2ddefine.DEBUG_LOG)Log.d(TAG, "顔をフリックした");
                models.get(i).startRandomMotion(live2ddefine.MOTION_GROUP_FLICK_HEAD, live2ddefine.PRIORITY_NORMAL );
            }
        }
    }



    public void maxScaleEvent()
    {
        if(live2ddefine.DEBUG_LOG)Log.d(TAG, "画面の最大化イベント");

        for (int i=0; i<models.size(); i++)
        {
            models.get(i).startRandomMotion(live2ddefine.MOTION_GROUP_PINCH_IN,live2ddefine.PRIORITY_NORMAL );
        }
    }



    public void minScaleEvent()
    {
        if(live2ddefine.DEBUG_LOG)Log.d(TAG, "画面の最小化イベント");

        for (int i=0; i<models.size(); i++)
        {
            models.get(i).startRandomMotion(live2ddefine.MOTION_GROUP_PINCH_OUT,live2ddefine.PRIORITY_NORMAL );
        }
    }



    public void shakeEvent()
    {
        if(live2ddefine.DEBUG_LOG)Log.d(TAG, "シェイクイベント");

        for (int i=0; i<models.size(); i++)
        {
            models.get(i).startRandomMotion(live2ddefine.MOTION_GROUP_SHAKE,live2ddefine.PRIORITY_FORCE );
        }
    }


    public void setAccel(float x,float y,float z)
    {
        for (int i=0; i<models.size(); i++)
        {
            models.get(i).setAccel(x, y, z);
        }
    }


    public void setDrag(float x,float y)
    {
        for (int i=0; i<models.size(); i++)
        {
            models.get(i).setDrag(x, y);
        }
    }


    public L2DViewMatrix getViewMatrix()
    {
        return view.getViewMatrix();
    }
}

