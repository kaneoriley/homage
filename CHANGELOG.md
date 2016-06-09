Change Log
==========

## Version 0.2.0

_2016-06-09_

 *  Add support for non-Card based adapters in recyclerview module (thanks DreierF)
 *  API change: HomageAdapter is now non-Card based. Use HomageCardAdapter for the card based style.

## Version 0.1.2

_2016-05-11_

 *  Homage constructor now accepts varargs parameter for resource IDs/asset paths. Useful if you have a common base
    application/library and wish to maintain a set of core licenses without having to also include them in the child
    project.
 *  Various optimisations to the expanded card view and license model
 *  Add support for specifying an owner URL, and an icon Uri (for use in your own adapters)
 *  Add dark theme option to HomageAdapter (homage-recyclerview)
 *  Drop minimum SDK level to 15 (4.0)
 *  Using github domain for JitPack prefix now (custom is too unreliable)

## Version 0.1.0

_2016-04-16_

 *  Parse JSON manually rather than relying on Gson (drops effective method count from 1300 -> 75)
 *  Added ability to specify an icon in licenses json
 *  Massively improved scrolling performance when using expandable views
 *  Breaking API change: Now using a single `HomageAdapter`/`HomageView` with configuration options

## Version 0.0.2

_2016-04-13_

 *  Simplify Adapter->Homage relationship
 *  Refactor views and add popup dialog version

## Version 0.0.1

_2016-04-12_

 *  Initial release.
