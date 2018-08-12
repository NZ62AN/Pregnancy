package ir.kisal.pregnancy;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Nezam on 8/11/2018.
 */

public class FontManager {

    public static final String ROOT = "font/",
            FONTAWESOME = ROOT + "iran.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

}