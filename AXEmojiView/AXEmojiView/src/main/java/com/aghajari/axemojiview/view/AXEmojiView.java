package com.aghajari.axemojiview.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.aghajari.axemojiview.AXEmojiManager;
import com.aghajari.axemojiview.AXEmojiUtils;
import com.aghajari.axemojiview.adapters.AXEmojiViewPagerAdapter;
import com.aghajari.axemojiview.adapters.onEmojiEvents;
import com.aghajari.axemojiview.emoji.Emoji;
import com.aghajari.axemojiview.listener.onEmojiActions;
import com.aghajari.axemojiview.shared.RecentEmoji;
import com.aghajari.axemojiview.shared.RecentEmojiManager;
import com.aghajari.axemojiview.shared.VariantEmoji;
import com.aghajari.axemojiview.shared.VariantEmojiManager;
import com.aghajari.axemojiview.utils.Utils;

public class AXEmojiView extends  AXEmojiLayout {
    public AXEmojiView(Context context) {
        super(context);
        init();
    }

    AXCategoryViews categoryViews;
    ViewPager vp;
    RecentEmoji recent;
    VariantEmoji variant;
    AXEmojiVariantPopup variantPopup;

    onEmojiEvents events = new onEmojiEvents() {
        @Override
        public void onClick(AXEmojiImageView view,boolean fromRecent,boolean fromVariant) {
            if (!fromVariant && variantPopup!=null && variantPopup.isShowing) return;
            Emoji emoji = view.currentEmoji;
            recent.addEmoji(emoji);
            if (editText!=null) AXEmojiUtils.input(editText,emoji);

            if (!fromRecent && ((AXEmojiViewPagerAdapter) vp.getAdapter()).add==1)
            ((AXEmojiViewPagerAdapter) vp.getAdapter()).recyclerViews.get(0).getAdapter().notifyDataSetChanged();

            variant.addVariant(emoji);
            if (variantPopup!=null) variantPopup.dismiss();

            if (emojiActions!=null) emojiActions.onClick(view,view.currentEmoji,fromRecent,fromVariant);
        }

        @Override
        public void onLongClick(AXEmojiImageView view,boolean fromRecent,boolean fromVariant) {

            if ((!fromRecent || AXEmojiManager.getInstance().isRecentVariantEnabled()) && variantPopup!=null) {
                if (view.currentEmoji.getBase().hasVariants())
                    variantPopup.show(view, view.currentEmoji);
            }

            if (emojiActions!=null) emojiActions.onLongClick(view,view.currentEmoji,fromRecent,fromVariant);
        }
    };

    onEmojiActions emojiActions=null;

    /**
     * add emoji click and longClick listener
     * @param listener
     */
    public void setOnEmojiActionsListener(onEmojiActions listener){
        emojiActions = listener;
    }
    public void removeOnEmojiActionsListener(){
        emojiActions = null;
    }


    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

        private boolean isShowing = true;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (recyclerView==null){
                if (!AXEmojiManager.getEmojiViewTheme().shouldShowAlwaysDivider()) {
                    if (!isShowing) {
                        isShowing = true;
                        categoryViews.Divider.setVisibility(GONE);
                    }
                }
                return;
            }
            if (dy==0) return;
            if (dy==1) dy=0;
            super.onScrolled(recyclerView, dx, dy);
            if (recyclerView!=null && scrollListener2!=null) scrollListener2.onScrolled(recyclerView,dx,dy);

            if (!AXEmojiManager.getEmojiViewTheme().shouldShowAlwaysDivider()) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
                int visibleItemCount = layoutManager.getChildCount();
                if ((visibleItemCount > 0 && (firstVisibleItemPosition) == 0)) {
                    if (!isShowing) {
                        isShowing = true;
                        categoryViews.Divider.setVisibility(GONE);
                    }
                } else {
                    if (isShowing) {
                        isShowing = false;
                        categoryViews.Divider.setVisibility(VISIBLE);
                    }
                }
            }
        }
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (scrollListener2!=null) scrollListener2.onScrollStateChanged(recyclerView,newState);
        }
    };

    RecyclerView.OnScrollListener scrollListener2 = null;

    private void init(){
        if (AXEmojiManager.getInstance().getRecentEmoji()!=null){
            recent = AXEmojiManager.getInstance().getRecentEmoji();
        }else {
            recent = new RecentEmojiManager(getContext());
        }
        if (AXEmojiManager.getInstance().getVariantEmoji()!=null){
            variant = AXEmojiManager.getInstance().getVariantEmoji();
        }else {
            variant = new VariantEmojiManager(getContext());
        }

        vp = new ViewPager(getContext());
        this.addView(vp,new AXEmojiLayout.LayoutParams(0, Utils.dpToPx(getContext(),39),-1,-1));
        vp.setAdapter(new AXEmojiViewPagerAdapter(events,scrollListener,recent,variant));
        vp.setPadding(0,0,0,Utils.dpToPx(getContext(),39));

        categoryViews = new AXCategoryViews(getContext(),this,recent);
        this.addView(categoryViews,new AXEmojiLayout.LayoutParams(0,0,-1,Utils.dpToPx(getContext(),39)));

        this.setBackgroundColor(AXEmojiManager.getEmojiViewTheme().getBackgroundColor());
        categoryViews.setBackgroundColor(AXEmojiManager.getEmojiViewTheme().getBackgroundColor());

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (pagerListener2!=null) pagerListener2.onPageScrolled(i,v,i1);
            }

            @Override
            public void onPageSelected(int i) {
                vp.setCurrentItem(i,true);
                if (((AXEmojiViewPagerAdapter)vp.getAdapter()).recyclerViews.size()>i) {
                    scrollListener.onScrolled(((AXEmojiViewPagerAdapter) vp.getAdapter()).recyclerViews.get(i), 0, 1);
                }else{
                    scrollListener.onScrolled(null,0,1);
                }
                categoryViews.setPageIndex(i);
                if (pagerListener2!=null) pagerListener2.onPageSelected(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (pagerListener2!=null) pagerListener2.onPageScrollStateChanged(i);
            }
        });

    }

    ViewPager.OnPageChangeListener pagerListener2 = null;

    @Override
    public void setPageIndex(int index){
        vp.setCurrentItem(index,true);
        if (!AXEmojiManager.getEmojiViewTheme().shouldShowAlwaysDivider()) {
            if (((AXEmojiViewPagerAdapter) vp.getAdapter()).recyclerViews.size() > index) {
                scrollListener.onScrolled(((AXEmojiViewPagerAdapter) vp.getAdapter()).recyclerViews.get(index), 0, 1);
            } else {
                scrollListener.onScrolled(null, 0, 1);
            }
        }
        categoryViews.setPageIndex(index);
    }

    @Override
    public void dismiss(){
        if (variantPopup!=null) variantPopup.dismiss();
        recent.persist();
        variant.persist();
    }

    @Override
    public void setEditText (EditText editText){
        super.setEditText(editText);
        variantPopup = new AXEmojiVariantPopup(editText.getRootView(),events);
    }

    @Override
    protected void setScrollListener(RecyclerView.OnScrollListener listener){
        super.setScrollListener(listener);
        scrollListener2 = listener;
    }

    @Override
    protected void setPageChanged(ViewPager.OnPageChangeListener listener){
        super.setPageChanged(listener);
        pagerListener2 = listener;
    }

    @Override
    protected void addItemDecoration(RecyclerView.ItemDecoration itemDecoration){
        ((AXEmojiViewPagerAdapter) vp.getAdapter()).itemDecoration = itemDecoration;
        for (int i = 0 ; i<((AXEmojiViewPagerAdapter) vp.getAdapter()).recyclerViews.size();i++){
            ((AXEmojiViewPagerAdapter) vp.getAdapter()).recyclerViews.get(i).addItemDecoration(itemDecoration);
        }
    }

    @Override
    protected void refresh(){
        super.refresh();
        vp.getAdapter().notifyDataSetChanged();
        categoryViews.removeAllViews();
        categoryViews.init();
        vp.setCurrentItem(0,false);
        if (!AXEmojiManager.getEmojiViewTheme().shouldShowAlwaysDivider())scrollListener.onScrolled(null,0,1);
        categoryViews.setPageIndex(0);
    }

    @Override
    public int getPageIndex(){
        return vp.getCurrentItem();
    }

    public ViewPager getViewPager() {
        return vp;
    }
}
