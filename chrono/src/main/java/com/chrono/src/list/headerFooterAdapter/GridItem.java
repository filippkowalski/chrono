package com.chrono.src.list.headerFooterAdapter;

/**
 * Provides span when using multicolumn (grid, staggered) layout managers on RecyclerView
 */
public interface GridItem {
    int getGridSpan();
}