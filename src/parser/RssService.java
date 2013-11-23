package parser;

import java.io.Serializable;
import java.util.List;

import parser.RssProvider.Section;


import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * This class provides the specific methods required to retrieve an Rss channel
 * using {@link IntentService}. The class registers itself as service under the
 * name "RssService", so it must be specified in the AndroidManifest, as * <b>
 * <"application"> ... <"service android:name=".RssService"" /> <"/application">
 * </b> and <b> the INTERNET permission. </b> <i>Remove the quotes</i>
 * 
 * @author Daniel
 * 
 */
public class RssService extends IntentService {

	public static final String ITEMS = "items";
	public static final String RECEIVER = "receiver";
	public static final String SECTION = "SECTION";

	public RssService() {
		super("UnioviRss");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Section section = Section.ANUNCIOS;
		if (intent.getExtras() != null) {
			Log.d("debug_section", "Hay extras");
			if (intent.getExtras().containsKey(SECTION)) {
				Log.d("debug_section", "\t que tienen clave SECTION");
				section = Section.values()[intent.getExtras()
						.getInt(SECTION)];
				Log.d("debug_section", "Seccion modificada!");
			}
		}

		Log.d("debug_section",
				"Recibida solicitud para SECTION = " + section.ordinal());
		// TODO work out this section a bit more
		// maybe returning directly if no SECTION specified
		Log.d("debug", "Dentro del Provider");
		RssProvider rss = new RssProviderImpl();
		List<RssItem> lista = rss.getRss(section);
		Bundle bundle = new Bundle();
		bundle.putSerializable(ITEMS, (Serializable) lista);
		ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);
		receiver.send(0, bundle);
	}
}
