package com.example.duouniovi011;

import intentManager.Entrada;
import intentManager.IntentsExternos;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import database.persistence.BDHelper;
import database.persistence.DatabaseManager;
import database.persistence.PersistenceFactory;
import database.persistence.impl.ItemsGateway;

@SuppressLint("DefaultLocale")
public class ListaNoticiasGuardadas extends Activity {
	private ItemsGateway bdItems;
	private Entrada[] entradas;
	private ListView listaGuardados;
	private  ListAdapter list_adapter;
	private TextView mTextViewSinNoticias;
	private Dialog ventanaNoticia;
	private static TextView mTextViewCuerpo;
	private static TextView mTextViewFecha;
	private static Button mButtonWebSite;
	private static Button mButtonSalir;


	@SuppressLint("DefaultLocale")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		/*
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_noticias_guardadas);
		ventanaNoticia = new Dialog(this);
		ventanaNoticia.setContentView(R.layout.noticia);
		mTextViewCuerpo = (TextView)ventanaNoticia.findViewById(R.id.textViewCuerpoNoticia);
		mTextViewFecha = (TextView)ventanaNoticia.findViewById(R.id.textViewFechaNoticia);
		mButtonSalir = (Button)ventanaNoticia.findViewById(R.id.btSalirNoticia);
		mButtonSalir.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				ventanaNoticia.cancel();
				
			}
		});
		mButtonWebSite = (Button)ventanaNoticia.findViewById(R.id.btSitioWebNoticia);
		bdItems = PersistenceFactory.getItemsGateway();
		BDHelper bd = DatabaseManager.getConnection();
		//final List<Map<String, Object>> mapaElementos=bdItems.getItemsGuardados();
		entradas = new Entrada[mapaElementos.size()];
		for(int i=0;i< entradas.length;i++)
		{
			entradas[i]=new Entrada();
			entradas[i].setTitulo(mapaElementos.get(i).get("Titulo").toString());
			entradas[i].setTexto(mapaElementos.get(i).get("Descripcion").toString());
			entradas[i].setFecha(mapaElementos.get(i).get("Fecha").toString());
			entradas[i].setUrl(mapaElementos.get(i).get("Link").toString());
		}
		final String[] titulosEntradas = new String[entradas.length];
		for(int i=0;i<entradas.length;i++)
		{
			
			titulosEntradas[i]=entradas[i].getTitulo().toUpperCase();
		}
		mTextViewSinNoticias = (TextView) findViewById(R.id.textViewNoNoticia);
		listaGuardados = (ListView) findViewById(R.id.listViewGuardados);
		list_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,titulosEntradas);
		listaGuardados.setAdapter(list_adapter);
		
		if(titulosEntradas.length==0)
			mTextViewSinNoticias.setText(R.string.noNoticias);
		else
			mTextViewSinNoticias.setText("");
		
		listaGuardados.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int pos,
					long arg3) 
			{
				
				
					ventanaNoticia.setTitle(entradas[pos].getTitulo());
					mTextViewCuerpo.setText(entradas[pos].getTexto());
					mTextViewFecha.setText(entradas[pos].getFecha());
					mButtonWebSite.setOnClickListener(new View.OnClickListener() 
					{
						
						@Override
						public void onClick(View v) 
						{
							IntentsExternos.navegateToUrl(ListaNoticiasGuardadas.this, entradas[pos].getUrl());
							
						}
					});
				
			}
		});
		listaGuardados.setOnItemLongClickListener(new OnItemLongClickListener() 
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista_noticias_guardadas, menu);
		return true;
	}

*/
	}
}
