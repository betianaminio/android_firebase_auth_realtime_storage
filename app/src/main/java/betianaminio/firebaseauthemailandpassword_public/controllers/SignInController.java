package betianaminio.firebaseauthemailandpassword_public.controllers;

import betianaminio.firebaseauthemailandpassword_public.firebasemanager.FirebaseAuthManager;
import betianaminio.firebaseauthemailandpassword_public.firebasemanager.IFirebaseManagerListener;
import betianaminio.firebaseauthemailandpassword_public.models.UserLogged;


public class SignInController {


    public void createUser( String name, String last_name, String email, String password){

        UserLogged.getInstance().initialize(name,last_name,email,password);

    }

    public void deleteUser(){

        if ( UserLogged.getInstance() != null )
            UserLogged.getInstance().shutDown();

    }

    public void createAccount(String name, String password, IFirebaseManagerListener listener ){

        FirebaseAuthManager.getInstance().createUserWithEmailAndPassword( name, password, listener);

    }

    public void userAlreadyExists(String email, FirebaseAuthManager.OnCheckIfUserExistsListener listener){

        FirebaseAuthManager.getInstance().userAlreadyExists(email, listener);

    }
}
