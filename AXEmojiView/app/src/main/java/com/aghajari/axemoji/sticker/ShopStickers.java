package com.aghajari.axemoji.sticker;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aghajari.axemoji.R;
import com.aghajari.axemojiview.sticker.Sticker;
import com.aghajari.axemojiview.sticker.StickerCategory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

public class ShopStickers implements StickerCategory<Integer> {
    @NonNull
    @Override
    public ShopSticker[] getStickers() {
        return new ShopSticker[]{
                new ShopSticker(
                        new Sticker[]{
                    new Sticker<Integer>(R.drawable.sticker),
                    new Sticker<Integer>(R.drawable.sticker1),
                    new Sticker<Integer>(R.drawable.sticker2),
                    new Sticker<Integer>(R.drawable.sticker3),
                    new Sticker<Integer>(R.drawable.sticker4)}
                        ,"Cuppy Pack 1",16),

                new ShopSticker(
                        new Sticker[]{
                                new Sticker<Integer>(R.drawable.sticker),
                                new Sticker<Integer>(R.drawable.sticker1),
                                new Sticker<Integer>(R.drawable.sticker2),
                                new Sticker<Integer>(R.drawable.sticker3),
                                new Sticker<Integer>(R.drawable.sticker4)}
                        ,"Cuppy Pack 2",16),

                new ShopSticker(
                        new Sticker[]{
                                new Sticker<Integer>(R.drawable.sticker),
                                new Sticker<Integer>(R.drawable.sticker1),
                                new Sticker<Integer>(R.drawable.sticker2),
                                new Sticker<Integer>(R.drawable.sticker3),
                                new Sticker<Integer>(R.drawable.sticker4)}
                        ,"Cuppy Pack 3",16),

                new ShopSticker(
                        new Sticker[]{
                                new Sticker<Integer>(R.drawable.sticker),
                                new Sticker<Integer>(R.drawable.sticker1),
                                new Sticker<Integer>(R.drawable.sticker2),
                                new Sticker<Integer>(R.drawable.sticker3),
                                new Sticker<Integer>(R.drawable.sticker4)}
                        ,"Cuppy Pack 4",16),
        };
    }

    @Override
    public Integer getCategoryData() {
        return R.drawable.ic_masks_msk;
    }

    @Override
    public boolean useCustomView() {
        return true;
    }

    @Override
    public View getView(ViewGroup viewGroup) {
        RecyclerView recyclerView = new RecyclerView(viewGroup.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(viewGroup.getContext()));
        return recyclerView;
    }

    @Override
    public void bindView(View view) {
        ((RecyclerView)view).setAdapter(new ShopAdapter(this));
    }




    public class ShopSticker extends Sticker{
        String title;
        int count;
        public ShopSticker(Sticker[] data,String Title,int StickersSize) {
            super(data);
            this.title = Title;
            this.count = StickersSize;
        }
    }




    public class ShopAdapter extends RecyclerView.Adapter<ShopStickers.ShopAdapter.ShopViewHolder>{
        ShopStickers provider;
        public ShopAdapter (ShopStickers provider){
            this.provider = provider;
        }

        @NonNull
        @Override
        public ShopStickers.ShopAdapter.ShopViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from( viewGroup.getContext()).inflate(R.layout.shop_recyclerview, viewGroup, false);

            TextView title = v.findViewById(R.id.sticker_title);
            TextView subtitle = v.findViewById(R.id.sticker_subtitle);
            Button add = v.findViewById(R.id.sticker_add);
            RecyclerView stickers = v.findViewById(R.id.stickers_rv);
            stickers.setLayoutManager(new GridLayoutManager(viewGroup.getContext(),5));

            return new ShopViewHolder(v,title,subtitle,add,stickers);
        }

        @Override
        public void onBindViewHolder(@NonNull final ShopStickers.ShopAdapter.ShopViewHolder viewHolder, int i) {
            final ShopSticker sticker = provider.getStickers()[i];
            viewHolder.title.setText(sticker.title);
            viewHolder.subTitle.setText(sticker.count+" Sticker");

            viewHolder.add.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(),"Add "+sticker.title,Toast.LENGTH_SHORT).show();
                }
            });

            viewHolder.stickers.setAdapter(new IconAdapter((Sticker[])sticker.getData()));
        }

        @Override
        public int getItemCount() {
            return provider.getStickers().length;
        }

        public class ShopViewHolder extends RecyclerView.ViewHolder{
            TextView title;
            TextView subTitle;
            Button add;
            RecyclerView stickers;
            public ShopViewHolder(@NonNull View itemView, TextView title, TextView subTitle,Button add,RecyclerView stickers) {
                super(itemView);
                this.title = title;
                this.subTitle = subTitle;
                this.add = add;
                this.stickers = stickers;
            }
        }
    }



    public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconViewHolder>{
        Sticker[] stickers;

        public IconAdapter (Sticker[] stickers){
            this.stickers = stickers;
        }

        @NonNull
        @Override
        public IconViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from( viewGroup.getContext()).inflate(R.layout.shop_sticker_item, viewGroup, false);
            return new IconViewHolder(v, (AppCompatImageView) v.findViewById(R.id.sticker_img));
        }

        @Override
        public void onBindViewHolder(@NonNull IconViewHolder viewHolder, int i) {
            Glide.with(viewHolder.imageView)
                    .load(Integer.valueOf(stickers[i].getData().toString()))
                    .apply(RequestOptions.fitCenterTransform())
                    .into(viewHolder.imageView);
        }

        @Override
        public int getItemCount() {
            return stickers.length;
        }

        public class IconViewHolder extends RecyclerView.ViewHolder{
            AppCompatImageView imageView;
            public IconViewHolder(@NonNull View itemView, AppCompatImageView imageView) {
                super(itemView);
                this.imageView = imageView;
            }
        }
    }
}
