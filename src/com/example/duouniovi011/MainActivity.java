package com.example.duouniovi011;

import principalAdapter.NormalSectionFragment;
import principalAdapter.SectionsPagerAdapter;
import intentManager.IntentsExternos;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import database.persistence.BDHelper;
import database.persistence.DatabaseManager;
import database.persistence.impl.ItemsGateway;


public class MainActivity extends FragmentActivity implements OnInitListener{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	private static Dialog ventanaNoticia;
	private static TextView mTextViewCuerpo;
	private static TextView mTextViewFecha;
	private static Button mButtonLeer;
	private static Button mButtonWebSite;
	private static Button mButtonSalir;
	private static int CODE_SETTINGS_ACTIVITY =0;
	private static TextToSpeech tts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tts = new TextToSpeech(this, this);
		ventanaNoticia = new Dialog(MainActivity.this);
		ventanaNoticia.setContentView(R.layout.noticia);
		mTextViewCuerpo = (TextView) ventanaNoticia.findViewById(R.id.textViewCuerpoNoticia);
		mTextViewFecha = (TextView) ventanaNoticia.findViewById(R.id.textViewFechaNoticia);
		mButtonWebSite = (Button)ventanaNoticia.findViewById(R.id.btSitioWebNoticia);
		mButtonSalir = (Button)ventanaNoticia.findViewById(R.id.btSalirNoticia);
		mButtonLeer = (Button)ventanaNoticia.findViewById(R.id.btLeer);
		
		NormalSectionFragment.ventanaNoticia = ventanaNoticia;
		NormalSectionFragment.tts = tts;
		
		mButtonSalir.setOnClickListener(new View.OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				tts.stop();
				ventanaNoticia.cancel();
				
			}
		});
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		
		//Abro la conexión con la base de datos predefinida para toda la app
		BDHelper bd = new BDHelper(this);
		DatabaseManager.createConnection(bd);
		//Se lanzan los servicios en segundo plano
		lanzarServicios();

	}
	
	private void lanzarServicios() {
		//Tiempos predefinidos
		SharedPreferences prefs =  getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		if(!prefs.contains("tiempo_descarga")){
			editor.putLong("tiempo_descarga", 2);			
		}		
		if(!prefs.contains("tiempo_borrado")){	
			editor.putInt("tiempo_borrado", 10);			
		}
		editor.commit();
	
		
		//Empiezo el servicio descarga	
		Intent servicio = new Intent(this, ServicioDescarga.class);
		sendBroadcast(servicio);
		
		//Empiezo el servicio de limpieza		
		Intent serv = new Intent(this, ServicioBorrado.class);
		sendBroadcast(serv);
	}
	
	private void launchSettingsActivity()
	{
		Intent mIntent = new Intent(this,SettingsActivity.class);
		startActivityForResult(mIntent, CODE_SETTINGS_ACTIVITY);
	}
	private void launchActivityGuardadas()
	{
		Intent mIntent = new Intent(this,NoticiasGuardadas.class);
		startActivity(mIntent);
	}
	private void launchActivitySuscripciones()
	{
		Intent mIntent = new Intent(this,SubscripcionesActivity.class);
		startActivity(mIntent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.canales, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
		case R.id.action_settings:
			launchSettingsActivity();
			break;
		case R.id.action_quit:
			finish();
			break;
		case R.id.action_noticias_guardadas:
			launchActivityGuardadas();
			break;
		case R.id.action_webSite:
			//TODO esto para qué está?
			IntentsExternos.navegateToUrl(this, "http://www.uniovi.es/comunicacion/duo?p_p_id=filter_by_categories_INSTANCE_wz1VF1bfR0hV&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_pos=1&p_p_col_count=6&p_r_p_564233524_categoryId=250110%2C2271359");
			break;
		case R.id.action_subcripciones:
			launchActivitySuscripciones();
			break;

		default:
			break;
		}
		return true;
	}
	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	
	

}
