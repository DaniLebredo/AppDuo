package intentManager;

import android.app.Activity;

import android.content.Intent;
import android.net.Uri;

public class IntentsExternos 
{
	public static void navegateToUrl(Activity activity, String url)
	{
		 Intent i = new Intent(Intent.ACTION_VIEW);
		 i.setData(Uri.parse(url));
		 activity.startActivity(i);
	}
}
