package com.aghajari.axemojiview.adapters;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.aghajari.axemojiview.AXEmojiManager;
import com.aghajari.axemojiview.R;
import com.aghajari.axemojiview.sticker.RecentSticker;
import com.aghajari.axemojiview.sticker.RecentStickerManager;
import com.aghajari.axemojiview.sticker.StickerProvider;
import com.aghajari.axemojiview.utils.Utils;
import com.aghajari.axemojiview.view.AXEmojiLayout;


public class AXCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    AXEmojiLayout pager;
    StickerProvider provider;
    boolean recent;
    public AXCategoryAdapter(AXEmojiLayout pager, StickerProvider provider, RecentSticker RecentStickerManager){
        recent = !RecentStickerManager.isEmptyA() && provider.isRecentEnabled();
        this.pager=pager;
        this.provider=provider;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int iconSize = Utils.dpToPx(viewGroup.getContext(),24);
        AXEmojiLayout layout = new AXEmojiLayout(viewGroup.getContext());
        AppCompatImageView icon = new AppCompatImageView(viewGroup.getContext());
        layout.addView(icon,new AXEmojiLayout.LayoutParams(Utils.dpToPx(viewGroup.getContext(),7),Utils.dpToPx(viewGroup.getContext(),7),iconSize,iconSize));
        layout.setLayoutParams(new ViewGroup.LayoutParams(Utils.dpToPx(viewGroup.getContext(),38),Utils.dpToPx(viewGroup.getContext(),38)));

        View selection = new View(viewGroup.getContext());
        layout.addView(selection,new AXEmojiLayout.LayoutParams(
                0,Utils.dpToPx(viewGroup.getContext(),36),Utils.dpToPx(viewGroup.getContext(),38),Utils.dpToPx(viewGroup.getContext(),2)));
        selection.setBackgroundColor(AXEmojiManager.getStickerViewTheme().getSelectionColor());
        selection.setVisibility(View.GONE);
        return  new RecyclerView.ViewHolder(layout){};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        boolean selected = pager.getPageIndex()==i;
        AXEmojiLayout layout = (AXEmojiLayout) viewHolder.itemView;
        AppCompatImageView icon = (AppCompatImageView) layout.getChildAt(0);
        View selection = (View) layout.getChildAt(1);

        if (selected) selection.setVisibility(View.VISIBLE); else selection.setVisibility(View.GONE);

        if (recent && i==0){
            Drawable dr0 = AppCompatResources.getDrawable(layout.getContext(), R.drawable.emoji_recent);
            Drawable dr = dr0.getConstantState().newDrawable();
            if (selected) {
                DrawableCompat.setTint(DrawableCompat.wrap(dr), AXEmojiManager.getStickerViewTheme().getSelectedColor());
            }else{
                DrawableCompat.setTint(DrawableCompat.wrap(dr), AXEmojiManager.getStickerViewTheme().getDefaultColor());
            }
            icon.setImageDrawable(dr);
        }else{
            int i2 = i; if (recent) i2--;
            provider.getLoader().onLoadStickerCategory(icon,provider.getCategories()[i2],selected);
        }

        Utils.setClickEffect(icon,true);

        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                pager.setPageIndex(i);
            }
        };
        icon.setOnClickListener(listener);
        layout.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        if (recent) return  provider.getCategories().length+1;
        return provider.getCategories().length;
    }
}
