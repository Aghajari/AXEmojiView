package com.aghajari.emojiview.emoji;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

/**
 * Interface for defining emoji category.
 */
public interface EmojiCategory {
  /**
   * Returns all of the emojis it can display.
   */
  @NonNull Emoji[] getEmojis();

  /**
   * Returns the icon of the category that should be displayed.
   */
  @DrawableRes int getIcon();

  /**
   * Returns title of the category
   */
  CharSequence getTitle();
}
