package betianaminio.firebaseauthemailandpassword_public.firebasemanager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import betianaminio.firebaseauthemailandpassword_public.models.UserLogged;


public class FirebaseRealTimeManager {

    private static final String DATABASE_USERS_NODE         = "users";
    private static final String DATABASE_USER_NODE_START_ID = "user_";

    private static FirebaseRealTimeManager s_instance;

    public static synchronized FirebaseRealTimeManager getInstance(){

        if ( s_instance == null )
            s_instance = new FirebaseRealTimeManager();

        return s_instance;
    }

    private DatabaseReference mUsersNode;

    public void initialize(){

        FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
        this.mUsersNode = dataBase.getReference(DATABASE_USERS_NODE);

    }

    public void insertUser( ){

        String user_id = DATABASE_USER_NODE_START_ID + UserLogged.getInstance().getId();

        this.mUsersNode.child(user_id).setValue(UserLogged.getInstance());
    }

    public void getUserById(final String user_id, final IFirebaseManagerListener listener ){

        if ( this.mUsersNode != null ){

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {

                        JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());

                        if ( jsonObject.has("name") && jsonObject.has("lastName") && jsonObject.has("email") && jsonObject.has("id")) {
                            UserLogged.getInstance().initialize(jsonObject.get("name").toString(), jsonObject.get("lastName").toString(), jsonObject.get("email").toString());
                            UserLogged.getInstance().setId(jsonObject.get("id").toString());
                        }

                        listener.onTaskCompleted();

                    }catch ( JSONException e ){

                        listener.onFailedToCompleteTask( e.getLocalizedMessage());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    listener.onFailedToCompleteTask( databaseError.getMessage());
                }
            };

            this.mUsersNode.child(DATABASE_USER_NODE_START_ID + user_id).addListenerForSingleValueEvent(eventListener);
        }

    }

}
