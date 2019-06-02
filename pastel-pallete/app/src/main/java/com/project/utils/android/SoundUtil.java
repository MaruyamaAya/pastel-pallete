
package com.project.utils.android;

import java.io.IOException;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;


public class SoundUtil {

	
	static public void release(MediaPlayer player){
		if( player != null ){
			player.setOnCompletionListener(null) ;
			player.release() ;
			player=null;
		}
	}


	
//	static public MediaPlayer loadAssets(Context context,String filename){
//
//
//		final MediaPlayer player = new MediaPlayer() ;
//
//		try {
//			final AssetFileDescriptor assetFileDescritorArticle = context.getAssets().openFd( filename );
//			player.reset();
//
//			player.setDataSource( assetFileDescritorArticle.getFileDescriptor(),
//					assetFileDescritorArticle.getStartOffset(), assetFileDescritorArticle.getLength() );
//			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//			assetFileDescritorArticle.close();
//
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return player;
//	}


	
	static public void playAssets(Context context,String filename){
		final MediaPlayer player = new MediaPlayer() ;

		try {
			final AssetFileDescriptor assetFileDescritorArticle = context.getAssets().openFd( filename );
			player.reset();

			player.setDataSource( assetFileDescritorArticle.getFileDescriptor(),
					assetFileDescritorArticle.getStartOffset(), assetFileDescritorArticle.getLength() );
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			assetFileDescritorArticle.close();

			
			player.setOnPreparedListener(new OnPreparedListener() {

				public void onPrepared(MediaPlayer mp) {
					player.start();

				}
			});

			
			player.setOnCompletionListener( new MediaPlayer.OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
					release(player);
				}
			});
			player.prepareAsync();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
