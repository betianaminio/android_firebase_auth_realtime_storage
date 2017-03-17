package betianaminio.firebaseauthemailandpassword_public.firebasemanager;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import betianaminio.firebaseauthemailandpassword_public.models.UserLogged;


public class FirebaseStorageManager {

    private static final String FIREBASE_STORAGE_BUCKET = "gs://auth-with-email-and-password.appspot.com";
    private static final String FIREBASE_STORAGE_PROFILE_IMAGE_PATH = "/images/profile.jpg";

    private static FirebaseStorageManager s_instance;

    public synchronized static FirebaseStorageManager getInstance(){

        if ( s_instance == null )
            s_instance = new FirebaseStorageManager();

        return s_instance;
    }

    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;

    public void initialize(){

        this.mStorage = FirebaseStorage.getInstance();

        this.mStorageReference = this.mStorage.getReferenceFromUrl(FIREBASE_STORAGE_BUCKET);


    }

    public void uploadProfilePicture(@NonNull  Uri path, final IFirebaseManagerListener listener ){

        StorageReference profileImagesRef = this.mStorageReference.child(UserLogged.getInstance().getId() + FIREBASE_STORAGE_PROFILE_IMAGE_PATH);

        UploadTask uploadTask = profileImagesRef.putFile(path);
        uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    listener.onFailedToCompleteTask(exception.getLocalizedMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    UserLogged.getInstance().setProfileImageUrl( downloadUrl.toString() );


                    listener.onTaskCompleted();
                }
        });
    }

    public void getProfilePictureUrl(final OnGetProfilePictureUrlListener listener ){

        StorageReference profileImagesRef = this.mStorageReference.child(UserLogged.getInstance().getId() + FIREBASE_STORAGE_PROFILE_IMAGE_PATH);

        profileImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                listener.onGetProfileUrl( uri.toString() );

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                listener.onFailedToGetProfileUrl( e.getLocalizedMessage());
            }
        });

    }

    public interface OnGetProfilePictureUrlListener{
        void onGetProfileUrl( String url );
        void onFailedToGetProfileUrl( String error );
    }
}
