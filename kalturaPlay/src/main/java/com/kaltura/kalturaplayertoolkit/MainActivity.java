package com.kaltura.kalturaplayertoolkit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.kaltura.playersdk.KPPlayerConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import Fragments.FullscreenFragment;
import Fragments.LoginFragment;
import Fragments.PlayerFragment;
import Utilities.FragmentUtilities;


public class MainActivity extends Activity implements LoginFragment.OnFragmentInteractionListener, PlayerFragment.OnFragmentInteractionListener, FullscreenFragment.OnFragmentInteractionListener{
	public static String TAG = MainActivity.class.getSimpleName();

    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        loadFragment();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        loadFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Intent intent = getIntent();
        Fragment fragment = new FullscreenFragment();
        Bundle extras = intent.getExtras();
        if (extras == null) {
            extras = new Bundle();
        }
       /* KPPlayerConfig config = new  KPPlayerConfig("http://cdnapi.kaltura.com/html5.kaltura/mwEmbed/mwEmbedFrame.php", "15115621", "kaltura_player_1428841967");
        config.setWid("1988382");
        //config.setCacheSt("1428841967");
        config.setEntryId("0_nt4qzwvs");
        config.setDebug(true);
        config.addConfig("nativeCallout", "{\"plugin\":true}");
        config.setForceMobileHTML5(true);
        extras.putString(getString(R.string.prop_iframe_url), "https://cdnapisec.kaltura.com/p/243342/sp/24334200/embedIframeJs/uiconf_id/12905712/partner_id/243342?iframeembed=true&entry_id=1_sf5ovm7u");*/
        FragmentUtilities.loadFragment(false, fragment, extras, getFragmentManager());
    }

    private void loadFragment(){
        Intent intent = getIntent();
        Fragment fragment = new LoginFragment();
        Bundle extras = intent.getExtras();

        if (Intent.ACTION_VIEW.equals( intent.getAction())) {
            Uri uri = intent.getData();
            String[] params = null;
            try {
                params = URLDecoder.decode(uri.toString(), "UTF-8").split(":=");
            } catch (UnsupportedEncodingException e) {
                Log.w(TAG, "couldn't decode/split intent url");
                e.printStackTrace();
            }
            if (params != null && params.length > 1) {
                String iframeUrl = params[1];

                extras.putString(getString(R.string.prop_iframe_url), iframeUrl);
                fragment = new FullscreenFragment();

            } else {
                Log.w(TAG, "didn't load iframe, invalid iframeUrl parameter was passed");
            }

        }

        FragmentUtilities.loadFragment(false, fragment, extras, getFragmentManager());
    }
}
