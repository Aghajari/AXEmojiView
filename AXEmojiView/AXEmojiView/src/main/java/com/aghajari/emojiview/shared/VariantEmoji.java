package com.aghajari.emojiview.shared;

import androidx.annotation.NonNull;

import com.aghajari.emojiview.emoji.Emoji;

/**
 * Interface for providing some custom implementation for variant emojis.
 */
public interface VariantEmoji {
  /**
   * Returns the variant for the passed emoji. Could be loaded from a database, shared preferences or just hard
   * coded.<br>
   *
   * This method will be called more than one time hence it is recommended to hold a collection of
   * desired emojis.
   *
   * @param desiredEmoji The emoji to retrieve the variant for. If none is found,
   *                     the passed emoji should be returned.
   */
  @NonNull
  Emoji getVariant(Emoji desiredEmoji);

  /**
   * Should add the emoji to the variants. After calling this method, {@link #getVariant(Emoji)}
   * should return the emoji that was just added.
   *
   * @param newVariant The new variant to save.
   */
  void addVariant(@NonNull Emoji newVariant);

  /**
   * Should persist all emojis.
   */
  void persist();
}
