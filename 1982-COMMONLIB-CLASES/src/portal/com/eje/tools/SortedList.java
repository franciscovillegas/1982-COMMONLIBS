package portal.com.eje.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortedList
{

    public SortedList(boolean orden)
    {
        list = new ArrayList();
        this.orden = orden;
    }

    public SortedList(List list)
    {
        this.list = list;
    }

    public void addSort(String element)
    {
        int index = Collections.binarySearch(list, element);
        if(index < 0)
            list.add(-(index + 1), element);
    }

    public List getList()
    {
        if(orden)
            Collections.reverse(list);
        return list;
    }

    private List list;
    private boolean orden;
}