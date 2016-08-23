#mjPermissions
=====
mjPermissions can easily handle the permissions in the Android M.

[ ![Download](https://api.bintray.com/packages/blackdole/maven/mjpermissions/images/download.svg) ](https://bintray.com/blackdole/maven/mjpermissions/_latestVersion)[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

###Maven
```
<dependency>
  <groupId>net.djcp</groupId>
  <artifactId>mjpermissions</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```

###Gradle
```groovy
dependencies { 
    compile 'net.djcp:mjpermissions:1.0.1'
}
```

###Usage
```java
import net.djcp.mjpermissions.mjPermissions;
import net.djcp.mjpermissions.annotations.OnPermissionDenied;
import net.djcp.mjpermissions.annotations.OnPermissionGranted;
import net.djcp.mjpermissions.annotations.OnPermissionGrantedAll;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mjPermissions.with(this)
                //.setOnPermissionListener(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    // Optional
    @OnPermissionGranted(Manifest.permission.CAMERA)
    public void cameraGranted() {

    }
    
    // Optional
    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void cameraDenied() {

    }

    // Optional
    @OnPermissionGranted({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onPermissionGranted() {

    }

    // Optional
    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onPermissionDenied() {
    
    }
    
    // Optional
    @OnPermissionGrantedAll
    public void onPermissionGrantedAll() {
    
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
