var editFmTable = function(name,taskid,index){
   
   //setArrow(0,index);
   if(taskid=='NEWQYKB'){
   	  url = "../jsp/taskManager/editnewqkybfm.jsp?taskID="+taskid+"&page=newqykbfmpage";
   }
   taskFrame.location = encodeURI(url);
}