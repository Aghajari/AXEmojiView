package com.aghajari.sample.emojiview;

import android.app.Application;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.microsoft3dprovider.AXMicrosoft3DEmojiProvider;

/*
import com.aghajari.emojiview.googleprovider.AXGoogleEmojiProvider;
import com.aghajari.emojiview.samsungprovider.AXSamsungEmojiProvider;
import com.aghajari.emojiview.twitterprovider.AXTwitterEmojiProvider;
import com.aghajari.emojiview.appleprovider.AXAppleEmojiProvider;
import com.aghajari.emojiview.emojidexprovider.AXEmojidexEmojiProvider;
import com.aghajari.emojiview.facebookprovider.AXFacebookEmojiProvider;
import com.aghajari.emojiview.iosprovider.AXIOSEmojiProvider;
import com.aghajari.emojiview.whatsappprovider.AXWhatsAppEmojiProvider;
*/

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AXEmojiManager.install(this, new AXMicrosoft3DEmojiProvider(this));
    }
}
