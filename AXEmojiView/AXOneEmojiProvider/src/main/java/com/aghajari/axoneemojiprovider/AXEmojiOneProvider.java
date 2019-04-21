package com.aghajari.axoneemojiprovider;

import android.support.annotation.NonNull;

import com.aghajari.axemojiview.emoji.EmojiCategory;
import com.aghajari.axemojiview.emoji.EmojiProvider;
import com.aghajari.axoneemojiprovider.category.ActivityCategory;
import com.aghajari.axoneemojiprovider.category.FlagsCategory;
import com.aghajari.axoneemojiprovider.category.FoodCategory;
import com.aghajari.axoneemojiprovider.category.NatureCategory;
import com.aghajari.axoneemojiprovider.category.ObjectsCategory;
import com.aghajari.axoneemojiprovider.category.PeopleCategory;
import com.aghajari.axoneemojiprovider.category.SymbolsCategory;
import com.aghajari.axoneemojiprovider.category.TravelCategory;

public final class AXEmojiOneProvider implements EmojiProvider {
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
