package betianaminio.firebaseauthemailandpassword_public.views;


import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import betianaminio.firebaseauthemailandpassword_public.controllers.LoginController;
import betianaminio.firebaseauthemailandpassword_public.firebasemanager.IFirebaseManagerListener;
import betianaminio.firebaseauthemailandpassword_public.R;
import betianaminio.firebaseauthemailandpassword_public.utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends Activity {

    private final int MAX_PASSWORD_LEN = 8;

    @BindView(R.id.log_in_main_wrapper)
    LinearLayout mLayoutMainWrapper;

    @BindView(R.id.log_in_authenticating_wrapper)
    LinearLayout mLayoutAuthenticating;

    @BindView(R.id.login_form_text_input_email)
    TextInputLayout mTextInputEmail;
    @BindView(R.id.login_form_edit_text_email)
    EditText mEditTextEmail;

    @BindView(R.id.login_form_text_input_password)
    TextInputLayout mTextInputPassword;
    @BindView(R.id.login_form_edit_text_password)
    EditText mEditTextPassword;

    @BindView(R.id.login_form_text_view_sign_in)
    TextView mTextViewSignUp;

    @BindView(R.id.log_in_progress_bar_authenticating)
    ProgressBar mProgressBar;

    private LoginController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

/*
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(MAX_PASSWORD_LEN);
        this.mEditTextPassword.setFilters(FilterArray);
*/
        this.mController = new LoginController();

        this.mTextViewSignUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if ( event.getAction() == MotionEvent.ACTION_DOWN)
                    startActivity( new Intent( LoginActivity.this, SignInActivity.class ));

                return false;
            }
        });


        Utils.setProgressBarStyle(this.getApplicationContext(), this.mProgressBar);

    }

    @Override
    public void onStart() {
        super.onStart();

        this.mController.addListener();
    }

    @Override
    public void onStop() {
        super.onStop();

        this.mController.removeListener();
    }

    private boolean isEmailFieldCompleted(){

        if ( this.mEditTextEmail.length() == 0 ){
            this.mTextInputEmail.setError(getString(R.string.form_error_email));
            return false;
        }else
            this.mTextInputEmail.setError(null);

        return true;
    }

    private boolean isPasswordFieldCompleted(){

        if ( this.mEditTextPassword.length() == 0 ){
            this.mTextInputPassword.setError(getString(R.string.form_error_password));
            return false;
        }else
            this.mTextInputPassword.setError(null);

        return true;
    }

    private boolean isValidEmail( ){

        if ( this.mEditTextEmail.length() == 0 ) {

            this.mTextInputEmail.setError(getString(R.string.form_error_email));
            return false;
        }
        else
            this.mTextInputEmail.setError(null);

        if ( !Patterns.EMAIL_ADDRESS.matcher(this.mEditTextEmail.getText().toString()).matches() ){

            this.mTextInputEmail.setError(getString(R.string.form_error_email_pattern));
            return false;
        }
        else
            this.mTextInputEmail.setError(null);

        return true;
    }

    private boolean checkForm(){

        if ( !isEmailFieldCompleted())
            return false;

        if ( !isPasswordFieldCompleted())
            return false;

        if ( !isValidEmail())
            return false;

        return true;
    }

    private void gotToMainActivity(){

        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        finish();
    }

    public void onLoginClicked( View view ){

        if ( checkForm()){

            showProgressBar();

            Utils.startScheduledToConnection(new Utils.OnScheduleTaskFinishListener() {
                @Override
                public void onScheduleTaskFinish() {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,getString(R.string.error_message), Toast.LENGTH_LONG).show();

                            hideProgressBar();
                        }
                    });

                }
            });

            this.mController.loginUser(this.mEditTextEmail.getText().toString(), this.mEditTextPassword.getText().toString(), new IFirebaseManagerListener() {
                @Override
                public void onTaskCompleted() {

                    LoginActivity.this.mController.getUserData(new IFirebaseManagerListener() {
                        @Override
                        public void onTaskCompleted() {

                            Utils.stopScheduledToConnection();

                            gotToMainActivity();
                        }

                        @Override
                        public void onFailedToCompleteTask(String error) {

                            Utils.stopScheduledToConnection();

                            hideProgressBar();

                            Toast.makeText(LoginActivity.this,getString(R.string.error_message) + ": " + error, Toast.LENGTH_LONG).show();


                        }
                    });

                }

                @Override
                public void onFailedToCompleteTask(String error) {

                    Utils.stopScheduledToConnection();

                    hideProgressBar();

                    Toast.makeText(LoginActivity.this,getString(R.string.error_message) + ": " + error, Toast.LENGTH_LONG).show();

                }
            });


        }
    }

    private void showProgressBar(){

        this.mLayoutMainWrapper.setVisibility(View.GONE);
        this.mLayoutAuthenticating.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){

        this.mLayoutMainWrapper.setVisibility(View.VISIBLE);
        this.mLayoutAuthenticating.setVisibility(View.GONE);
    }

}
