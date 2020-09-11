package com.test.anjane.service;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.test.anjane.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RealService extends Service {
    private Thread mainThread;
    private static final String TAG = "RealService";
    public static Intent serviceIntent = null;
    private List<Map<String, Object>> dustList = new ArrayList<>();
    private List<Map<String, Object>> rainList = new ArrayList<>();
    private List<Map<String, Object>> earthquakesList = new ArrayList<>();
    private List<Map<String, Object>> snowList = new ArrayList<>();
    private List<Map<String, Object>> windList = new ArrayList<>();
    private FirebaseFirestore db;

    public RealService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceIntent = intent;
        showToast(getApplication(), "Start Service");

        db = FirebaseFirestore.getInstance();
        db.collection("dust")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dustList.add(i, document.getData());
                                Log.d("dust 가져온 값", dustList.get(i).toString());
                                i++;
                            }
                        } else {
                            Log.d("오류", "Error getting documents: ", task.getException());
                        }
                    }
                });

        db.collection("rain")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                rainList.add(i, document.getData());
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                Log.d("rain 가져온 값", rainList.get(i).toString());
                                i++;
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        db.collection("sonw")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                snowList.add(i, document.getData());
                                Log.d("snow 가져온 값", snowList.get(i).toString());
                                i++;
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        db.collection("earthquakes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                earthquakesList.add(i, document.getData());
                                Log.d("earthquakes 가져온 값", earthquakesList.get(i).toString());
                                i++;
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        db.collection("wind")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                windList.add(i, document.getData());
                                Log.d("wind 가져온 값", windList.get(i).toString());
                                i++;
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        mainThread = new Thread(new Runnable() {

            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("aa hh:mm");
                boolean run = true;
                while (run) {
                    try {
                        Thread.sleep(1000 * 50 * 1); // 1 minute

                        for(int k = 0; k < dustList.size(); k++) {
                            sendNotification(dustList.get(k));
                        }
                        for(int k = 0; k < rainList.size(); k++) {
                            sendNotification(rainList.get(k));
                        }
                        for(int k = 0; k < snowList.size(); k++) {
                            sendNotification(snowList.get(k));
                        }
                        for(int k = 0; k < earthquakesList.size(); k++) {
                            sendNotification(earthquakesList.get(k));
                        }
                        for(int k = 0; k < windList.size(); k++) {
                            sendNotification(windList.get(k));
                        }
                    } catch (InterruptedException e) {
                        run = false;
                        e.printStackTrace();
                    }
                }
            }
        });
        mainThread.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        serviceIntent = null;
        setAlarmTimer();
        Thread.currentThread().interrupt();

        if (mainThread != null) {
            mainThread.interrupt();
            mainThread = null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void showToast(final Application application, final String msg) {
        Handler h = new Handler(application.getMainLooper());
        h.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(application, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void setAlarmTimer() {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 1);
        Intent intent = new Intent(this, AlarmRecever.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0,intent,0);

        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
    }

    private void sendNotification(Map messageBody) {
        Intent intent = new Intent(this, ServiceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int)(System.currentTimeMillis()/1000) /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        Iterator<String> keys = messageBody.keySet().iterator();
        String address = null;
        String value = null;
        while(keys.hasNext()) {
            String key = keys.next();
            switch (key) {
                case "areaname" :
                    address = messageBody.get(key).toString();
                    value = messageBody.get("warnstress").toString();
                    break;
                case "gps" :
                    address = messageBody.get(key).toString();
                    value = messageBody.get("size").toString();
                    break;
                case "address" :
                    address = messageBody.get(key).toString();
                    value = messageBody.get("content").toString();
                    break;
            }
        }

        String channelId = "fcm_default_channel";//getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)//drawable.splash)
                        .setContentTitle(address)
                        .setContentText(value)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,"Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify((int)(System.currentTimeMillis()/1000) /* ID of notification */, notificationBuilder.build());
    }
}
