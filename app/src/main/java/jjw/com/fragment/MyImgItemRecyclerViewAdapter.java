package jjw.com.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jjw.com.fragment.ItemFragment.OnListFragmentInteractionListener;
import jjw.com.fragment.model.ImageContent;
import jjw.com.mytest.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ImageContent.OneImageItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyImgItemRecyclerViewAdapter extends RecyclerView.Adapter<MyImgItemRecyclerViewAdapter.ViewHolder> {

    private String TAG = MyImgItemRecyclerViewAdapter.class.getSimpleName();
    private final List<ImageContent.OneImageItem> mValues;
    private final OnListFragmentInteractionListener mListener;

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
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        System.out.println(TAG + " jjw onBindViewHolder position " + position);

        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).title);
        holder.mContentView.setText(mValues.get(position).content);
        holder.mImgUrlView.setText(mValues.get(position).img_url);

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
        public final TextView mContentView;
        public final TextView mImgUrlView;

        public ImageContent.OneImageItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mContentView = (TextView) view.findViewById(R.id.content);
            mImgUrlView = (TextView) view.findViewById(R.id.img_url);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + " title : " + mTitleView + " content : " + mContentView.getText() + "'";
        }
    }
}
