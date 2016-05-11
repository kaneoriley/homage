[![Release](https://jitpack.io/v/com.github.oriley-me/homage.svg)](https://jitpack.io/#com.github.oriley-me/homage)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Build Status](https://travis-ci.org/oriley-me/homage.svg?branch=master)](https://travis-ci.org/oriley-me/homage)
[![Dependency Status](https://www.versioneye.com/user/projects/570cea97fcd19a00518553df/badge.svg?style=flat)](https://www.versioneye.com/user/projects/570cea97fcd19a00518553df)<br/>

<a href="http://www.methodscount.com/?lib=me.oriley.homage%3Ahomage-core%3A0.1.0"><img src="https://img.shields.io/badge/homage_core-methods: 97 | deps: 20 | size: 15 KB-f44336.svg"></img></a>
<a href="http://www.methodscount.com/?lib=me.oriley.homage%3Ahomage-recyclerview%3A0.1.0"><img src="https://img.shields.io/badge/homage_recyclerview-methods: 146 | deps: 11757 | size: 18 KB-ff9800.svg"></img></a>

# Homage
![Logo](artwork/icon.png)

Homage is a simple library, designed to make it more enjoyable (or less obnoxious, depending on your viewpoint) to
include open source licenses for all your used libraries. Features a very simple JSON interface, support for loading
from either assets or a raw resource, and the `homage-recyclerview` module includes some predefined widgets for you to
use straight away in your app.


## Usage


To construct a `Homage` instance, you will need to pass in a `Context` (the `Application` context will automatically be
retrieved so you don't have to worry about any `Activity` references being held on to), and a varargs array of either
asset paths or raw resource IDs.

Example construction (from your host activity):

```java
// Using a single raw resource
mHomage = new Homage(getActivity(), R.raw.licenses);

// Using multiple raw resources
mHomage = new Homage(getActivity(), R.raw.licenses_my_base_project, R.raw.licenses_my_app);

// Using an asset path
mHomage = new Homage(getActivity(), "path/to/licenses.json");

// Using multiple asset paths
mHomage = new Homage(getActivity(), "path/to/licenses_my_base_project.json", "path/to/licenses_my_app.json");
```

After construction, you must add any custom license definitions, and refresh the library list. If you only need to use
the included license definitions (more on this below), you can just refresh the list straight away:

```java
mHomage.addLicense("oriley", R.string.license_oriley_name, R.string.license_oriley_url, R.string.license_oriley_description);
mHomage.addLicense("another", R.string.license_oriley_name, R.string.license_oriley_url, R.string.license_oriley_description);
mHomage.refreshLibraries();
```

Once this is done, the library definitions will be ready to be displayed within your application. If you wish to bind
them to views yourself, you can call `mHomage.getLibraries()` to get an unmodifiable collection to do with as you wish.

Each `Library` item contains the following methods for information retrieval:

```java
public String getLibraryName();
public String getLibraryVersion();
public String getLibraryDescription();
public String getLibraryYear();
public String getLibraryOwner();
public String getLibraryOwnerUrl();
public String getLibraryUrl();
public String getLicenseName();
public String getLicenseUrl();

// Note: The description is returned as a spanned to support HTML formatting
public Spanned getLicenseDescription();

// Will return a resource ID if the key was a valid drawable name, otherwise `android.R.drawable.sym_def_app_icon`
public int getIconResource();

// Will return the `Uri` string for the library icon, or `null` if no valid `Uri` was found
public String getIconUri();
```


## Included Adapters


The add-on module `homage-recyclerview` contains a simple adapter you can use to handle all the view binding
and library display logic (you can check it out for yourself in the sample application). The constructor takes three
parameters, a `Homage` instance, the extra info display mode, and whether or not to show icons. Some examples (all used
in the sample application):

```java
// Expandable views with icons
HomageAdapter homageAdapter = new HomageAdapter(mHomage, HomageView.ExtraInfoMode.EXPANDABLE, true);

// Popup views with icons
HomageAdapter homageAdapter = new HomageAdapter(mHomage, HomageView.ExtraInfoMode.POPUP, true);

// Expandable views with no icons
HomageAdapter homageAdapter = new HomageAdapter(mHomage, HomageView.ExtraInfoMode.EXPANDABLE, false);

// Popup views with no icons
HomageAdapter homageAdapter = new HomageAdapter(mHomage, HomageView.ExtraInfoMode.POPUP, false);
```

Now all you need is a `RecyclerView` to set the adapter to:

```java
mRecyclerView.setAdapter(homageAdapter);
```

Simple, no?


## JSON format


[Sample licenses.json file](../master/homage-sample/src/main/res/raw/licenses.json)

The required format for the JSON file you pass to Homage is as follows:

```java
{
  "licenses": [
    {
      "name": "Power Adapters",
      "icon": "library_power_adapters",
      "version": "0.9.0",
      "year": "2016",
      "owner": "NextFaze",
      "ownerUrl": "https://nextfaze.com",
      "description": "Universal Android adapter interface combined with a collection of utility adapters like headers, loading indicators, and dividers.",
      "url": "https://github.com/NextFaze/power-adapters",
      "license": "apache2"
    },
    {
      "name": "My Next Project",
      "icon": "http://my.project/icon.png",
      "version": "-0.1",
      "year": "2017",
      "owner": "O'Riley ME",
      "ownerUrl": "http://oriley.me",
      "description": "Here is an example of using a custom license model, which I've injected from within the sample app.",
      "url": "https://github.com/oriley-me",
      "license": "oriley"
    }
  ]
}
```

The `icon` field can either be the name of a drawable resource included in your application, or a `Uri` to a file, and
Homage will automatically fill out the appropriate fields based on the format of the entry.
 
The following are valid values for the `license` field:

```
"apache_2_0"  - Apache 2.0
"bsd_2"       - BSD 2-Clause
"bsd_3"       - BSD 3-Clause
"cc0_1_0"     - CC0 1.0 Universal
"cc_3_0"      - Creative Commons 3.0
"lgpl_3_0"    - Lesser GNU Public License 3.0
"mit"         - The MIT License
```

I'm happy to accept pull requests/templates or suggestions for licenses which should be added.

If you add any custom licenses to your `Homage` instance, be sure to set the json key for the applicable library to
match the one you use to add it so that it can be matched up at runtime.


## Gradle Dependency


 * Add JitPack.io to your repositories list in the root projects build.gradle:

```gradle
repositories {
    maven { url "https://jitpack.io" }
}
```

 * Add the required dependencies:

```gradle
dependencies {
    // Required
    compile 'me.oriley.homage:homage-core:0.1.0'

    // Optional, only needed if you want to use the included widgets instead of rolling your own
    compile 'me.oriley.homage:homage-recyclerview:0.1.0'
}
```

If you would like to check out the latest development version, please substitute all versions for `develop-SNAPSHOT`.
Keep in mind that it is very likely things could break or be unfinished, so stick the official releases if you want
things to be more predictable.

Please checkout the sample application to familiarise yourself with the implementation details. Don't be afraid to
make an issue or contact me if you have any problems or feature suggestions.


## Credits


* [Mike Penz](https://github.com/mikepenz) for his [AboutLibraries](https://github.com/mikepenz/AboutLibraries) project where I pulled the core license
information strings from. I urge you to check it out if you don't find Homage suits you well.
