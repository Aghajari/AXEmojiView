package com.aghajari.emojiview.emoji;

import androidx.annotation.NonNull;

public interface EmojiProvider {
  @NonNull EmojiCategory[] getCategories();
}
