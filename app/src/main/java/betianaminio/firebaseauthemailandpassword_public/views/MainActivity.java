package betianaminio.firebaseauthemailandpassword_public.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import betianaminio.firebaseauthemailandpassword_public.controllers.MainController;
import betianaminio.firebaseauthemailandpassword_public.R;
import betianaminio.firebaseauthemailandpassword_public.firebasemanager.FirebaseStorageManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends Activity {

    @BindView(R.id.main_image_user)
    CircleImageView mImageProfile;

    @BindView(R.id.main_text_view_user)
    TextView mTextViewUserName;

    private MainController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        this.mController = new MainController();

        this.mTextViewUserName.setText(MainActivity.this.mController.getUserLoggedName());

        Glide.clear(this.mImageProfile);

        this.mController.getImageProfile(new FirebaseStorageManager.OnGetProfilePictureUrlListener() {
            @Override
            public void onGetProfileUrl(String url) {

                Glide.with(MainActivity.this).load(url).centerCrop()
                        .crossFade().into(MainActivity.this.mImageProfile);

                  }

            @Override
            public void onFailedToGetProfileUrl(String error) {

                MainActivity.this.mImageProfile.setBackgroundResource(R.drawable.profile_icon);
            }
        });


    }

    public void onLogOutClicked( View view ){

        this.mController.logOut();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();
    }
}
