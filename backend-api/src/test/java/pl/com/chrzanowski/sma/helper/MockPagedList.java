package pl.com.chrzanowski.sma.helper;

import com.blazebit.persistence.PagedList;

import java.util.List;

public class MockPagedList {

    public static <T> PagedList<T> mockPagedListSpy(List<T> items, long totalSize) {
        return new SimplePagedList<>(items, totalSize);
    }
}
