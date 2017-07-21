var configData;
var modeFlag = 0;//0为全量测试

var BATCH_AUTO_TEST_URL = "test-scenesTest";
var GET_REPORT_INFO_URL = "report-get";
var GET_TEST_CONFIG_URL = "test-getConfig";
var UPDATE_TEST_CONFIG_URL = "test-updateConfig";
var LIST_MY_SETS_URL = "set-getMySet";

var isTesting = false;//当前是否有测试任务?
var reportId;
var count;

var mySetViewTemplate; //当前用户选择测试集测试

var eventList = {
	'#choose-test-set':function() {
		$.get(LIST_MY_SETS_URL, function(json) {
			if (json.returnCode == 0) {
				layer_show("我的测试集", mySetViewTemplate(json.data), '680', '640', 1);
			} else {
				layer.alert(json.msg, {icon:5});
			}
		});
	},
	'#prepare-total-test':function() {
		batchTest(0);
	},
	"#update-option":function() {
		updateTestOptions();
	},
	"#reset-option":function() {
		resetOptions();
	}	
};


var mySetting = {
		eventList:eventList,
		userDefaultRender:false,    
   	 	userDefaultTemplate:false,
   	 	customCallBack:function(params){
   	 		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
   	 		mySetViewTemplate =  Handlebars.compile($("#show-test-set").html());
   	 		$.get(GET_TEST_CONFIG_URL, function(json) {
   	 			if (json.returnCode == 0) {
   	 				configData = json.config;
   	 				resetOptions();
   	 			} else {
   	 				layer.alert("获取当前用户自动化测试配置失败：" + msg, {icon:5});
   	 			}
   	 		});
   	 		
   	 	}	
};

$(function(){			
	publish.renderParams = $.extend(true,publish.renderParams,mySetting);
	publish.init();
});


/*************************************************************************************************************/
function resetOptions () {
	if (configData != null) {
		$("#requestUrlFlag").val(configData.requestUrlFlag);
		$("#connectTimeOut").val(configData.connectTimeOut);
		$("#readTimeOut").val(configData.readTimeOut);
		$("#checkDataFlag").val(configData.checkDataFlag);
		$("#configId").val(configData.configId);
		$("#userId").val(configData.userId);
	}
}

//更新配置信息
function updateTestOptions(){
	var updateConfigData=$("#form-article-add").serializeArray();
	$.post(UPDATE_TEST_CONFIG_URL, updateConfigData, function(data){
		if(data.returnCode == 0){
			configData=data.config;
			layer.msg('更新成功',{icon:1, time:1500});
		} else {
			layer.alert("更新失败：" + data.msg, {icon:5});
		}
	});	
}

function batchTest(setId) {	
	if (isTesting) {
		layer.alert('当前已存在正在进行的测试任务,请等待测试完成!', {icon:0, title:'提示'});
		return false;
	}
	
	layer.closeAll("page");
	
	$("#testTips").html('');
	$("#total-count").text('0');
	$("#current-complete-count").text('0');
	$("#current-success-count").text('0');
	$("#current-fail-count").text('0');
	$("#current-stop-count").text('0');
	
	isTesting = true;
	$("#testTips").append('<p>正在发送测试请求...</p>');
	$("#testTips").append('<p>正在准备测试数据...</p>');
	$.get(BATCH_AUTO_TEST_URL + "?setId=" + setId, function(json) {
		if (json.returnCode == 0) {
			$("#testTips").append('<p>开始执行测试...</p>');
			$("#total-count").text(json.count);
			
			reportId = json.reportId;
			count = json.count;
						
			var intervalID = setInterval(function() {
				getProcessInfo(intervalID);
			}, 1000);
		} else {
			$("#testTips").append('<p>准备测试过程中出现错误,请检查接口、报文、场景的完整性!</p>');
			isTesting = false;
			layer.alert(json.msg,{icon:5});
		}
	});	
}

function getProcessInfo(intervalID) {
	$.get(GET_REPORT_INFO_URL + "?reportId=" + reportId, function(json) {
		if (json.returnCode == 0) {
			
			$("#current-complete-count").text(json.report.sceneNum);
			$("#current-success-count").text(json.report.successNum);
			$("#current-fail-count").text(json.report.failNum);
			$("#current-stop-count").text(json.report.stopNum);
			
			if (json.report.sceneNum == count) {
				isTesting = false;
				clearInterval(intervalID);
				$("#testTips").append('<p>测试完成,请至测试报告模块查看测试详细测试报告!</p>');
			}
		} else {
			layer.alert(data.msg,{icon:5});
		}
	});
}