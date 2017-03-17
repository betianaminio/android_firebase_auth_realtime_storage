package betianaminio.firebaseauthemailandpassword_public.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;


public class Utils {

    private static final int MAX_TIME_RETRY_JOB = 5;

    private static final String KEY_PERMISSION_GRANTED = "permission_granted";

    private static ScheduledFuture s_ScheduleFutureFailedToRunTask;

    public static boolean isSearchPicturePermissionGranted(Context context){

        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_PERMISSION_GRANTED,false);

    }

    public static void savePermissionToSearchPicture(Context context){

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(KEY_PERMISSION_GRANTED,true);
        editor.apply();

    }

    public static void setProgressBarStyle(Context context, ProgressBar progressBar){

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, android.R.color.white), PorterDuff.Mode.SRC_IN );

    }

    public static void setBackgroundTintMode(Context context, CircleImageView imageView ){

       // if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            imageView.setColorFilter(ContextCompat.getColor(context,android.R.color.white));

    }

    public static void removeBackgroundTintMode(Context context, CircleImageView imageView){

        imageView.setColorFilter(ContextCompat.getColor(context,android.R.color.transparent));
    }

    public static void startScheduledToConnection(final OnScheduleTaskFinishListener listener){

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        s_ScheduleFutureFailedToRunTask = scheduler.schedule(new Runnable() {
            @Override
            public void run() {

                stopScheduledToConnection();

                listener.onScheduleTaskFinish();

            }
        },Utils.MAX_TIME_RETRY_JOB, TimeUnit.SECONDS);


    }

    public static void stopScheduledToConnection(){

        if ( s_ScheduleFutureFailedToRunTask != null){

            s_ScheduleFutureFailedToRunTask.cancel(true);
            s_ScheduleFutureFailedToRunTask = null;

        }

    }

    public interface OnScheduleTaskFinishListener{
        void onScheduleTaskFinish();
    }


}
