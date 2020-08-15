package com.aghajari.emojiview.sticker;

import androidx.annotation.NonNull;

import java.util.Collection;

/**
 * Interface for providing some custom implementation for recent stickers.
 */
@SuppressWarnings("rawtypes")
public interface RecentSticker {
  /**
   * Returns the recent sticker. Could be loaded from a database, shared preferences or just hard
   * coded.<br>
   *
   * This method will be called more than one time hence it is recommended to hold a collection of
   * recent sticker.
   */
@NonNull Collection<Sticker> getRecentStickers();

  /**
   * Should add the sticker to the recent ones. After calling this method, {@link #getRecentStickers()}
   * should return the sticker that was just added.
   */
  void addSticker(@NonNull Sticker sticker);

  /**
   * Should persist all sticker.
   */
  void persist();

  /**
   * Returns true if recent is empty
   */
  boolean isEmpty();
}
