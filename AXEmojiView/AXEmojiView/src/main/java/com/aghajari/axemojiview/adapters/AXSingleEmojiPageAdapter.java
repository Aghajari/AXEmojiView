package com.aghajari.axemojiview.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aghajari.axemojiview.AXEmojiManager;
import com.aghajari.axemojiview.emoji.Emoji;
import com.aghajari.axemojiview.emoji.EmojiCategory;
import com.aghajari.axemojiview.shared.RecentEmoji;
import com.aghajari.axemojiview.shared.VariantEmoji;
import com.aghajari.axemojiview.utils.Utils;
import com.aghajari.axemojiview.view.AXEmojiImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AXSingleEmojiPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    EmojiCategory[] categories;
    RecentEmoji recentEmoji;
    VariantEmoji variantEmoji;
    onEmojiEvents events;
    public  List<Integer> titlesPosition = new ArrayList<Integer>();
    List<Emoji> emojis = new ArrayList<Emoji>();

    public int getLastEmojiCategoryCount(){
        return categories[categories.length-1].getEmojis().length;
    }

    public int getFirstTitlePosition(){
        return  titlesPosition.get(0);
    }

    public AXSingleEmojiPageAdapter (EmojiCategory[] categories,onEmojiEvents events, RecentEmoji recentEmoji,VariantEmoji variantEmoji){
        this.categories =categories;
        this.recentEmoji = recentEmoji;
        this.variantEmoji =variantEmoji;
        this.events = events;
        calItemsCount();
    }

    public void refresh(){
        titlesPosition.clear();
        emojis.clear();
        calItemsCount();
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       if (i==1) {
           FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
           StaggeredGridLayoutManager.LayoutParams lm = new StaggeredGridLayoutManager.LayoutParams(-1, Utils.dpToPx(viewGroup.getContext(), 28));
           lm.setFullSpan(true);
           frameLayout.setLayoutParams(lm);

           TextView tv = new TextView(viewGroup.getContext());
           frameLayout.addView(tv, new FrameLayout.LayoutParams(-1, -1));
           tv.setTextColor(AXEmojiManager.getInstance().getTheme().getTitleColor());
           tv.setTypeface(AXEmojiManager.getInstance().getTheme().getTitleTypeface());
           tv.setTextSize(16);
           tv.setPadding(Utils.dpToPx(viewGroup.getContext(), 16), Utils.dpToPx(viewGroup.getContext(), 4),
                   Utils.dpToPx(viewGroup.getContext(), 16), Utils.dpToPx(viewGroup.getContext(), 4));

           return new TitleHolder(frameLayout, tv);
       }else if (i==2){
           FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
           StaggeredGridLayoutManager.LayoutParams lm = new StaggeredGridLayoutManager.LayoutParams(-1,Utils.dpToPx(viewGroup.getContext(),38));
           lm.setFullSpan(true);
           frameLayout.setLayoutParams(lm);
           return new SpaceHolder(frameLayout);
       }else{
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

           return new EmojiHolder(frameLayout,emojiView,ripple);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof TitleHolder){
            EmojiCategory category = categories[titlesPosition.indexOf(i)];
            ((TextView)((FrameLayout) viewHolder.itemView).getChildAt(0)).setText(category.getTitle());
        }else if (viewHolder instanceof EmojiHolder){
            FrameLayout frameLayout = (FrameLayout) viewHolder.itemView;
            final AXEmojiImageView emojiView = (AXEmojiImageView) frameLayout.getChildAt(0);
            View ripple = (View) frameLayout.getChildAt(1);

            Utils.setClickEffect(ripple,true);

            Emoji emoji =emojis.get(i);
            if (emoji==null) return;
            emojiView.setEmoji(variantEmoji.getVariant(emoji));
            //ImageLoadingTask currentTask = new ImageLoadingTask(emojiView);
            //currentTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, emoji, null, null);


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
    }

    @Override
    public int getItemCount() {
        return emojis.size();
    }

    @Override
    public int getItemViewType(int position){
        if (position==0) return 2;
        if (titlesPosition.contains(position)) return 1;
        return 0;
    }

    int calItemsCount(){
        emojis.add(null);
        int number = 0;
        Emoji[] recents = new Emoji[recentEmoji.getRecentEmojis().size()];
        recents = recentEmoji.getRecentEmojis().toArray(recents);
        number = number + recents.length;
        emojis.addAll(Arrays.asList(recents));
        for (int i = 0; i <categories.length;i++){
            number++;
            titlesPosition.add(number);
            emojis.add(null);
            number = number + categories[i].getEmojis().length;
            emojis.addAll(Arrays.asList(categories[i].getEmojis()));
        }
     return emojis.size();
    }

    public class TitleHolder extends RecyclerView.ViewHolder{
        TextView tv;
        public TitleHolder(@NonNull View itemView, TextView tv) {
            super(itemView);
            this.tv = tv;
        }
    }

    public class SpaceHolder extends RecyclerView.ViewHolder{
        public SpaceHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class EmojiHolder extends RecyclerView.ViewHolder{
        View ripple;
        AXEmojiImageView imageView;
        public EmojiHolder(@NonNull View itemView, AXEmojiImageView imageView, View ripple) {
            super(itemView);
            this.ripple = ripple;
            this.imageView = imageView;
        }
    }
}
