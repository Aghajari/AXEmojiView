package com.aghajari.emojiview.emoji.iosprovider;

import android.content.Context;
import android.graphics.Paint;
import android.text.Spannable;

import androidx.annotation.NonNull;

import com.aghajari.emojiview.R;
import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.emoji.EmojiCategory;
import com.aghajari.emojiview.emoji.EmojiData;
import com.aghajari.emojiview.emoji.EmojiProvider;
import com.aghajari.emojiview.utils.EmojiReplacer;

public final class AXIOSEmojiProvider extends AXIOSEmojiReplacer implements EmojiProvider {

  public static EmojiCategory[] emojiCategories = null;

  public AXIOSEmojiProvider (Context context){
    AXIOSEmojiLoader.init(context);

    if (emojiCategories == null) {
      int[] icons = new int[]{
              R.drawable.emoji_ios_category_people,
              R.drawable.emoji_ios_category_nature,
              R.drawable.emoji_ios_category_food,
              R.drawable.emoji_ios_category_activity,
              R.drawable.emoji_ios_category_travel,
              R.drawable.emoji_ios_category_objects,
              R.drawable.emoji_ios_category_symbols,
              R.drawable.emoji_ios_category_flags
      };

      emojiCategories = new EmojiCategory[EmojiData.titles.length];
      for (int c = 0; c < EmojiData.titles.length; c++) {
        emojiCategories[c] = new AXIOSEmojiCategoty(c, icons[c]);
      }
    }
  }

  public AXIOSEmojiProvider (Context context,int[] icons){
    AXIOSEmojiLoader.init(context);

    if (emojiCategories == null) {
      emojiCategories = new EmojiCategory[EmojiData.titles.length];
      for (int c = 0; c < EmojiData.titles.length; c++) {
        emojiCategories[c] = new AXIOSEmojiCategoty(c, icons[c]);
      }
    }
  }

  @Override @NonNull public EmojiCategory[] getCategories() {
    return emojiCategories;
  }

  public static class AXIOSEmojiCategoty implements EmojiCategory{
     Emoji[] DATA;
     String title;
     int icon;

    public AXIOSEmojiCategoty (int i,int icon){
      DATA = new Emoji[EmojiData.releaseData[i].length];
      for (int j = 0 ; j < EmojiData.releaseData[i].length;j++){
        DATA[j] = new AXIOSEmoji(EmojiData.releaseData[i][j]);
      }
      title = EmojiData.titles[i];
      this.icon = icon;
    }

    @NonNull
    @Override
    public Emoji[] getEmojis() {
      return DATA;
    }

    @Override
    public int getIcon() {
      return icon;
    }

    @Override
    public CharSequence getTitle() {
      return title;
    }
  }
}
