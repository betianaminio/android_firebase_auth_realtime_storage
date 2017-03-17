package betianaminio.firebaseauthemailandpassword_public.controllers;

import android.net.Uri;

import betianaminio.firebaseauthemailandpassword_public.firebasemanager.FirebaseAuthManager;
import betianaminio.firebaseauthemailandpassword_public.firebasemanager.FirebaseStorageManager;
import betianaminio.firebaseauthemailandpassword_public.firebasemanager.IFirebaseManagerListener;
import betianaminio.firebaseauthemailandpassword_public.models.UserLogged;


public class SignInPictureController {

    public SignInPictureController(){

    }

    public void createAccount( IFirebaseManagerListener listener){

        FirebaseAuthManager.getInstance().createUserWithEmailAndPassword(UserLogged.getInstance().getEmail(),UserLogged.getInstance().getPassword(),listener);

    }

    public void uploadProfilePicture( final Uri profile_image_path, IFirebaseManagerListener listener){

        FirebaseStorageManager.getInstance().uploadProfilePicture(profile_image_path,listener);

    }
}
