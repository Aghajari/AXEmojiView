package com.aghajari.axemojiview.sticker;

import android.support.annotation.NonNull;


public interface StickerProvider {
  /**
   * @return The Array of categories.
   * @since 0.4.0
   */
  @NonNull StickerCategory[] getCategories();

  @NonNull StickerLoader getLoader();

  boolean isRecentEnabled();
}
