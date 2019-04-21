package com.aghajari.axemojiview.sticker;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * Interface for defining a category.
 *
 * @since 0.4.0
 */
public interface StickerCategory<T> {
  /**
   * Returns all of the stickers it can display.
   *
   * @since 0.4.0
   */
  @NonNull Sticker[] getStickers();

  /**
   * Returns the icon of the category that should be displayed.
   *
   * @since 0.4.0
   */
  T getCategoryData();

  boolean useCustomView();

  /**
   * @return add custom view
   */
  View getView(ViewGroup viewGroup);

  /**
   * update your custom view
   */
  void bindView(View view);
}
