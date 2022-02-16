package com.aghajari.sample.emojiview.activity;

import android.os.Bundle;

import com.aghajari.sample.emojiview.R;

public class DarkActivity extends EmojiPopupViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emoji_activity_layout_view_dark);
        init(0xFF677382);
    }

}
