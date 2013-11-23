package com.example.duouniovi011;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;
import database.persistence.BDHelper;
import database.persistence.DatabaseManager;
import database.persistence.PersistenceFactory;
import database.persistence.impl.ItemsGateway;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class MyTimerTaskBorrado extends TimerTask {
	
	
	private Context c;
	
	public MyTimerTaskBorrado(Context contexto){
		this.c = contexto;
	}

	@Override
	public void run() {
		try{
			//Lee los dias que hay borrar
			SharedPreferences prefs =  c.getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
			int dias = prefs.getInt("tiempo_borrado", 10);
			//Lee la fecha actual del sistema
			Date fechaActual = Calendar.getInstance().getTime();
			//Pasala a milisegundos
			long tiempoActual = fechaActual.getTime();
			//Calcula los milisegundos del número de dias que el usuario quiere
			long unDia = dias * 24 * 3660 * 1000;
			//Calcula la fecha de borrado
			Date fechaBorrado = new Date(tiempoActual - unDia);
			//Abre las conexiones con la base de datos
			BDHelper base = DatabaseManager.getConnection();
		    ItemsGateway ig = PersistenceFactory.getItemsGateway();
			//Llama a la base de datos
		    ig.limpiarPersistencia(fechaBorrado, base);			
		}catch(Exception e){
			Log.e("ServicioBorrado", "Error borrando" + e.getMessage());
		}

	}

}
