
package com.project.framework;

import java.util.HashMap ;
import java.util.Map ;

import jp.live2d.ALive2DModel ;
import jp.live2d.motion.AMotion ;
import jp.live2d.motion.MotionQueueManager ;

public class L2DBaseModel {
	
	protected final String 			PARAM_ANGLE_X="PARAM_ANGLE_X";
	protected final String 			PARAM_ANGLE_Y="PARAM_ANGLE_Y";
	protected final String 			PARAM_ANGLE_Z="PARAM_ANGLE_Z";
	protected final String 			PARAM_BODY_X="PARAM_BODY_X";
	protected final String 			PARAM_BREATH="PARAM_BREATH";
	protected final String 			PARAM_EYE_BALL_X="PARAM_EYE_BALL_X";
	protected final String 			PARAM_EYE_BALL_Y="PARAM_EYE_BALL_Y";
	protected final String 			PARAM_MOUTH_OPEN_Y="PARAM_MOUTH_OPEN_Y";

	
	protected ALive2DModel		 		live2DModel=null;			
	protected L2DModelMatrix 			modelMatrix=null;			

	
	protected Map<String,AMotion> 	expressions ;			
	protected Map<String,AMotion> 	motions ;		

	protected L2DMotionManager 		mainMotionManager;		
	protected L2DMotionManager 		expressionManager;		
	protected L2DEyeBlink 			eyeBlink;				
	protected L2DPhysics 			physics;				
	protected L2DPose 				pose;					

	protected boolean 				initialized = false;	
	protected boolean 				updating = false;		
	protected float 				alpha = 1;				
	protected float 				accAlpha = 0;			
	protected boolean 				lipSync = false;		
	protected float 				lipSyncValue;			

	
	protected float 				accelX=0;
	protected float 				accelY=0;
	protected float 				accelZ=0;

	
	protected float 				dragX=0;
	protected float 				dragY=0;

	protected long 					startTimeMSec;


	public L2DBaseModel()
	{
		
		mainMotionManager=new L2DMotionManager();
		expressionManager=new L2DMotionManager();

		motions=new HashMap< String , AMotion >();
		expressions=new HashMap< String , AMotion >();
	}


	public L2DModelMatrix getModelMatrix()
	{
		return modelMatrix;
	}


	public void setAlpha(float a)
	{
		if(a>0.999)a=1;
		if(a<0.001)a=0;
		alpha=a;
	}


	public float getAlpha()
	{
		return alpha;
	}


	
	public boolean isInitialized() {
		return initialized;
	}


	public void setInitialized(boolean v)
	{
		initialized=v;
	}


	
	public boolean isUpdating() {
		return updating;
	}


	public void setUpdating(boolean v)
	{
		updating=v;
	}


	
	public ALive2DModel getLive2DModel() {
		return live2DModel;
	}


	public void setLipSync(boolean v)
	{
		lipSync=v;
	}


	public void setLipSyncValue(float v)
	{
		lipSyncValue=v;
	}


	public void setAccel(float x,float y,float z)
	{
		accelX=x;
		accelY=y;
		accelZ=z;
	}


	public void setDrag(float x,float y)
	{
		dragX=x;
		dragY=y;
	}


	public MotionQueueManager getMainMotionManager()
	{
		return mainMotionManager;
	}


	public MotionQueueManager getExpressionManager()
	{
		return expressionManager;
	}
}
