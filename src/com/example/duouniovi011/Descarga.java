package com.example.duouniovi011;

import java.util.Timer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

public class Descarga extends Service {

	private Timer timer;
	private Long tiempo;
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		timer= new Timer();				
	}

	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(final Intent intent, int flags, final int startID){
		super.onStart(intent, startID);		
		
		SharedPreferences prefs =  getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
		Long horas = prefs.getLong("tiempo_descarga", 2L);
		tiempo =  (long) (horas*3600*1000);	
		timer.schedule(new MyTimerTaskDescarga(), 0, tiempo);
		return START_STICKY;		
	}
	
	
	
}
