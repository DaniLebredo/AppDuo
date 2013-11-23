package parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

/**
 * This is my adapted version of the PcWorldRssParser taken from the Internet.
 * It retrieves the previously chosen Rss Channel and parses it, doing a second
 * parse for each entry, in order to get the content and additional links
 * provided by the dUO.
 * 
 * It is a quite slow parser, so be patient!
 * 
 * @author Daniel
 * 
 */
public class UnioviRssParser {

	private final String ns = null;
	private String link = null;
	private int last;

	/**
	 * This constructor is mandatory because it sets the channel to parse when
	 * retrieving the additional information (content, date, additional link).
	 * 
	 * @param link
	 *            The link to the required channel.
	 */
	public UnioviRssParser(String link) {
		this.link = link;
		this.last = Integer.MIN_VALUE;
	}

	/**
	 * This constructor is mandatory because it sets the channel to parse when
	 * retrieving the additional information (content, date, additional link).
	 * 
	 * @param link
	 *            The link to the required channel.
	 * @param last
	 *            The id of the last entry in the rss that is also in the
	 *            database.
	 */
	public UnioviRssParser(String link, int last) {
		this.link = link;
		this.last = last;
	}

	/**
	 * This method parses a link (that has to be an Rss channel) and returns a
	 * list of {@link RssItem}.
	 * 
	 * @param inputStream
	 *            {@link InputStream} from a link to a Rss.
	 * @return A List<{@link RssItem}> containing all the info from the Rss
	 *         Channel.
	 * @throws XmlPullParserException
	 *             You have an error in the webpage, such as it is invalid.
	 * @throws IOException
	 *             There's a network error, I suppose.
	 */
	public List<RssItem> parse(InputStream inputStream)
			throws XmlPullParserException, IOException {
		try {
			getAllContent();
			Log.d("debug", "Entrando al parser");
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(inputStream, null);
			parser.nextTag();
			return readFeed(parser);
		} finally {
			inputStream.close();
		}
	}

	/**
	 * Parses the RSS channel and returns the List of RssItem
	 * 
	 * @param parser
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private List<RssItem> readFeed(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		Log.d("debug", "Dentro del parser");
		parser.require(XmlPullParser.START_TAG, null, "rss");
		String title = null;
		String link = null;
		String creator = null;
		String date = null;

		List<RssItem> items = new ArrayList<RssItem>();
		while (parser.next() != XmlPullParser.END_DOCUMENT) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("title")) {
				title = readTitle(parser);
			} else if (name.equals("link")) {
				link = readLink(parser);
			} else if (name.equals("dc:creator")) {
				creator = readCreator(parser);
			} else if (name.equals("dc:date")) {
				date = readDate(parser);
			}
			if (title != null && link != null && creator != null
					&& date != null) {

				String[] linkSplit = link.split("/");
				int id = Integer.parseInt(linkSplit[linkSplit.length - 1]);
				if (id <= last) {
					return items;
				}
				Map<String, Object> content = getArticleContent(id);
				RssItem item = new RssItem(id, title, link, creator, date,
						(String) content.get("content"),
						(List<Map<String, String>>) content
								.get("additionalLinks"));
				items.add(item);
				Log.d("debug", id + " " + title);
				title = null;
				link = null;
				creator = null;
				date = null;
			}
		}
		return items;
	}

	private String readLink(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "link");
		String link = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "link");
		return link;
	}

	private String readTitle(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "title");
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "title");
		return title;
	}

	// For the tags title and link, extract their text values.
	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private String readCreator(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "dc:creator");
		String creator = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "dc:creator");
		return creator;
	}

	/**
	 * Parse the date of the article from the rss.
	 * 
	 * @param parser
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private String readDate(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "dc:date");
		String creator = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "dc:date");
		return creator;
	}

	int counter = 1;
	boolean firstTime = true;

	/**
	 * Parses an specified dUO url looking for the content not present in the
	 * RSS channel.
	 * 
	 * @param index
	 *            The unique id of the dUO article
	 * @return A Map<String, String> containing the "content" and
	 *         "additionalLink" keys.
	 */
	private Map<String, Object> getArticleContent(int id) {

		Map<String, Object> articleContent = new HashMap<String, Object>();
		String text = null;
		List<Map<String, String>> additionalLink = new ArrayList<Map<String, String>>();
		String textID = (id - 4) + "";
		Log.d("debug_name", "Solicitado el articulo = " + textID);

		/*
		 * Retrieve the content from the article
		 */
		if (name.get(counter).getElementsByClass("cuerpo").isEmpty()) {
			text = name.get(counter).getElementsByClass("cuerpoOculto").get(0)
					.getElementsByTag("p").text();
			if (text.isEmpty()) {
				text = name.get(counter).getElementsByClass("cuerpoOculto")
						.get(0).text();
				if (text.isEmpty()) {
					text = name.get(counter).getElementsByClass("cuerpoOculto")
							.get(0).text();
				}
			}
		} else {
			text = name.get(counter).getElementsByClass("cuerpo").get(0)
					.getElementsByTag("p").text();
			if (text.isEmpty()) {
				text = name.get(counter).getElementsByClass("cuerpo").get(0)
						.getElementsByAttribute("style").text();
				if (text.isEmpty()) {
					text = name.get(counter).getElementsByClass("cuerpo")
							.get(0).text();
				}
			}
		}

		/*
		 * retrieve the links from the article
		 */
		Elements linkNode = name.get(counter).getElementsByAttribute("href");
		for (Node node : linkNode) {
			Map<String, String> link = new HashMap<String, String>();
			link.put("LINK", node.attr("href").toString());
			link.put("TITLE", node.attr("title").toString());
			Log.d("debug_link", node.attr("href").toString() + "\t"
					+ node.attr("title").toString());
			additionalLink.add(link);
		}

		counter++;

		articleContent.put("content", text);
		articleContent.put("additionalLinks", additionalLink);

		return articleContent;
	}

	private Document doc = null;
	private Elements name = null;

	/**
	 * Here the HTML page related o the RSS cshannel is parsed and split into
	 * pieces of data.
	 * 
	 * @throws IOException
	 */
	private void getAllContent() throws IOException {

		try {
			String subLink = link.split("-/")[0];
			Log.d("debug", "Enlace generado = " + subLink);

			doc = Jsoup.connect(subLink).get();
			name = doc.body().getElementsByClass("duo");
			Log.d("debug_name", "DUO ELEMENTS =" + name.size());

		} catch (IOException e) {
			Log.d("debug_name",
					"HA PETADO AL PILLAR EL DOC, ASI QUE UNA **** ******");
			e.printStackTrace();
			throw e;

		}
	}
}
