[![Release](https://jitpack.io/v/com.github.oriley-me/homage.svg)](https://jitpack.io/#com.github.oriley-me/homage) [![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0) [![Build Status](https://travis-ci.org/oriley-me/homage.svg?branch=master)](https://travis-ci.org/oriley-me/homage) [![Dependency Status](https://www.versioneye.com/user/projects/570cea97fcd19a00518553df/badge.svg?style=flat)](https://www.versioneye.com/user/projects/570cea97fcd19a00518553df)

### TODO: Complete documentation (thank you for your patience). Please checkout the sample app to get an initial feel for the integration process, and look at `res/raw/licenses.json` for the expected format.

# Homage
![Logo](artwork/icon.png)

# Gradle Dependency

1. Add JitPack.io to your repositories list in the root projects build.gradle:

```gradle
repositories {
    maven { url "https://jitpack.io" }
}
```

2. Add the following to your module dependencies:

```gradle
dependencies {
    // Required for license parsing
    compile 'me.oriley.homage:homage-core:0.0.1'

    // Only needed if you want to use the included widgets instead of rolling your own
    compile 'me.oriley.homage:homage-recyclerview:0.0.1'
}
```

# Credits

* [Mike Penz](https://github.com/mikepenz) for his [AboutLibraries](https://github.com/mikepenz/AboutLibraries) project where I pulled the core license
information strings from. I urge you to check it out if you don't find Homage suits you well.