var findunit = function(){
	var url = "/hbsaserver/servlet/model?operation=showUnitTreeByNameOrCode&mainMenuStatus=reportTD&subMenuStatus=showDataTD&sessionTaskID=NEWQYKB&sessionTaskName=ABC";
	$.ajax({
	  url: url,
	  type:"GET",
//	  contentType:"application/x-www-form-urlencoded; charset=UTF-8",
//	  contentType : "application/x-www-form-urlencoded; charset=GBK",
	  data:{name:$("#txtunitname").val(),taskID:'NEWQYKB'},
	  dataType:'json',
	  success: function(data, textStatus, jqXHR){
	  	$("#treepanel").empty();
	  	
	  	console.log(data);
	  	var obj = data[0];
	  	console.log(data.length);
	  	
	  	
	  	var _url = "utilServlet?operation=showTree&unitID="+obj.unitCode+obj.reportType+"&taskID=NEWQYKB&treeName=cn.com.youtong.apollo.servlet.unittree.AddressInfoTree";
	  	var tree155 = new WebFXLoadTree(obj.unitName,_url,"javascript:changeUnit('"+obj.unitCode+obj.reportType+"','"+obj.unitName+"')");
		    tree155.icon = "../img/icon_9.gif";
			tree155.openIcon = "../img/icon_9.gif";
			tree155.setBehavior('classic');
			$("#treepanel").append(tree155.toString());
				
//				alert('111');
	  }
	});
	return;
	var txtvalue = $("#txtunitname").val(); 
	
	var nodes = $(".webfx-tree-item");
	console.log(nodes.length);
	$("#treepanel").empty();
	for (var i = 0; i < nodes.length; i++) {
		var str = $(nodes[i]).last().text();
		if(str.indexOf(txtvalue)>=0){
//			$("#treepanel").append($(nodes[i]));
		}
//		console.log($(nodes[i]).last().text());
	}
}