package parser;

import java.util.List;

/**
 * Use this class to get data from the Rss that the University of Oviedo
 * provides <b>without using services, so it can't be within the UI thread</b>.
 * If you want to use a service, check {@link RssProvider}. <br>
 * For further information check {@link http
 * ://androidresearch.wordpress.com/2013
 * /06/01/creating-a-simple-rss-application-in-android-v2/}
 * 
 * @author Daniel
 * 
 */
public interface RssProvider {

	static String URL = "http://www.uniovi.es/comunicacion/duo/SECTION/-/asset_publisher/L2iEKaAZuBNz/rss?p_p_cacheability=cacheLevelPage";
	static String ALT_URL = "http://www.uniovi.es/comunicacion/duo/SECTION";

	public static final String ITEMS = "items";
	public static final String RECEIVER = "receiver";
	public static final String SECTION = "SECTION";

	/**
	 * Use this Enum to set the rss channel you want to retrieve.
	 * 
	 * @author Daniel
	 * 
	 */
	public enum Section {
		ACTOS, ANUNCIOS, BECAS, BOLETINES_OFICIALES, CONVOCATORIAS, OTROS
	}

	/**
	 * Ask for the content of a RSS channel from the <i>dUO</i>. Check
	 * {@link #getLast(Section, int)} if you need more control on want to
	 * retrieve.
	 * 
	 * @param section
	 *            Section required. Please, use the Section enum to set it.
	 * @return A List of {@link RssItem} ordered by date (most recent first).
	 */
	public List<RssItem> getRss(Section section);

	/**
	 * This is an alternative method to get rss entries, specifying the entry id
	 * for the last one in the database. Usually, one would call
	 * {@link #getRss(Section)} first and then update regularly with this one.
	 * 
	 * @param section
	 *            Section required. Please, use the Section enum to set it.
	 * @param id
	 *            The id of the last entry in the rss that is also in the
	 *            database.
	 * @return A List of {@link RssItem} ordered by date (most recent first).
	 */
	public List<RssItem> getLast(Section section, int id);

	/**
	 * Quick method to get the <i>Actos</i>'s RSS channel content.
	 * 
	 * @return List of {@link RssItem} from the Actos Rss channel.
	 */
	public List<RssItem> getActos();

	/**
	 * Quick method to get the <i>Anuncios</i>'s RSS channel content.
	 * 
	 * @return List of {@link RssItem} from the Anuncios Rss channel.
	 */
	public List<RssItem> getAnuncios();

	/**
	 * Quick method to get the <i>Becas</i>'s RSS channel content.
	 * 
	 * @return List of {@link RssItem} from the Becas Rss channel.
	 */
	public List<RssItem> getBecas();

	/**
	 * Quick method to get the <i>Boletines Oficiales</i>'s RSS channel content.
	 * 
	 * @return List of {@link RssItem} from the Boletines Oficiales Rss channel.
	 */
	public List<RssItem> getBoletinesOficiales();

	/**
	 * Quick method to get the <i>Convocatorias</i>'s RSS channel content.
	 * 
	 * @return List of {@link RssItem} from the Convocatorias Rss channel.
	 */
	public List<RssItem> getConvocatorias();

	/**
	 * Quick method to get the <i>Otros</i>'s RSS channel content.
	 * 
	 * @return List of {@link RssItem} from the Otros Rss channel.
	 */
	public List<RssItem> getOtros();
	

}
