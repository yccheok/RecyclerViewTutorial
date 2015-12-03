package org.yccheok.recyclerviewtutorial;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yccheok on 17/11/2015.
 */
public class RecyclerViewDemoAdapter extends RecyclerView.Adapter<RecyclerViewDemoAdapter.ListItemViewHolder> {
    private List<DemoModel> items;
    private SparseBooleanArray selectedItems;
    private RecyclerView recyclerView;
    private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;
    private boolean actionMode = false;

    public RecyclerViewDemoAdapter(List<DemoModel> modelData, RecyclerView recyclerView, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        if (modelData == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
        items = modelData;
        this.recyclerView = recyclerView;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
        selectedItems = new SparseBooleanArray();

        this.setHasStableIds(true);
    }

    @Override
    public long getItemId( int position ) {
        return this.items.get(position).getId();
    }
    
    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_demo_01, parent, false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = recyclerView.getChildAdapterPosition(view);

                    if (actionMode) {
                        toggleSelection(position);
                        itemView.setActivated(selectedItems.get(position, false));
                    }

                    recyclerViewOnItemClickListener.onItemClick(view, position);
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = recyclerView.getChildAdapterPosition(view);

                if (!actionMode) {
                    toggleSelection(position);
                    itemView.setActivated(selectedItems.get(position, false));
                }

                recyclerViewOnItemClickListener.onItemLongPress(view, position);

                return true;
            }
        });

        ListItemViewHolder listItemViewHolder = new ListItemViewHolder(itemView);

        return listItemViewHolder;
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        DemoModel model = items.get(position);
        holder.label.setText(model.label);
        String dateStr = DateUtils.formatDateTime(
                holder.label.getContext(),
                model.dateTime.getTime(),
                DateUtils.FORMAT_ABBREV_ALL);
        holder.dateTime.setText(dateStr);

        if (selectedItems.get(position, false)) {
            holder.itemView.setActivated(true);
        } else {
            final View itemView = holder.itemView;
            if (itemView.isActivated()) {
                itemView.setBackgroundResource(R.drawable.card);
                itemView.setActivated(false);
                itemView.post(new Runnable() {
                    @Override
                    public void run() {
                        itemView.setBackgroundResource(R.drawable.statelist_item_background);
                    }
                });

            } else {
                itemView.setActivated(false);
            }
        }
    }

    private void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        // It is important not to notifyItemChanged and trigger onBindViewHolder. If not, we will
        // get unwanted flickering effect.
        //notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        //notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView label;
        TextView dateTime;
        public View view;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            label = (TextView) itemView.findViewById(R.id.txt_label_item);
            dateTime = (TextView) itemView.findViewById(R.id.txt_date_time);
        }
    }

    public void setActionMode(boolean actionMode) {
        this.actionMode = actionMode;
    }

    public void removeData(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }
}
