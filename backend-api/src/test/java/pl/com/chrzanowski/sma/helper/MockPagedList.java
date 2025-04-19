package pl.com.chrzanowski.sma.helper;

import com.blazebit.persistence.PagedList;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockPagedList {
    @SuppressWarnings("unchecked")
    public static <T> PagedList<T> mockPagedList(List<T> items, long totalSize) {
        PagedList<T> mock = mock(PagedList.class);
        when(mock.getTotalSize()).thenReturn(totalSize);
        when(mock.iterator()).thenReturn(items.iterator());
        when(mock.size()).thenReturn(items.size());
        if (!items.isEmpty()) {
            for (int i = 0; i < items.size(); i++) {
                when(mock.get(i)).thenReturn(items.get(i));
            }
        }
        return mock;
    }

    public static <T> PagedList<T> mockPagedListSpy(List<T> items, long totalSize) {
        return new SimplePagedList<>(items, totalSize);
    }
}
