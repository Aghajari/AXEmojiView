package com.aghajari.sample.emojiview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Switch;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.AXEmojiUtils;

import com.aghajari.emojiview.preset.AXPresetEmojiLoader;
import com.aghajari.emojiview.view.AXEmojiButton;
import com.aghajari.sample.emojiview.activity.DarkActivity;
import com.aghajari.sample.emojiview.activity.EmojiActivity;
import com.aghajari.sample.emojiview.activity.EmojiPopupViewActivity;

import com.aghajari.emojiview.googleprovider.AXGoogleEmojiProvider;
/*
import com.aghajari.emojiview.samsungprovider.AXSamsungEmojiProvider;
import com.aghajari.emojiview.twitterprovider.AXTwitterEmojiProvider;
import com.aghajari.emojiview.appleprovider.AXAppleEmojiProvider;
import com.aghajari.emojiview.emojidexprovider.AXEmojidexEmojiProvider;
import com.aghajari.emojiview.facebookprovider.AXFacebookEmojiProvider;
import com.aghajari.emojiview.iosprovider.AXIOSEmojiProvider;
import com.aghajari.emojiview.whatsappprovider.AXWhatsAppEmojiProvider;
*/

public class MainActivity extends AppCompatActivity {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AXEmojiManager.install(this, new AXGoogleEmojiProvider(this));
        //AXEmojiManager.filterEmojis(Arrays.asList("😀", "😃", "😄", "😁"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Switch mEmojiView = findViewById(R.id.emoji_view_switch);
        final Switch mSingleEmojiView = findViewById(R.id.single_emoji_view_switch);
        final Switch mStickerView = findViewById(R.id.sticker_view_switch);
        final Switch mCustomView = findViewById(R.id.custom_view_switch);
        final Switch mFooterView = findViewById(R.id.footer_switch);
        final Switch mCustomFooterView = findViewById(R.id.custom_footer_switch);
        final Switch mWhiteCategory = findViewById(R.id.theme_switch);
        final Switch mPopupView = findViewById(R.id.popup_view);
        final Switch mDarkTheme = findViewById(R.id.dark_theme);

        final AXEmojiButton btn = findViewById(R.id.start_emoji_activity);

        AXPresetEmojiLoader.preloadEmoji(AXEmojiUtils.getEmojiUnicode(0x1f60d),
                emoji -> {
                    getSupportActionBar().setTitle(AXEmojiUtils.replaceWithEmojis(MainActivity.this,
                            "AXEmojiView " + emoji, 20));

                    btn.setText("Start Emoji Activity " + emoji);
                });

        btn.setOnClickListener(view -> {
            UI.mEmojiView = mEmojiView.isChecked();
            UI.mSingleEmojiView = mSingleEmojiView.isChecked();
            UI.mStickerView = mStickerView.isChecked();
            UI.mCustomView = mCustomView.isChecked();
            UI.mFooterView = mFooterView.isChecked();
            UI.mCustomFooter = mCustomFooterView.isChecked();
            UI.mWhiteCategory = mWhiteCategory.isChecked();

            if (mDarkTheme.isChecked()) {
                UI.loadDarkTheme();

                Intent intent = new Intent(MainActivity.this, DarkActivity.class);
                MainActivity.this.startActivity(intent);
            } else {
                UI.loadTheme();

                Intent intent = new Intent(MainActivity.this,
                        (mPopupView.isChecked() ? EmojiPopupViewActivity.class : EmojiActivity.class));
                intent.putExtra("load", true);
                MainActivity.this.startActivity(intent);
            }
        });
    }

}
