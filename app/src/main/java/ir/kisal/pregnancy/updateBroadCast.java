package ir.kisal.pregnancy;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class updateBroadCast extends BroadcastReceiver {
    String Today;
    SharedPreferences perf;
    private int updateNumber;
    private int noteNumber;

    public updateBroadCast() {
        this.Today = BuildConfig.FLAVOR;
    }

    public static boolean isInternetAvailable(Context context) {
        try {
            @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void onReceive(Context context, Intent intent) {
        Log.d("update", "--------onreceive  ");
        this.Today = new SimpleDateFormat("yyyyMMdd").format(new Date()).toString().trim();
        AsyncTask<Void, Void, Void> get_not_back = new updateBroadCastAsyncTask(context);
        if (get_not_back.getStatus() == Status.RUNNING) {
            get_not_back.cancel(true);
        }
        get_not_back.execute(new Void[0]);
    }

    @SuppressLint("WrongConstant")
    private void createNotification(Context c, Notifaction notifacation) {
        Random rand = new Random();
        Log.d("update", "--------create notification  ");
        if (!updateExpire(c, notifacation.updateId)) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(notifacation.updateLink));
            Builder NoteBuilder = new Builder(c);
            NoteBuilder.setContentTitle(notifacation.updateTitle);
            NoteBuilder.setContentText(notifacation.updateContent);
            NoteBuilder.setAutoCancel(true);
            NoteBuilder.setStyle(new BigTextStyle().bigText(notifacation.updateContent));
            NoteBuilder.setTicker(notifacation.updateTitle);
            NoteBuilder.setLargeIcon(BitmapFactory.decodeResource(c.getResources(), R.mipmap.ic_launcher));
            NoteBuilder.setSmallIcon(R.mipmap.ic_launcher);
            NoteBuilder.setContentIntent(PendingIntent.getActivity(c, 0, intent, 0));
            NoteBuilder.setVibrate(new long[]{1000, 500, 1500, 1200, 1000, 800, 1000, 500, 200});
            NoteBuilder.setColor(Color.rgb(rand.nextInt(MotionEventCompat.ACTION_MASK), rand.nextInt(MotionEventCompat.ACTION_MASK), rand.nextInt(MotionEventCompat.ACTION_MASK)));
            ((NotificationManager) c.getSystemService("notification")).notify(notifacation.updateId, NoteBuilder.build());
        }

        if (!noteExpire(c, notifacation.noteId)) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(notifacation.noteLink));
            Builder NoteBuilder = new Builder(c);
            NoteBuilder.setContentTitle(notifacation.noteTitle);
            NoteBuilder.setContentText(notifacation.noteContent);
            NoteBuilder.setAutoCancel(true);
            NoteBuilder.setStyle(new BigTextStyle().bigText(notifacation.noteContent));
            NoteBuilder.setTicker(notifacation.noteTitle);
            NoteBuilder.setLargeIcon(BitmapFactory.decodeResource(c.getResources(), R.mipmap.ic_launcher));
            NoteBuilder.setSmallIcon(R.mipmap.ic_launcher);
            NoteBuilder.setContentIntent(PendingIntent.getActivity(c, 0, intent, 0));
            NoteBuilder.setVibrate(new long[]{1000, 500, 1500, 1200, 1000, 800, 1000, 500, 200});
            NoteBuilder.setColor(Color.rgb(rand.nextInt(MotionEventCompat.ACTION_MASK), rand.nextInt(MotionEventCompat.ACTION_MASK), rand.nextInt(MotionEventCompat.ACTION_MASK)));
            ((NotificationManager) c.getSystemService("notification")).notify(notifacation.noteId, NoteBuilder.build());
        }
    }

    private boolean updateExpire(Context c, int updateid) {
        Log.d("update", "--------updateExpire  ");
        if (updateid <= this.perf.getInt("updateID", updateNumber)) {
            Log.d("update", "--------updateExpire is:  "+ "true");
            return true;
        }
        Editor editor = this.perf.edit();
        editor.putInt("updateID", updateid);
        editor.commit();
        return false;
    }

    private boolean noteExpire(Context c, int noteid) {
        Log.d("note", "--------noteExpire  ");
        if (noteid <= this.perf.getInt("noteID", noteNumber)) {
            Log.d("note", "--------noteExpire is:  "+ "true");
            return true;
        }
        Editor editor = this.perf.edit();
        editor.putInt("noteID", noteid);
        editor.commit();
        return false;
    }

    public Notifaction getNotifacation() throws Exception {
        Notifaction notUpdate = new Notifaction();
        HttpURLConnection con = (HttpURLConnection) new URL("http://simorgh-leather.com/files/AbidarTeam/abidarteam.txt").openConnection();
        //HttpURLConnection con = (HttpURLConnection) new URL("http://rozup.ir/download/1808937/not.txt").openConnection();
        con.setConnectTimeout(5000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String _json = BuildConfig.FLAVOR;
        for (String ln = reader.readLine(); ln != null; ln = reader.readLine()) {
            _json = _json + ln;
        }

        JSONObject json = new JSONObject(_json);

        String otherapp = json.getString("otherapp");

        Log.d("Other", "--------other : " + otherapp);


        JSONArray jsonMainArr = json.getJSONArray("pregnancyapps");

        JSONObject childJSONObject = jsonMainArr.getJSONObject(0);

        notUpdate.updateId = childJSONObject.getInt("currentVersionBazaar");
        notUpdate.updateLink = childJSONObject.getString("bazaarlink");
        notUpdate.updateTitle = childJSONObject.getString("titleNotification");
        notUpdate.updateContent = childJSONObject.getString("changes");

        notUpdate.noteId = childJSONObject.getInt("noteId");
        notUpdate.noteTitle = childJSONObject.getString("noteTile");
        notUpdate.noteContent = childJSONObject.getString("noteContent");
        notUpdate.noteLink = childJSONObject.getString("noteLink");

        return notUpdate;
    }

    /* renamed from: ir.develogerammer.estelam120.internetConnectionBroadCast.1 */
    class updateBroadCastAsyncTask extends AsyncTask<Void, Void, Void> {
        final /* synthetic */ Context val$context;

        updateBroadCastAsyncTask(Context context) {
            this.val$context = context;
        }

        protected Void doInBackground(Void... voids) {

            try {
                PackageInfo pinfo =val$context.getPackageManager().getPackageInfo(val$context.getPackageName(), 0);
                updateNumber = pinfo.versionCode;
                noteNumber=1;

                updateBroadCast.this.perf = PreferenceManager.getDefaultSharedPreferences(this.val$context);
                String last_update = updateBroadCast.this.perf.getString("numberOfDay", BuildConfig.FLAVOR);
                Thread.sleep(5000);
                if (updateBroadCast.this.isInternetAvailable(this.val$context) && last_update != updateBroadCast.this.Today) {
                    updateBroadCast.this.createNotification(this.val$context, updateBroadCast.this.getNotifacation());
                    Editor editor = updateBroadCast.this.perf.edit();
                    editor.putString("numberOfDay", updateBroadCast.this.Today);
                    editor.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class Notifaction {
        String updateContent;
        int updateId;
        String updateLink;
        String updateTitle;

        String noteContent;
        int noteId;
        String noteLink;
        String noteTitle;


        Notifaction() {
        }
    }
}
