package com.aghajari.axemojiview.emoji;

import android.support.annotation.NonNull;

public interface EmojiProvider {
  /**
   * @return The Array of categories.
   * @since 0.4.0
   */
  @NonNull EmojiCategory[] getCategories();
}
