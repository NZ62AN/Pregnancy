package ir.kisal.pregnancy;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * Created by Android on 2/10/2017.
 */
public class MyApp extends Application {
    public static SharedPreferences pref;
    public static int noteId=6;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyApp", "oncreate");

        pref = getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("Font/iran.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }
}
