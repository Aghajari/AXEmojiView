package com.aghajari.axemoji.sticker;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatImageView;

import com.aghajari.axemojiview.AXEmojiManager;
import com.aghajari.axemojiview.sticker.Sticker;
import com.aghajari.axemojiview.sticker.StickerCategory;
import com.aghajari.axemojiview.sticker.StickerLoader;
import com.aghajari.axemojiview.sticker.StickerProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class WhatsAppProvider implements StickerProvider {
    @NonNull
    @Override
    public StickerCategory[] getCategories() {
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
            public void onLoadSticker(AppCompatImageView view, Sticker sticker) {
                Glide.with(view).load(Integer.valueOf(sticker.getData().toString())).apply(RequestOptions.fitCenterTransform()).into(view);
            }

            @Override
            public void onLoadStickerCategory(AppCompatImageView view, StickerCategory stickerCategory, boolean selected) {
              try{
                  if (stickerCategory instanceof ShopStickers) {
                      Drawable dr0 = AppCompatResources.getDrawable(view.getContext(), (Integer) stickerCategory.getCategoryData());
                      Drawable dr = dr0.getConstantState().newDrawable();
                      if (selected) {
                          DrawableCompat.setTint(DrawableCompat.wrap(dr), AXEmojiManager.getInstance().getTheme().getSelectedColor());
                      }else{
                          DrawableCompat.setTint(DrawableCompat.wrap(dr), AXEmojiManager.getInstance().getTheme().getDefaultColor());
                      }
                      view.setImageDrawable(dr);
                  } else {
                      Glide.with(view).load(Integer.valueOf(stickerCategory.getCategoryData().toString())).apply(RequestOptions.fitCenterTransform()).into(view);
                  }
              }catch (Exception e){
              }
            }
        };
    }

    @Override
    public boolean isRecentEnabled() {
        return true;
    }
}
