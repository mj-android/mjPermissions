package net.djcp.mjpermissions;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import java.util.*;

public class mjPermissionsActivity extends AppCompatActivity {
    private static final String TAG = mjPermissionsActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST = 100;
    private String[] mPermissions;
    private int mRequestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        init(savedInstanceState);
        checkPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST) {
            broadcast(permissions, grantResults);
            finish();
        }
    }

    private void init(Bundle state) {
        if (state != null) {
            mPermissions = state.getStringArray(Constants.PERMISSIONS);
            mRequestId = state.getInt(Constants.REQUEST_ID);
        } else {
            Intent intent = getIntent();
            mPermissions = intent.getStringArrayExtra(Constants.PERMISSIONS);
            mRequestId = intent.getExtras().getInt(Constants.REQUEST_ID, 0);
        }
    }

    private void checkPermissions() {
        List<String> needPermissions = new ArrayList<>();
        List<String> showRationalsFor = new ArrayList<>();

        for (String permission : mPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                needPermissions.add(permission);
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    showRationalsFor.add(permission);
                }
            }
        }

        if (showRationalsFor.size() > 0) {
            ActivityCompat.requestPermissions(this, showRationalsFor.toArray(new String[showRationalsFor.size()]), PERMISSION_REQUEST);
        } else if (needPermissions.size() > 0) {
            ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), PERMISSION_REQUEST);
        } else {
            int[] result = new int[mPermissions.length];
            Arrays.fill(result, PackageManager.PERMISSION_GRANTED);
            broadcast(mPermissions, result);
            finish();
        }
    }

    private void broadcast(String[] permissions, int[] grantResults) {
        if (grantResults.length > 0) {
            Intent intent = new Intent();
            intent.setAction("net.djcp.mjpermissions.PERMISSION_RESULT_INTENT");
            intent.putExtra(Constants.PERMISSIONS, permissions);
            intent.putExtra(Constants.GRANT_RESULTS, grantResults);
            intent.putExtra(Constants.REQUEST_ID, mRequestId);
            sendBroadcast(intent);
        }
    }
}
