package com.aghajari.axemojiview.sticker;

import android.support.annotation.NonNull;

import com.aghajari.axemojiview.emoji.Emoji;

import java.util.Collection;

/**
 * Interface for providing some custom implementation for recent emojis.
 *
 * @since 0.2.0
 */
public interface RecentSticker {
  /**
   * Returns the recent sticker. Could be loaded from a database, shared preferences or just hard
   * coded.<br>
   *
   * This method will be called more than one time hence it is recommended to hold a collection of
   * recent sticker.
   *
   * @since 0.2.0
   */
  @NonNull Collection<Sticker> getRecentStickers();

  /**
   * Should add the sticker to the recent ones. After calling this method, {@link #getRecentStickers()}
   * should return the sticker that was just added.
   *
   * @since 0.2.0
   */
  void addSticker(@NonNull Sticker sticker);

  /**
   * Should persist all sticker.
   *
   * @since 0.2.0
   */
  void persist();

  boolean isEmptyA();
}
