package jjw.com.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.toast.android.analytics.common.utils.StringUtil;
import com.toast.android.analytics.common.utils.WebUtil;

import java.util.List;

import jjw.com.fragment.ItemFragment.OnListFragmentInteractionListener;
import jjw.com.fragment.model.ImageContent;
import jjw.com.myfirstapp.R;

import static android.media.CamcorderProfile.get;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ImageContent.OneImageItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyImgItemRecyclerViewAdapter extends RecyclerView.Adapter<MyImgItemRecyclerViewAdapter.ViewHolder> {

    private String TAG = MyImgItemRecyclerViewAdapter.class.getSimpleName();
    private final List<ImageContent.OneImageItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context mParentContext = null;

    public MyImgItemRecyclerViewAdapter(List<ImageContent.OneImageItem> items, OnListFragmentInteractionListener listener) {
        System.out.println(TAG + " jjw MyItemRecyclerViewAdapter  ");
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println(TAG + " jjw onCreateViewHolder viewType: " + viewType);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        mParentContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        System.out.println(TAG + " jjw onBindViewHolder position " + position);

        final ImageContent.OneImageItem oneImgObj = mValues.get(position);

        holder.mItem = oneImgObj;
        holder.mTitleView.setText(oneImgObj.title);
        String one_img_url = oneImgObj.img_url;
//        holder.mImgUrlView.setText(one_img_url);

        if(StringUtil.isEmpty(one_img_url) == false){
            // 이미지 표현
            Glide.with(mParentContext).load(one_img_url).into(holder.mOneImgView);

            holder.mOneImgView.setVisibility(View.VISIBLE);
        } else {
            holder.mOneImgView.setImageDrawable(null);
            holder.mOneImgView.setVisibility(View.GONE);
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
//        public final TextView mImgUrlView;
        public final ImageView mOneImgView;

        public ImageContent.OneImageItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
//            mImgUrlView = (TextView) view.findViewById(R.id.img_url);
            mOneImgView = (ImageView) view.findViewById(R.id.oneimage);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + " title : " + mTitleView + "'";
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mParentContext = null;
        super.onDetachedFromRecyclerView(recyclerView);
    }
}
