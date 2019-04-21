package com.aghajari.axemojiview.adapters;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.aghajari.axemojiview.emoji.Emoji;
import com.aghajari.axemojiview.shared.VariantEmoji;
import com.aghajari.axemojiview.utils.Utils;
import com.aghajari.axemojiview.view.AXEmojiImageView;

public class AXEmojiRecyclerAdapter extends RecyclerView.Adapter<AXEmojiRecyclerAdapter.ViewHolder> {
    Emoji[] emojis;
    int count;
    onEmojiEvents events;
    VariantEmoji variantEmoji;

    public AXEmojiRecyclerAdapter (Emoji[] emojis,onEmojiEvents events,VariantEmoji variantEmoji){
        this.emojis = emojis;
        this.count = emojis.length;
        this.events = events;
        this.variantEmoji = variantEmoji;
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

        View ripple = new View(viewGroup.getContext());
        frameLayout.addView(ripple,new ViewGroup.MarginLayoutParams(cw-dp6*2,cw-dp6*2));
        ((ViewGroup.MarginLayoutParams) ripple.getLayoutParams()).leftMargin=dp6;
        ((ViewGroup.MarginLayoutParams) ripple.getLayoutParams()).topMargin=dp6;

        return new ViewHolder(frameLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        FrameLayout frameLayout = (FrameLayout) viewHolder.itemView;
        final AXEmojiImageView emojiView = (AXEmojiImageView) frameLayout.getChildAt(0);
        View ripple = (View) frameLayout.getChildAt(1);

        Emoji emoji =emojis[i];
        emojiView.setEmoji(variantEmoji.getVariant(emoji));
        //ImageLoadingTask currentTask = new ImageLoadingTask(emojiView);
        //currentTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, emoji, null, null);
        Utils.setClickEffect(ripple,true);

        ripple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (events !=null) events.onClick(emojiView,false,false);
            }
        });
            ripple.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (events!=null) events.onLongClick(emojiView,false,false);
                    return false;
                }
            });
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
