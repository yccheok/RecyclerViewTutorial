package org.yccheok.recyclerviewtutorial;

import android.view.View;

public interface RecyclerViewOnItemClickListener {
    void onItemClick(View childView, int position);
    void onItemLongPress(View childView, int position);
}