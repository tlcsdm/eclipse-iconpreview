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
3. <table style="border: none;">
  <tbody>
    <tr style="border:none;">
      <td style="vertical-align: middle; padding-top: 10px; border: none;">
        <a href='http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=6997461' title='Drag and drop into a running Eclipse Indigo workspace to install PDE Tools'> 
          <img src='https://marketplace.eclipse.org/modules/custom/eclipsefdn/eclipsefdn_marketplace/images/btn-install.svg'/>
        </a>
      </td>
      <td style="vertical-align: middle; text-align: left; border: none;">
        ← Drag it to your eclipse workbench to install! (I recommand Main Toolbar as Drop Target)
      </td>
    </tr>
  </tbody>
</table>

