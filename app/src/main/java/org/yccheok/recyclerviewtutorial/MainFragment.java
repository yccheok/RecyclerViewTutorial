package org.yccheok.recyclerviewtutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SimpleItemAnimator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yccheok on 16/11/2015.
 */
public class MainFragment extends Fragment implements RecyclerViewOnItemClickListener {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ActionMode actionMode;
    private RecyclerViewDemoAdapter adapter;
    private List<DemoModel> models;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        models = getDemoData();

        adapter = new RecyclerViewDemoAdapter(models, mRecyclerView, this);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 10);

        return view;
    }

    private final List<DemoModel> getDemoData() {
        List<DemoModel> demoData = new ArrayList<DemoModel>();
        for (int i = 0; i < 20; i++) {
            DemoModel model = new DemoModel();
            model.dateTime = new Date();
            model.label = "Test Label No. " + i;
            demoData.add(model);
        }
        return new ArrayList<DemoModel>(demoData);
    }

    @Override
    public void onItemClick(View childView, int position) {
        if (actionMode == null) {

        } else {
            updateTitle(position);
        }
    }

    public void sortAndNotifyDataSetChanged() {
        int i0 = 0;
        int i1 = models.size() - 1;

        while (i0 < i1) {
            DemoModel o0 = models.get(i0);
            DemoModel o1 = models.get(i1);

            models.set(i0, o1);
            models.set(i1, o0);

            i0++;
            i1--;

            //break;
        }

        // Prevent animation when sorting.
        final RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
        mRecyclerView.setItemAnimator(null);
        adapter.notifyDataSetChanged();
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setItemAnimator(animator);
            }
        });

    }

    private void updateTitle(int idx) {
        int count = adapter.getSelectedItemCount();
        if (count <= 0) {
            actionMode.finish();
            adapter.setActionMode(false);
            return;
        }
        String title = getString(R.string.selected_count, count);
        actionMode.setTitle(title);
    }

    @Override
    public void onItemLongPress(View childView, int position) {
        if (actionMode != null) {
            return;
        }

        actionMode = ((AppCompatActivity)getActivity()).startSupportActionMode(new AnActionModeOfEpicProportions());
        adapter.setActionMode(true);

        updateTitle(position);
    }

    private final class AnActionModeOfEpicProportions implements ActionMode.Callback {

        private boolean canCallNotify = true;

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.select_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_delete:
                    List<Integer> selectedItemPositions = adapter.getSelectedItems();
                    for (int i = selectedItemPositions.size()-1; i >= 0; i--) {
                        adapter.removeData(selectedItemPositions.get(i));
                    }

                    canCallNotify = false;

                    actionMode.finish();
                    adapter.setActionMode(false);

                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            MainFragment.this.actionMode = null;
            adapter.clearSelections();
            // http://stackoverflow.com/questions/27187733/no-animation-on-item-removal-on-recyclerview
            if (canCallNotify) {
                adapter.notifyDataSetChanged();
            } else {
                canCallNotify = true;
            }
            adapter.setActionMode(false);
        }
    }

}
