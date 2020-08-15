package com.aghajari.emojiview.listener;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aghajari.emojiview.variant.AXEmojiVariantPopup;

public interface EmojiVariantCreatorListener {
    AXEmojiVariantPopup create(@NonNull final View rootView, @Nullable final OnEmojiActions listener);
}
