package com.aghajari.emojiview.variant;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.listener.OnEmojiActions;
import com.aghajari.emojiview.view.AXEmojiImageView;

public abstract class AXEmojiVariantPopup {

    public AXEmojiVariantPopup(@NonNull final View rootView, @Nullable final OnEmojiActions listener) {}

    public abstract boolean isShowing();
    public abstract void show(@NonNull final AXEmojiImageView clickedImage, @NonNull final Emoji emoji, boolean fromRecent);
    public abstract void dismiss();

    public boolean onTouch(MotionEvent event, RecyclerView recyclerView){
        return false;
    }
}
