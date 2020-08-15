package com.aghajari.emojiview.listener;

import com.aghajari.emojiview.view.AXEmojiBase;
import com.aghajari.emojiview.view.AXEmojiPager;

public interface OnEmojiPagerPageChanged {
        void onPageChanged(AXEmojiPager emojiPager, AXEmojiBase base, int position);
}
