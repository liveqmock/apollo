var findunit = function(){
	var url = "/hbsaserver/servlet/model?operation=showUnitTreeByNameOrCode&mainMenuStatus=reportTD&subMenuStatus=showDataTD&sessionTaskID=NEWQYKB&sessionTaskName=ABC";
	if($("#txtunitname").val()!=null&&$("#txtunitname").val()!=''){
		$.ajax({
		  url: url,
		  type:"GET",
	//	  contentType:"application/x-www-form-urlencoded; charset=UTF-8",
	//	  contentType : "application/x-www-form-urlencoded; charset=GBK",
		  data:{name:$("#txtunitname").val(),taskID:'NEWQYKB'},
		  dataType:'json',
		  success: function(data, textStatus, jqXHR){
		  	$("#treepanel").empty();
		  	for (var i = 0; i < data.length; i++) {
		  		var obj = data[i];
		  		if(obj.unitCode){
		  			var _url = "utilServlet?operation=showTree&unitID="+obj.unitCode+obj.reportType+"&taskID=NEWQYKB&treeName=cn.com.youtong.apollo.servlet.unittree.AddressInfoTree";
		  			var treenode = new WebFXLoadTree(obj.unitName,_url,"javascript:changeUnit('"+obj.unitCode+obj.reportType+"','"+obj.unitName+"')");
			    		treenode.icon = "../img/icon_"+obj.reportType+".gif";
						treenode.openIcon = "../img/icon_"+obj.reportType+".gif";
						treenode.setBehavior('classic');
						$("#treepanel").append(treenode.toString());
				}
		  	}
		  }
		});
	}else{
		alert(27);
		location.reload();
	}
	
	return;
}