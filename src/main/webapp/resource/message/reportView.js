var resultData;
var reportId;
var eventList = {
	'tbody > tr':function() {
		var id = $(this).attr("id");       
        
        layer.open({
            type: 1,
            area: ['760px', '700px'],
            maxmin: false,
            fixed:false,
            //isOutAnim:false,
            //anim:-1,
            shade:0.4,
            title: '详细结果',
            shadeClose:true,
            content: $("#view-html").html(),
            success:function(layero) {
            	
            	$(".layui-layer-content > .view-details > .row:eq(0) .col-sm-9").text(resultData[id].requestUrl);
                $(".layui-layer-content > .view-details > .row:eq(1) .col-sm-9").text(resultData[id].statusCode);
                $(".layui-layer-content > .view-details > .row:eq(2) .col-sm-9 textarea").text(resultData[id].requestMessage);
                $(".layui-layer-content > .view-details > .row:eq(3) .col-sm-9 textarea").text((resultData[id].responseMessage != null) ? resultData[id].responseMessage : "");
                $(".layui-layer-content > .view-details > .row:eq(4) .col-sm-9 textarea").text((resultData[id].mark != null) ? resultData[id].mark : "");
            }
        });
	},	
};

var mySetting = {
		eventList:eventList,
		userDefaultRender:false,    
   	 	userDefaultTemplate:false,
   	 	customCallBack:function(params){
   	 		
	   	 	//esc关闭所有弹出层
	       	 $(window).keydown (function(e) {
	       		 var keycode = event.which;
	       		 if(keycode == 27){
	       			 layer.closeAll('page');
	       			 e.preventDefault();
	       		 } 
	       	 });
   	 		
   	 		reportId = GetQueryString("reportId"); 
	   	 	$.get("report-getReportDetail?reportId=" + reportId, function(data){
				if (data.returnCode == 0) {
					
					$(".panel-heading").text(data.title);
			        $("#sceneNum").append(data.desc.sceneNum);
			        $("#successNum").append(data.desc.successNum);
			        $("#failNum").append(data.desc.failNum);
			        $("#stopNum").append(data.desc.stopNum);
			        $("#successRate").append(data.desc.successRate);
			        $("#testDate").append(data.desc.testDate);
			        
			        var dataHtml = '';
			        resultData = data.data;
			        $.each(resultData, function(i, report){
			        	var messageInfo = (report.messageInfo).split(",");
			        	
			            dataHtml += '<tr id="' + i + '">'    
			                    + '<td>' + messageInfo[0] + '</td>'
			                    + '<td>' + report.protocolType + '</td>'
			                    + '<td>' + messageInfo[1] + '</td>'
			                    + '<td>' + messageInfo[2] + '</td>'
			                    + '<td class="status">' + report.runStatus + '</td>'
			                    + '<td>' + report.useTime + '</td>'
			                    + '</tr>';
			         });
			         $("tbody").append(dataHtml);
			         
			         var statusList = $(".status");
			         var status;
			         $.each(statusList, function(i, n){
			             status = $(n).text();
			             if (status == "0") {
			                 $(n).css("color", "green");
			                 $(n).text("SUCCESS");
			             }

			             if (status == "1") {
			            	 $(n).css("color", "red");
			            	 $(n).text("FAIL");
			             }

			             if (status == "2") {
			            	 $(n).css("color", "#aaaaaa");
			            	 $(n).text("STOP");
			             }
			         });			         
				} else {
					layer.alert(data.msg,{icon:5});
				}
			});
   	 	}
	};


$(function(){			
	publish.renderParams = $.extend(true,publish.renderParams,mySetting);
	publish.init();
});