package com.rowan.homepwner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class ItemPagerActivity extends AppCompatActivity
        implements ItemFragment.Callbacks {

    private static final String EXTRA_ITEM_ID =
            "com.rowan.homepwner.item_id";

    private ViewPager mViewPager;
    private List<Item> mItems;

    public static Intent newIntent(Context packageContext, UUID itemID) {
        Intent intent = new Intent(packageContext, ItemPagerActivity.class);
        intent.putExtra(EXTRA_ITEM_ID, itemID);
        return intent;
    }

    @Override
    public void onItemUpdated(Item item) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_pager);

        UUID itemID = (UUID) getIntent()
                .getSerializableExtra(EXTRA_ITEM_ID);

        mViewPager = findViewById(R.id.item_view_pager);

        mItems = ItemLab.get(this).getItems();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Item item = mItems.get(position);
                return ItemFragment.newInstance(item.getId());
            }

            @Override
            public int getCount() {
                return mItems.size();
            }
        });

        for (int i = 0; i < mItems.size(); i++) {
            if (mItems.get(i).getId().equals(itemID)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
