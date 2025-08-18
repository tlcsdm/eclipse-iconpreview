# IconPreview

IconPreview is a lightweight Eclipse plugin that allows you to preview image files in the Eclipse "Package Explorer" view.

Now support the following image formats:  
* png
* jpg/jpeg
* gif
* bmp
* ico

## Use
![screenshot](https://raw.github.com/tlcsdm/eclipse-iconpreview/master/plugins/com.tlcsdm.eclipse.iconpreview/help/images/example.png)

## Build

This project uses [Tycho](https://github.com/eclipse-tycho/tycho) with [Maven](https://maven.apache.org/) to build. It requires Maven 3.9.0 or higher version.

Dev build:

```
mvn clean verify
```

Release build:

```
mvn clean org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=2.0.0 verify
```

## Install

1. Add `https://raw.githubusercontent.com/tlcsdm/eclipse-iconpreview/master/update_site/` as the upgrade location in Eclipse.
2. Download from [Jenkins](https://jenkins.tlcsdm.com/job/eclipse-plugin/job/eclipse-iconpreview)

