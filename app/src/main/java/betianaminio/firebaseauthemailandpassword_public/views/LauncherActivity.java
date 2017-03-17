package betianaminio.firebaseauthemailandpassword_public.views;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;

import betianaminio.firebaseauthemailandpassword_public.R;
import betianaminio.firebaseauthemailandpassword_public.utils.Utils;

public class LauncherActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int REQUEST_PERMISSION_RESULT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSION_RESULT);
        else{
            Utils.savePermissionToSearchPicture(this.getApplicationContext());
            startApplicationFlow();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults){

        if ( requestCode == REQUEST_PERMISSION_RESULT){

            if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Utils.savePermissionToSearchPicture(this.getApplicationContext());

        }

        startApplicationFlow();
    }

    private void startApplicationFlow(){

        startActivity(new Intent(LauncherActivity.this, LoginActivity.class));
        finish();
    }
}
