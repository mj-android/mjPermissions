package net.djcp.mjpermissions;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v4.app.Fragment;
import android.util.Log;
import net.djcp.mjpermissions.annotations.OnPermissionDenied;
import net.djcp.mjpermissions.annotations.OnPermissionGranted;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class mjPermissions {
    private static final String TAG = mjPermissions.class.getSimpleName();
    private static Context mContext;
    private static int mId;

    private mjPermissions() {
        Random rand = new Random();
        mId = rand.nextInt();
    }

    public static mjPermissions with(Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("Null Fragment Reference");
        }
        mContext = activity;
        return new mjPermissions();
    }

    public static mjPermissions with(Fragment fragment) {
        if (fragment == null) {
            throw new IllegalArgumentException("Null Fragment Reference");
        }
        mContext = fragment.getActivity();
        return new mjPermissions();
    }

    public void request(@NonNull @Size(min = 1) String... permissions) {
        if (isOverMarshmallow()) {
            if (permissions == null || permissions.length == 0) {
                throw new IllegalArgumentException("The permissions to request are missing");
            }

            Intent intent = new Intent(mContext, mjPermissionsActivity.class);
            intent.putExtra(Constants.PERMISSIONS, permissions);
            intent.putExtra(Constants.REQUEST_ID, mId);
            mContext.startActivity(intent);
        }
    }

    public boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int requestId = intent.getIntExtra(Constants.REQUEST_ID, 0);
            if (mId != requestId) {
                return;
            }

            String[] permissions = intent.getStringArrayExtra(Constants.PERMISSIONS);
            int[] grantResults = intent.getIntArrayExtra(Constants.GRANT_RESULTS);
            for (int i = 0; i < permissions.length; i++) {
                boolean isGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                runAnnotatedMethods(permissions[i], isGranted);
            }
        }
    }

    private static void runAnnotatedMethods(String permission, boolean isGranted) {
        Method[] methods = mContext.getClass().getMethods();
        for (Method method : methods) {
            if (isGranted) {
                if (method.isAnnotationPresent(OnPermissionGranted.class)) {
                    OnPermissionGranted onPermissionGranted = method.getAnnotation(OnPermissionGranted.class);
                    if (onPermissionGranted.value().equals(permission)) {
                        invokeMethod(method);
                    }
                }
            } else {
                if (method.isAnnotationPresent(OnPermissionDenied.class)) {
                    OnPermissionDenied onPermissionDenied = method.getAnnotation(OnPermissionDenied.class);
                    if (onPermissionDenied.value().equals(permission)) {
                        invokeMethod(method);
                    }
                }
            }
        }
    }

    private static void invokeMethod(Method method) {
        try {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            method.invoke(mContext);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "runDefaultMethod:IllegalAccessException", e);
        } catch (InvocationTargetException e) {
            Log.e(TAG, "runDefaultMethod:InvocationTargetException", e);
        }
    }
}
