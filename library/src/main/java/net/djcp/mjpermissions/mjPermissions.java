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
import net.djcp.mjpermissions.annotations.OnPermissionDenied;
import net.djcp.mjpermissions.annotations.OnPermissionGranted;
import net.djcp.mjpermissions.annotations.OnPermissionGrantedAll;

import java.lang.reflect.Method;
import java.util.*;

public class mjPermissions {
    private static final String TAG = mjPermissions.class.getSimpleName();
    private static Context mContext;
    private static int mId;
    private static Map<String, Method> mGrantedAllMethods;
    private static Map<String, Method> mGrantedMethods;
    private static Map<String, Method> mDeniedMethods;
    private static OnPermissionListener mListener;

    private mjPermissions() {
        Random rand = new Random();
        mId = rand.nextInt();
        mListener = null;
        mGrantedAllMethods = new HashMap<>();
        mGrantedMethods = new HashMap<>();
        mDeniedMethods = new HashMap<>();
    }

    private void getAnnotatedMethod() {
        Method[] methods = mContext.getClass().getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(OnPermissionGranted.class)) {
                OnPermissionGranted onPermissionGranted = method.getAnnotation(OnPermissionGranted.class);
                for (int i = 0; i< onPermissionGranted.value().length; i++) {
                    mGrantedMethods.put(onPermissionGranted.value()[i], method);
                }
            } else if (method.isAnnotationPresent(OnPermissionDenied.class)) {
                OnPermissionDenied onPermissionDenied = method.getAnnotation(OnPermissionDenied.class);
                for (int i = 0; i< onPermissionDenied.value().length; i++) {
                    mDeniedMethods.put(onPermissionDenied.value()[i], method);
                }
            } else if (method.isAnnotationPresent(OnPermissionGrantedAll.class)) {
                OnPermissionGrantedAll onPermissionGranted = method.getAnnotation(OnPermissionGrantedAll.class);
                mGrantedAllMethods.put(onPermissionGranted.value(), method);
            }
        }
    }

    private static void invokeMethod(Method method) {
        try {
            if (method != null) {
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                method.invoke(mContext);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public void request(@NonNull @Size(min = 1) String... permissions) {
        if (isOverMarshmallow()) {
            if (permissions == null || permissions.length == 0) {
                throw new IllegalArgumentException("The permissions to request are missing");
            }

            getAnnotatedMethod();

            Intent intent = new Intent(mContext, mjPermissionsActivity.class);
            intent.putExtra(Constants.PERMISSIONS, permissions);
            intent.putExtra(Constants.REQUEST_ID, mId);
            mContext.startActivity(intent);
        }
    }

    public void setOnPermissionListener(OnPermissionListener listener) {
        mListener = listener;
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

    public static class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int requestId = intent.getIntExtra(Constants.REQUEST_ID, 0);
            if (mId != requestId) {
                return;
            }

            List<String> grantedPermissions = new ArrayList<>();
            List<String> deniedPermissions = new ArrayList<>();
            String[] permissions = intent.getStringArrayExtra(Constants.PERMISSIONS);
            int[] grantResults = intent.getIntArrayExtra(Constants.GRANT_RESULTS);
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    grantedPermissions.add(permissions[i]);
                } else {
                    deniedPermissions.add(permissions[i]);
                }
            }

            if (grantedPermissions.size() > 0) {
                List<Method> grantedMethods = new ArrayList<>();
                for (int i = 0; i < grantedPermissions.size(); i++) {
                    String permission = grantedPermissions.get(i);
                    if (mGrantedMethods.containsKey(permission)) {
                        if (!grantedMethods.contains(mGrantedMethods.get(permission)))
                            grantedMethods.add(mGrantedMethods.get(permission));
                    }
                }
                for (int i = 0; i < grantedMethods.size(); i++) {
                    invokeMethod(grantedMethods.get(i));
                }
            }

            if (deniedPermissions.size() > 0) {
                List<Method> deniedMethods = new ArrayList<>();
                for (int i = 0; i < deniedPermissions.size(); i++) {
                    String permission = deniedPermissions.get(i);
                    if (mDeniedMethods.containsKey(permission)) {
                        if (!deniedMethods.contains(mDeniedMethods.get(permission)))
                            deniedMethods.add(mDeniedMethods.get(permission));
                    }
                }
                for (int i = 0; i < deniedMethods.size(); i++) {
                    invokeMethod(deniedMethods.get(i));
                }
            } else {
                if (mGrantedAllMethods.size() > 0) {
                    invokeMethod(mGrantedAllMethods.get(0));
                }
            }

            if (mListener != null) {
                mListener.onPermissionGranted(grantedPermissions);
                mListener.onPermissionDenied(deniedPermissions);
                if (deniedPermissions.size() == 0) {
                    mListener.onPermissionGrantedAll();
                }
            }
        }
    }

    public interface OnPermissionListener {

        void onPermissionGranted(List<String> permissions);

        void onPermissionDenied(List<String> permissions);

        void onPermissionGrantedAll();

    }

}
