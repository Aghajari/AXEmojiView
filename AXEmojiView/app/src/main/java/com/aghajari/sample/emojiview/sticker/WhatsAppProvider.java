package com.aghajari.sample.emojiview.sticker;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.View;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.sticker.Sticker;
import com.aghajari.emojiview.sticker.StickerCategory;
import com.aghajari.emojiview.sticker.StickerLoader;
import com.aghajari.emojiview.sticker.StickerProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class WhatsAppProvider implements StickerProvider {
    @NonNull
    @Override
    public StickerCategory<?>[] getCategories() {
        return new StickerCategory[]{
                new ShopStickers(),
                new WhatsAppStickers(),
                new WhatsAppStickers(),
                new WhatsAppStickers()
        };
    }

    @NonNull
    @Override
    public StickerLoader getLoader() {
        return new StickerLoader() {
            @Override
            public void onLoadSticker(View view, Sticker<?> sticker) {
                if (sticker.isInstance(Integer.class)) {
                    Glide.with(view).load((int) sticker.getData()).apply(RequestOptions.fitCenterTransform()).into((AppCompatImageView)view);
                }

            }

            @Override
            public void onLoadStickerCategory(View view, StickerCategory<?> stickerCategory, boolean selected) {
              try{
                  if (stickerCategory instanceof ShopStickers) {
                      Drawable dr0 = AppCompatResources.getDrawable(view.getContext(), (Integer) stickerCategory.getCategoryData());
                      Drawable dr = dr0.getConstantState().newDrawable();
                      if (selected) {
                          DrawableCompat.setTint(DrawableCompat.wrap(dr), AXEmojiManager.getStickerViewTheme().getSelectedColor());
                      }else{
                          DrawableCompat.setTint(DrawableCompat.wrap(dr), AXEmojiManager.getStickerViewTheme().getDefaultColor());
                      }
                      ((AppCompatImageView)view).setImageDrawable(dr);
                  } else {
                      Glide.with(view).load(Integer.valueOf(stickerCategory.getCategoryData().toString())).apply(RequestOptions.fitCenterTransform()).into((AppCompatImageView)view);
                  }
              }catch (Exception ignore){
              }
            }
        };
    }

    @Override
    public boolean isRecentEnabled() {
        return true;
    }
}
