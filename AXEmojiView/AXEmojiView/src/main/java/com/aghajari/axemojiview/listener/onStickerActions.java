package com.aghajari.axemojiview.listener;

import android.view.View;
import com.aghajari.axemojiview.sticker.Sticker;

public interface onStickerActions {
    void onClick(View view, Sticker sticker, boolean fromRecent);
    void onLongClick(View view, Sticker sticker, boolean fromRecent);
}
