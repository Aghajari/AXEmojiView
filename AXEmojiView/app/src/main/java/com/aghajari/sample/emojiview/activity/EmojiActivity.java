package com.aghajari.sample.emojiview.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.View;
import android.widget.FrameLayout;

import com.aghajari.emojiview.listener.SimplePopupAdapter;
import com.aghajari.emojiview.view.AXEmojiPopup;

import com.aghajari.emojiview.AXEmojiUtils;
import com.aghajari.emojiview.view.AXEmojiEditText;
import com.aghajari.emojiview.view.AXEmojiPager;
import com.aghajari.emojiview.view.AXEmojiTextView;
import com.aghajari.sample.emojiview.R;
import com.aghajari.sample.emojiview.UI;

public class EmojiActivity extends AppCompatActivity {
    FrameLayout edtParent;
    AXEmojiEditText edt;
    AppCompatImageView emojiImg;
    AXEmojiTextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emoji_activity_layout);


        getSupportActionBar().setTitle(AXEmojiUtils.replaceWithEmojis(this,
                "AXEmojiView "+AXEmojiUtils.getEmojiUnicode(0x1f60d),20));

        // get emoji edit text
        edtParent = findViewById(R.id.edt_parent);
        edt = findViewById(R.id.edt);
        emojiImg = findViewById(R.id.imageView);
        textView = findViewById(R.id.textview);

        AXEmojiPager emojiPager = UI.loadView(this,edt);

        // create emoji popup
        final AXEmojiPopup popup = new AXEmojiPopup(emojiPager);

        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popup.isShowing()) popup.toggle();
            }
        });

        emojiImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.toggle();
            }
        });

        findViewById(R.id.send_emoji).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt.getText().length()>0){
                    textView.setText(edt.getText().toString());
                    edt.setText("");
                }
            }
        });

        popup.setPopupListener(new SimplePopupAdapter() {
            @Override
            public void onShow() {
                Drawable dr = AppCompatResources.getDrawable(EmojiActivity.this, R.drawable.ic_msg_panel_kb);
                DrawableCompat.setTint(DrawableCompat.wrap(dr), Color.BLACK);
                emojiImg.setImageDrawable(dr);
            }

            @Override
            public void onDismiss() {
                Drawable dr = AppCompatResources.getDrawable(EmojiActivity.this, R.drawable.ic_msg_panel_smiles);
                DrawableCompat.setTint(DrawableCompat.wrap(dr), Color.BLACK);
                emojiImg.setImageDrawable(dr);
            }
        });

    }

}
