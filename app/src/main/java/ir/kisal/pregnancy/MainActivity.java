package ir.kisal.pregnancy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ir.kisal.pregnancy.DatePicker.DateWatcher;
import ir.kisal.utils.PersianCalendar;

import java.io.File;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int NOTIFICATION_ID = 1;
    DatePicker dpStart;
    DatePicker dpEnd;
    Calendar cal;
    Calendar calini;
    public static String fonts = "iran.ttf";
    private Typeface type, type2;
    TextView tvWeek;
    long t1, t2;
    int day, r, m;

    private Calendar cal2;
    private PersianCalendar pcStart, pcEnd;
    private Typeface face;
    private int firstPost;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ProgressBar progressbar;


    private Resources res;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    Context context;

    private static final String SHOWCASE_ID = "simple example";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = MyApp.pref;
        context = MainActivity.this;

        this.type = Typeface.createFromAsset(getAssets(), "font/IranSansMedium.ttf");
        this.type2 = Typeface.createFromAsset(getAssets(), "font/IranSansLight.ttf");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        mPlanetTitles = getResources().getStringArray(R.array.dokmeha);

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[5];
        drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_share, mPlanetTitles[0]);
        drawerItem[1] = new ObjectDrawerItem(R.drawable.other_app, mPlanetTitles[1]);
        drawerItem[2] = new ObjectDrawerItem(R.drawable.contact, mPlanetTitles[2]);
        drawerItem[3] = new ObjectDrawerItem(R.drawable.star, mPlanetTitles[3]);
        drawerItem[4] = new ObjectDrawerItem(R.drawable.exit, mPlanetTitles[4]);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerList = (ListView) findViewById(R.id.list_drawer);
        DrawerListAdapter drawerListAdapter = new DrawerListAdapter(this, R.layout.listview_item_row, drawerItem);
        mDrawerList.setAdapter(drawerListAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        ImageButton btnToggle = (ImageButton) findViewById(R.id.btn_toggle);
        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                    mDrawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });
        tvWeek = (TextView) findViewById(R.id.tvConutWeek);
        tvWeek.setText(null);
        cal = Calendar.getInstance();
        calini = Calendar.getInstance();
        dpStart = (DatePicker) findViewById(R.id.datePickerStart);

        try {
            dpStart.setStartYear(1279);
            dpStart.setEndYear(1632);
        } catch (Exception e) {
            Log.e("", e.toString());
        }
        DatePicker.DateWatcher dc1 = new DateWatcher() {

            public void onDateChanged(Calendar c) {
                // TODO Auto-generated method stub

                cal.set(dpStart.getGregorianYear(), dpStart.getGregorianMonth() - 1, dpStart.getGregorianDayOfMonth());
                t1 = cal.getTimeInMillis();

                cal.set(dpEnd.getGregorianYear(), dpEnd.getGregorianMonth() - 1, dpEnd.getGregorianDayOfMonth());
                t2 = cal.getTimeInMillis();
                dpStart.setTextLable(Farsi.Convert("تاریخ شروع بارداري"));
                dpEnd.setTextLable("تاریخ يوم جاري");
                if (t2 < t1) {
                    Typeface face = Typeface.createFromAsset(getAssets(), "font/" + fonts + "");
                    Farsi f = new Farsi();

                    //tvWeek.setTypeface(face);

                    tvWeek.setText(Farsi.Convert(getResources().getString(R.string.title_activity_main)));

                    return;
                }
                day = Integer.parseInt(String.valueOf((t2 - t1) / (24 * 60 * 60 * 1000)));
                tvWeek.setText(String.valueOf(day + 1));

                r = (day + 1) / 7;

                m = (day + 1) - (7 * r);

                if (r > 0)
                    tvWeek.setText(String.valueOf(r) + "  هفته و " + String.valueOf(m) + "روز");
                else if (r == 0)
                    tvWeek.setText(String.valueOf(m) + "روز");

            }
        };
        dpStart.setDateChangedListener(dc1);

        //dpStart.setCurrentDate();
        dpEnd = (DatePicker) findViewById(R.id.datePickerEnd);
        try {
            dpEnd.setStartYear(1279);
            dpEnd.setEndYear(1632);
        } catch (Exception e) {
            Log.e("", e.toString());
        }
        DateWatcher dc2 = new DateWatcher() {

            public void onDateChanged(Calendar c) {
                // TODO Auto-generated method stub

                cal.set(dpStart.getGregorianYear(), dpStart.getGregorianMonth() - 1, dpStart.getGregorianDayOfMonth());
                t1 = cal.getTimeInMillis();

                cal.set(dpEnd.getGregorianYear(), dpEnd.getGregorianMonth() - 1, dpEnd.getGregorianDayOfMonth());
                t2 = cal.getTimeInMillis();
                dpStart.setTextLable(Farsi.Convert("تاریخ شروع بارداري"));
                dpEnd.setTextLable("تاریخ يوم جاري");
                if (t2 < t1) {
                    tvWeek.setText("لطفا تاریخ شروع کمتر از تاریخ پایان باشد");
                    return;
                }
                day = Integer.parseInt(String.valueOf((t2 - t1) / (24 * 60 * 60 * 1000)));
                tvWeek.setText(String.valueOf(day + 1));

                r = (day + 1) / 7;

                m = (day + 1) - (7 * r);

                if (r > 0)
                    tvWeek.setText(String.valueOf(r) + "  هفته و " + String.valueOf(m) + "روز");
                else if (r == 0)
                    tvWeek.setText(String.valueOf(m) + "روز");

            }
        };
        dpEnd.setDateChangedListener(dc2);

        // dpEnd.setCurrentDate();
        dpStart.setTextLable(Farsi.Convert("تاریخ شروع بارداري"));
        dpEnd.setTextLable("تاریخ يوم جاري");

        dpStart.performClick();
        loadPrefernce();

    }


    private int loadPrefernce() {
        // TODO Auto-generated method stub
        SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();


        int y1 = shared.getInt("myYear", 1397);
        int d1 = shared.getInt("myDay", 1);
        int m1 = shared.getInt("myMonth", 1);
        dpStart.setDate(y1, m1, d1);

        int key = 2;
        String k;
        switch (key) {
            case 3:
                k = "3";
                break;
            case 2:
                k = "2";
                break;
            default:
                k = "0";
        }

        return key;

    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putInt("myYear", dpStart.getPersionYear());
        editor.putInt("myDay", dpStart.getPersianDayOfMonth());
        editor.putInt("myMonth", dpStart.getPersianMonth());
        editor.commit();
        //Toast.makeText(getApplicationContext(), "Your Message!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mywebsite:
                openKisalDotIr();
                break;
            case R.id.share:
                shareApp();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void openKisalDotIr() {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://kisal.ir"));
        startActivity(i);
    }


    private void shareApp() {
        ApplicationInfo app = getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;

        Intent intent = new Intent(Intent.ACTION_SEND);

        // MIME of .apk is "application/vnd.android.package-archive".
        // but Bluetooth does not accept this. Let's use "*/*" instead.
        intent.setType("application/vnd.android.package-archive");


        // Append file and send Intent
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
        startActivity(Intent.createChooser(intent, "ارسال از طريق"));
    }

    @Override
    public void onBackPressed() {
        //Display alert message when back button has been pressed
        //Log.d(TAG, "onBackPressed: " + this.toString());
        //backButtonHandler();
        return;
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("خروج از برنامه");
        // Setting Dialog Message
        alertDialog.setMessage("آيا مايل هستيد قبل از خروج از برنامه نظر دهيد؟");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.dialog_icon);
        // Setting Positive "Yes" Button
        alertDialog.setNeutralButton("خروج",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("بلي",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       // if (checkBazaar) {
                            //rateApp();
                       // } else {
                            Toast.makeText(MainActivity.this, "لطفا ابتدا برنامه کافه بازار را نصب نمایید", Toast.LENGTH_SHORT).show();
                        //}
                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("انصراف",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });
        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    //منو
    private void selectItem(int position) {
        switch (position) {
            case 0:
                shareApp();
                break;
            case 1:

                    anotherApp();


            case 4:
                backButtonHandler();
                break;

            case 2:
                try {
                    Intent i2 = new Intent(Intent.ACTION_VIEW);
                    i2.setData(Uri.parse("http://kisal.ir"));
                    MainActivity.this.startActivity(i2);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "لطفاً از ارتباط اینترنت در دستگاه خود مطمئن شوید", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void anotherApp() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("bazaar://collection?slug=by_author&aid=" + "847228728064"));

        startActivity(intent);
    }

}
