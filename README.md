<div align="center">
  <br><b>AXEmojiView</b> is an advanced Android Library<br>which adds emoji,sticker,... support to your Android application
  <br><a href="https://github.com/Aghajari/AXEmojiView/blob/master/README.md#download-apk">DemoAPK</a> • <a href="https://github.com/Aghajari/AXEmojiView/releases">Releases</a>
  <br><br><img width="40" alt="LCoders | AmirHosseinAghajari" src="https://user-images.githubusercontent.com/30867537/90538314-a0a79200-e193-11ea-8d90-0a3576e28a18.png"><br><img width="420" alt="picker" src="./images/header.png">
  
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.aghajari/AXEmojiView.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.github.aghajari/AXEmojiView/1.5.2/aar)
[![Join the chat at https://gitter.im/Aghajari/community](https://badges.gitter.im/Aghajari/community.svg)](https://gitter.im/Aghajari/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
</div>


## Screenshot
<img src="./images/main.png" width=300 title="Screen">  <img src="./images/dark.png" width=300 title="Screen">

## Table of Contents  
- [Installation](#installation)  
- [Microsoft Fluent 3D Emoji](#microsoft-fluent-3d-emoji)
- [Usage](#usage)
  - [Install Emoji Provider](#install-emoji-provider)
    - [Custom Emoji Provider](#custom-emoji-provider)
  - [Basic Usage](#basic-usage)
    - [EmojiView](#basic-usage)
    - [EmojiPopup](#basic-usage)
    - [EmojiPopupLayout](#axemojipopuplayout)
    - [SingleEmojiView](#single-emoji-view)
    - [StickerView](#stickerview)
  - [EmojiPager (Use Multiple Pages Together!)](#axemojipager---use-multiple-pages-together)
    - [Create Your Custom Pages](#create-your-custom-pages)
  - [AXEmojiSearchView](#axemojisearchview)
  - [Popup Animation](#popup-animation)
  - [Customization](#customization)
    - [Custom Theme](#customization)
    - [Custom Footer](#custom-footer)
    - [Dark Mode](#darkmode)
- [Views](#views)
- [Listeners](#listeners)
- [Replace String With Emojis](#replace-string-with-emojis)
- [RecentManager And VariantManager](#recentmanager-and-variantmanager)
- [Variant View](#variant-view)
- [Emoji Loader](#emoji-loader)
- [AnimatedStickers (AXrLottie)](#animatedstickers-axrlottie)
- [AXMemojiView](#axmemojiview)
- [Download APK](#download-apk)
- [Author](#author)
- [License](#license)

## Changelogs
1.5.2:
- [New emoji provider!](#microsoft-fluent-3d-emoji)
  - Microsoft3DProvider
- Some bugs fixed

1.5.0:
- [New emoji providers!](#install-emoji-provider)
  - AppleProvider
  - iOSProvider
  - GoogleProvider
  - SamsungProvider
  - FacebookProvider
  - WhatsAppProvider
  - TwitterProvider
  - EmojidexProvider
- EmojiPediaLoader project for auto updating emojis from [emojipedia.org](https://emojipedia.org/)
- [New Variants!](#variant-view)
- Now you can filter some emojis from EmojiManager
- Now you can disable variants from EmojiManager

## Installation

AXEmojiView is available in the `mavenCentral()`, so you just need to add it as a dependency (Module gradle)

**LatestVersion : 1.5.0**

Gradle
```gradle
def emojiViewVersion = "1.5.2"
def emojiViewProvider = "AppleProvider"

implementation "io.github.aghajari:AXEmojiView:$emojiViewVersion"
implementation "io.github.aghajari:AXEmojiView-$emojiViewProvider:$emojiViewVersion"
```

List of providers:
```gradle
implementation "io.github.aghajari:AXEmojiView-Microsoft3DProvider:$emojiViewVersion"
implementation "io.github.aghajari:AXEmojiView-AppleProvider:$emojiViewVersion"
implementation "io.github.aghajari:AXEmojiView-iOSProvider:$emojiViewVersion"
implementation "io.github.aghajari:AXEmojiView-GoogleProvider:$emojiViewVersion"
implementation "io.github.aghajari:AXEmojiView-SamsungProvider:$emojiViewVersion"
implementation "io.github.aghajari:AXEmojiView-FacebookProvider:$emojiViewVersion"
implementation "io.github.aghajari:AXEmojiView-WhatsAppProvider:$emojiViewVersion"
implementation "io.github.aghajari:AXEmojiView-TwitterProvider:$emojiViewVersion"
implementation "io.github.aghajari:AXEmojiView-EmojidexProvider:$emojiViewVersion"
```

Maven
```xml
<dependency>
  <groupId>io.github.aghajari</groupId>
  <artifactId>AXEmojiView</artifactId>
  <version>1.5.2</version>
  <type>pom</type>
</dependency>
```

## Microsoft Fluent 3D Emoji
Fluent Emoji are a collection of familiar, friendly, and modern emoji from Microsoft. [Reference](https://github.com/microsoft/fluentui-emoji)
<img src="https://github.com/microsoft/fluentui-emoji/blob/main/art/readme_banner.webp" width=500 title="Fluent Emoji">

*Note: This emoji provider doesn't include flags!* [Why?](https://github.com/microsoft/fluentui-emoji/issues/25)

# Usage
Let's START! :smiley:


## Install Emoji Provider 
First step, you should install EmojiView with your EmojiProvider!

```java
AXEmojiManager.install(this,new AXIOSEmojiProvider()); // new ProviderClassName
```

*Note: iOSProvider is almost the same as AppleProvider but has less variants, It's a provider of Telegram emoji set*
<br>
*Note: GoogleProvider includes [Emoji-14](https://emojipedia.org/emoji-14.0/) new emojis*

| AppleProvider | GoogleProvider | Microsoft3DProvider |
| :---: | :---: | :---: |
| <img src="./images/providers/apple_light.png" width=200 title="Screen"> | <img src="./images/providers/google_light.png" width=200 title="Screen"> | <img src="./images/providers/microsoft3d_light.png" width=200 title="Screen"> |
| <img src="./images/providers/apple_dark.png" width=200 title="Screen"> | <img src="./images/providers/google_dark.png" width=200 title="Screen"> | <img src="./images/providers/microsoft3d_dark.png" width=200 title="Screen"> |

| FacebookProvider | WhatsAppProvider | SamsungProvider |
| :---: | :---: | :---: |
| <img src="./images/providers/facebook_light.png" width=200 title="Screen"> | <img src="./images/providers/whatsapp_light.png" width=200 title="Screen"> | <img src="./images/providers/samsung_light.png" width=200 title="Screen"> |
| <img src="./images/providers/facebook_dark.png" width=200 title="Screen"> | <img src="./images/providers/whatsapp_dark.png" width=200 title="Screen"> | <img src="./images/providers/samsung_dark.png" width=200 title="Screen"> |

| iOSProvider | TwitterProvider | EmojidexProvider |
| :---: | :---: | :---: |
| <img src="./images/providers/apple_light.png" width=200 title="Screen"> | <img src="./images/providers/twitter_light.png" width=200 title="Screen"> | <img src="./images/providers/emojidex_light.png" width=200 title="Screen"> |
| <img src="./images/providers/apple_dark.png" width=200 title="Screen"> | <img src="./images/providers/twitter_dark.png" width=200 title="Screen"> | <img src="./images/providers/emojidex_dark.png" width=200 title="Screen"> |

### Custom Emoji Provider
If you wanna display your own Emojis you can create your own implementation of [`EmojiProvider`](AXEmojiView/AXEmojiView/src/main/java/com/aghajari/emojiview/emoji/EmojiProvider.java) and pass it to `AXEmojiManager.install`.

 <img src="./images/google.jpg" width=250 title="Screen"> <img src="./images/Twitter.jpg" width=250 title="Screen"> <img src="./images/one.jpg" width=250 title="Screen">

## Basic Usage

Create an [`AXEmojiEditText`](AXEmojiView/AXEmojiView/src/main/java/com/aghajari/emojiview/view/AXEmojiEditText.java) in your layout.
```xml
<com.aghajari.emojiview.view.AXEmojiEditText
            android:id="@+id/edt"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:hint="enter your message ..." />
```

Now, you should create a Page. 
Current pages are :
- EmojiView
- SingleEmojiView
- StickerView

Let's try EmojiView :
```java
AXEmojiEditText edt = findViewById(R.id.edt);

AXEmojiView emojiView = new AXEmojiView(this);
emojiView.setEditText(edt);
```

And add this page to AXEmojiPopup :

*Note: AXEmojiPopup deprecated! Use [AXEmojiPopupLayout](#axemojipopuplayout) instead.*

```java
AXEmojiPopup emojiPopup = new AXEmojiPopup(emojiView); 

emojiPopup.toggle(); // Toggles visibility of the Popup.
emojiPopup.show(); // Shows the Popup.
emojiPopup.dismiss(); // Dismisses the Popup.
emojiPopup.isShowing(); // Returns true when Popup is showing.
```

And we are done! :smiley:
Output :

<img src="./images/ios.jpg" width=200 title="Screen">

[Back to contents](#table-of-contents)

### AXEmojiPopupLayout

you can also create an AXEmojiPopupLayout instead of AXEmojiPopup!

i believe that AXEmojiPopupLayout has better performance, more customizable and it's faster.

1. create an AXEmojiPopupLayout in your layout.
```xml
    <com.aghajari.emojiview.view.AXEmojiPopupLayout
        android:id="@+id/layout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom"/>
```

2. add the created page to AXEmojiPopupLayout :
```java
AXEmojiPopupLayout layout = findViewById(R.id.layout);
layout.initPopupView(emojiView);

layout.toggle(); // Toggles visibility of the Popup.
layout.show(); // Shows the Popup.
layout.dismiss(); // Dismisses the Popup.
layout.hideAndOpenKeyboard(); // Hides the popup
layout.isShowing(); // Returns true when Popup is showing.
```

Output is just same as the AXEmojiPopup's output!

[Back to contents](#table-of-contents)

### Single Emoji View
SingleEmojiView is a RecyclerView and all emojis will load in one page (Same As Telegram Inc)

```java
AXSingleEmojiView emojiView = new AXSingleEmojiView(this);
emojiView.setEditText(edt);
```

Output :

<img src="./images/SingleEmojiView.png" width=350 title="Screen">

[Back to contents](#table-of-contents)

### StickerView
StickerView :
you have to create your StickerProvider and load all your Stickers (from Url,Res,Bitmap or anything you want!)
see example : [`WhatsAppProvider`](./AXEmojiView/app/src/main/java/com/aghajari/sample/emojiview/sticker/WhatsAppProvider.java)

```java
AXStickerView stickerView = new AXStickerView(this , "stickers" , new MyStickerProvider());
```

Result :

<img src="./images/sticker.png" width=350 title="Screen">


Also you can create your custom pages in StickerProvider . see example : [`ShopStickers`](./AXEmojiView/app/src/main/java/com/aghajari/sample/emojiview/sticker/ShopStickers.java)

Output :

<img src="./images/shop_sticker.png" width=350 title="Screen">

[Back to contents](#table-of-contents)

## AXEmojiPager - Use Multiple Pages Together!
you can create an AXEmojiPager and add all your pages (EmojiView,StickerView,...) to the EmojiPager

Enable Footer view in theme settings (if you want) :
```java
AXEmojiManager.getEmojiViewTheme().setFooterEnabled(true);
```

And Create your EmojiPager :
```java
AXEmojiPager emojiPager = new AXEmojiPager(this);

AXSingleEmojiView singleEmojiView = new AXSingleEmojiView(this);
emojiPager.addPage(singleEmojiView, R.drawable.ic_msg_panel_smiles);

AXStickerView stickerView = new AXStickerView(this, "stickers", new WhatsAppProvider());
emojiPager.addPage(stickerView, R.drawable.ic_msg_panel_stickers);

emojiPager.setSwipeWithFingerEnabled(true);
emojiPager.setEditText(edt);

AXEmojiPopup emojiPopup = new AXEmojiPopup(emojiPager);
//layout.initPopupView(emojiPager);
```

Add search button to the footer:
```java
emojiPager.setLeftIcon(R.drawable.ic_ab_search);
        
        //Click Listener
        emojiPager.setOnFooterItemClicked(new AXEmojiPager.onFooterItemClicked() {
            @Override
            public void onClick(boolean leftIcon) {
                if (leftIcon) Toast.makeText(EmojiActivity.this,"Search Clicked",Toast.LENGTH_SHORT).show();
            }
        });
```

Output :

<img src="./images/emojipager.png" width=350 title="Screen">

[Back to contents](#table-of-contents)

### Create Your Custom Pages
Create an AXEmojiBase (ViewGroup) and load your page layout
And add your CustomPage to emojiPager

Example: [`LoadingPage`](./AXEmojiView/app/src/main/java/com/aghajari/sample/emojiview/customs/LoadingView.java)

```java
emojiPager.addPage(new LoadingView(this), R.drawable.msg_round_load_m);
```

Output :

<img src="./images/loading.png" width=350 title="Screen">

[Back to contents](#table-of-contents)

## AXEmojiSearchView
Now you can search for the emoji by text in the default AXEmojiView's database (More than 5800+ words!) or your own db with the AXEmojiSearchView or your own customized view!

Example :
```java
popupLayout.setSearchView(new AXEmojiSearchView(this, emojiPager.getPage(0)));

emojiPager.setOnFooterItemClicked(new AXEmojiPager.OnFooterItemClicked() {
    @Override
    public void onClick(View view, boolean leftIcon) {
        if (leftIcon) layout.showSearchView();
    }
});
```

Output :

<img src="./images/search.png" width=350 title="Screen">

[Back to contents](#table-of-contents)

## Popup Animation
an smooth popup animation has been enabled for the AXEmojiPopupLayout.

```java
popupLayout.setPopupAnimationEnabled(true);
popupLayout.setPopupAnimationDuration(250);

popupLayout.setSearchViewAnimationEnabled(true);
popupLayout.setSearchViewAnimationDuration(250);
```

Output :

<img src="./images/AXEmojiView1.30.gif" width=350 title="Screen">

[Back to contents](#table-of-contents)

## Customization
Customize theme with AXEmojiTheme.
```java
AXEmojiManager.getEmojiViewTheme().setSelectionColor(0xffFF4081);
AXEmojiManager.getEmojiViewTheme().setFooterSelectedItemColor(0xffFF4081);
AXEmojiManager.getEmojiViewTheme().setFooterBackgroundColor(Color.WHITE);
AXEmojiManager.getEmojiViewTheme().setSelectionColor(Color.TRANSPARENT);
AXEmojiManager.getEmojiViewTheme().setSelectedColor(0xffFF4081);
AXEmojiManager.getEmojiViewTheme().setCategoryColor(Color.WHITE);
AXEmojiManager.getEmojiViewTheme().setAlwaysShowDivider(true);
AXEmojiManager.getEmojiViewTheme().setBackgroundColor(Color.LTGRAY);

AXEmojiManager.getStickerViewTheme().setSelectedColor(0xffFF4081);
AXEmojiManager.getStickerViewTheme().setCategoryColor(Color.WHITE);
AXEmojiManager.getStickerViewTheme().setAlwaysShowDivider(true);
AXEmojiManager.getStickerViewTheme().setBackgroundColor(Color.LTGRAY);
```

Output :

<img src="./images/theme.png" width=350 title="Screen">

[Back to contents](#table-of-contents)

### Custom Footer

```java
// disable default footer
AXEmojiManager.getEmojiViewTheme().setFooterEnabled(false);
AXEmojiManager.getInstance().setBackspaceCategoryEnabled(false);

// add your own footer to the AXEmojiPager
EmojiPager.setCustomFooter(footerView,true);
```

Output :

<img src="./images/custom_footer_1.png" width=300 title="Screen">     <img src="./images/custom_footer_2.png" width=300 title="Screen">

[Back to contents](#table-of-contents)

### DarkMode

+ Style 1
```java
AXEmojiManager.getEmojiViewTheme().setFooterEnabled(true);
AXEmojiManager.getEmojiViewTheme().setSelectionColor(0xff82ADD9);
AXEmojiManager.getEmojiViewTheme().setSelectedColor(0xff82ADD9);
AXEmojiManager.getEmojiViewTheme().setFooterSelectedItemColor(0xff82ADD9);
AXEmojiManager.getEmojiViewTheme().setBackgroundColor(0xFF1E2632);
AXEmojiManager.getEmojiViewTheme().setCategoryColor(0xFF1E2632);
AXEmojiManager.getEmojiViewTheme().setFooterBackgroundColor(0xFF1E2632);
AXEmojiManager.getEmojiViewTheme().setVariantPopupBackgroundColor(0xFF232D3A);
AXEmojiManager.getEmojiViewTheme().setVariantDividerEnabled(false);
AXEmojiManager.getEmojiViewTheme().setDividerColor(0xFF1B242D);
AXEmojiManager.getEmojiViewTheme().setDefaultColor(0xFF677382);
AXEmojiManager.getEmojiViewTheme().setTitleColor(0xFF677382);

AXEmojiManager.getStickerViewTheme().setSelectionColor(0xff82ADD9);
AXEmojiManager.getStickerViewTheme().setSelectedColor(0xff82ADD9);
AXEmojiManager.getStickerViewTheme().setBackgroundColor(0xFF1E2632);
AXEmojiManager.getStickerViewTheme().setCategoryColor(0xFF1E2632);
AXEmojiManager.getStickerViewTheme().setDividerColor(0xFF1B242D);
AXEmojiManager.getStickerViewTheme().setDefaultColor(0xFF677382);
```

Output :

<img src="./images/dark1.png" width=350 title="Screen">

+ Style 2
```java
AXEmojiManager.getEmojiViewTheme().setFooterEnabled(true);
AXEmojiManager.getEmojiViewTheme().setSelectionColor(Color.TRANSPARENT);
AXEmojiManager.getEmojiViewTheme().setSelectedColor(0xff82ADD9);
AXEmojiManager.getEmojiViewTheme().setFooterSelectedItemColor(0xff82ADD9);
AXEmojiManager.getEmojiViewTheme().setBackgroundColor(0xFF1E2632);
AXEmojiManager.getEmojiViewTheme().setCategoryColor(0xFF232D3A);
AXEmojiManager.getEmojiViewTheme().setFooterBackgroundColor(0xFF232D3A);
AXEmojiManager.getEmojiViewTheme().setVariantPopupBackgroundColor(0xFF232D3A);
AXEmojiManager.getEmojiViewTheme().setVariantDividerEnabled(false);
AXEmojiManager.getEmojiViewTheme().setDividerColor(0xFF1B242D);
AXEmojiManager.getEmojiViewTheme().setDefaultColor(0xFF677382);
AXEmojiManager.getEmojiViewTheme().setTitleColor(0xFF677382);
AXEmojiManager.getEmojiViewTheme().setAlwaysShowDivider(true);

AXEmojiManager.getStickerViewTheme().setSelectionColor(0xff82ADD9);
AXEmojiManager.getStickerViewTheme().setSelectedColor(0xff82ADD9);
AXEmojiManager.getStickerViewTheme().setBackgroundColor(0xFF1E2632);
AXEmojiManager.getStickerViewTheme().setCategoryColor(0xFF232D3A);
AXEmojiManager.getStickerViewTheme().setDividerColor(0xFF1B242D);
AXEmojiManager.getStickerViewTheme().setDefaultColor(0xFF677382);
AXEmojiManager.getStickerViewTheme().setAlwaysShowDivider(true);
```

Output :

<img src="./images/dark2.png" width=350 title="Screen">

[Back to contents](#table-of-contents)

## Views
- AXEmojiPopupLayout
- AXEmojiBase / AXEmojiLayout
- AXEmojiView
- AXSingleEmojiView
- AXStickerView
- AXEmojiSearchView
- AXEmojiEditText
- AXEmojiMultiAutoCompleteTextView
- AXEmojiButton
- AXEmojiImageView
- AXEmojiTextView
- AXEmojiCheckBox
- AXEmojiRadioButton

[Back to contents](#table-of-contents)

## Listeners
onEmojiActions :
```java
    void onClick (View view, Emoji emoji, boolean fromRecent, boolean fromVariant);
    void onLongClick (View view, Emoji emoji, boolean fromRecent, boolean fromVariant);
```

onStickerActions :
```java
    void onClick(View view, Sticker sticker, boolean fromRecent);
    void onLongClick(View view, Sticker sticker, boolean fromRecent);
```

onEmojiPagerPageChanged :
```java
    void onPageChanged (AXEmojiPager emojiPager, AXEmojiBase base, int position);
```

PopupListener :
```java
    void onDismiss();
    void onShow();
    void onKeyboardOpened(int height);
    void onKeyboardClosed();
    void onViewHeightChanged(int height);
```

[Back to contents](#table-of-contents)

## Replace String With Emojis
first you need to get Unicode of emoji :
```java
String unicode = AXEmojiUtils.getEmojiUnicode(0x1f60d); // or Emoji.getUnicode();
```
Or
```java
String unicode = "😍";
```
now set it to your view with AXEmojiUtils.replaceWithEmojis.

Example: Set ActionBar Title :
```java
String title = "AXEmojiView " + unicode;
getSupportActionBar().setTitle(AXEmojiUtils.replaceWithEmojis(this, title, 20));
```

Output :

<img src="./images/actionbar.jpg" width=500 title="Screen">

[Back to contents](#table-of-contents)

## RecentManager And VariantManager
you can add your custom recentManager for emojis and stickers . implements to RecentEmoji/RecentSticker
```java
AXEmojiManager.setRecentEmoji(emojiRecentManager);
AXEmojiManager.setRecentSticker(stickerRecentManager);
```

Disable RecentManagers :
```java
AXEmojiManager.getInstance().disableRecentManagers();
```

[Back to contents](#table-of-contents)

## Variant View
you can also create your own VariantPopupView !
but you don't need to, the default one is also nice :)

The Default Variant:

<img src="./images/variants.png" width=350 title="Screen">

**New Variants!**
<br><br>
<img src="./images/variants_new.png" width=350 title="Screen">

[Back to contents](#table-of-contents)

## Emoji Loader
you can add an custom EmojiLoader with AXEmojiLoader :
```java
AXEmojiManager.setEmojiLoader(new EmojiLoader(){
  @Override
  public void loadEmoji (AXEmojiImageView imageView,Emoji emoji){
   imageView.setImageDrawable(emoji.getDrawable(imageView.getContext());
  }
});
```

[Back to contents](#table-of-contents)

## AnimatedStickers [(AXrLottie)](https://github.com/Aghajari/AXrLottie)
[See AXrLottie](https://github.com/Aghajari/AXrLottie#animatedsticker---axemojiview)

<img src="https://github.com/Aghajari/AXrLottie/blob/master/images/screen.png" width=350 title="Screen">

[Back to contents](#table-of-contents)

## [AXMemojiView](https://github.com/Aghajari/AXMemojiView)
[AXMemojiView](https://github.com/Aghajari/AXMemojiView) is a page for AXEmojiView which shows memoji just like stickers

<img width="550" alt="AXMemojiView" src="https://user-images.githubusercontent.com/30867537/100551530-6cfdfd00-3296-11eb-8963-026ba0641d44.png">
 
<img width="180" alt="AXMemojiView" src="https://user-images.githubusercontent.com/30867537/100551778-2a3d2480-3298-11eb-915d-a0b7ab9ef763.png"> <img width="180" alt="AXMemojiView" src="https://user-images.githubusercontent.com/30867537/100551817-6f615680-3298-11eb-8f56-c5e8e6adc23c.png"> <img width="180" alt="AXMemojiView" src="https://user-images.githubusercontent.com/30867537/100551853-a172b880-3298-11eb-87a0-7bcbc0bc7687.png">

[Back to contents](#table-of-contents)

## Download Apk
<img src="./images/apk.png" width=200 title="Screen">

- Version: 1.5.2 (Microsoft3DProvider)<br>
  LastUpdate: 12 August 2022<br>
  [`Download Apk`](./AXEmojiView1.5.2-Microsoft3DProvider.apk)

- Version: 1.3.0 (iOSProvider)<br>
  LastUpdate: 4 January 2021<br>
  [`Download Apk`](./AXEmojiView1.3.0.apk)

[Back to contents](#table-of-contents)

## Author 
- **Amir Hossein Aghajari**
- Emojis from [emojipedia.org](https://emojipedia.org/)
- iOSProvider from Telegram
- [Microsoft Fluent Emoji](https://github.com/microsoft/fluentui-emoji)

*TelegramID : @KingAmir272*

License
=======

    Copyright 2020 Amir Hossein Aghajari
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


<br>
<div align="center">
  <img width="64" alt="LCoders | AmirHosseinAghajari" src="https://user-images.githubusercontent.com/30867537/90538314-a0a79200-e193-11ea-8d90-0a3576e28a18.png">
  <br><a>Amir Hossein Aghajari</a> • <a href="mailto:amirhossein.aghajari.82@gmail.com">Email</a> • <a href="https://github.com/Aghajari">GitHub</a>
</div>
