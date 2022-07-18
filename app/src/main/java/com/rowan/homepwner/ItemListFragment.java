package com.rowan.homepwner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ItemListFragment extends Fragment {

    private RecyclerView mItemRecyclerView;
    private ItemAdapter mAdapter;
    private Callbacks mCallbacks;

    /**
     * Required interface for hosting activities
     */
    public interface Callbacks {
        void onItemSelected(Item item);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        mItemRecyclerView = view
                .findViewById(R.id.item_recycler_view);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_item_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.new_item:
                Item item = new Item();
                ItemLab.get(getActivity()).addItem(item);
                updateUI();
                mCallbacks.onItemSelected(item);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public void updateUI() {
        ItemLab itemLab = ItemLab.get(getActivity());
        List<Item> items = itemLab.getItems();

        if (mAdapter == null) {
            mAdapter = new ItemAdapter(items);
            mItemRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }



    private class ItemHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Item mItem;
        private final TextView mNameTextView;
        private final TextView mValueTextView;



        public void bind(Item item) {
            mItem = item;
            mNameTextView.setText(mItem.getName());
            mValueTextView.setText(mItem.getValue());
        }

        public ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_item, parent, false));

            itemView.setOnClickListener(this);

            mNameTextView = itemView.findViewById(R.id.item_name);
            mValueTextView = itemView.findViewById(R.id.item_value);

        }

        @Override
        public void onClick(View view) {
            mCallbacks.onItemSelected(mItem);
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        private final List<Item> mItems;
        public ItemAdapter(List<Item> items) {
            mItems = items;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            Item item = mItems.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }
}
