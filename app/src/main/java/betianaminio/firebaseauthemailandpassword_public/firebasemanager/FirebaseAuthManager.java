package betianaminio.firebaseauthemailandpassword_public.firebasemanager;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;

import betianaminio.firebaseauthemailandpassword_public.models.UserLogged;

public class FirebaseAuthManager {

    private static FirebaseAuthManager s_instance;

    public synchronized static FirebaseAuthManager getInstance() {

        if (s_instance == null)
            s_instance = new FirebaseAuthManager();

        return s_instance;
    }

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public void initialize(){

        this.mAuth = FirebaseAuth.getInstance();
        this.mAuth.signOut();
        this.mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {




            }
        };

    }


    public void addListener(){

        if ( this.mAuthListener != null )
            this.mAuth.addAuthStateListener(this.mAuthListener);
    }

    public void removeListener(){

        if ( this.mAuthListener != null )
            this.mAuth.removeAuthStateListener(this.mAuthListener);
    }

    public void createUserWithEmailAndPassword(String email, String password, final IFirebaseManagerListener listener ){

        this.mAuth.createUserWithEmailAndPassword( email, password ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if ( task.isSuccessful()){

                    UserLogged.getInstance().setId(FirebaseAuthManager.this.mAuth.getCurrentUser().getUid());

                    FirebaseRealTimeManager.getInstance().insertUser();
                    listener.onTaskCompleted();
                }else
                    listener.onFailedToCompleteTask(task.getException().getLocalizedMessage());
            }
        });

    }

    public void logInUserWithEmailAndPassword(String email, String password, final IFirebaseManagerListener listener ){

        this.mAuth.signInWithEmailAndPassword( email, password ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if ( task.isSuccessful()){



                    listener.onTaskCompleted();
                }else
                    listener.onFailedToCompleteTask(task.getException().getLocalizedMessage());

            }
        });

    }

    public void userAlreadyExists(final String email, final OnCheckIfUserExistsListener listener ){

        this.mAuth.fetchProvidersForEmail( email ).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {

                if ( !task.isSuccessful() ) {

                    listener.onFailedToCheck(task.getException().getLocalizedMessage());
                }

                else if ( task.getResult().getProviders() != null ){

                    if ( task.getResult().getProviders().size() > 0)
                        listener.onCheckIfUserExists(true);
                    else
                        listener.onCheckIfUserExists(false);
                }
                else
                    listener.onCheckIfUserExists(false);

            }
        });


    }

    public FirebaseUser getUserAuthenticated(){

        return this.mAuth.getCurrentUser();
    }

    public void signOut(){

        this.mAuth.signOut();
    }

    public interface OnCheckIfUserExistsListener{

        void onCheckIfUserExists( boolean exists );
        void onFailedToCheck( String error );
    }
}
