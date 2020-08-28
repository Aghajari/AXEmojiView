package com.aghajari.emojiview.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.listener.OnEmojiActions;
import com.aghajari.emojiview.shared.RecentEmoji;
import com.aghajari.emojiview.shared.VariantEmoji;
import com.aghajari.emojiview.utils.Utils;
import com.aghajari.emojiview.view.AXEmojiImageView;

public class AXRecentEmojiRecyclerAdapter extends RecyclerView.Adapter<AXRecentEmojiRecyclerAdapter.ViewHolder> {
    RecentEmoji recentEmoji;
    OnEmojiActions events;
    VariantEmoji variantEmoji;

    public AXRecentEmojiRecyclerAdapter(RecentEmoji recentEmoji, OnEmojiActions events, VariantEmoji variantEmoji){
        this.recentEmoji = recentEmoji;
        this.events = events;
        this.variantEmoji =variantEmoji;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
        AXEmojiImageView emojiView = new AXEmojiImageView(viewGroup.getContext());
        int cw = Utils.getColumnWidth(viewGroup.getContext());
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(cw,cw));
        frameLayout.addView(emojiView);

        int dp6=Utils.dpToPx(viewGroup.getContext(),6);
        emojiView.setPadding(dp6,dp6,dp6,dp6);

        return new ViewHolder(frameLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        FrameLayout frameLayout = (FrameLayout) viewHolder.itemView;
        final AXEmojiImageView emojiView = (AXEmojiImageView) frameLayout.getChildAt(0);

        Emoji emoji = variantEmoji.getVariant((Emoji) recentEmoji.getRecentEmojis().toArray()[i]);
        emojiView.setEmoji(emoji);
        emojiView.setOnEmojiActions(events,true);


        if (!AXEmojiManager.getInstance().isRecentVariantEnabled()) {
            emojiView.setShowVariants(false);
        }else{
            emojiView.setShowVariants(AXEmojiManager.getTheme().isVariantDividerEnabled());
        }
    }

    @Override
    public int getItemCount() {
        return recentEmoji.getRecentEmojis().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
