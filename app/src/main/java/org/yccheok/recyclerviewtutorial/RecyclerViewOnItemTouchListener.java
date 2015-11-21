package org.yccheok.recyclerviewtutorial;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yccheok on 17/11/2015.
 */
public class RecyclerViewOnItemTouchListener implements RecyclerView.OnItemTouchListener {

    public interface OnItemClickListener {
        void onItemClick(View childView, int position);
        void onItemLongPress(View childView, int position);
    }

    public RecyclerViewOnItemTouchListener(Context context, OnItemClickListener onItemClickListener) {
        this.gestureDetector = new GestureDetector(context, new RecyclerViewGestureListener());
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent e) {
        this.recyclerView = recyclerView;
        if (recyclerView != null) {
            this.childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (this.childView != null) {
                this.childViewPosition = recyclerView.getChildAdapterPosition(childView);
                gestureDetector.onTouchEvent(e);
            }
        }

        return false;
    }

    public void onClick(View view) {
        RecyclerView recyclerView = this.recyclerView;

        if (recyclerView != null) {
            this.childView = view;
            if (this.childView != null) {
                this.childViewPosition = recyclerView.getChildAdapterPosition(childView);
                onItemClickListener.onItemClick(childView, childViewPosition);
            }
        }
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        // Do nothing.
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // Do nothing.
    }

    private class RecyclerViewGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {        	
            // Too slow.
            
            //
            //View childView = RecyclerViewOnItemTouchListener.this.childView;
            //
            //if (childView != null && childViewPosition >= 0) {
            //    onItemClickListener.onItemClick(childView, childViewPosition);
            //
            //    RecyclerViewOnItemTouchListener.this.childView = null;
            //    RecyclerViewOnItemTouchListener.this.childViewPosition = -1;
            //}
            //
            return super.onSingleTapConfirmed(e);
        }

        public void onLongPress(MotionEvent e) {
            View childView = RecyclerViewOnItemTouchListener.this.childView;
            int childViewPosition = RecyclerViewOnItemTouchListener.this.childViewPosition;

            if (childView != null && childViewPosition >= 0) {
                onItemClickListener.onItemLongPress(childView, childViewPosition);

                RecyclerViewOnItemTouchListener.this.childView = null;
                RecyclerViewOnItemTouchListener.this.childViewPosition = -1;
            }

            super.onLongPress(e);
        }
    }

    private volatile RecyclerView recyclerView = null;
    private volatile View childView = null;
    private volatile int childViewPosition = -1;

    private final GestureDetector gestureDetector;
    private final OnItemClickListener onItemClickListener;
}
