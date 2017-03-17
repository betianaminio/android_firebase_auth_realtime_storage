package betianaminio.firebaseauthemailandpassword_public.controllers;


import betianaminio.firebaseauthemailandpassword_public.firebasemanager.FirebaseAuthManager;
import betianaminio.firebaseauthemailandpassword_public.firebasemanager.FirebaseStorageManager;
import betianaminio.firebaseauthemailandpassword_public.models.UserLogged;


public class MainController {

    public void getImageProfile(FirebaseStorageManager.OnGetProfilePictureUrlListener listener){

        FirebaseStorageManager.getInstance().getProfilePictureUrl( listener );

    }

    public String getUserLoggedName() {

        return UserLogged.getInstance().getName() + " " + UserLogged.getInstance().getLastName();
    }

    public void logOut(){

        UserLogged.getInstance().shutDown();

        FirebaseAuthManager.getInstance().signOut();

    }
}
