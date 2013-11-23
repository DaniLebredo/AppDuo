package com.example.duouniovi011;

import intentManager.Entrada;
import intentManager.IntentsExternos;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import database.persistence.BDHelper;
import database.persistence.DatabaseManager;
import database.persistence.PersistenceFactory;
import database.persistence.impl.ItemsGateway;

public class NoticiasGuardadas extends FragmentActivity {

	
	private static  ItemsGateway bdItems;
	private static ListAdapter list_adapter;
	private static Dialog ventanaNoticia;
	private static TextView mTextViewCuerpo;
	private static TextView mTextViewFecha;
	private static Button mButtonLeer;
	private static Button mButtonWebSite;
	private static Button mButtonSalir;
	private static int CODE_SETTINGS_ACTIVITY =0;
	private static TextToSpeech tts;
	
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_noticias_guardadas);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.noticias_guardadas, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DUOSectionFragment();
			Bundle args = new Bundle();
			args.putInt(DUOSectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 6 total pages.
			return 6;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return getString(R.string.title_section4).toUpperCase(l);
			case 4:
				return getString(R.string.title_section5).toUpperCase(l);
			case 5:
				return getString(R.string.title_section6).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DUOSectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DUOSectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_canales_dummy,
					container, false);
			
			ListView dummyListView = (ListView) rootView.findViewById(R.id.listNoticias);			
			int idCanal = getArguments().getInt(ARG_SECTION_NUMBER);
			ItemsGateway bdItems = PersistenceFactory.getItemsGateway();
			BDHelper bd = DatabaseManager.getConnection();
			final List<Map<String, Object>> mapaElementos= bdItems.getItemsGuardados(bd, (long)idCanal);
			final Entrada[] entradas = new Entrada[mapaElementos.size()];
			for(int i=0;i<mapaElementos.size();i++)
			{
				
				entradas[i]= new Entrada();
				entradas[i].setTitulo(mapaElementos.get(i).get("Titulo").toString());
				entradas[i].setTexto(mapaElementos.get(i).get("Descripcion").toString());
				entradas[i].setFecha(mapaElementos.get(i).get("Fecha").toString());
				entradas[i].setUrl(mapaElementos.get(i).get("Link").toString());
				entradas[i].setId((Long) mapaElementos.get(i).get("Id_item"));
			}
			String[] titulosEntradas = new String[entradas.length];
			for(int i=0;i<entradas.length;i++)
			{
				titulosEntradas[i]=entradas[i].getTitulo().toUpperCase();
			}
			list_adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,titulosEntradas);
			dummyListView.setAdapter(list_adapter);
			dummyListView.setOnItemClickListener(new OnItemClickListener() 
			{

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						final int pos, long arg3) 
				{
					ventanaNoticia.setTitle(entradas[pos].getTitulo());
					mTextViewCuerpo.setText(entradas[pos].getTexto());
					mTextViewFecha.setText(entradas[pos].getFecha());
					mButtonWebSite.setOnClickListener(new View.OnClickListener() 
					{
						
						@Override
						public void onClick(View v) 
						{
							IntentsExternos.navegateToUrl(getActivity(), entradas[pos].getUrl());
							
						}
					});
					mButtonLeer.setOnClickListener(new View.OnClickListener() 
					{
						
						@Override
						public void onClick(View v) 
						{
							tts.speak(mTextViewCuerpo.getText().toString(), TextToSpeech.QUEUE_ADD, null);
							
						}
					});
					ventanaNoticia.show();
					
				}
			});
			dummyListView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						final int pos, long arg3) 
				{
					new AlertDialog.Builder(getActivity())
					.setTitle(R.string.noGuardarNoticia)
					.setNegativeButton("No", new DialogInterface.OnClickListener() 
					{						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							Toast.makeText(getActivity(),R.string.NoBorrarNoticia, Toast.LENGTH_SHORT).show();							
						}
					})
					.setNeutralButton("Si", new DialogInterface.OnClickListener() 
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							BDHelper bd = DatabaseManager.getConnection();
							ItemsGateway bdItems = PersistenceFactory.getItemsGateway();
							bdItems.noGuardar(entradas[pos].getId(), bd);
							Toast.makeText(getActivity(), R.string.BorrarNoticia, Toast.LENGTH_SHORT).show();							
						}
					})
					.show();
					return false;
				}
			});
			return rootView;
		}
	}

}