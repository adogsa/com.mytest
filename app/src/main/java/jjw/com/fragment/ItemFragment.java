package jjw.com.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jjw.com.fragment.model.ImageContent;
import jjw.com.myfirstapp.R;
import jjw.com.fragment.dummy.DummyContent;
import jjw.com.fragment.dummy.DummyContent.DummyItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment {

    private String TAG = ItemFragment.class.getSimpleName();
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 4;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println(TAG + " jjw onCreate  ");

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println(TAG + " jjw onCreateView  ");

        final View fr_view = inflater.inflate(R.layout.fragment_item_list, container, false);

        final ImageContent imgContentObj = new ImageContent();
        imgContentObj.makeList(getActivity(), "", fr_view.findViewById(R.id.item_fr_list), mListener);

        // Spinner
        final Spinner searchTpSpinner = (Spinner) fr_view.findViewById(R.id.spinner_key);
        searchTpSpinner.setPrompt("검색구분을 선택하세요.");
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.search_type, R.layout.spinner_item);

        yearAdapter.setDropDownViewResource(R.layout.spinner_drop_item);
        searchTpSpinner.setAdapter(yearAdapter);
        searchTpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    ((EditText) fr_view.findViewById(R.id.keyword)).setText("선택하세요");
                } else {
                    ((EditText) fr_view.findViewById(R.id.keyword)).setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.RED);
                adapterView.setSelection(0);
            }
        });


        fr_view.findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedItem = String.valueOf(searchTpSpinner.getSelectedItem());
//                Snackbar.make(view, ((EditText)fr_view.findViewById(R.id.keyword)).getText().toString(), Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if ("제목".equalsIgnoreCase(selectedItem) == true) {
                    imgContentObj.searchListWithKwd("title", ((EditText) fr_view.findViewById(R.id.keyword)).getText().toString());
                } else if ("재료".equalsIgnoreCase(selectedItem) == true) {
                    imgContentObj.searchListWithKwd("food_stuffs", ((EditText) fr_view.findViewById(R.id.keyword)).getText().toString());
                } else {
                    imgContentObj.searchListWithKwd("title", "");
                }

//                imgContentObj.searchListWithKwd("title", ((EditText)fr_view.findViewById(R.id.keyword)).getText().toString());
            }
        });


        return fr_view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        System.out.println(TAG + " jjw onAttach  ");

        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        System.out.println(TAG + " jjw onDetach  ");

        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ImageContent.OneImageItem item);
    }
}
