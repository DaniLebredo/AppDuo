package com.example.duouniovi011;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SubscripcionesActivity extends Activity {
	private ListView mlListView;
	private static int CODE_SETTINGS_ACTIVITY =0;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suscripciones);
		
		mlListView = (ListView) findViewById(R.id.listViewCanales);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.canales,android.R.layout.simple_list_item_1);
		mlListView.setAdapter(adapter);
		mlListView.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) 
			{
				new AlertDialog.Builder(SubscripcionesActivity.this)
				.setTitle(R.string.deseaNotificaciones)
				.setNeutralButton("SI", new DialogInterface.OnClickListener() 
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
					}
				})
				.setNegativeButton("NO", new DialogInterface.OnClickListener() 
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						// TODO Auto-generated method stub
						
					}
				}).show();
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
		case R.id.action_settings:
			launchSettingsActivity();
			break;
		case R.id.action_quit:
			finish();
			break;

		default:
			break;
		}
		return true;
	}
	private void launchSettingsActivity()
	{
		Intent mIntent = new Intent(this,SettingsActivity.class);
		startActivityForResult(mIntent, CODE_SETTINGS_ACTIVITY);
	}
//	private void launchActivityGuardadas()
//	{
//		Intent mIntent = new Intent(this,NoticiasGuardadas.class);
//		startActivity(mIntent);
//	}

}
