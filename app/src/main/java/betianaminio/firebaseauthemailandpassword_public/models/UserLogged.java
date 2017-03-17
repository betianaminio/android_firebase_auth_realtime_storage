package betianaminio.firebaseauthemailandpassword_public.models;


public class UserLogged {

    private static UserLogged s_instance;

    public static synchronized UserLogged getInstance(){

        if ( s_instance == null )
            s_instance = new UserLogged();

        return s_instance;
    }

    private String mId;
    private String mName;
    private String mLastName;
    private String mEmail;
    private String mPassword;
    private String mProfileImageUrl;

    private UserLogged(){

        this.mName            = null;
        this.mLastName        = null;
        this.mEmail           = null;
        this.mPassword        = null;
        this.mProfileImageUrl = null;
    }

    public void shutDown(){

        s_instance = null;
    }

    public void initialize( String name, String last_name, String email ){

        this.mName     = name;
        this.mLastName = last_name;
        this.mEmail    = email;
    }

    public void initialize( String name, String last_name, String email, String password ){

        this.mName     = name;
        this.mLastName = last_name;
        this.mEmail    = email;
        this.mPassword = password;
    }

    public void setId( String id ){ this.mId = id; }
    public void setName( String name ){ this.mName = name; }
    public void setmLastName( String lastName ){ this.mLastName = lastName; }
    public void setEmail( String email ){ this.mEmail = email; }
    public void setPassword( String password ){ this.mPassword = password; }
    public void setProfileImageUrl( String profile_url ){ this.mProfileImageUrl = profile_url; }

    public String getId(){ return this.mId; }
    public String getName(){ return this.mName; }
    public String getLastName() { return this.mLastName; }
    public String getEmail() { return this.mEmail; }
    public String getPassword() { return this.mPassword; }
    public String getProfileImageUrl(){ return this.mProfileImageUrl; }

}
