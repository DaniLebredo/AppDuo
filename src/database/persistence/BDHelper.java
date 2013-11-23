package database.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDHelper extends SQLiteOpenHelper {

	private static int version = 1;

	private Context contexto;

	public static final String NOMBRE_TABLA_CANALES = "Canal";
	public static final String ID_CANAL = "Id_canal";
	public static final String TITULO = "Titulo";
	public static final String LINK = "Link";
	public static final String DESCRIPCION = "Descripcion";
	public static final String SUSCRITO = "Suscrito";
	public static final String CREA_TABLA_CANAL = "create table "
			+ NOMBRE_TABLA_CANALES + "( " + ID_CANAL
			+ " integer primary key autoincrement, " + TITULO
			+ " text not null, " + LINK + " text not null, " + DESCRIPCION
			+ " text, " + SUSCRITO + " text);";

	public static final String NOMBRE_TABLA_ITEMS = "RssItem";
	public static final String FECHA = "Fecha";
	public static final String ID_ITEM = "Id_item";
	public static final String GUARDADO = "Guardado";
	public static final String CREA_TABLA_ITEMS = "create table "
			+ NOMBRE_TABLA_ITEMS + " ( " + TITULO + " text not null, " + LINK
			+ " text not null, " + DESCRIPCION + " text, " + FECHA + " date, "
			+ ID_CANAL + " integer, " + GUARDADO + " text  ," + ID_ITEM
			+ " integer, "
			+ // NO AUTOINCREMENT
			"FOREIGN KEY(" + ID_CANAL + ") REFERENCES " + NOMBRE_TABLA_CANALES
			+ "(" + ID_CANAL + ")," + "PRIMARY KEY(Id_canal, Id_item));";

	public BDHelper(Context context) {
		super(context, "AppDuo", null, version);
		this.contexto = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREA_TABLA_CANAL);
		db.execSQL(CREA_TABLA_ITEMS);
		db.execSQL("PRAGMA foreign_keys = ON;");
		crearCanales(db);
	}

	// TODO poner enlaces a la web, no a los RSS...
	private void crearCanales(SQLiteDatabase db) {

		// Canal actos
		String titulo = "Actos";
		String descripcion = "Diario de la Universidad de Oviedo. Actos.";
		String link = "http://www.uniovi.es/comunicacion/duo/actos/-/asset_publisher/L2iEKaAZuBNz/rss";
		String crearCanal = "INSERT INTO CANAL (Titulo, Link, Descripcion, Suscrito) VALUES ";

		db.execSQL(crearCanal + "('" + titulo + "', '" + link + "', '"
				+ descripcion + "', 'No');");

		// Canal anuncios
		titulo = "Anuncios.";
		descripcion = "Diario de la Universidad de Oviedo. Anuncios.";
		link = "http://www.uniovi.es/comunicacion/duo/anuncios/-/asset_publisher/k22otAqlur25/rss";

		db.execSQL(crearCanal + "('" + titulo + "', '" + link + "', '"
				+ descripcion + "', 'No');");

		// Canal becas
		titulo = "Becas.";
		descripcion = "Diario de la Universidad de Oviedo. Becas.";
		link = "http://www.uniovi.es/comunicacion/duo/becas/-/asset_publisher/ZICH7zdCzufQ/rss";

		db.execSQL(crearCanal + "('" + titulo + "', '" + link + "', '"
				+ descripcion + "', 'No');");

		// Canal boletin
		titulo = "Boletines Oficiales";
		link = "http://www.uniovi.es/comunicacion/duo/boletinesoficiales/-/asset_publisher/7iuBUjFjsoD2/rss";
		descripcion = "Diario de la Universidad de Oviedo. Boletines Oficiales.";

		db.execSQL(crearCanal + "('" + titulo + "', '" + link + "', '"
				+ descripcion + "', 'No');");

		// Canal convocatorias
		link = "http://www.uniovi.es/comunicacion/duo/convocatorias/-/asset_publisher/cxX13ntusT2E/rss";
		titulo = "Convocatorias";
		descripcion = "Diario de la Universidad de Oviedo. Convocatorias.";

		db.execSQL(crearCanal + "('" + titulo + "', '" + link + "', '"
				+ descripcion + "', 'No');");

		// Canal otros
		link = "http://www.uniovi.es/comunicacion/duo/otros/-/asset_publisher/tJrNest4WQMj/rss";
		titulo = "Otros";
		descripcion = "Diario de la Universidad de Oviedo. Otros.";

		db.execSQL(crearCanal + "('" + titulo + "', '" + link + "', '"
				+ descripcion + "', 'No');");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		version++;
		db.execSQL("DROP TABLE IF EXISTS CANAL");
		db.execSQL("DROP TABLE IF EXISTS RSSITEM");
		onCreate(db);
	}

	// TODO sustituir por getApplicationContext() ?
	public Context getContexto() {
		return contexto;
	}

}
