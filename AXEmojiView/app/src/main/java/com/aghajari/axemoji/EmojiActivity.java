package com.aghajari.axemoji;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatImageView;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.aghajari.axemoji.customs.LoadingView;
import com.aghajari.axemoji.sticker.WhatsAppProvider;
import com.aghajari.axemojiview.AXEmojiManager;
import com.aghajari.axemojiview.listener.PopupListener;
import com.aghajari.axemojiview.listener.onEmojiPagerPageChanged;
import com.aghajari.axemojiview.listener.onStickerActions;
import com.aghajari.axemojiview.sticker.Sticker;
import com.aghajari.axemojiview.view.AXEmojiBase;
import com.aghajari.axemojiview.view.AXEmojiEditText;
import com.aghajari.axemojiview.view.AXEmojiPager;
import com.aghajari.axemojiview.view.AXEmojiPopup;
import com.aghajari.axemojiview.view.AXEmojiTextView;
import com.aghajari.axemojiview.view.AXEmojiView;
import com.aghajari.axemojiview.view.AXSingleEmojiView;
import com.aghajari.axemojiview.view.AXStickerView;
import com.aghajari.axgoogleemojiprovider.AXGoogleEmojiProvider;
import com.aghajari.axiosemojiprovider.AXIOSEmojiProvider;
import com.aghajari.axoneemojiprovider.AXEmojiOneProvider;
import com.aghajari.axtwitteremojiprovider.AXTwitterEmojiProvider;


public class EmojiActivity extends AppCompatActivity {
    FrameLayout edtParent;
    AXEmojiEditText edt;
    AppCompatImageView emojiImg;
    AXEmojiTextView textView;

    public static boolean mEmojiView;
    public static boolean mSingleEmojiView;
    public static boolean mStickerView;
    public static boolean mCustomView;
    public static boolean mFooterView;
    public static boolean mWhiteCategory;
    public static int mEmojiType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emoji_activity_layout);

        // install emoji provider
        switch (mEmojiType){
            case 0: //IOS
                AXEmojiManager.install(this, new AXIOSEmojiProvider());
                break;
            case 1: //Google
                AXEmojiManager.install(this, new AXGoogleEmojiProvider());
                break;
            case 2: //Twitter
                AXEmojiManager.install(this, new AXTwitterEmojiProvider());
                break;
            case 3: //Emoji One
                AXEmojiManager.install(this, new AXEmojiOneProvider());
                break;
        }

        // set EmojiView Theme
        AXEmojiManager.getTheme().setFooterEnabled(mFooterView);
        AXEmojiManager.getTheme().setSelectionColor(0xffFF4081);
        AXEmojiManager.getTheme().setFooterSelectedItemColor(0xffFF4081);
        AXEmojiManager.getStickerViewTheme().setSelectionColor(0xffFF4081);

        if (mWhiteCategory) {
            AXEmojiManager.getTheme().setSelectionColor(Color.TRANSPARENT);
            AXEmojiManager.getTheme().setSelectedColor(0xffFF4081);
            AXEmojiManager.getTheme().setCategoryColor(Color.WHITE);
            AXEmojiManager.getTheme().setFooterBackgroundColor(Color.WHITE);
            AXEmojiManager.getTheme().setAlwaysShowDivider(true);

            AXEmojiManager.getStickerViewTheme().setSelectedColor(0xffFF4081);
            AXEmojiManager.getStickerViewTheme().setCategoryColor(Color.WHITE);
            AXEmojiManager.getStickerViewTheme().setAlwaysShowDivider(true);
        }

        // get emoji edit text
        edtParent = findViewById(R.id.edt_parent);
        edt = findViewById(R.id.edt);
        emojiImg = findViewById(R.id.imageView);
        textView = findViewById(R.id.textview);

        AXEmojiPager emojiPager = new AXEmojiPager(this);


        if (mSingleEmojiView) {
            /**
             * add single emoji view
             */
            AXSingleEmojiView singleEmojiView = new AXSingleEmojiView(this);
            emojiPager.addPage(singleEmojiView, R.drawable.ic_msg_panel_smiles);
        }

        if (mEmojiView) {
            /**
             * add emoji view (with viewpager)
             */
            AXEmojiView emojiView = new AXEmojiView(this);
            emojiPager.addPage(emojiView, R.drawable.ic_msg_panel_smiles);
        }

        if (mStickerView) {
            /**
             * add Sticker View
             */
            AXStickerView stickerView = new AXStickerView(this, "stickers", new WhatsAppProvider());
            emojiPager.addPage(stickerView, R.drawable.ic_msg_panel_stickers);

            //add sticker click listener
            stickerView.setOnStickerActionsListener(new onStickerActions() {
                @Override
                public void onClick(View view, Sticker sticker, boolean fromRecent) {
                    Toast.makeText(view.getContext(),sticker.toString()+" clicked!",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLongClick(View view, Sticker sticker, boolean fromRecent) {

                }
            });
        }

        if (mCustomView){
            emojiPager.addPage(new LoadingView(this), R.drawable.msg_round_load_m);
        }


        /**
         * add custom page (LoadingView)
         */
        //emojiPager.addPage(new LoadingView(this), com.aghajari.axiosemojiprovider.R.drawable.abc_btn_check_to_on_mtrl_015);

        // set target emoji edit text to emojiViewPager
        emojiPager.setEditText(edt);
        emojiPager.setSwipeWithFingerEnabled(true);

        emojiPager.setLeftIcon(R.drawable.ic_ab_search);
        emojiPager.setOnFooterItemClicked(new AXEmojiPager.onFooterItemClicked() {
            @Override
            public void onClick(boolean leftIcon) {
                if (leftIcon) Toast.makeText(EmojiActivity.this,"Search Clicked",Toast.LENGTH_SHORT).show();
            }
        });

        // create emoji popup
        final AXEmojiPopup popup = new AXEmojiPopup(emojiPager);

        emojiImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.toggle();
            }
        });

        if (mFooterView) {
            emojiPager.setOnEmojiPageChangedListener(new onEmojiPagerPageChanged() {
                @Override
                public void onPageChanged(AXEmojiPager emojiPager, AXEmojiBase base, int position) {
                    if (AXEmojiManager.isAXEmojiView(base)) {
                        emojiPager.getFooterRightView().setVisibility(View.VISIBLE);
                    } else {
                        emojiPager.getFooterRightView().setVisibility(View.GONE);
                    }
                }
            });
        }

        findViewById(R.id.send_emoji).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt.getText().length()>0){
                    textView.setText(edt.getText());
                    edt.setText("");
                }
            }
        });

        popup.setPopupListener(new PopupListener() {
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

            @Override
            public void onKeyboardClosed() {}
            @Override
            public void onKeyboardOpened(int height) {}
        });


    }

}
