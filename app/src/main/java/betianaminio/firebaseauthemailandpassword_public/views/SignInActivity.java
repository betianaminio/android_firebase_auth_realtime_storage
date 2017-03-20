package betianaminio.firebaseauthemailandpassword_public.views;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import betianaminio.firebaseauthemailandpassword_public.R;
import betianaminio.firebaseauthemailandpassword_public.controllers.SignInController;
import betianaminio.firebaseauthemailandpassword_public.firebasemanager.FirebaseAuthManager;
import betianaminio.firebaseauthemailandpassword_public.firebasemanager.IFirebaseManagerListener;
import betianaminio.firebaseauthemailandpassword_public.utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends Activity {

    @BindView(R.id.sign_in_form_text_input_name)
    TextInputLayout mTextInputName;
    @BindView(R.id.sign_in_form_edit_name)
    EditText mEditTextName;

    @BindView(R.id.sign_in_form_text_input_lastname)
    TextInputLayout mTextInputLastName;
    @BindView(R.id.sign_in_form_edit_last_name)
    EditText mEditTextLastName;

    @BindView(R.id.sign_in_form_text_input_email)
    TextInputLayout mTextInputEmail;
    @BindView(R.id.sign_in_form_edit_email)
    EditText mEditTextEmail;

    @BindView(R.id.sign_in_form_text_input_password)
    TextInputLayout mTextInputPassword;
    @BindView(R.id.sign_in_form_edit_password)
    EditText mEditTextPassword;

    @BindView(R.id.sign_in_form_text_input_confirm_password)
    TextInputLayout mTextInputConfirmPassword;
    @BindView(R.id.sign_in_form_edit_confirm_password)
    EditText mEditTextConfirmPassword;

    @BindView(R.id.sign_in_form_button_continue)
    Button mButtonContinue;

    @BindView(R.id.sign_in_form_text_view_back)
    TextView mTextViewBack;

    @BindView(R.id.sign_in_form_layout_next_container)
    LinearLayout mLayoutNextContainer;

    @BindView(R.id.sign_in_form_progress_bar)
    ProgressBar mProgressBar;

    private SignInController mController;
    private boolean mGoToChoosePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ButterKnife.bind(this);

        Utils.setProgressBarStyle(this.getApplicationContext(), this.mProgressBar);

        this.mTextViewBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if ( event.getAction() == MotionEvent.ACTION_DOWN)
                    backToLoginActivity();

                return false;
            }
        });

        this.mController = new SignInController();

        if ( Utils.isSearchPicturePermissionGranted(this.getApplicationContext()) ){
            this.mGoToChoosePicture = true;
            this.mButtonContinue.setText(getString(R.string.sign_in_form_next));

        }else {
            this.mGoToChoosePicture = false;
            this.mButtonContinue.setText(getString(R.string.sign_in_form_create_account));
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        hideProgressBar();
    }

    @Override
    public void onBackPressed(){

        backToLoginActivity();
    }

    private void backToLoginActivity(){

        this.mController.deleteUser();
        finish();

    }

    private boolean isNameFieldCompleted(){

        if ( this.mEditTextName.length() == 0 ){

            this.mTextInputName.setError(getString(R.string.sign_in_form_error_name));
            return false;

        }else
            this.mTextInputName.setError(null);

        return true;
    }

    private boolean isLastNameFieldCompleted(){

        if ( this.mEditTextLastName.length() == 0){

            this.mTextInputLastName.setError(getString(R.string.sign_in_form_error_last_name));
            return false;

        }else
            this.mTextInputLastName.setError(null);

        return true;
    }

    private boolean isEmailFieldCompleted(){

        if ( this.mEditTextEmail.length() == 0){

            this.mTextInputEmail.setError(getString(R.string.form_error_email));
            return false;

        }else
            this.mTextInputEmail.setError(null);

        return true;
    }


    private boolean isPasswordFieldCompleted(){

        if ( this.mEditTextPassword.length() == 0){

            this.mTextInputPassword.setError(getString(R.string.form_error_password));
            return false;

        }else
            this.mTextInputPassword.setError(null);

        return true;
    }

    private boolean isValidPassword(){

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";

        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(this.mEditTextPassword.getText().toString());

        if ( !matcher.matches()){
            this.mTextInputPassword.setError(getString(R.string.sign_in_form_error_password_pattern));
            return false;
        }else
            this.mTextInputPassword.setError(null);


        return true;
    }

    private boolean arePasswordFieldsEquals(){

        if ( !isPasswordFieldCompleted())
            return false;

        if ( this.mEditTextPassword.getText().toString().compareToIgnoreCase(this.mEditTextConfirmPassword.getText().toString()) != 0){

            this.mTextInputConfirmPassword.setError(getString(R.string.sign_in_form_error_confirm_password));
            return false;

        }else
            this.mTextInputConfirmPassword.setError(null);

        return true;
    }

    private boolean isValidEmail( CharSequence email ){

        if ( email.length() == 0 )
            return false;

        if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            this.mTextInputEmail.setError(getString(R.string.form_error_email_pattern));
            return false;
        }else
            this.mTextInputEmail.setError(null);

        return true;
    }

    private boolean checkForm(){

        if ( !isNameFieldCompleted())
            return false;

        if ( !isLastNameFieldCompleted())
            return false;

        if ( !isEmailFieldCompleted())
            return false;

        if ( !isValidEmail(this.mEditTextEmail.getText().toString()))
            return false;

        if ( !isPasswordFieldCompleted())
            return false;

        if ( !isValidPassword())
            return false;

        if ( !arePasswordFieldsEquals())
            return false;


        return true;
    }



    public void onContinueButtonClicked( View view ){

        if ( checkForm() ){

            showProgressBar();

            Utils.startScheduledToConnection(new Utils.OnScheduleTaskFinishListener() {
                @Override
                public void onScheduleTaskFinish() {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SignInActivity.this,getString(R.string.error_message), Toast.LENGTH_LONG).show();

                            hideProgressBar();
                        }
                    });

                }
            });


            this.mController.userAlreadyExists(this.mEditTextEmail.getText().toString(), new FirebaseAuthManager.OnCheckIfUserExistsListener() {
                @Override
                public void onCheckIfUserExists(boolean exists) {

                    Utils.stopScheduledToConnection();

                    if ( !exists ){


                        SignInActivity.this.mTextInputEmail.setError( null );

                        final String name      = SignInActivity.this.mEditTextName.getText().toString();
                        final String last_name = SignInActivity.this.mEditTextLastName.getText().toString();
                        final String email     = SignInActivity.this.mEditTextEmail.getText().toString();
                        final String password  = SignInActivity.this.mEditTextPassword.getText().toString();

                        SignInActivity.this.mController.createUser(name, last_name, email, password);

                        if ( SignInActivity.this.mGoToChoosePicture )
                            gotToChoosePicture();
                        else
                            goToMainActivity( email,password );

                    }else {
                        SignInActivity.this.mTextInputEmail.setError(getString(R.string.form_error_email_in_use));
                        hideProgressBar();
                    }


                }

                @Override
                public void onFailedToCheck(String error) {

                    Utils.stopScheduledToConnection();

                    hideProgressBar();

                    Toast.makeText(SignInActivity.this,getString(R.string.error_message) + ": " + error, Toast.LENGTH_LONG).show();

                }
            });

        }
    }

    private void showProgressBar(){

        this.mLayoutNextContainer.setVisibility(View.GONE);
        this.mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){

        this.mLayoutNextContainer.setVisibility(View.VISIBLE);
        this.mProgressBar.setVisibility(View.GONE);
    }

    private void gotToChoosePicture(){

        startActivity(new Intent(SignInActivity.this, SignInPicutreActivity.class));

    }

    private void goToMainActivity( String email, String password ){

        this.mController.createAccount(email, password, new IFirebaseManagerListener() {
            @Override
            public void onTaskCompleted() {

                Intent mainActivityIntent = new Intent( SignInActivity.this, MainActivity.class);
                mainActivityIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivityIntent);
            }

            @Override
            public void onFailedToCompleteTask(String error) {
                hideProgressBar();
                Toast.makeText(SignInActivity.this, getString(R.string.error_message) + ": " + error, Toast.LENGTH_LONG).show();
            }
        });


    }

}
