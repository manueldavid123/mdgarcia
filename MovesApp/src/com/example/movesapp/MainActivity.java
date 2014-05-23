package com.example.movesapp;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * Demonstrates app-to-app and browser-app-browser integration with Moves API authorize flow.
 */
public class MainActivity extends Activity {

    private static final String TAG = "AppsDemo";

    private static final String CLIENT_ID = "krMl6FcP0kn1Hf00ElX0ai1WIzl65fEf";

    private static final String REDIRECT_URI = "http://www.juegosparanenes.es";

    private static final String CLIENT_SECRET = "7CBa4CNLhEfXqWnykygJ1M0ka7172PEAwwAH_1ulvGg33Zjp_7HlfqfTzNO3RmUL";
    
    private static final int REQUEST_AUTHORIZE = 1;
    
    private static String CODE="";
    
    

    private CheckBox mLocation;

    private CheckBox mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.authorizeInApp).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                doRequestAuthInApp();
            }
        });
        
        mLocation = (CheckBox) findViewById(R.id.location);
        mActivity = (CheckBox) findViewById(R.id.activity);
    }
    public void setAuth(String code){
    	
    	MainActivity.CODE = code;
    }
    /**
     * App-to-app. Creates an intent with data uri starting moves://app/authorize/xxx (for more
     * details, see documentation link below) to be handled by Moves app. When Moves receives this
     * Intent it opens up a dialog asking for user to accept the requested permission for your app.
     * The result of this user interaction is delivered to 
     * {@link #onActivityResult(int, int, android.content.Intent) }
     *
     * @see https://dev.moves-app.com/docs/api
     */
    private void doRequestAuthInApp() {

        Uri uri = createAuthUri("moves", "app", "/authorize").build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivityForResult(intent, REQUEST_AUTHORIZE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Moves app not installed", Toast.LENGTH_SHORT).show();
        }

    } 


    /**
     * Handle the result from Moves authorization flow. The result is delivered as an uri documented
     * on the developer docs (see link below).
     *
     * @see https://dev.moves-app.com/docs/api
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_AUTHORIZE:
                Uri resultUri = data.getData();
                Toast.makeText(this,
                        (resultCode == RESULT_OK ? "Authorized: " : "Failed: ")
                        + resultUri, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Helper method for building a valid Moves authorize uri.
     */
    private Uri.Builder createAuthUri(String scheme, String authority, String path) {
        return new Uri.Builder()
                .scheme(scheme)
                .authority(authority)
                .path(path)
                .appendQueryParameter("client_id", CLIENT_ID)
                .appendQueryParameter("redirect_uri", REDIRECT_URI)
                .appendQueryParameter("scope", getSelectedScopes())
                .appendQueryParameter("state", String.valueOf(SystemClock.uptimeMillis()));
    }
    private Uri.Builder createAccessUri(String scheme, String authority, String path) {
        return new Uri.Builder()
                .scheme(scheme)
                .authority(authority)
                .path(path)
                .appendQueryParameter("grant_type", "authorization_code")
                .appendQueryParameter("code",CODE )
                .appendQueryParameter("client_id", CLIENT_ID)
                .appendQueryParameter("client_secret", CLIENT_SECRET)
                .appendQueryParameter("redirect_uri", REDIRECT_URI);
    }

    private String getSelectedScopes() {
        StringBuilder sb = new StringBuilder();
        if (mLocation.isChecked()) {
            sb.append("location");
        }
        if (mActivity.isChecked()) {
            sb.append(" activity");
        }
        return sb.toString().trim();
    }

}
