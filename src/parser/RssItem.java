package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Value class in charge of holding the different elements that form an Rss
 * entry. It provides several methods to retrieve its information.
 * 
 * @author Daniel
 * 
 */
public class RssItem {

	private final int id;
	private final String title;
	private final String link;
	private final String creator;
	private final String content;
	private final List<Map<String, String>> additionalLink;
	private final String date;

	/**
	 * This constructor requires the following parameters to be passed. All off
	 * them are mandatory, otherwise it won't work.
	 * 
	 * @param id
	 *            Id of the article
	 * 
	 * @param title
	 *            Title of the RSS Entry
	 * @param link
	 *            Link to the original source of the RSS, i.e. the link in the
	 *            dUO RSS.
	 */
	public RssItem(int id, String title, String link) {
		this.id = id;
		this.title = title;
		this.link = link;
		this.creator = "DUMMY CREATOR";
		this.content = "DUMMY CONTENT DUMMY CONTENT DUMMY CONTENT DUMMY CONTENT DUMMY CONTENT DUMMY CONTENT DUMMY CONTENT DUMMY CONTENT DUMMY CONTENT DUMMY CONTENT DUMMY CONTENT ";
		this.additionalLink = new ArrayList<Map<String, String>>();
		this.additionalLink.add(new HashMap<String, String>());
		this.additionalLink.get(0).put("link", "http://www.dummy-link.com");
		this.date = "DUMMYTDUMMYZ";
	}

	/**
	 * This constructor requires the following parameters to be passed. All off
	 * them are mandatory, otherwise it won't work.
	 * 
	 * @param id
	 *            Id of the article
	 * @param title
	 *            Title of the RSS Entry
	 * @param link
	 *            Link to the original source of the RSS, i.e. the link in the
	 *            dUO RSS.
	 * @param creator
	 *            Writer of the entry. Not at all necessary, but may be required
	 *            in the future.
	 * @param date
	 *            The date the article was written.
	 * @param articleContent
	 *            This is a Map<String, String> generated with the
	 *            {@link UnioviRssParser #getArticleContent(int)
	 *            getArticleContent} method.
	 * @param additionalLink
	 *            This is the 
	 */
	public RssItem(int id, String title, String link, String creator,
			String date, String articleContent,
			List<Map<String, String>> additionalLink) {
		this.id = id;
		this.title = title;
		this.link = link;
		this.creator = creator;
		this.date = date;
		this.content = articleContent;
		this.additionalLink = additionalLink;

	}

	/**
	 * Id of the article within the dUO. It is unique for each article and the
	 * higher, the more recent.
	 * 
	 * @return The id of the article.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Title of the RSS Entry
	 * 
	 * @return A String containing the title of the article
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Link to the original source of the RSS, i.e. the link in the dUO RSS.
	 * 
	 * @return A String containing the link to the source of the Rss entry
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Author of the article in the Rss. It is usually the same person, so I
	 * don't encourage its use.
	 * 
	 * @return A String containing the author of the article
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * Content of the article, i.e. the text with the information about news,
	 * unformatted (I think).
	 * 
	 * @return A String containing the text from within the article
	 */
	public String getContent() {
		return content;
	}

	/**
	 * The related links provided by the Rss. It can be a link to another page
	 * or .pdf files.It is a valid URL of the form http://the.web.page/etc
	 * 
	 * @return A list of maps containing the links to any additional resource
	 *         and its titles.
	 */
	public List<Map<String, String>> getAdditionalLink() {
		return additionalLink;
	}

	/**
	 * The date when the article was published, in the form YYYY-MM-DD.
	 * 
	 * @return A String containing the date when the entry was published.
	 */
	public String getDate() {
		return date.split("T")[0];
	}
}
