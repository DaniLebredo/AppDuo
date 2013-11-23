package principalAdapter;

import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.duouniovi011.R;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

	private Context context;
	
	
	public SectionsPagerAdapter(FragmentManager fm, Context c) {
		super(fm);
		this.context = c;
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a DummySectionFragment (defined as a static inner class
		// below) with the page number as its lone argument.
		Fragment fragment = new NormalSectionFragment();
		Bundle args = new Bundle();
		args.putInt(NormalSectionFragment.ARG_SECTION_NUMBER, position + 1);
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
			return context.getString(R.string.title_section1).toUpperCase(l);
		case 1:
			return context.getString(R.string.title_section2).toUpperCase(l);
		case 2:
			return context.getString(R.string.title_section3).toUpperCase(l);
		case 3:
			return context.getString(R.string.title_section4).toUpperCase(l);
		case 4:
			return context.getString(R.string.title_section5).toUpperCase(l);
		case 5:
			return context.getString(R.string.title_section6).toUpperCase(l);
			
		}
		return null;
	}
}
