//遮罩层覆盖区域
var $wrapper = $('#div-table-container');

var messageSceneId; //当前正在操作的sceneId
var sceneName; //当前场景名称
var currIndex;//当前正在操作的layer窗口的index

var currParams; //当前设置数据内容

var setttingParameterTemplate; //数据设置模板

/**
 * ajax地址
 */
var DATA_LIST_URL = "data-list"; //获取数据列表
var DATA_EDIT_URL = "data-edit";  //测试数据编辑
var DATA_GET_URL = "data-get"; //获取指定测试数据信息
var DATA_DEL_URL = "data-del"; //删除指定数据
var DATA_CHECK_NAME_URL = "data-checkName"; //验证数据标记是否重复

var GET_SETTING_DATA_URL = "data-getSettingData";

var CREATE_NEW_DATA_MSG_URL = "data-createDataMsg";

var templateParams = {
		tableTheads:["标记", "状态", "操作"],
		btnTools:[{
			type:"primary",
			size:"M",
			markClass:"add-object",
			iconFont:"&#xe600;",
			name:"添加数据"
		},{
			type:"primary",
			size:"M",
			markClass:"batch-add-object",
			iconFont:"&#xe600;",
			name:"批量导入"
		},{
			type:"danger",
			size:"M",
			markClass:"batch-del-object",
			iconFont:"&#xe6e2;",
			name:"批量删除"
		}],
		formControls:[
		{
			edit:true,
			label:"数据ID",  	
			objText:"dataIdText",
			input:[{	
				hidden:true,
				name:"dataId"
				}]
		},
		{
			required:true,
			label:"数据标记",  
			input:[{	
				name:"dataDiscr",
				placeholder:"区别不同数据的标示,自定义"
				}]
		},
		{
			label:"数据内容",  
			button:[{
				style:"danger",
				value:"查看设置",
				name:"view-params-data"
			}]
		},
		{	
			required:true,
			label:"可用状态",  			
			select:[{	
				name:"status",
				option:[{
					value:"0",
					text:"可用"
				},{
					value:"1",
					text:"已使用"
				}]
				}]
		},
		{
			name:"messageScene.messageSceneId",
		},
		{
			name:"paramsData"
		},
		]		
	};


var columnsSetting = [
                      {
                      	"data":null,
                      	"render":function(data, type, full, meta){                       
                              return checkboxHmtl(data.dataDiscr, data.dataId, "selectData");
                          }},
                      {"data":"dataId"},
                      {
                    	  "data":null,
                    	  "className":"appoint ellipsis",
                          "render":function(data, type, full, meta){  
                        	  	data = data.dataDiscr||"";
                          		return '<span title="' + data + '">' + data + '</span>';
                            }
            		    },
                      {
                    	  "data":null,
                    	  "render":function(data) {
                    		  	var option = {
                    		  			"0":{
                    		  				btnStyle:"success",
                    		  				status:"可用"
                    		  				},
                		  				"1":{
                		  					btnStyle:"danger",
                		  					status:"已使用"
                		  					}
                    		  	};	
                    		  	return labelCreate(data.status, option);							
                    	  }
                      },                                      
                      {
                          "data":null,
                          "render":function(data, type, full, meta){
                            var context = [{
                	    		title:"数据编辑",
                	    		markClass:"data-edit",
                	    		iconFont:"&#xe6df;"
                	    	},{
                	    		title:"数据删除",
                	    		markClass:"data-del",
                	    		iconFont:"&#xe6e2;"
                	    	}];                           
                          	return btnIconTemplate(context);
                          }}
                  ];


var eventList = {
		".add-object":function() {
			publish.renderParams.editPage.modeFlag = 0;					
			currIndex = layer_show("增加数据", editHtml, "550", "320", 1);
			//layer.full(index);
			publish.init();			
		},
		".batch-add-object":function() { //批量导入数据						
			var index = parent.layer.open({
				type: 2,
				maxmin: false,
				shade:0.4,
				title: sceneName + "-批量导入数据",
				content: "testData-batchImportData.html?messageSceneId=" + messageSceneId
			});
			
			parent.layer.full(index);
		},
		".batch-del-object":function() {
			var checkboxList = $(".selectData:checked");
			batchDelObjs(checkboxList,DATA_DEL_URL);
		},
		".data-edit":function() {
			var data = table.row( $(this).parents('tr') ).data();
			publish.renderParams.editPage.modeFlag = 1;
			publish.renderParams.editPage.objId = data.dataId;
			layer_show("编辑数据信息", editHtml, "550", "340",1);
			publish.init();	
		},
		".data-del":function() {
			var data = table.row( $(this).parents('tr') ).data();
			delObj("确定删除此测试数据？请慎重操作!",DATA_DEL_URL, data.dataId, this);
		},
		".appoint":function() { //展示替换好了数据的报文
			var data = table.row( $(this).parents('tr') ).data();
			$.get(DATA_GET_URL + "?dataId=" + data.dataId, function(json) {
				if (json.returnCode == 0) {
					layer.prompt({
						  formType: 2,
						  value: json.object.dataJson,
						  title: json.object.dataDiscr,
						  area: ['500px', '300px'] //自定义文本域宽高
						}, function(value, index, elem){
						  layer.close(index);
					});
				} else {
					layer.alert(json.msg, {icon:5});
				}
			});
		},
		"#view-params-data":function() {  //查看或者设置数据内容
			//publish.renderParams.editPage.modeFlag 0 1
			$.post(GET_SETTING_DATA_URL, {messageSceneId:messageSceneId, dataId:$("#dataId").val()} ,function(json) {
				if (json.returnCode == 0) {
					currParams = json.params;
					layer_show("设置数据", setttingParameterTemplate(json), '900', '640', 1, null, function(index, layero) {
						$("#paramsData").val(getParamsData());
						layer.close(index);
					});
				} else {
					layer.alert(json.msg, {icon:5});
				}				
			});			
		}
};


var mySetting = {
		eventList:eventList,
		templateCallBack:function(df){
			messageSceneId = GetQueryString("messageSceneId");
			sceneName = GetQueryString("sceneName");
			publish.renderParams.listPage.listUrl = DATA_LIST_URL + "?messageSceneId=" + messageSceneId;
			setttingParameterTemplate = Handlebars.compile($("#setting-parameter").html());			
			df.resolve();			   		 	
   	 	},
   	 	customCallBack:function() {
   	 		
   	 	},
		editPage:{
			editUrl:DATA_EDIT_URL,
			getUrl:DATA_GET_URL,
			rules:{
				dataDiscr:{
					required:true,
					minlength:1,
					maxlength:100,
					remote:{
						url:DATA_CHECK_NAME_URL,
						type:"post",
						dataType: "json",
						data: {                   
					        dataDiscr: function() {
					            return $("#dataDiscr").val();
					        },
					        dataId:function(){
					        	return $("#dataId").val();
					        },
					        messageSceneId:function() {
					        	return messageSceneId;
					        }
					}}
				}				
			},
			beforeInit:function(df){
				$("#messageScene\\.messageSceneId").val(messageSceneId);
       		 	df.resolve();
       	 	},

		},		
		listPage:{
			drawTableCallback:function() {
				
			},
			listUrl:DATA_LIST_URL,
			tableObj:".table-sort",
			columnsSetting:columnsSetting,
			columnsJson:[0,4]
		},
		templateParams:templateParams		
	};

$(function(){
	publish.renderParams = $.extend(true,publish.renderParams,mySetting);
	publish.init();
});

/******************************************************************************************************/
function getParamsData() {
	var paramInputs = $("#select-parameters input");
	var paramsData={};
	$.each(paramInputs, function(i, n) {
		if ($(n).val() != null && $(n).val() != "") {
			for (var i = 0;i < currParams.length;i++) {
				if ($(n).val() == currParams[i].defaultValue) {
					return true;
				}
			}
			if (paramsData["TopRoot." + $(n).attr("name")] != null) {
				var indexValue = paramsData["TopRoot." + $(n).attr("name")];
				
				if (typeof indexValue == 'object') {
					indexValue.push($(n).val());
				} else {
					paramsData["TopRoot." + $(n).attr("name")] = [];
					paramsData["TopRoot." + $(n).attr("name")].push(indexValue);
					paramsData["TopRoot." + $(n).attr("name")].push($(n).val());
				}
				
			} else {
				paramsData["TopRoot." + $(n).attr("name")] = $(n).val();
			}								
		}
	});
	
	if ($.isEmptyObject(paramsData)) {
		return "";
	}
	
	return JSON.stringify(paramsData);
}

/**
 * 根据设置的数据重新生成带数据的报文
 */
function createDataMessage() {
	var paramsDataStr = getParamsData();
	
	if (paramsDataStr != "") {
		$.post(CREATE_NEW_DATA_MSG_URL, {messageSceneId:messageSceneId, paramsData:paramsDataStr}, function(json) {
			if (json.returnCode == 0) {
				layer.msg("生成报文成功!", {icon:1,time:1500});
				$("#dataMsg > textarea").val(json.dataMsg);
			} else {
				layer.alert(json.msg, {icon:5});
			}
		});
	}	
}