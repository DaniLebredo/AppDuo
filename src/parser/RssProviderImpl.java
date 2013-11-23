package parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * This class provides the specific methods required to retrieve an Rss channel.
 * 
 * 
 * @author Daniel
 * 
 */
public class RssProviderImpl implements RssProvider {

	public static final String ITEMS = "items";
	public static final String RECEIVER = "receiver";
	public static final String SECTION = "SECTION";

	static String ACTOS = "http://www.uniovi.es/comunicacion/duo/actos/-/asset_publisher/L2iEKaAZuBNz/rss?p_p_cacheability=cacheLevelPage";
	static String ANUNCIOS = "http://www.uniovi.es/comunicacion/duo/anuncios/-/asset_publisher/k22otAqlur25/rss?p_p_cacheability=cacheLevelPage";
	static String BECAS = "http://www.uniovi.es/comunicacion/duo/becas/-/asset_publisher/ZICH7zdCzufQ/rss?p_p_cacheability=cacheLevelPage";
	static String BOLETINES_OFICIALES = "http://www.uniovi.es/comunicacion/duo/boletinesoficiales/-/asset_publisher/7iuBUjFjsoD2/rss?p_p_cacheability=cacheLevelPage";
	static String CONVOCATORIAS = "http://www.uniovi.es/comunicacion/duo/convocatorias/-/asset_publisher/cxX13ntusT2E/rss?p_p_cacheability=cacheLevelPage";
	static String OTROS = "http://www.uniovi.es/comunicacion/duo/otros/-/asset_publisher/tJrNest4WQMj/rss?p_p_cacheability=cacheLevelPage";

	private static Map<Section, String> em = new EnumMap<Section, String>(
			Section.class);
	static {
		em.put(Section.ACTOS, ACTOS);
		em.put(Section.ANUNCIOS, ANUNCIOS);
		em.put(Section.BECAS, BECAS);
		em.put(Section.BOLETINES_OFICIALES, BOLETINES_OFICIALES);
		em.put(Section.CONVOCATORIAS, CONVOCATORIAS);
		em.put(Section.OTROS, OTROS);
	}

	public static Map<Section, String> getSecciones() {
		return em;
	}

	/**
	 * Empty constructor.
	 */
	public RssProviderImpl() {
	}

	private static int retries = 0;
	private static final int MAX_RETRIES = 3;

	@SuppressLint("DefaultLocale")
	public List<RssItem> getRss(Section section) {
		Log.d(Constants.TAG, "Service started");
		Log.d("debug", "Iniciando el getRSS para la section "
				+ section.toString().toLowerCase());
		String link = em.get(section);
		List<RssItem> rssItems = new ArrayList<RssItem>();
		try {
			UnioviRssParser parser = new UnioviRssParser(link);
			rssItems = parser.parse(getInputStream(link));
		} catch (XmlPullParserException e) {
			Log.w(e.getMessage(), e);
			Log.d("debug", "XML EXCEPTION");

			if (++retries < MAX_RETRIES)
				return getRss(section);
		} catch (IOException e) {
			Log.w(e.getMessage(), e);
			Log.d("debug", "IO EXCEPTION");

			if (++retries < MAX_RETRIES)
				return getRss(section);
		} catch (Exception e) {

			Log.w(e.getMessage(), e);
			Log.d("debug", "Exception");

			if (++retries < MAX_RETRIES)
				return getRss(section);
		}
		return rssItems;
	}

	private InputStream getInputStream(String link) {
		try {
			URL url = new URL(link);
			return url.openConnection().getInputStream();
		} catch (IOException e) {
			Log.w(Constants.TAG, "Exception while retrieving the input stream",
					e);
			return null;
		}
	}

	@Override
	public List<RssItem> getActos() {
		return getRss(Section.ACTOS);
	}

	@Override
	public List<RssItem> getAnuncios() {
		return getRss(Section.ANUNCIOS);
	}

	@Override
	public List<RssItem> getBecas() {
		return getRss(Section.BECAS);
	}

	@Override
	public List<RssItem> getBoletinesOficiales() {
		return getRss(Section.BOLETINES_OFICIALES);
	}

	@Override
	public List<RssItem> getConvocatorias() {
		return getRss(Section.CONVOCATORIAS);
	}

	@Override
	public List<RssItem> getOtros() {
		return getRss(Section.OTROS);
	}

	@SuppressLint("DefaultLocale")
	@Override
	public List<RssItem> getLast(Section section, int id) {
		Log.d(Constants.TAG, "Service started");
		Log.d("debug", "Iniciando el getRSS para la section "
				+ section.toString().toLowerCase());
		String link = em.get(section);
		Log.d("debug", "Enlace utilizado = " + link);
		List<RssItem> rssItems = null;
		try {
			UnioviRssParser parser = new UnioviRssParser(link, id);
			rssItems = parser.parse(getInputStream(link));
		} catch (XmlPullParserException e) {
			Log.w(e.getMessage(), e);
		} catch (IOException e) {
			Log.w(e.getMessage(), e);
		}
		return rssItems;
	}

}
