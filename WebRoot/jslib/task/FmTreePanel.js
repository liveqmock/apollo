Ext.ns('Yotop');
Yotop.FmTreePanel = function(){
	var config = {
		id:'fmtree',
		layout:"fit",
		animate:true,
		border:false,
		autoScroll:true,
		loader:new Ext.tree.TreeLoader({
			listeners : {"beforeload":function(treeLoader, node) {
		        this.baseParams.pid = node.attributes.id;
		    }},
			baseParams:{type:'tree',taskid:taskid},
//			url:'../back/role/gettreemenuinfo.jspx',
//			url:'../../jslib/task/data.json',
			url:'../../servlet/task?operation=makeUnitTree',
			autoLoad:true
		}),
		root:new Ext.tree.AsyncTreeNode({
			text:'',
			id:0
		}),
		listeners:{
			"click":function(node,e){
				alert(node.text);
			}
		},
		rootVisible:false
	};
	Yotop.FmTreePanel.superclass.constructor.call(this,config);
}
Ext.extend(Yotop.FmTreePanel,Ext.tree.TreePanel);