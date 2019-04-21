package com.aghajari.axemoji;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import com.aghajari.axemojiview.AXEmojiManager;
import com.aghajari.axemojiview.AXEmojiUtils;
import com.aghajari.axemojiview.view.AXEmojiButton;
import com.aghajari.axiosemojiprovider.AXIOSEmojiProvider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AXEmojiManager.install(this,new AXIOSEmojiProvider());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(AXEmojiUtils.replaceWithEmojis(this,
                "AXEmojiView "+AXEmojiUtils.getEmojiUnicode(0x1f60d),20));

        final Spinner spinner = findViewById(R.id.spinner);
        String[] values = new String[]{"IOS Emojis","Google Emojis","Twitter Emojis","Emoji One"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item,values);
        spinner.setAdapter(adapter);

        final Switch mEmojiView = findViewById(R.id.emoji_view_switch);
        final Switch mSingleEmojiView = findViewById(R.id.single_emoji_view_switch);
        final Switch mStickerView = findViewById(R.id.sticker_view_switch);
        final Switch mCustomView = findViewById(R.id.custom_view_switch);
        final Switch mFooterView = findViewById(R.id.footer_switch);
        final Switch mWhiteCategory = findViewById(R.id.theme_switch);

        AXEmojiButton btn = findViewById(R.id.start_emoji_activity);
        btn.setText("Start Emoji Activity "+ AXEmojiUtils.getEmojiUnicode(0x1f60d));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmojiActivity.mEmojiView = mEmojiView.isChecked();
                EmojiActivity.mSingleEmojiView = mSingleEmojiView.isChecked();
                EmojiActivity.mStickerView = mStickerView.isChecked();
                EmojiActivity.mCustomView = mCustomView.isChecked();
                EmojiActivity.mFooterView = mFooterView.isChecked();
                EmojiActivity.mWhiteCategory = mWhiteCategory.isChecked();
                EmojiActivity.mEmojiType = spinner.getSelectedItemPosition();

                Intent intent = new Intent(MainActivity.this, EmojiActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

}
