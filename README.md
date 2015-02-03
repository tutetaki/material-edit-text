Material Design EditText
========================

![demo_dark.gif](./img/demo_dark.gif) ![demo_light.gif](./img/demo_light.gif)

Usage
-----

```xml
<MaterialEditText
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  android:hint="Hint text"
  android:text="Input text"
  material:floatingLabel="true"
  material:maxCharacters="120"
  material:withIcon="@+id/image_view"/>
```

Download
--------

Download the [latest JAR]() or grab via Maven:

```xml
<dependency>
  <groupId>fr.erictruong</groupId>
  <artifactId>materialedittext</artifactId>
  <version>1.0.0</version>
</dependency>
```

or Gradle:

```groovy
compile 'fr.erictruong:materialedittext:1.0.0'
```

License
-------

```
Copyright 2015 Eric Truong

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
