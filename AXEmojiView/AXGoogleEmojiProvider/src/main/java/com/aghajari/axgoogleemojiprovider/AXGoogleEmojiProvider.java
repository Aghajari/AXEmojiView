package com.aghajari.axgoogleemojiprovider;

import android.support.annotation.NonNull;

import com.aghajari.axemojiview.emoji.EmojiCategory;
import com.aghajari.axemojiview.emoji.EmojiProvider;
import com.aghajari.axgoogleemojiprovider.category.ActivityCategory;
import com.aghajari.axgoogleemojiprovider.category.FlagsCategory;
import com.aghajari.axgoogleemojiprovider.category.FoodCategory;
import com.aghajari.axgoogleemojiprovider.category.NatureCategory;
import com.aghajari.axgoogleemojiprovider.category.ObjectsCategory;
import com.aghajari.axgoogleemojiprovider.category.PeopleCategory;
import com.aghajari.axgoogleemojiprovider.category.SymbolsCategory;
import com.aghajari.axgoogleemojiprovider.category.TravelCategory;

public final class AXGoogleEmojiProvider implements EmojiProvider {
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
