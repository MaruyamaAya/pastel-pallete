package com.project.pastel_pallete.our_live2d;


import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import com.project.framework.L2DBaseModel;
import com.project.framework.L2DExpressionMotion;
import com.project.framework.L2DEyeBlink;
import com.project.framework.L2DModelMatrix;
import com.project.framework.L2DPhysics;
import com.project.framework.L2DPose;
import com.project.utils.android.BufferUtil;
import com.project.utils.android.FileManager;
import com.project.utils.android.LoadUtil;
import com.project.utils.android.ModelSetting;
import com.project.utils.android.ModelSettingJson;
import com.project.utils.android.OffscreenImage;
import com.project.utils.android.SoundUtil;
import jp.live2d.Live2D;
import jp.live2d.android.Live2DModelAndroid;
import jp.live2d.error.Live2DException;
import jp.live2d.motion.AMotion;
import jp.live2d.util.UtSystem;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;



public class live2dmodel extends L2DBaseModel
{

    public String 					TAG = "LAppModel ";


    private ModelSetting 			modelSetting = null;
    private String 					modelHomeDir;


    private MediaPlayer 			voice;


    static FloatBuffer 				debugBufferVer = null ;
    static FloatBuffer 				debugBufferColor = null ;


    /**
     * コンストラクタ
     * param live2DMgr
     */
    public live2dmodel()
    {
        super();

        if(live2ddefine.DEBUG_LOG)mainMotionManager.setMotionDebugMode(true);
    }


    public void release()
    {
        if(live2DModel==null)return;
        live2DModel.deleteTextures();
    }


    /**
     * モデルを初期化する
     * param gl
     * throws Exception
     */
    public void load(GL10 gl,String modelSettingPath) throws Exception
    {
        updating=true;
        initialized=false;

        modelHomeDir=modelSettingPath.substring(0,modelSettingPath.lastIndexOf("/") + 1);//live2d/model/xxx/


        if(live2ddefine.DEBUG_LOG) Log.d(TAG, "json读入: "+modelSettingPath);

        try
        {
            InputStream in = FileManager.open(modelSettingPath);
            modelSetting = new ModelSettingJson(in);
            in.close() ;
        }
        catch (IOException e)
        {
            e.printStackTrace();


            throw new Exception();
        }

        if(modelSetting.getModelName()!=null)
        {
            TAG+=modelSetting.getModelName();
        }

        if(live2ddefine.DEBUG_LOG)Log.d(TAG, "模型读入");


        loadModelData( gl,modelHomeDir,modelSetting.getModelFile(),modelSetting.getTextureFiles() );


        loadExpressions(modelHomeDir, modelSetting.getExpressionNames(),modelSetting.getExpressionFiles() );


        loadPhysics( modelHomeDir,modelSetting.getPhysicsFile() );


        loadPose(modelHomeDir,modelSetting.getPoseFile());


        HashMap<String, Float> layout = new HashMap<String,Float>();
        if (modelSetting.getLayout(layout) )
        {
            if (layout.get("width")!=null)modelMatrix.setWidth(layout.get("width"));
            if (layout.get("height")!=null)modelMatrix.setHeight(layout.get("height"));
            if (layout.get("x")!=null)modelMatrix.setX(layout.get("x"));
            if (layout.get("y")!=null)modelMatrix.setY(layout.get("y"));
            if (layout.get("center_x")!=null)modelMatrix.centerX(layout.get("center_x"));
            if (layout.get("center_y")!=null)modelMatrix.centerY(layout.get("center_y"));
            if (layout.get("top")!=null)modelMatrix.top(layout.get("top"));
            if (layout.get("bottom")!=null)modelMatrix.bottom(layout.get("bottom"));
            if (layout.get("left")!=null)modelMatrix.left(layout.get("left"));
            if (layout.get("right")!=null)modelMatrix.right(layout.get("right"));
        }


        for(int i=0; i<modelSetting.getInitParamNum() ;i++)
        {
            String id = modelSetting.getInitParamID(i);
            float value = modelSetting.getInitParamValue(i);
            live2DModel.setParamFloat(id, value);
        }

        for(int i=0; i<modelSetting.getInitPartsVisibleNum() ;i++)
        {
            String id = modelSetting.getInitPartsVisibleID(i);
            float value = modelSetting.getInitPartsVisibleValue(i);
            live2DModel.setPartsOpacity(id, value);
        }


        eyeBlink=new L2DEyeBlink();

        updating=false;
        initialized=true;
    }


    public void loadExpressions(String dir, String[] names,String[] fileNames )
    {
        if(fileNames==null || fileNames.length==0)return;
        try
        {
            for (int i = 0; i < fileNames.length; i++)
            {
                if(live2ddefine.DEBUG_LOG)Log.d(TAG, "表情读取 : "+fileNames[i]);
                InputStream in = FileManager.open(dir+fileNames[i]) ;

                expressions.put(names[i], L2DExpressionMotion.loadJson(in) );

                in.close() ;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadPose( String dir,String fileName )
    {
        if(fileName==null)return;
        if(live2ddefine.DEBUG_LOG) Log.d(TAG, "json读取 : "+fileName);
        try
        {
            InputStream in = FileManager.open(dir+fileName);
            pose = L2DPose.load(in);
            in.close() ;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadPhysics( String dir,String fileName )
    {
        if(fileName==null)return;
        if(live2ddefine.DEBUG_LOG) Log.d(TAG, "json读取 : "+fileName);
        try
        {
            InputStream in = FileManager.open( dir + fileName );
            physics = L2DPhysics.load(in);
            in.close() ;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadModelData( GL10 gl,String dir, String modelFile,String[] texFiles) throws Exception
    {
        if(modelFile==null || texFiles==null )return;
        if(live2DModel!=null)
        {
            live2DModel.deleteTextures();
        }
        try
        {
            if(live2ddefine.DEBUG_LOG)Log.d(TAG, "モデル読み込み : "+dir+modelFile);


            {
                InputStream in = FileManager.open(dir+modelFile) ;
                Log.d(TAG, "error is here : "+in.read()+in.read()+in.read());
                in.close();
                in = FileManager.open(dir+modelFile) ;
                live2DModel = Live2DModelAndroid.loadModel(in);
                if(live2ddefine.DEBUG_LOG)Log.d(TAG, "error is here : "+modelFile);
                in.close() ;
                if(Live2D.getError()!=Live2D.L2D_NO_ERROR)
                {

                    throw new Exception();
                }
            }


            for (int i = 0; i < texFiles.length; i++)
            {
                if(live2ddefine.DEBUG_LOG) Log.d("", "テクスチャ読み込み : "+texFiles[i]);
                InputStream in = FileManager.open(dir+ texFiles[i]) ;

                int modelTexNo = i;
                boolean mipmap = true;


                int glTexNo = LoadUtil.loadTexture(gl, in, mipmap);
                ((Live2DModelAndroid) live2DModel).setTexture(modelTexNo, glTexNo);

                in.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            if(live2DModel!=null)
            {
                live2DModel.deleteTextures();
            }

            throw new Exception();
        }
        catch(Live2DException e)
        {
            e.printStackTrace();
        }

        modelMatrix=new L2DModelMatrix(live2DModel.getCanvasWidth(),live2DModel.getCanvasHeight());
        modelMatrix.setWidth(2);
        modelMatrix.setCenterPosition(0, 0);
    }


    public void preloadMotionGroup( String name)
    {
        int len = modelSetting.getMotionNum(name);
        for (int i = 0; i < len; i++)
        {
            String fileName = modelSetting.getMotionFile(name, i);
            AMotion motion = LoadUtil.loadAssetsMotion(modelHomeDir + fileName);
            motion.setFadeIn(modelSetting.getMotionFadeIn(name, i));
            motion.setFadeOut(modelSetting.getMotionFadeOut(name, i));
            motions.put( fileName , motion );
        }
    }


    public void update()
    {
        if(live2DModel == null)
        {
            if(live2ddefine.DEBUG_LOG)Log.d(TAG, "モデルデータがないので更新できません");
            return;
        }

        long timeMSec = UtSystem.getUserTimeMSec() - startTimeMSec  ;
        double timeSec = timeMSec / 1000.0 ;
        double t = timeSec * 2 * Math.PI  ;//2πt


        if(mainMotionManager.isFinished())
        {

            startRandomMotion(live2ddefine.MOTION_GROUP_IDLE, live2ddefine.PRIORITY_IDLE);
        }
        //-----------------------------------------------------------------
        live2DModel.loadParam();

        boolean update = mainMotionManager.updateParam(live2DModel);

        if( ! update)
        {

            eyeBlink.updateParam(live2DModel);
        }

        live2DModel.saveParam();
        //-----------------------------------------------------------------

        if(expressionManager!=null)expressionManager.updateParam(live2DModel);




        live2DModel.addToParamFloat( PARAM_ANGLE_X, dragX *  30 , 1 );
        live2DModel.addToParamFloat( PARAM_ANGLE_Y, dragY *  30 , 1 );
        live2DModel.addToParamFloat( PARAM_ANGLE_Z, (dragX*dragY) * -30 , 1 );


        live2DModel.addToParamFloat( PARAM_BODY_X    , dragX * 10 , 1 );


        live2DModel.addToParamFloat( PARAM_EYE_BALL_X, dragX  , 1 );
        live2DModel.addToParamFloat( PARAM_EYE_BALL_Y, dragY  , 1 );


        live2DModel.addToParamFloat(PARAM_ANGLE_X,	(float) (15 * Math.sin( t/ 6.5345 )) , 0.5f);
        live2DModel.addToParamFloat(PARAM_ANGLE_Y,	(float) ( 8 * Math.sin( t/ 3.5345 )) , 0.5f);
        live2DModel.addToParamFloat(PARAM_ANGLE_Z,	(float) (10 * Math.sin( t/ 5.5345 )) , 0.5f);
        live2DModel.addToParamFloat(PARAM_BODY_X,(float) ( 4 * Math.sin( t/15.5345 )) , 0.5f);
        live2DModel.setParamFloat(PARAM_BREATH,	(float) (0.5f + 0.5f * Math.sin( t/3.2345 )),1);


        live2DModel.addToParamFloat(PARAM_ANGLE_Z,	 90 * accelX  ,0.5f);

        if(physics!=null)physics.updateParam(live2DModel);


        if(lipSync)
        {
            live2DModel.setParamFloat(PARAM_MOUTH_OPEN_Y, lipSyncValue ,0.8f);
        }


        if(pose!=null)pose.updateParam(live2DModel);

        live2DModel.update();
    }



    private void drawHitArea(GL10 gl) {
        gl.glDisable( GL10.GL_TEXTURE_2D ) ;
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY) ;
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glPushMatrix() ;
        {
            gl.glMultMatrixf(modelMatrix.getArray(), 0) ;
            int len = modelSetting.getHitAreasNum();
            for (int i=0;i<len;i++)
            {
                String drawID=modelSetting.getHitAreaID(i);
                int drawIndex=live2DModel.getDrawDataIndex(drawID);
                if(drawIndex<0)continue;
                float[] points=live2DModel.getTransformedPoints(drawIndex);
                float left=live2DModel.getCanvasWidth();
                float right=0;
                float top=live2DModel.getCanvasHeight();
                float bottom=0;

                for (int j = 0; j < points.length; j=j+2)
                {
                    float x = points[j];
                    float y = points[j+1];
                    if(x<left)left=x;
                    if(x>right)right=x;
                    if(y<top)top=y;
                    if(y>bottom)bottom=y;
                }

                float[] vertex={left,top,right,top,right,bottom,left,bottom,left,top};
                float r=1;
                float g=0;
                float b=0;
                float a=0.5f;
                int size=5;
                float color[] = {r,g,b,a,r,g,b,a,r,g,b,a,r,g,b,a,r,g,b,a};


                gl.glLineWidth( size );
                gl.glVertexPointer( 2, GL10.GL_FLOAT, 0, BufferUtil.setupFloatBuffer( debugBufferVer,vertex));
                gl.glColorPointer( 4, GL10.GL_FLOAT, 0, BufferUtil.setupFloatBuffer( debugBufferColor,color ) );
                gl.glDrawArrays( GL10.GL_LINE_STRIP, 0, 5 );
            }
        }
        gl.glPopMatrix() ;
        gl.glEnable( GL10.GL_TEXTURE_2D ) ;
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY) ;
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }


    public void startRandomMotion(String name ,int priority)
    {
        int max=modelSetting.getMotionNum(name);
        int no=(int)(Math.random() * max);
        startMotion(name,no,priority);
    }



    public void startMotion(String name, int no,int priority)
    {
        String motionName=modelSetting.getMotionFile(name, no);

        if( motionName==null || motionName.equals(""))
        {
            if(live2ddefine.DEBUG_LOG){Log.d(TAG, "モーション名が無効です");}
            return;//
        }



        //


        if( ! mainMotionManager.reserveMotion(priority))
        {
            if(live2ddefine.DEBUG_LOG){Log.d(TAG, "再生予約があるか再生中のモーションがあるので再生しません");}
            return ;
        }

        String motionPath=modelHomeDir + motionName;
        AMotion motion = LoadUtil.loadAssetsMotion( motionPath);

        if(motion==null)
        {
            Log.w(TAG, "モーションの読み込みに失敗しました。");
            mainMotionManager.setReservePriority(0);
            return;
        }


        motion.setFadeIn(modelSetting.getMotionFadeIn(name, no));
        motion.setFadeOut(modelSetting.getMotionFadeOut(name, no));


        if( modelSetting.getMotionSound(name, no) == null)
        {
            if(live2ddefine.DEBUG_LOG)Log.d(TAG,"モーションの開始 : "+motionName);
            mainMotionManager.startMotionPrio(motion,priority);
        }

        else
        {
            String soundName=modelSetting.getMotionSound(name, no);
            String soundPath=modelHomeDir + soundName;
            MediaPlayer player=LoadUtil.loadAssetsSound( soundPath);

            if(live2ddefine.DEBUG_LOG)Log.d(TAG,"モーションの開始 : "+motionName+" 音声 : "+soundName);
            startVoiceMotion( motion,player,priority);
        }
    }



    public void startVoiceMotion(final AMotion motion,final MediaPlayer player,final int priority)
    {

        player.setOnPreparedListener(new OnPreparedListener()
        {
            public void onPrepared(MediaPlayer mp)
            {
                SoundUtil.release(voice);
                mainMotionManager.startMotionPrio(motion,priority);

                voice=player;
                voice.start();
            }
        });


        player.setOnCompletionListener( new MediaPlayer.OnCompletionListener()
        {
            public void onCompletion(MediaPlayer mp)
            {
                SoundUtil.release(player);
            }
        });

        try
        {
            player.prepare();
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }



    public void setExpression(String name)
    {
        if( ! expressions.containsKey(name))return;
        if(live2ddefine.DEBUG_LOG)Log.d(TAG,"表情の設定 : "+name);
        AMotion motion=expressions.get(name);
        expressionManager.startMotion(motion,false);
    }



    public void setRandomExpression()
    {
        int no=(int)(Math.random() * expressions.size());

        String[] keys = expressions.keySet().toArray(new String[expressions.size()]);

        setExpression(keys[no]);
    }


    public void draw(GL10 gl)
    {
        ((Live2DModelAndroid) live2DModel).setGL(gl);

        alpha+=accAlpha;

        if (alpha<0)
        {
            alpha=0;
            accAlpha=0;
        }
        else if (alpha>1)
        {
            alpha=1;
            accAlpha=0;
        }

        if(alpha<0.001)return;

        if (alpha<0.999)
        {


            OffscreenImage.setOffscreen(gl);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            gl.glPushMatrix() ;
            {
                gl.glMultMatrixf( modelMatrix.getArray(), 0) ;
                live2DModel.draw();
            }
            gl.glPopMatrix() ;


            OffscreenImage.setOnscreen(gl);
            gl.glPushMatrix() ;
            {
                gl.glLoadIdentity();
                OffscreenImage.drawDisplay(gl,alpha);
            }
            gl.glPopMatrix() ;
        }
        else
        {

            gl.glPushMatrix() ;
            {
                gl.glMultMatrixf(modelMatrix.getArray(), 0) ;
                live2DModel.draw();
            }
            gl.glPopMatrix() ;

            if(live2ddefine.DEBUG_DRAW_HIT_AREA )
            {

                drawHitArea(gl);
            }
        }
    }



    public boolean hitTest(String id,float testX,float testY)
    {
        if(alpha<1)return false;
        if(modelSetting==null)return false;
        int len=modelSetting.getHitAreasNum();
        for (int i = 0; i < len; i++)
        {
            if( id.equals(modelSetting.getHitAreaName(i)) )
            {
                String drawID=modelSetting.getHitAreaID(i);
                int drawIndex=live2DModel.getDrawDataIndex(drawID);
                if(drawIndex<0)return false;
                float[] points=live2DModel.getTransformedPoints(drawIndex);

                float left=live2DModel.getCanvasWidth();
                float right=0;
                float top=live2DModel.getCanvasHeight();
                float bottom=0;

                for (int j = 0; j < points.length; j=j+2)
                {
                    float x = points[j];
                    float y = points[j+1];
                    if(x<left)left=x;
                    if(x>right)right=x;
                    if(y<top)top=y;
                    if(y>bottom)bottom=y;
                }

                float tx=modelMatrix.invertTransformX(testX);
                float ty=modelMatrix.invertTransformY(testY);

                return ( left <= tx && tx <= right && top <= ty && ty <= bottom ) ;
            }
        }
        return false;
    }


    public void feedIn()
    {
        alpha=0;
        accAlpha=0.1f;
    }
}
