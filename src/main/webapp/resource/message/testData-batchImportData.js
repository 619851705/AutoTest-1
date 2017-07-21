/**
 * ajax地址
 */

var GET_SETTING_DATA_URL = "data-getSettingData"; //获取当前场景所属报文的所有可编辑入参节点 包含其他信息
var IMPORT_DATA_VALUES_URL = "data-importDatas";

var messageSceneId;
var params;
var dataMsg;
var currParam;

var parameterInfoTemplate;

var numInfo = {
		"1":"一",
		"2":"二",
		"3":"三",
		"4":"四",
		"5":"五",
		"6":"六",
		"7":"七",
		"8":"八",
		"9":"九",
		"10":"十"
};

var currColNum = 2;

var eventList = {
	"#parameters > button":function() {
		var parameterId = $(this).attr("id");
		var thisParam = getParamInfo(parameterId);
		if (thisParam != null) {
			currParam = thisParam;
			layer_show("参数信息", parameterInfoTemplate(thisParam), '500', '300', 1);
		}
	},
	".parameter-info-table button:eq(0)":function() { //确定选中参数
		if (currColNum == 10) {
			layer.alert("最大可处理列数为10列!", {icon:5});
			layer.closeAll('page');
			return false;
		}
		currColNum++;
		var selectParamHtml =  '<div class="row cl" style="margin-bottom: 8px;">'
			+ '<label class="form-label col-xs-4 col-sm-3"><strong>第' + numInfo[currColNum + ""] + '列:</strong></label>'
			+ '<div class="formControls col-xs-5 col-sm-4">'
			+ '<button class="btn btn-default radius cancel-selected-parameter" name="' + currParam.parameterId + '">' + currParam.path + '</button>'
			+ '</div>'
			+ '</div>';
		$("#select-parameters").append(selectParamHtml);
		layer.closeAll('page');
		
	},
	".parameter-info-table button:eq(1)":function() {
		layer.closeAll('page');
	},
	".cancel-selected-parameter":function() {
		var that = $(this);
		layer.confirm("确定删除此列？", function(index) {
			that.parents('.row').remove();
			//重新计算列数
			currColNum--;
			var labels = $("#select-parameters label > strong:gt(1)");
			
			$.each(labels, function(i, n) {
				$(n).text('第' + numInfo[(i + 3) + ""] + '列:');
			});
			
			layer.close(index);
		});
		
	}

};



var mySetting = {
		eventList:eventList,
		userDefaultRender:false,    
   	 	userDefaultTemplate:false,
   	 	customCallBack:function(renderParams){
   	 		parameterInfoTemplate = Handlebars.compile($("#parameter-info-template").html());
   	 		
	   	 	$(".page-container").spinModal();
	   	 	messageSceneId = GetQueryString("messageSceneId");
	   	 	$.post(GET_SETTING_DATA_URL, {messageSceneId:messageSceneId}, function(json) {
	   	 		if (json.returnCode == 0) {
	   	 			params = json.params;
	   	 			dataMsg = json.dataMsg;
	   	 			var param = $("#parameters");
	   	 			$.each(params, function(i, n) {
	   	 				param.append('<button class="btn btn-sm radius btn-success" title="' + n.path + '" id="' + n.parameterId + '">' + n.parameterIdentify + '</button>');
	   	 			});	   	 			
	   	 			
	   	 			$(".page-container").spinModal(false);
	   	 		} else {
	   	 			layer.alert(json.msg, {icon:5});
	   	 		}
	   	 	});	   
	   	 	
	   	 	$(".l > button:eq(0)").click(viewDataMsg);//查看完整报文按钮
	   	 	$(".l > button:eq(1)").click(clearAllSelected);//清除已选择按钮
	   	 	$(".r > button:eq(0)").click(ensureSelected);//确定选择按钮
	   	 	$(".r > button:eq(1)").click(confrimData);//提交数据按钮
   	 	}	
	};


$(function(){			
	publish.renderParams = $.extend(true,publish.renderParams,mySetting);
	publish.init();
});

/************************************************************************/
/**
 * 查看完整报文
 */
function viewDataMsg() {
	layer.prompt({
		  formType: 2,
		  value: dataMsg,
		  title: "完整报文",
		  area: ['500px', '300px'] //自定义文本域宽高
		}, function(value, index, elem){
		  layer.close(index);
	});
}

/**
 * 清除已选择
 */
function clearAllSelected() {
	if (currColNum == 2) {
		return false;
	}
	layer.confirm("是否清除所有已选择参数？", function(index) {
		$("#select-parameters > .row:gt(1)").remove();
		currColNum = 2;
		layer.close(index);
	});
	
}

/**
 * 确定选择
 */
function ensureSelected() {
	if (currColNum == 2) {
		layer.msg("请点击上方的按钮至少选择一个参数!", {icon:0, time:1500});
		return false;
	}
	
	var info = getSortedParams();
	$("#input-data-textarea > textarea").attr("placeholder" ,"数据标识,状态," + info.paths);
	
	if ($("#input-data-textarea").is(":hidden")) {
		$("#input-data-textarea").show('normal');
	}		
}

/**
 * 提交数据
 */
function confrimData() {
	if (currColNum == 2) {
		layer.msg("请点击上方的按钮至少选择一个参数!", {icon:0, time:1500});
		return false;
	}
	
	if ($("textarea").val() == "" || $("textarea").val() == null) {
		layer.msg("上传数据不能为空!", {icon:5, time:1500});
		return false;
	}
	
	layer.confirm("确定提交数据？请在提交之前确认选择!", function(index) {
		var info = getSortedParams();
		var dataValues = $("textarea").val();
		$(".page-container").spinModal();
		$.post(IMPORT_DATA_VALUES_URL, {ids:info.ids, paths:info.paths, datas:dataValues, messageSceneId:messageSceneId}, function(json) {
			if (json.returnCode == 0) {				
				layer.alert("本次共提交数据" + json.totalCount + "条,成功" + json.successCount + "条,失败" + json.failCount + "条!"
						,{icon:1, title:"提示"}, function(index) {
							layer.close(index);
							layer.msg("提交成功,请返回查看数据或者继续提交数据!", {icon:1, time:1500});
						});
			} else {
				layer.alert(json.msg, {icon:5});
			}
			$(".page-container").spinModal(false);
		});					
		layer.close(index);
	});
	
}

/**
 * 获取参数列表
 */
function getSortedParams() {
	var paramsBtn = $("#select-parameters > .row:gt(1) button");
	var selectedParamsInfo = {
			ids:"",
			paths:""
	};
	$.each(paramsBtn, function(i, n) {
		selectedParamsInfo.ids += $(n).attr("name");
		selectedParamsInfo.paths +=  $(n).text();
			
		if (i < (currColNum - 3)) {
			selectedParamsInfo.ids += ",";
			selectedParamsInfo.paths += ",";
		}
	});
	
	return selectedParamsInfo;
	
} 

/**
 * 从内存中获取指定的参数信息
 */
function getParamInfo(parameterId) {
	var thisParam = null;
	$.each(params, function(i, n) {
		if (parameterId == n.parameterId) {
			thisParam = n;
			return false;
		}
	});
	return thisParam;
}