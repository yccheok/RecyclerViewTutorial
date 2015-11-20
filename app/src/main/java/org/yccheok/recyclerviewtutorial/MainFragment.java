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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yccheok on 16/11/2015.
 */
public class MainFragment extends Fragment implements RecyclerViewOnItemTouchListener.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerViewOnItemTouchListener recyclerViewOnItemTouchListener;
    private ActionMode actionMode;
    private RecyclerViewDemoAdapter adapter;

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

        adapter = new RecyclerViewDemoAdapter(getDemoData());
        mRecyclerView.setAdapter(adapter);

        recyclerViewOnItemTouchListener = new RecyclerViewOnItemTouchListener(this.getContext(), this);
        mRecyclerView.addOnItemTouchListener(recyclerViewOnItemTouchListener);

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
            myToggleSelection(position);
        }
    }

    private void myToggleSelection(int idx) {
        adapter.toggleSelection(idx);
        int count = adapter.getSelectedItemCount();
        if (count <= 0) {
            actionMode.finish();
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
        myToggleSelection(position);
    }

    private final class AnActionModeOfEpicProportions implements ActionMode.Callback {

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
                    actionMode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            MainFragment.this.actionMode = null;
            adapter.clearSelections();
        }
    }
}
