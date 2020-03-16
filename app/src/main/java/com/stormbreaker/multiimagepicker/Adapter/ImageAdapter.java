package com.stormbreaker.multiimagepicker.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.esafirm.imagepicker.model.Image;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.stormbreaker.multiimagepicker.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.stormbreaker.multiimagepicker.MainActivity.IMAGE_PICKER_ESAFIRM;
import static com.stormbreaker.multiimagepicker.MainActivity.IMAGE_PICKER_PIX;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mImageListPix = new ArrayList<String>();
    private ArrayList<Image> mImageListEsafirm = new ArrayList<>();
    private String mPickerType;


    public ImageAdapter(Context context) {
        this.mContext = context;
    }

    public void setPickerType(String type){
        this.mPickerType = type;
    }

    public void addImagePix(ArrayList<String> list) {
        this.mImageListPix.clear();
        this.mImageListPix.addAll(list);
        notifyDataSetChanged();
    }

    public void addImageEsafirm(List<Image> list){
        this.mImageListEsafirm.clear();
        this.mImageListEsafirm.addAll(list);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_adapter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        switch (mPickerType){
            case IMAGE_PICKER_PIX:{
                Log.d("ImageAdapter", mImageListPix.get(position));
                File imageFile = new File(mImageListPix.get(position));
//               Bitmap bitmap = new BitmapDrawable(mContext.getResources(), imageFile.getAbsolutePath()).getBitmap();
//               ((ViewHolder) holder).ivSelectedImage.setImageBitmap(bitmap);

                int radius = 8;
                Picasso.get().load(imageFile)
                        .config(Bitmap.Config.RGB_565)
                        .fit()
                        .centerCrop()
                        .transform(new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL))
                        .error(R.drawable.ic_photo_size_select_actual_white_24dp)
                        .into(((ViewHolder) holder).ivSelectedImage, new Callback() {
                            @Override
                            public void onSuccess() {
//                        Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onError(Exception e) {
                                Log.d("ImageAdapter", e.toString());
                            }
                        });
            }
            break;

            case IMAGE_PICKER_ESAFIRM:{
                Log.d("ImageAdapter", mImageListEsafirm.get(position).getPath());
                File imageFile = new File(mImageListEsafirm.get(position).getPath());
                int radius = 8;
                Picasso.get().load(imageFile)
                        .config(Bitmap.Config.RGB_565)
                        .fit()
                        .centerCrop()
                        .transform(new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL))
                        .error(R.drawable.ic_photo_size_select_actual_white_24dp)
                        .into(((ViewHolder) holder).ivSelectedImage, new Callback() {
                            @Override
                            public void onSuccess() {
//                        Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onError(Exception e) {
                                Log.d("ImageAdapter", e.toString());
                            }
                        });
            }
            break;
        }

    }

    @Override
    public int getItemCount() {
        switch (mPickerType){
            case IMAGE_PICKER_PIX:
                return mImageListPix.size();

            case IMAGE_PICKER_ESAFIRM:
                return mImageListEsafirm.size();

            default:
                return 0;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivSelectedImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ivSelectedImage = (ImageView) itemView.findViewById(R.id.iv_selected_image);

        }
    }


}
