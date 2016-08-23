#mjPermissions
=====
mjPermissions can easily handle the permissions in the Android M.

[ ![Download](https://api.bintray.com/packages/blackdole/maven/mjpermissions/images/download.svg) ](https://bintray.com/blackdole/maven/mjpermissions/_latestVersion)[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

###Maven
```
<dependency>
  <groupId>net.djcp</groupId>
  <artifactId>mjpermissions</artifactId>
  <version>1.0.3</version>
  <type>pom</type>
</dependency>
```

###Gradle
```groovy
dependencies { 
    compile 'net.djcp:mjpermissions:1.0.3'
}
```

###Usage
```java
import net.djcp.mjpermissions.mjPermissions;
import net.djcp.mjpermissions.annotations.OnPermissionDenied;
import net.djcp.mjpermissions.annotations.OnPermissionGranted;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mjPermissions.with(this)
                //.setOnPermissionListener(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    // Optional
    @OnPermissionGranted
    public void onPermissionGranted() {
        
    }

    // Optional
    @OnPermissionDenied
    public void onPermissionDenied() {
        
    }

    // Optional
    @OnPermissionGranted(Manifest.permission.CAMERA)
    public void onCameraGranted() {
        
    }

    // Optional
    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void onCameraDenied() {
        
    }

    // Optional
    @OnPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void onStrorageGranted() {
        
    }

    // Optional
    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void onStrorageDenied() {
        
    }
}
```

###License
    
    Copyright (c) 2016 Black}{ole
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
