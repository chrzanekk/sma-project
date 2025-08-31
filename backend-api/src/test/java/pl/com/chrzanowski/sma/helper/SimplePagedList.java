package pl.com.chrzanowski.sma.helper;

import com.blazebit.persistence.KeysetPage;
import com.blazebit.persistence.PagedList;

import java.util.ArrayList;
import java.util.Collection;

public class SimplePagedList<T> extends ArrayList<T> implements PagedList<T> {
    private final long totalSize;

    public SimplePagedList(Collection<? extends T> items, long totalSize) {
        super(items);
        this.totalSize = totalSize;
    }

    @Override
    public long getTotalSize() {
        return totalSize;
    }

    @Override
    public int getSize() {
        return size();
    }

    @Override
    public int getTotalPages() {
        return 1;
    }

    @Override
    public int getPage() {
        return 0;
    }

    @Override
    public int getFirstResult() {
        return 0;
    }

    @Override
    public int getMaxResults() {
        return size();
    }

    @Override
    public KeysetPage getKeysetPage() {
        return null; // jeśli nie używasz keysetów w testach
    }
}
