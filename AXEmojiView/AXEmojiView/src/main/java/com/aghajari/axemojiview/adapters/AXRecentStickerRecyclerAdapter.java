package com.aghajari.axemojiview.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.aghajari.axemojiview.sticker.RecentSticker;
import com.aghajari.axemojiview.sticker.RecentStickerManager;
import com.aghajari.axemojiview.sticker.Sticker;
import com.aghajari.axemojiview.sticker.StickerLoader;
import com.aghajari.axemojiview.utils.Utils;
import com.aghajari.axemojiview.view.AXEmojiImageView;

public class AXRecentStickerRecyclerAdapter extends RecyclerView.Adapter<AXRecentStickerRecyclerAdapter.ViewHolder> {
    RecentSticker recent;
    onStickerEvents events;
    StickerLoader loader;

    public AXRecentStickerRecyclerAdapter(RecentSticker recent, onStickerEvents events, StickerLoader loader){
        this.recent = recent;
        this.events = events;
        this.loader = loader;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
        AXEmojiImageView emojiView = new AXEmojiImageView(viewGroup.getContext());
        int cw = Utils.getStickerColumnWidth(viewGroup.getContext());
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(cw,cw));
        frameLayout.addView(emojiView);

        int dp6=Utils.dpToPx(viewGroup.getContext(),6);
        emojiView.setPadding(dp6,dp6,dp6,dp6);

        View ripple = new View(viewGroup.getContext());
        frameLayout.addView(ripple,new ViewGroup.MarginLayoutParams(cw,cw));

        return new ViewHolder(frameLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        FrameLayout frameLayout = (FrameLayout) viewHolder.itemView;
        final AppCompatImageView stickerView = (AppCompatImageView) frameLayout.getChildAt(0);
        View ripple = (View) frameLayout.getChildAt(1);

        final Sticker sticker = (Sticker) recent.getRecentStickers().toArray()[i];
        loader.onLoadSticker(stickerView,sticker);

        Utils.setClickEffect(ripple,false);

        ripple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (events !=null) events.onClick(stickerView,sticker,true);
            }
        });
            ripple.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (events!=null) events.onLongClick(stickerView,sticker,true);
                    return false;
                }
            });
    }

    @Override
    public int getItemCount() {
        return recent.getRecentStickers().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
