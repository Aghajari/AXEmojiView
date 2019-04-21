package com.aghajari.axiosemojiprovider;

import android.support.annotation.NonNull;
import com.aghajari.axemojiview.emoji.EmojiCategory;
import com.aghajari.axemojiview.emoji.EmojiProvider;
import com.aghajari.axiosemojiprovider.category.ActivityCategory;
import com.aghajari.axiosemojiprovider.category.FlagsCategory;
import com.aghajari.axiosemojiprovider.category.FoodCategory;
import com.aghajari.axiosemojiprovider.category.NatureCategory;
import com.aghajari.axiosemojiprovider.category.ObjectsCategory;
import com.aghajari.axiosemojiprovider.category.PeopleCategory;
import com.aghajari.axiosemojiprovider.category.SymbolsCategory;
import com.aghajari.axiosemojiprovider.category.TravelCategory;

public final class AXIOSEmojiProvider implements EmojiProvider {
  @Override @NonNull public EmojiCategory[] getCategories() {
    return new EmojiCategory[] {
      new PeopleCategory(),
      new NatureCategory(),
      new FoodCategory(),
      new ActivityCategory(),
      new TravelCategory(),
      new ObjectsCategory(),
      new SymbolsCategory(),
      new FlagsCategory()
    };
  }
}
