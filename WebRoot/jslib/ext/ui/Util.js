var msg = function(title, msg,icon) {
	Ext.Msg.show({
		title : title,
		msg : msg,
		minWidth : 200,
		modal : true,
		icon : icon,
		buttons : Ext.Msg.OK
	});
};
window.renderHallName = function(value, meta, rec, rowIdx, colIdx, ds){
    return '<div ext:qtitle="" ext:qtip="' + value + '">'+ value +'</div>';
}
function renderHallName(value, meta, rec, rowIdx, colIdx, ds){
    return '<div ext:qtitle="" ext:qtip="' + value + '">'+ value +'</div>';
}
