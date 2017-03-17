package betianaminio.firebaseauthemailandpassword_public.firebasemanager;


public interface IFirebaseManagerListener {

    void onTaskCompleted();
    void onFailedToCompleteTask( String error );
}
