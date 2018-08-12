package ir.kisal.pregnancy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Prefs extends PreferenceActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
		
		int mday=prefs.getInt("myDay", 1);
		int mMonth=prefs.getInt("myMonth", 1);
		int mYear=prefs.getInt("myYear", 1393);
		
	}

}
