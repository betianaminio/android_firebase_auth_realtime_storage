package betianaminio.firebaseauthemailandpassword_public.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import betianaminio.firebaseauthemailandpassword_public.controllers.SignInPictureController;
import betianaminio.firebaseauthemailandpassword_public.firebasemanager.IFirebaseManagerListener;
import betianaminio.firebaseauthemailandpassword_public.R;
import betianaminio.firebaseauthemailandpassword_public.utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignInPicutreActivity extends Activity {

    private static final int IMAGE_GALLERY_REQUEST = 25;
    private static final String DATA_AND_IMAGE_TYPE = "image/*";

    @BindView(R.id.sign_in_picture_wrapper)
    LinearLayout mLayoutPictureWrapper;

    @BindView(R.id.sign_in_picture_creating_account_wrapper)
    LinearLayout mLayoutCreatingAccountWrapper;

    @BindView(R.id.sign_in_form_picture_image)
    CircleImageView mImageProfile;

    @BindView(R.id.sign_in_form_picture_button_create_account)
    Button mButtonCreateAccount;

    @BindView(R.id.sign_in_picture_progress_bar_creating)
    ProgressBar mProgressBar;

    private SignInPictureController mController;

    private Uri mProfilePictureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in_picutre);
        ButterKnife.bind(this);

        this.mController = new SignInPictureController();

        Utils.setBackgroundTintMode(this.getApplicationContext(), this.mImageProfile);

        Utils.setProgressBarStyle(this.getApplicationContext(), this.mProgressBar);
    }

    public void onActivityResult( int request_code, int response_code, Intent data ){

        if( response_code == Activity.RESULT_OK && request_code == IMAGE_GALLERY_REQUEST){
            //Address of the image on SD-card
            Uri imageUri =  data.getData();

            //Declare a stream to read the image from SD-card
            InputStream inputStream;

            try {

                inputStream = getContentResolver().openInputStream(imageUri);

                this.mProfilePictureUri = imageUri;

                //Get a bitmap
                Bitmap image = BitmapFactory.decodeStream(inputStream);

                this.mImageProfile.setImageBitmap(image);

                Utils.removeBackgroundTintMode(this.getApplicationContext(), this.mImageProfile);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, getString(R.string.sign_in_image_gallery_error), Toast.LENGTH_LONG).show();
            }
        }else{
            this.mProfilePictureUri = Uri.parse( "android.resource://" + getPackageName() + "/" + R.drawable.profile_icon );
        }


    }

    public void onSearchFileClicked( View view ){

        Intent photoPickerIntent = new Intent( );
        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType(DATA_AND_IMAGE_TYPE);

        //Invoke activity and wait for result
        startActivityForResult(Intent.createChooser(photoPickerIntent,getString(R.string.sign_in_image_gallery_title)), IMAGE_GALLERY_REQUEST);
    }

    public void onCreateAccountClick( View view ){

        showProgressBar();

        Utils.startScheduledToConnection(new Utils.OnScheduleTaskFinishListener() {
            @Override
            public void onScheduleTaskFinish() {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SignInPicutreActivity.this,getString(R.string.error_message), Toast.LENGTH_LONG).show();

                        hideProgressBar();
                    }
                });

            }
        });



        if ( SignInPicutreActivity.this.mProfilePictureUri == null )
            SignInPicutreActivity.this.mProfilePictureUri = Uri.parse( "android.resource://" + getPackageName() + "/" + R.drawable.profile_icon );;

        this.mController.createAccount(new IFirebaseManagerListener() {
            @Override
            public void onTaskCompleted() {

                Utils.stopScheduledToConnection();

                SignInPicutreActivity.this.mController.uploadProfilePicture(SignInPicutreActivity.this.mProfilePictureUri, new IFirebaseManagerListener() {
                    @Override
                    public void onTaskCompleted() {

                        goToMainActivity();

                    }

                    @Override
                    public void onFailedToCompleteTask(String error) {

                        hideProgressBar();
                        Toast.makeText(SignInPicutreActivity.this, getString(R.string.error_message) + ": " + error, Toast.LENGTH_LONG).show();

                    }
                });


            }

            @Override
            public void onFailedToCompleteTask(String error) {

                Utils.stopScheduledToConnection();

                hideProgressBar();
                Toast.makeText(SignInPicutreActivity.this, getString(R.string.error_message) + ": " + error, Toast.LENGTH_LONG).show();

            }
        });

    }

    private void goToMainActivity(){

        Intent mainActivityIntent = new Intent( SignInPicutreActivity.this, MainActivity.class);
        mainActivityIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivityIntent);

    }

    private void showProgressBar(){

        this.mLayoutPictureWrapper.setVisibility(View.GONE);
        this.mButtonCreateAccount.setVisibility(View.GONE);

        this.mLayoutCreatingAccountWrapper.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){

        this.mLayoutCreatingAccountWrapper.setVisibility(View.GONE);

        this.mLayoutPictureWrapper.setVisibility(View.VISIBLE);
        this.mButtonCreateAccount.setVisibility(View.VISIBLE);
    }
}
