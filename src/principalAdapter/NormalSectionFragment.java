package principalAdapter;

import intentManager.Entrada;
import intentManager.IntentsExternos;

import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.example.duouniovi011.R;

import database.persistence.BDHelper;
import database.persistence.DatabaseManager;
import database.persistence.PersistenceFactory;
import database.persistence.impl.ItemsGateway;

public class NormalSectionFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	private Entrada[] entradas;
	private TextView mTextViewCuerpo;
	private TextView mTextViewFecha;
	private Button mButtonLeer;
	private Button mButtonSalir;
	private Button mButtonWebSite;
	public static Dialog ventanaNoticia;
	public static TextToSpeech tts;
	
	public NormalSectionFragment(){ 
		
		mTextViewCuerpo = (TextView) ventanaNoticia.findViewById(R.id.textViewCuerpoNoticia);
		mTextViewFecha = (TextView) ventanaNoticia.findViewById(R.id.textViewFechaNoticia);
		mButtonWebSite = (Button)ventanaNoticia.findViewById(R.id.btSitioWebNoticia);
		mButtonSalir = (Button)ventanaNoticia.findViewById(R.id.btSalirNoticia);
		mButtonLeer = (Button)ventanaNoticia.findViewById(R.id.btLeer);
		mButtonSalir.setOnClickListener(new View.OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				tts.stop();
				ventanaNoticia.cancel();
				
			}
		});
	};
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_canales_dummy,
				container, false);
		ListView dummyListView = (ListView) rootView.findViewById(R.id.listNoticias);
		
		int idCanal = getArguments().getInt(ARG_SECTION_NUMBER);
		final ItemsGateway bdItems = PersistenceFactory.getItemsGateway();
		BDHelper bd = DatabaseManager.getConnection();
		final List<Map<String, Object>> mapaElementos= bdItems.getItemsDelCanal((long)idCanal, bd);
		entradas = new Entrada[mapaElementos.size()];
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
		ArrayAdapter<String> list_adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,titulosEntradas);
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
				.setTitle(R.string.guardarNoticia)
				.setNegativeButton("No", new DialogInterface.OnClickListener() 
				{						
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						Toast.makeText(getActivity(), R.string.noGuardado, Toast.LENGTH_SHORT).show();							
					}
				})
				.setNeutralButton("Si", new DialogInterface.OnClickListener() 
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						BDHelper bd = DatabaseManager.getConnection();
						bdItems.guardar(entradas[pos].getId(), bd);
						Toast.makeText(getActivity(), R.string.guardado, Toast.LENGTH_SHORT).show();							
					}
				})
				.show();
				return false;
			}
		});
		return rootView;
	}
	
}