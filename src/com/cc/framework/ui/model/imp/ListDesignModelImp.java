// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi

package com.cc.framework.ui.model.imp;

import com.cc.framework.security.*;
import com.cc.framework.ui.SelectMode;
import com.cc.framework.ui.model.ColumnDesignModel;
import com.cc.framework.ui.model.ListDesignModel;
import java.util.ArrayList;

// Referenced classes of package com.cc.framework.ui.model.imp:
//            ControlDesignModelImp

public class ListDesignModelImp extends ControlDesignModelImp
    implements ListDesignModel
{

    private int rowCount;
    private int headerHeight;
    private int rowHeight;
    private String title;
    private String emptyText;
    private SelectMode selectMode;
    private Permission refreshButton;
    private Permission createButton;
    private ArrayList columns;

    public ListDesignModelImp()
    {
        rowCount = -1;
        headerHeight = 24;
        rowHeight = 20;
        title = null;
        emptyText = "对不起,没有数据!";
        selectMode = SelectMode.NONE;
        refreshButton = StaticPermission.NONE;
        createButton = StaticPermission.NONE;
        columns = new ArrayList();
    }

    public int getRowCount()
    {
        return rowCount;
    }

    public void setRowCount(int i)
    {
        rowCount = i;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String s)
    {
        title = s;
    }

    public String getEmptyText()
    {
        return emptyText;
    }

    public void setEmptyText(String s)
    {
        emptyText = s;
    }

    public void addColumn(ColumnDesignModel columndesignmodel)
    {
        synchronized(columns)
        {
            columns.add(columndesignmodel);
        }
    }

    public void removeColumn(ColumnDesignModel columndesignmodel)
    {
        synchronized(columns)
        {
            columns.remove(columndesignmodel);
        }
    }

    public ColumnDesignModel[] getColumns()
    {
        ArrayList arraylist = columns;
//        JVM INSTR monitorenter ;
        ColumnDesignModel acolumndesignmodel[] = new ColumnDesignModel[columns.size()];
        return (ColumnDesignModel[])columns.toArray(acolumndesignmodel);
//        Exception exception;
//        exception;
//        arraylist;
//        JVM INSTR monitorexit ;
//        throw exception;
    }

    public int size()
    {
        return columns.size();
    }

    public int getHeaderHeight()
    {
        return headerHeight;
    }

    public int getRowHeight()
    {
        return rowHeight;
    }

    public SelectMode getSelectMode()
    {
        return selectMode;
    }

    public void setSelectMode(SelectMode selectmode)
    {
        selectMode = selectmode;
    }

    public boolean showRefreshButton(Principal principal)
    {
        return refreshButton.isGranted(principal);
    }

    public void setRefreshButtonPermission(Permission permission)
    {
        refreshButton = permission;
    }

    public boolean showCreateButton(Principal principal)
    {
        return createButton.isGranted(principal);
    }

    public void setCreateButtonPermission(Permission permission)
    {
        createButton = permission;
    }
}
