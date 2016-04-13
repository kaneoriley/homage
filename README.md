[![Release](https://jitpack.io/v/com.github.oriley-me/homage.svg)](https://jitpack.io/#com.github.oriley-me/homage) [![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0) [![Build Status](https://travis-ci.org/oriley-me/homage.svg?branch=master)](https://travis-ci.org/oriley-me/homage) [![Dependency Status](https://www.versioneye.com/user/projects/570cea97fcd19a00518553df/badge.svg?style=flat)](https://www.versioneye.com/user/projects/570cea97fcd19a00518553df)

# Homage
![Logo](artwork/icon.png)

Homage is a simple library, designed to make it more enjoyable (or less obnoxious, depending on your viewpoint) to
include open source licenses for all your used libraries. Features a very simple JSON interface, support for loading
from either assets or a raw resource, and the `homage-recyclerview` module includes some predefined adapters and custom
views for you to use straight away in your app.

## Usage

To construct a `Homage` instance, you will need to pass in a `Context` (the `Application` context will automatically be
retrieved so you don't have to worry about any `Activity` references being held on to), and either an asset path or a
raw resource ID.

Example construction (from your host activity):

```java
// Using a raw resource
mHomage = new Homage(getActivity(), R.raw.licenses);

// Or, using an asset path
mHomage = new Homage(getActivity(), "path/to/licenses.json");
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
public String getLibraryUrl();
public String getLicenseName();
public String getLicenseUrl();

// Note: The description is returned as a spanned to support HTML formatting
public Spanned getLicenseDescription();
```

## Included Adapters

The add-on module `homage-recyclerview` contains some extra adapters you can use to handle all the view binding
and library display logic (you can see them for yourself in the sample application).

`HomageExpandableAdapter`: CardView that expands height when pressed to show license details
`HomagePopupAdapter`: Simple view that shows a popup dialog containing license details when pressed

To use these, all you need is a preconfigured `Homage` instance, and a `RecyclerView` to set the adapter to:

```java
mRecyclerView.setAdapter(new HomageExpandableAdapter(mHomage));

// OR

mRecyclerView.setAdapter(new HomagePopupAdapter(mHomage));
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
      "version": "0.9.0",
      "year": "2016",
      "owner": "NextFaze",
      "description": "Universal Android adapter interface combined with a collection of utility adapters like headers, loading indicators, and dividers.",
      "url": "https://github.com/NextFaze/power-adapters",
      "license": "apache2"
    },
    {
      "name": "My Next Project",
      "version": "-0.1",
      "year": "2017",
      "owner": "O'Riley ME",
      "description": "Here is an example of using a custom license model, which I've injected from within the sample app.",
      "url": "https://github.com/oriley-me",
      "license": "oriley"
    }
  ]
}
```

The following are valid values for the `license` field: "cc0", "cc3", "apache2", "bsd2", "bsd3", "lgpl3", "mit"

If you need to include any custom licenses, then enter the key you use to add the license to your `Homage`
instance instead.

# Gradle Dependency

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
    compile 'me.oriley.homage:homage-core:0.0.1'

    // Optional, only needed if you want to use the included widgets instead of rolling your own
    compile 'me.oriley.homage:homage-recyclerview:0.0.1'
}
```

Please checkout the sample application to familiarise yourself with the implementation details. Don't be afraid to
make an issue or contact me if you have any problems or feature suggestions.

# Credits

* [Mike Penz](https://github.com/mikepenz) for his [AboutLibraries](https://github.com/mikepenz/AboutLibraries) project where I pulled the core license
information strings from. I urge you to check it out if you don't find Homage suits you well.