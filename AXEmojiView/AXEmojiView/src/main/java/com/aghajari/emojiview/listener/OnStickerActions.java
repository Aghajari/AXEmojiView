package com.aghajari.emojiview.listener;

import android.view.View;
import com.aghajari.emojiview.sticker.Sticker;

public interface OnStickerActions {
    void onClick(View view, Sticker sticker, boolean fromRecent);
    void onLongClick(View view, Sticker sticker, boolean fromRecent);
}
