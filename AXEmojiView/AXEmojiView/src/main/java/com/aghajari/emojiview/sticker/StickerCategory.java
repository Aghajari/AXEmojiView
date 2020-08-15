package com.aghajari.emojiview.sticker;

import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * Interface for defining a category.
 */
public interface StickerCategory<T> {
  /**
   * Returns all of the stickers it can display.
   */
  @SuppressWarnings("rawtypes")
  @NonNull Sticker[] getStickers();

  /**
   * Returns the icon of the category that should be displayed.
   */
  T getCategoryData();

  /**
   * Return true if you want to build your own CustomView
   */
  boolean useCustomView();

  /**
   * @return add custom view
   */
  View getView(ViewGroup viewGroup);

  /**
   * update your custom view
   */
  void bindView(View view);

  /**
   * set an emptyView if you aren't using custom view
   */
  View getEmptyView(ViewGroup viewGroup);
}
