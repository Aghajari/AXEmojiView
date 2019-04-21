package com.aghajari.axemojiview.adapters;

import android.view.View;

import com.aghajari.axemojiview.view.AXEmojiImageView;

public interface onEmojiEvents {
    void onClick (AXEmojiImageView view, boolean fromRecent, boolean fromVariant);
    void onLongClick (AXEmojiImageView view,boolean fromRecent,boolean fromVariant);
}
