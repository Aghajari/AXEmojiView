package com.aghajari.axemojiview.sticker;

import android.support.v7.widget.AppCompatImageView;

public interface StickerLoader {
    void onLoadSticker (AppCompatImageView view, Sticker sticker);
    void onLoadStickerCategory (AppCompatImageView view, StickerCategory stickerCategory,boolean selected);
}
