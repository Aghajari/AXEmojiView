package com.aghajari.axemojiview.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aghajari.axemojiview.AXEmojiManager;
import com.aghajari.axemojiview.adapters.AXStickerViewPagerAdapter;
import com.aghajari.axemojiview.adapters.onStickerEvents;
import com.aghajari.axemojiview.listener.onStickerActions;
import com.aghajari.axemojiview.sticker.RecentSticker;
import com.aghajari.axemojiview.sticker.RecentStickerManager;
import com.aghajari.axemojiview.sticker.Sticker;
import com.aghajari.axemojiview.sticker.StickerProvider;
import com.aghajari.axemojiview.utils.Utils;

public class AXStickerView extends AXEmojiLayout {
    StickerProvider stickerProvider;
    RecentSticker recent;

    final String type;

    public AXStickerView(Context context,String type,StickerProvider stickerProvider) {
        super(context);
        this.type = type;
        this.stickerProvider = stickerProvider;

        init();
    }

    public final String getType(){
        return type;
    }



    AXCategoryRecycler categoryViews;
    ViewPager vp;


    onStickerEvents events = new onStickerEvents() {

        @Override
        public void onClick(AppCompatImageView view, Sticker sticker, boolean fromRecent) {
            if (recent!=null) recent.addSticker(sticker);
            if (stickerActions!=null) stickerActions.onClick(view,sticker,fromRecent);
        }

        @Override
        public void onLongClick(AppCompatImageView view, Sticker sticker,boolean fromRecent) {
            if (stickerActions!=null) stickerActions.onLongClick(view,sticker,fromRecent);
        }
    };

    onStickerActions stickerActions=null;

    /**
     * add sticker click and longClick listener
     * @param listener
     */
    public void setOnStickerActionsListener(onStickerActions listener){
        stickerActions = listener;
    }
    public void removeOnStickerActionsListener(){
        stickerActions = null;
    }



    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

        private boolean isShowing = true;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (recyclerView==null){
                if (!AXEmojiManager.getStickerViewTheme().shouldShowAlwaysDivider()) {
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
            if (scrollListener2!=null) scrollListener2.onScrolled(recyclerView,dx,dy);

            if (!AXEmojiManager.getStickerViewTheme().shouldShowAlwaysDivider()) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int firstVisibleItemPosition = 1;
                if (layoutManager instanceof GridLayoutManager) {
                    firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
                } else if (layoutManager instanceof LinearLayoutManager) {
                    firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
                }

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
        if (AXEmojiManager.getInstance().getRecentSticker()!=null){
            recent = AXEmojiManager.getInstance().getRecentSticker();
        }else {
            recent = new RecentStickerManager(getContext(), type);
        }

        vp = new ViewPager(getContext());
        this.addView(vp,new AXEmojiLayout.LayoutParams(0, Utils.dpToPx(getContext(),39),-1,-1));
        vp.setAdapter(new AXStickerViewPagerAdapter(events,scrollListener,stickerProvider,recent));
        vp.setPadding(0,0,0,Utils.dpToPx(getContext(),39));

        categoryViews = new AXCategoryRecycler(getContext(),this,stickerProvider,recent);
        this.addView(categoryViews,new AXEmojiLayout.LayoutParams(0,0,-1,Utils.dpToPx(getContext(),39)));

        this.setBackgroundColor(AXEmojiManager.getStickerViewTheme().getBackgroundColor());
        categoryViews.setBackgroundColor(AXEmojiManager.getStickerViewTheme().getBackgroundColor());

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (pagerListener2!=null) pagerListener2.onPageScrolled(i,v,i1);
            }

            @Override
            public void onPageSelected(int i) {
                vp.setCurrentItem(i,true);
                if (!AXEmojiManager.getStickerViewTheme().shouldShowAlwaysDivider()) {
                    if (((AXStickerViewPagerAdapter) vp.getAdapter()).recyclerViews.size() > i) {
                        View view = ((AXStickerViewPagerAdapter) vp.getAdapter()).recyclerViews.get(i);
                        if (view instanceof RecyclerView)
                            scrollListener.onScrolled((RecyclerView) view, 0, 1);
                    } else {
                        scrollListener.onScrolled(null, 0, 1);
                    }
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
        if (!AXEmojiManager.getStickerViewTheme().shouldShowAlwaysDivider()) {
            if (((AXStickerViewPagerAdapter) vp.getAdapter()).recyclerViews.size() > index) {
                View view = ((AXStickerViewPagerAdapter) vp.getAdapter()).recyclerViews.get(index);
                if (view instanceof RecyclerView)
                    scrollListener.onScrolled((RecyclerView) view, 0, 1);
            } else {
                scrollListener.onScrolled(null, 0, 1);
            }
        }
        categoryViews.setPageIndex(index);
    }

    @Override
    public void dismiss(){
        recent.persist();
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
    public int getPageIndex(){
        return vp.getCurrentItem();
    }

    @Override
    protected void addItemDecoration(RecyclerView.ItemDecoration itemDecoration){
        ((AXStickerViewPagerAdapter) vp.getAdapter()).itemDecoration = itemDecoration;
        for (int i = 0 ; i<((AXStickerViewPagerAdapter) vp.getAdapter()).recyclerViews.size();i++){
            View view = ((AXStickerViewPagerAdapter) vp.getAdapter()).recyclerViews.get(i);
            if (view instanceof RecyclerView) ((RecyclerView) view).addItemDecoration(itemDecoration);
        }
    }

    @Override
    protected void refresh(){
        super.refresh();
        for(int i=0; i<((AXStickerViewPagerAdapter)vp.getAdapter()).recyclerViews.size();i++){
            View view = ((AXStickerViewPagerAdapter) vp.getAdapter()).recyclerViews.get(i);
            if (view instanceof RecyclerView) ((RecyclerView) view).getAdapter().notifyDataSetChanged();
        }
        vp.getAdapter().notifyDataSetChanged();
        categoryViews.removeAllViews();
        categoryViews.init(stickerProvider);
        vp.setCurrentItem(0,false);
        if (!AXEmojiManager.getStickerViewTheme().shouldShowAlwaysDivider()) scrollListener.onScrolled(null,0,1);
        categoryViews.setPageIndex(0);
    }

    public ViewPager getViewPager() {
        return vp;
    }

}
