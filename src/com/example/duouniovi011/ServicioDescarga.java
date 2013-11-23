package com.example.duouniovi011;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServicioDescarga extends BroadcastReceiver {

	 @Override
	 public void onReceive(Context context, Intent intent) {
		 //Empiezo el servicio
			 Intent servicio = new Intent(context, Descarga.class);
		 	 context.startService(servicio);
	 }
	

}
