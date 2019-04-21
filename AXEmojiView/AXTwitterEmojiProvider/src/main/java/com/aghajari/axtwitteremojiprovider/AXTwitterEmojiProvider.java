package com.aghajari.axtwitteremojiprovider;

import android.support.annotation.NonNull;

import com.aghajari.axemojiview.emoji.EmojiCategory;
import com.aghajari.axemojiview.emoji.EmojiProvider;
import com.aghajari.axtwitteremojiprovider.category.ActivityCategory;
import com.aghajari.axtwitteremojiprovider.category.FlagsCategory;
import com.aghajari.axtwitteremojiprovider.category.FoodCategory;
import com.aghajari.axtwitteremojiprovider.category.NatureCategory;
import com.aghajari.axtwitteremojiprovider.category.ObjectsCategory;
import com.aghajari.axtwitteremojiprovider.category.PeopleCategory;
import com.aghajari.axtwitteremojiprovider.category.SymbolsCategory;
import com.aghajari.axtwitteremojiprovider.category.TravelCategory;


public final class AXTwitterEmojiProvider implements EmojiProvider {
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
