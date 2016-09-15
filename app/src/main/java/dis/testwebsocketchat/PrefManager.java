package dis.testwebsocketchat;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Lenovo on 15.09.2016.
 */
public class PrefManager {

    private Context mContext;

    private SharedPreferences mPref;

    public PrefManager(Context mContext) {
        this.mContext = mContext;
        this.mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public String getUserName() {
        return mPref.getString("username", "Unknown");
    }

    public void setUserName(String name) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("username", name);
        editor.apply();
    }
}
