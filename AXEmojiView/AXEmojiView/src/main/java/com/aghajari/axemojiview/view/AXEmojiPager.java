package com.aghajari.axemojiview.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.aghajari.axemojiview.AXEmojiManager;
import com.aghajari.axemojiview.adapters.AXSingleEmojiPageAdapter;
import com.aghajari.axemojiview.listener.onEmojiPagerPageChanged;
import com.aghajari.axemojiview.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class AXEmojiPager extends AXEmojiLayout {
    boolean isShowing=false;
    boolean footer;

    public AXEmojiPager(Context context) {
        super(context);
        init();
    }

    public AXEmojiPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AXEmojiPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    onEmojiPagerPageChanged pageListener = null;
    public void setOnEmojiPageChangedListener(onEmojiPagerPageChanged listener){
        this.pageListener = listener;
    }

    ViewPager vp;
    private void init(){
        footer = AXEmojiManager.getEmojiViewTheme().isFooterEnabled();

       vp = new ViewPager(getContext()){
            @Override
            public boolean onInterceptTouchEvent(MotionEvent event) {
                if (viewPagerTouchEnabled){
                    return  super.onInterceptTouchEvent(event);
                }else {
                    return false;
                }
            }

            @Override
            public boolean onTouchEvent(MotionEvent event) {
                if (viewPagerTouchEnabled){
                    return  super.onTouchEvent(event);
                }else {
                    return false;
                }
            }
        };

       vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int i, float v, int i1) {

           }

           @Override
           public void onPageSelected(int i) {
               pages.get(i).base.onShow();
               if (footer) {
                   scrollListener.show();
                   footerView.setPageIndex(i);
               }
               if (pageListener!=null) pageListener.onPageChanged(AXEmojiPager.this,pages.get(i).base,i);
           }

           @Override
           public void onPageScrollStateChanged(int i) {

           }
       });
    }

    List<AXPage> pages = new ArrayList<AXPage>();
    public void addPage (AXEmojiBase parent,@DrawableRes int iconRes){
        if (vp.getAdapter()!=null) return;
        pages.add(new AXPage(parent,iconRes));
    }
    public void removePage (int index){
        pages.remove(index);
    }
    public AXEmojiBase getPage(int index){
        return pages.get(index).base;
    }
    public @DrawableRes int getPageIcon(int index){
        return pages.get(index).icon;
    }

    public int getPagesCount(){
        return  pages.size();
    }


    private class AXPage{
        AXEmojiBase base;
        int icon;
        public AXPage(AXEmojiBase base,@DrawableRes  int icon){
            this.base = base;
            this.icon = icon;
        }
    }

    public AXFooterParallax scrollListener;

    AXFooterView footerView;

    int Left = -1;

    /**
     * add footer left view
     * NOTE : You should add left icon before call emojiPager.show();
     * @param res
     */
    public void setLeftIcon (@DrawableRes int res){
        Left = res;
    }

    boolean viewPagerTouchEnabled = false;

    public void setSwipeWithFingerEnabled(boolean enabled){
        viewPagerTouchEnabled = enabled;
    }

    public void show(){
        if (isShowing) return;
        isShowing=true;
        this.addView(vp,new AXEmojiLayout.LayoutParams());

        vp.setAdapter(new PagerAdapter() {
            public Object instantiateItem(ViewGroup collection, int position) {
                AXEmojiBase base = pages.get(position).base;
                collection.addView(base);
                return  base;
            }

            @Override
            public int getCount() {
                return pages.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view.equals(o);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        pages.get(0).base.onShow();

        if (footer) {
            footerView = new AXFooterView(getContext(), this, Left);
            this.addView(footerView, new AXEmojiLayout.LayoutParams(0, 0, -1, Utils.dpToPx(getContext(), 44)));

            scrollListener = new AXFooterParallax(footerView, Utils.dpToPx(getContext(), 44),-1);
            scrollListener.setDuration(Utils.dpToPx(getContext(), 44));
            scrollListener.setIDLEHideSize(scrollListener.getDuration() / 2);
            scrollListener.setMinComputeScrollOffset(Utils.dpToPx(getContext(), 44));
            scrollListener.setScrollSpeed((long) 1.2);
            scrollListener.setChangeOnIDLEState(true);
            for (int i = 0; i < pages.size(); i++) {
                pages.get(i).base.setScrollListener(scrollListener);
            }

            ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {
                }

                @Override
                public void onPageSelected(int i) {
                    scrollListener.show();
                }

                @Override
                public void onPageScrollStateChanged(int i) {
                }
            };
            for (int i = 0; i < pages.size(); i++) {
                pages.get(i).base.setPageChanged(pageChangeListener);
            }

            for (int i = 0; i < pages.size(); i++) {
                pages.get(i).base.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        super.getItemOffsets(outRect, view, parent, state);
                        if (view == null || outRect == null || parent == null) return;
                        int position = parent.getChildAdapterPosition(view);
                        int max = parent.getAdapter().getItemCount();
                        int spanCount = 1;
                        if (parent.getLayoutManager() instanceof GridLayoutManager) {
                            spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();

                            if (max % spanCount == 0) {
                                if ((parent.getAdapter().getItemCount() - position) > spanCount) {
                                    outRect.bottom = 0;
                                } else {
                                    outRect.bottom = Utils.dpToPx(getContext(), 44);
                                }
                            } else if (max % spanCount >= max - position) {
                                if ((parent.getAdapter().getItemCount() - position) > spanCount) {
                                    outRect.bottom = 0;
                                } else {
                                    outRect.bottom = Utils.dpToPx(getContext(), 44);
                                }
                            }
                        } else if (parent.getLayoutManager() instanceof LinearLayoutManager){
                            if (position == max-1){
                                outRect.bottom = Utils.dpToPx(getContext(), 44);
                            }else{
                                outRect.bottom = 0;
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void dismiss(){
        super.dismiss();
        for (int i=0;i<pages.size();i++){
            pages.get(i).base.dismiss();
        }
    }

    @Override
    public void setEditText(EditText editText){
        super.setEditText(editText);
        for (int i=0;i<pages.size();i++){
            pages.get(i).base.setEditText(editText);
        }
    }

    @Override
    protected void refresh(){
        super.refresh();
        if (footer) {
            scrollListener.show();
        }
        for (int i=0;i<pages.size();i++){
            pages.get(i).base.refresh();
        }
    }

    /**
     * @return emoji view pager
     */
    public ViewPager getViewPager(){
        return vp;
    }

    @Override
    public void onSizeChanged(int w,int h,int oldw,int oldh){
        super.onSizeChanged(w,h,oldw,oldh);
        if (footer)
        ((LayoutParams) footerView.getLayoutParams()).top = h - ((LayoutParams) footerView.getLayoutParams()).height;
    }

    onFooterItemClicked listener = null;
    /**
     * set footer left and right view click listener
     * @param listener
     */
    public void setOnFooterItemClicked (onFooterItemClicked listener){
        this.listener = listener;
    }
    public void removeOnFooterItemClicked(){listener=null;}

    public interface onFooterItemClicked{
        void onClick (boolean leftIcon);
    }


    @Override
    public void setPageIndex (int index){
        vp.setCurrentItem(index,true);
        pages.get(index).base.onShow();
        if (footer) {
            scrollListener.show();
            footerView.setPageIndex(index);
        }
        if (pageListener!=null) pageListener.onPageChanged(AXEmojiPager.this,pages.get(index).base,index);
    }

    @Override
    public int getPageIndex(){
        return vp.getCurrentItem();
    }



    /**
     * get footer left view.
     */
    public AppCompatImageView getFooterLeftView(){
        return footerView.leftIcon;
    }

    /**
     * get footer right view. (backspace)
     */
    public AppCompatImageView getFooterRightView(){
        return footerView.backSpace;
    }

    @Override
    protected void onShow(){
        super.onShow();
        for (int i=0;i<pages.size();i++){
            pages.get(i).base.onShow();
        }
    }
}
