<div align="center">
  <br><b>AXEmojiView</b> is an advanced Android Library<br>which adds emoji,sticker,... support to your Android application
  <br><a href="https://github.com/Aghajari/AXEmojiView/blob/master/README.md#download-apk">DemoAPK</a> • <a href="https://github.com/Aghajari/AXEmojiView/releases">Releases</a>
  <br><br><img width="420" alt="picker" src="./images/header.png">
  <br>Amir Hossein Aghajari
</div>

# AXEmojiView
it is an advanced Android Library which adds emoji,sticker,... support to your Android application.

<img src="./images/main.png" width=300 title="Screen">

## Table of Contents  
- [Installation](#installation)  
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
- [Download APK](#download-apk)
- [Author](#author)
- [License](#license)

## Installation

AXEmojiView is available in the JCenter, so you just need to add it as a dependency (Module gradle)

Gradle
```gradle
implementation 'com.aghajari.emojiview:AXEmojiView:1.2.0'
```

Maven
```xml
<dependency>
  <groupId>com.aghajari.emojiview</groupId>
  <artifactId>AXEmojiView</artifactId>
  <version>1.2.0</version>
  <type>pom</type>
</dependency>
```

# Usage
Let's START! :smiley:


## Install Emoji Provider 
First step, you should install EmojiView with your EmojiProvider!

```java
AXEmojiManager.install(this,new AXIOSEmojiProvider());
```

### Custom Emoji Provider
If you wanna display your own Emojis you can create your own implementation of [`EmojiProvider`](AXEmojiView/AXEmojiView/src/main/java/com/aghajari/emojiview/emoji/EmojiProvider.java) and pass it to `AXEmojiManager.install`.

 <img src="./images/google.jpg" width=250 title="Screen"> <img src="./images/Twitter.jpg" width=250 title="Screen"> <img src="./images/one.jpg" width=250 title="Screen">

## Basic Usage

Create an [`AXEmojiEditText`](AXEmojiView/AXEmojiView/src/main/java/com/aghajari/emojiview/view/AXEmojiEditText.java) in your layout.
```xml
<com.aghajari.axemojiview.view.AXEmojiEditText
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
```java
AXEmojiPopup emojiPopup = new AXEmojiPopup(emojiView);

emojiPopup.toggle(); // Toggles visibility of the Popup.
emojiPopup.show(); // Shows the Popup.
emojiPopup.dismiss(); // Dismisses the Popup.
emojiPopup.isShowing(); // Returns true when Popup is showing.
```

And we are done! :smiley:
Result :

<img src="./images/ios.jpg" width=200 title="Screen">

### AXEmojiPopupLayout

you can also create an AXEmojiPopupLayout instead of AXEmojiPopup!
i believe that AXEmojiPopupLayout has a better performance.

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

Result is just same as AXEmojiPopup result!

### Single Emoji View
SingleEmojiView is a RecyclerView and all emojis will load in one page (Same As Telegram Inc)

```java
AXSingleEmojiView emojiView = new AXSingleEmojiView(this);
emojiView.setEditText(edt);
```

Result :

<img src="./images/SingleEmojiView.png" width=350 title="Screen">

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

Result :

<img src="./images/shop_sticker.png" width=350 title="Screen">

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

Result :

<img src="./images/emojipager.png" width=350 title="Screen">

### Create Your Custom Pages
Create an AXEmojiBase (ViewGroup) and load your page layout
And add your CustomPage to emojiPager

Example: [`LoadingPage`](./AXEmojiView/app/src/main/java/com/aghajari/sample/emojiview/customs/LoadingView.java)

```java
emojiPager.addPage(new LoadingView(this), R.drawable.msg_round_load_m);
```

Result :

<img src="./images/loading.png" width=350 title="Screen">

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

Result :

<img src="./images/theme.png" width=350 title="Screen">

### Custom Footer

```java
// disable default footer
AXEmojiManager.getEmojiViewTheme().setFooterEnabled(false);
AXEmojiManager.getInstance().setBackspaceCategoryEnabled(false);

// add your own footer to the AXEmojiPager
EmojiPager.setCustomFooter(footerView,true);
```

Result :

<img src="./images/custom_footer_1.png" width=300 title="Screen">     <img src="./images/custom_footer_2.png" width=300 title="Screen">

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

Result :

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

Result :

<img src="./images/dark2.png" width=350 title="Screen">

## Views
- AXEmojiPopupLayout
- AXEmojiBase / AXEmojiLayout
- AXEmojiView
- AXSingleEmojiView
- AXStickerView
- AXEmojiEditText
- AXEmojiMultiAutoCompleteTextView
- AXEmojiButton
- AXEmojiImageView
- AXEmojiTextView
- AXEmojiCheckBox

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
```

## Replace String With Emojis
first you need to get Unicode of emoji :
```java
String unicode = AXEmojiUtils.getEmojiUnicode(0x1f60d); // or Emoji.getUnicode();
```
now set it to your view with AXEmojiUtils.replaceWithEmojis.

Example: Set ActionBar Title :
```java
String title = "AXEmojiView " + unicode;
getSupportActionBar().setTitle(AXEmojiUtils.replaceWithEmojis(this, title, 20));
```

Result :

<img src="./images/actionbar.jpg" width=500 title="Screen">

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

## Variant View
you can also create your own VariantPopupView !
but you don't need to, the default one is also nice :)

The Default Variant:

<img src="./images/variants.png" width=350 title="Screen">


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

## Download Apk
<img src="./images/apk.png" width=200 title="Screen">

- Version: 1.2.0
- LastUpdate: 15 August 2020

[`Download Apk`](./AXEmojiView1.2.0.apk)

## Author 
- **Amir Hossein Aghajari**

*Special thanks to the Telegram! (Using latest telegram emojis update!)*

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
