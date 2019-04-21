package com.aghajari.axemojiview.adapters;

import android.support.v7.widget.AppCompatImageView;

import com.aghajari.axemojiview.sticker.Sticker;
import com.aghajari.axemojiview.view.AXEmojiImageView;

public interface onStickerEvents {
    void onClick(AppCompatImageView view,Sticker sticker, boolean fromRecent);
    void onLongClick(AppCompatImageView view, Sticker sticker, boolean fromRecent);
}
