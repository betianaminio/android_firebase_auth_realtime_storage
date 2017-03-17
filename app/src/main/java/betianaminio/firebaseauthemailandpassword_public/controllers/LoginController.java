package betianaminio.firebaseauthemailandpassword_public.controllers;

import betianaminio.firebaseauthemailandpassword_public.firebasemanager.FirebaseAuthManager;
import betianaminio.firebaseauthemailandpassword_public.firebasemanager.FirebaseRealTimeManager;
import betianaminio.firebaseauthemailandpassword_public.firebasemanager.FirebaseStorageManager;
import betianaminio.firebaseauthemailandpassword_public.firebasemanager.IFirebaseManagerListener;


public class LoginController {

    public LoginController(){

        FirebaseRealTimeManager.getInstance().initialize();
        FirebaseAuthManager.getInstance().initialize();
        FirebaseStorageManager.getInstance().initialize();

    }

    public void addListener(){

        FirebaseAuthManager.getInstance().addListener();
    }

    public void removeListener(){

        FirebaseAuthManager.getInstance().removeListener();
    }

    public void loginUser(String email, String password, IFirebaseManagerListener listener){

        FirebaseAuthManager.getInstance().logInUserWithEmailAndPassword( email, password, listener);

    }

    public void getUserData( IFirebaseManagerListener listener){

        FirebaseRealTimeManager.getInstance().getUserById( FirebaseAuthManager.getInstance().getUserAuthenticated().getUid(), listener);

    }
}
