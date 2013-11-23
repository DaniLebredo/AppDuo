package com.example.duouniovi011;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServicioBorrado extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		//Empiezo el servicio
		Intent servicio = new Intent(context, Borrado.class);
		context.startService(servicio);
	}

}
