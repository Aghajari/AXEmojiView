package com.aghajari.axemojiview.listener;

import android.view.View;

import com.aghajari.axemojiview.emoji.Emoji;

public interface onEmojiActions {
    void onClick (View view, Emoji emoji, boolean fromRecent, boolean fromVariant);
    void onLongClick (View view, Emoji emoji, boolean fromRecent, boolean fromVariant);
}
