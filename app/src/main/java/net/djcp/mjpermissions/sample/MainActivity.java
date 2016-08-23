package net.djcp.mjpermissions.sample;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import net.djcp.mjpermissions.mjPermissions;
import net.djcp.mjpermissions.annotations.OnPermissionDenied;
import net.djcp.mjpermissions.annotations.OnPermissionGranted;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mjPermissions.with(this).request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @OnPermissionGranted
    public void onPermissionGranted() {
        Log.d(TAG, "onPermissionGranted");
    }

    @OnPermissionDenied
    public void onPermissionDenied() {
        Log.d(TAG, "onPermissionDenied");
    }

    @OnPermissionGranted(Manifest.permission.CAMERA)
    public void onCameraGranted() {
        Log.d(TAG, "onCameraGranted");
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void onCameraDenied() {
        Log.d(TAG, "onCameraDenied");
    }

    @OnPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void onStrorageGranted() {
        Log.d(TAG, "onStrorageGranted");
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void onStrorageDenied() {
        Log.d(TAG, "onStrorageDenied");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
