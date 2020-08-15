package com.aghajari.emojiview.listener;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aghajari.emojiview.sticker.StickerCategory;

public interface StickerViewCreatorListener {
    View onCreateStickerView(@NonNull final Context context, @Nullable final StickerCategory category, final boolean isRecent);
    View onCreateCategoryView(@NonNull final Context context);
}