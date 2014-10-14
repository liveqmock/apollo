// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi

package com.cc.framework.ui.painter.def;

import com.cc.framework.ui.model.ImageModel;
import com.cc.framework.ui.model.imp.ImageModelImp;
import com.cc.framework.ui.painter.ResourceMapImp;

// Referenced classes of package com.cc.framework.ui.painter.def:
//            DefColorPalette, DefResources

public class DefResourceMap extends ResourceMapImp
    implements DefResources
{

    public DefResourceMap()
    {
    }

    protected void doRegisterImages()
    {
        registerImage("img.spacer", createImage("spacer.gif", 1, 1));
        registerImage("img.bullet", createImage("bullet.gif", 6, 15));
        registerImage("def.img.mag", createImage("magnifier.gif", 20, 20));
        registerImage("def.img.err", createImage("error.gif", 12, 25));
        registerImage("def.img.inf", createImage("info.gif", 14, 23));
        registerImage("def.img.errin", createImage("errInput.gif", 16, 16));
        registerImage("def.img.msgin", createImage("msgInput.gif", 16, 16));
        registerImage("def.img.hdrtop", createImage("headertop.gif", 9, 17));
        registerImage("def.img.hdrbot", createImage("headerbottom.gif", 9, 17));
        registerImage("def.img.dotc", createImage("dots/dot_{0}.gif", 5, 5));
        registerImage("def.img.hand", createImage("imgHandRight.gif", 43, 22));
        registerImage("def.img.link.external", createImage("lnkExternal.gif", 12, 9));
        registerImage("def.img.sev.fatal", createImage("severity/imgFatal13x13.gif", 13, 13));
        registerImage("def.img.sev.err", createImage("severity/imgError13x13.gif", 13, 13));
        registerImage("def.img.sev.warn", createImage("severity/imgWarning13x13.gif", 13, 13));
        registerImage("def.img.sev.inf", createImage("severity/imgInfo13x13.gif", 13, 13));
        registerImage("def.img.sev.none", createImage("severity/imgNone13x13.gif", 13, 13));
        registerImage("def.btn.sortable", createImage("buttons/btnSortable1.gif", 11, 13));
        registerImage("def.btn.sortasc", createImage("buttons/btnSortUp1.gif", 11, 13));
        registerImage("def.btn.sortdesc", createImage("buttons/btnSortDown1.gif", 11, 13));
        registerImage("def.btn.next1", createImage("buttons/btnNext1.gif", 15, 15));
        registerImage("def.btn.next2", createImage("buttons/btnNext2.gif", 15, 15));
        registerImage("def.btn.first1", createImage("buttons/btnFirst1.gif", 15, 15));
        registerImage("def.btn.first2", createImage("buttons/btnFirst2.gif", 15, 15));
        registerImage("def.btn.last1", createImage("buttons/btnLast1.gif", 15, 15));
        registerImage("def.btn.last2", createImage("buttons/btnLast2.gif", 15, 15));
        registerImage("def.btn.prev1", createImage("buttons/btnPrev1.gif", 15, 15));
        registerImage("def.btn.prev2", createImage("buttons/btnPrev2.gif", 15, 15));
        registerImage("def.btn.create1", createImage("buttons/btnCreate1.gif", 15, 15));
        registerImage("def.btn.refresh1", createImage("buttons/btnRefresh1.gif", 15, 15));
        registerImage("def.cor.table.right", createImage("corners/r.gif", 10, 17));
        registerImage("def.cor.table.left", createImage("corners/l.gif", 10, 17));
        registerImage("def.cor.form.right", createImage("corners/r.gif", 10, 17));
        registerImage("def.cor.form.left", createImage("corners/l.gif", 10, 17));
        registerImage("def.cor.form.search.right", createImage("corners/r.gif", 10, 17));
        registerImage("def.cor.form.search.left", createImage("corners/l.gif", 10, 17));
        registerImage("def.cor.panel.top.left", createImage("corners/l.gif", 10, 17));
        registerImage("def.cor.panel.top.right", createImage("corners/r.gif", 10, 17));
        registerImage("def.cor.panel.bottom.left", createImage("corners/pl.gif", 10, 8));
        registerImage("def.cor.panel.bottom.right", createImage("corners/pr.gif", 10, 8));
        registerImage("def.cor.form.rightc", createImage("corners/r_{0}.gif", 10, 17));
        registerImage("def.cor.form.leftc", createImage("corners/l_{0}.gif", 10, 17));
        registerImage("ico.add", createImage("icons/add.gif", 16, 16));
        registerImage("ico.edit", createImage("icons/edit.gif", 16, 16));
        registerImage("ico.insert", createImage("icons/insert.gif", 16, 15));
        registerImage("ico.delete", createImage("icons/delete.gif", 16, 16));
        registerImage("ico.select", createImage("icons/select.gif", 15, 15));
        registerImage("ico.check", createImage("icons/check.gif", 16, 16));
        registerImage("ico.checked", createImage("icons/checked.gif", 16, 16));
        registerImage("def.tab.bg", createImage("tab/tab.gif", 1, 19));
        registerImage("def.tab.lc", createImage("tab/tabLSel_{0}.gif", 10, 19));
        registerImage("def.tab.rc", createImage("tab/tabRSel_{0}.gif", 11, 19));
        registerImage("def.tab.bgc", createImage("tab/tabBgSel_{0}.gif", 1, 19));
        registerImage("def.tab.unsell", createImage("tab/tabL.gif", 8, 19));
        registerImage("def.tab.unselr", createImage("tab/tabR.gif", 10, 19));
        registerImage("def.tab.unselbg", createImage("tab/tabBg.gif", 1, 19));
        registerImage("def.tab.disl", createImage("tab/tabDisL.gif", 8, 19));
        registerImage("def.tab.disr", createImage("tab/tabDisR.gif", 10, 19));
        registerImage("def.tab.disbg", createImage("tab/tabDisBg.gif", 1, 19));
        registerImage(15, "def.tree.open", createImage("tree/15/folderOpen.gif", 15, 15));
        registerImage(15, "def.tree.closed", createImage("tree/15/folderClosed.gif", 15, 15));
        registerImage(15, "def.tree.item", createImage("tree/15/item.gif", 15, 15));
        registerImage(15, "def.tree.ol", createImage("tree/15/0.gif", 15, 15));
        registerImage(15, "def.tree.ol2", createImage("tree/15/2.gif", 15, 15));
        registerImage(15, "def.tree.ol10", createImage("tree/15/10.gif", 15, 15));
        registerImage(15, "def.tree.ol12", createImage("tree/15/12.gif", 15, 15));
        registerImage(15, "def.tree.ol14", createImage("tree/15/14.gif", 15, 15));
        registerImage(15, "def.tree.ol16", createImage("tree/15/16.gif", 15, 15));
        registerImage(15, "def.tree.ol18", createImage("tree/15/18.gif", 15, 15));
        registerImage(15, "def.tree.ol26", createImage("tree/15/26.gif", 15, 15));
        registerImage(15, "def.tree.ol30", createImage("tree/15/30.gif", 15, 15));
        registerImage(15, "def.tree.ol32", createImage("tree/15/32.gif", 15, 15));
        registerImage(15, "def.tree.ol34", createImage("tree/15/34.gif", 15, 15));
        registerImage(15, "def.tree.ol42", createImage("tree/15/42.gif", 15, 15));
        registerImage(15, "def.tree.ol46", createImage("tree/15/46.gif", 15, 15));
        registerImage(15, "def.cb.inv", createImage("check/15/cb.gif", 15, 15));
        registerImage(15, "def.cb.unchk", createImage("check/15/cb0.gif", 15, 15));
        registerImage(15, "def.cb.chk", createImage("check/15/cb1.gif", 15, 15));
        registerImage(15, "def.cb.na", createImage("check/15/cb2.gif", 15, 15));
        registerImage(20, "def.tree.open", createImage("tree/20/0.gif", 1, 1));
        registerImage(20, "def.tree.closed", createImage("tree/20/0.gif", 1, 1));
        registerImage(20, "def.tree.item", createImage("tree/20/0.gif", 1, 1));
        registerImage(20, "def.tree.ol", createImage("tree/20/0.gif", 20, 20));
        registerImage(20, "def.tree.ol2", createImage("tree/20/2.gif", 20, 20));
        registerImage(20, "def.tree.ol10", createImage("tree/20/10.gif", 20, 20));
        registerImage(20, "def.tree.ol12", createImage("tree/20/12.gif", 20, 20));
        registerImage(20, "def.tree.ol14", createImage("tree/20/14.gif", 20, 20));
        registerImage(20, "def.tree.ol16", createImage("tree/20/16.gif", 20, 20));
        registerImage(20, "def.tree.ol18", createImage("tree/20/18.gif", 20, 20));
        registerImage(20, "def.tree.ol26", createImage("tree/20/26.gif", 20, 20));
        registerImage(20, "def.tree.ol30", createImage("tree/20/30.gif", 20, 20));
        registerImage(20, "def.tree.ol32", createImage("tree/20/32.gif", 20, 20));
        registerImage(20, "def.tree.ol34", createImage("tree/20/34.gif", 20, 20));
        registerImage(20, "def.tree.ol42", createImage("tree/20/42.gif", 20, 20));
        registerImage(20, "def.tree.ol46", createImage("tree/20/46.gif", 20, 20));
        registerImage(20, "def.cb.inv", createImage("check/20/cb.gif", 20, 20));
        registerImage(20, "def.cb.unchk", createImage("check/20/cb0.gif", 20, 20));
        registerImage(20, "def.cb.chk", createImage("check/20/cb1.gif", 20, 20));
        registerImage(20, "def.cb.na", createImage("check/20/cb2.gif", 20, 20));
    }

    protected void doRegisterStrings()
    {
        registerString("fw.items.noentries", "对不起,没有数据");
        registerString("fw.items.1te", "1 item");
        registerString("fw.items", "{0} items");
        registerString("fw.items.range", "{0} to {1} of {2}");
        registerString("fw.empty.text", "对不起,没有数据!");
        registerString("fw.page.noentries", "没有数据");
        registerString("fw.page.1te", "");
        registerString("fw.page.range", "第几{0}页 共{1}页 ");
        registerString("fw.tooltip.firstpage", "首页");
        registerString("fw.tooltip.prevpage", "前页");
        registerString("fw.tooltip.nextpage", "后页");
        registerString("fw.tooltip.lastpage", "尾页");
        registerString("fw.tooltip.refresh.list", "刷新");
        registerString("fw.tooltip.create.item", "create new item");
    }

    protected void doRegisterColors()
    {
        setColorPalette(new DefColorPalette());
    }

    private ImageModel createImage(String s, int i, int j)
    {
        StringBuffer stringbuffer = (new StringBuffer()).append("fw/def/").append("image/").append(s);
        return new ImageModelImp(stringbuffer.toString(), i, j);
    }
}
