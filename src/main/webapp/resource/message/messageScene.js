//遮罩层覆盖区域
var $wrapper = $('#div-table-container');

var messageId; //当前messageid
var messageSceneId; //当前正在操作的sceneId
var currIndex;//当前正在操作的layer窗口的index

var currSceneMark;//当前测试备注
var resultTemplate;//测试详情结果页面模板

var interfaceName;
var messageName;

var validateFullJsonHtml; //全文验证
var validateKeywordHtml; //关键字验证
var sceneTestHtml; //场景测试

var validateValueOriginal; //原始验证值
/**
 * ajax地址
 */
var SCENE_LIST_URL = "scene-list"; //获取场景列表
var SCENE_EDIT_URL = "scene-edit";  //场景编辑
var SCENE_GET_URL = "scene-get"; //获取指定场景信息
var SCENE_DEL_URL = "scene-del"; //删除指定场景
var SCENE_CHANGE_VALIDATE_RULE_URL = "scene-changeValidateRule";
var SCENE_GET_TEST_OBJECT_URL = "scene-getTestObject"; //获取场景的测试数据和测试地址

var TEST_SCENE_URL = "test-sceneTest";

var VALIDATE_GET_URL = "validate-getValidate";
var VALIDATE_FULL_EDIT_URL = "validate-validateFullEdit";

var templateParams = {
		tableTheads:["场景名","验证方式", "测试数据","备注", "操作"],
		btnTools:[{
			type:"primary",
			size:"M",
			markClass:"add-object",
			iconFont:"&#xe600;",
			name:"添加场景"
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
			label:"场景ID",  	
			objText:"messageSceneIdText",
			input:[{	
				hidden:true,
				name:"messageSceneId"
				}]
		},
		{
			required:true,
			label:"场景名称",  
			input:[{	
				name:"sceneName",
				placeholder:"输入场景名称"
				}]
		},
		{	
			required:true,
			label:"验证规则",  			
			select:[{	
				name:"validateRuleFlag",
				option:[{
					value:"0",
					text:"关键字验证"
				},{
					value:"1",
					text:"节点验证"
				},{
					value:"2",
					text:"全文验证"
				}]
				}]
		},
		{
			name:"message.messageId",
		},
		{
			label:"备注",  
			textarea:[{	
				name:"mark",
				placeholder:"输入场景备注或者备忘的查询数据用的SQL语句"
				}]
		},
		]		
	};

var columnsSetting = [
                      {
                      	"data":null,
                      	"render":function(data, type, full, meta){                       
                              return checkboxHmtl(data.sceneName, data.messageSceneId, "selectScene");
                          }},
                      {"data":"messageSceneId"},
                      ellipsisData("sceneName"),
                      {
                    	  "data":null,
                    	  "render":function(data) {
                    		  	var option = {
                    		  			"0":{
                    		  				btnStyle:"primary",
                    		  				status:"关键字验证"
                    		  				},
                		  				"1":{
                		  					btnStyle:"warning",
                		  					status:"节点验证"
                		  					},
            		  					"2":{
            		  						btnStyle:"secondary",
            		  						status:"全文验证"
            		  						}
                    		  	};	
                    		  	
                    		  	messageSceneId = data.messageSceneId;
                    		  	return '<a href="javascript:;" onclick="showValidatRulePage(' + data.validateRuleFlag + ',\'' + data.sceneName + '\');">' + labelCreate(data.validateRuleFlag, option) + '</a>';							
                    	  }
                      },
                      {
                    	  "data":null,
                          "render":function(data, type, full, meta){
                          	var context =
                          		[{
                        			type:"default",
                        			size:"M",
                        			markClass:"show-test-data",
                        			name:data.testDataNum
                        		}];
                              return btnTextTemplate(context);
                              }
            		    },  
                      {
            		    "data":null,
            		    "className":"ellipsis",
            		    "render":function(data) { 
            		    	if (data.mark != "" && data.mark != null) {
            		    		currSceneMark = data.mark;
                		    	return '<a href="javascript:;" onclick="showSceneMark(\'' + data.sceneName + '\');"><span title="' + data.mark + '">' + data.mark + '</span></a>';
            		    	}
            		    	return "";
            		    }
                      },
                      {
                          "data":null,
                          "render":function(data, type, full, meta){
                            var context = [{
                            	title:"验证规则设定",
                	    		markClass:"validate-method",
                	    		iconFont:"&#xe654;"                           	
                            },{
                            	title:"场景测试",
                	    		markClass:"scene-test",
                	    		iconFont:"&#xe603;"
                            },{
                	    		title:"接口编辑",
                	    		markClass:"scene-edit",
                	    		iconFont:"&#xe6df;"
                	    	},{
                	    		title:"接口删除",
                	    		markClass:"scene-del",
                	    		iconFont:"&#xe6e2;"
                	    	}];                           
                          	return btnIconTemplate(context);
                          }}
                  ];
var eventList = {
		".add-object":function() {
			publish.renderParams.editPage.modeFlag = 0;					
			currIndex = layer_show("增加场景", editHtml, "550", "360", 1);
			//layer.full(index);
			publish.init();			
		},
		".batch-del-object":function() {
			var checkboxList = $(".selectScene:checked");
			batchDelObjs(checkboxList,SCENE_DEL_URL);
		},
		".scene-edit":function() {
			var data = table.row( $(this).parents('tr') ).data();
			publish.renderParams.editPage.modeFlag = 1;
			publish.renderParams.editPage.objId = data.messageSceneId;
			layer_show("编辑场景信息", editHtml, "550", "380",1);
			publish.init();	
		},
		".scene-del":function() {
			var data = table.row( $(this).parents('tr') ).data();
			delObj("确定删除此场景？请慎重操作!",SCENE_DEL_URL, data.messageSceneId, this);
		},
		".scene-test":function() {
			var data = table.row( $(this).parents('tr') ).data();
			messageSceneId = data.messageSceneId;
			layer_show("场景测试", sceneTestHtml, '800','500', 1, function() {
				renderSceneTestPage();
			});
			
		},
		".validate-method":function() {
			var data = table.row( $(this).parents('tr') ).data();
			messageSceneId = data.messageSceneId;
			layer.confirm(
					'请选择对测试返回结果进行验证的方式![默认为关键字验证]',
					{
						title:'提示',
						btn:['关键字验证','节点验证','全文验证'],
						btn3:function(index){
							changeValidateRule(data.messageSceneId, '2');   		
						}
					},function(index){ 
						changeValidateRule(data.messageSceneId, '0'); 
					},function(index){
						changeValidateRule(data.messageSceneId, '1');
					});
		},
		".show-test-data":function() {
			var data = table.row( $(this).parents('tr') ).data();	
			var title = interfaceName + "-" + messageName + "-" + data.sceneName + " " + "测试数据";
			var url = "testData.html?messageSceneId=" + data.messageSceneId + "&sceneName=" + data.sceneName;
			
			var index = layer_show(title, url, '1000','700', 2);
			//layer.full(index);
			
		}
};


var mySetting = {
		eventList:eventList,
		templateCallBack:function(df){
			messageId = GetQueryString("messageId");
			interfaceName = GetQueryString("interfaceName");
			messageName = GetQueryString("messageName");				
			publish.renderParams.listPage.listUrl = SCENE_LIST_URL + "?messageId=" + messageId;
			jqueryLoad("messageScene-validateFullJson.htm", $("#validate-full-json-page"), function(domHmtl) {
				validateFullJsonHtml = domHmtl;				
			});
			
			jqueryLoad("messageScene-test.htm", $("#scene-test-page"), function(domHmtl) {
				sceneTestHtml = domHmtl;			
			});
			
			jqueryLoad("messageScene-validateKeyword.htm", $("#validate-keyword-page"), function(domHmtl) {
				validateKeywordHtml = domHmtl;			
			});
			
			resultTemplate =  Handlebars.compile($("#scene-test-result").html());
			
			df.resolve();
			   		 	
   	 	},
		editPage:{
			editUrl:SCENE_EDIT_URL,
			getUrl:SCENE_GET_URL,
			rules:{
				sceneName:{
					required:true,
					minlength:2,
					maxlength:255
				}										
			},
			beforeInit:function(df){
				$("#message\\.messageId").val(messageId);
       		 	df.resolve();
       	 	},

		},		
		listPage:{
			listUrl:SCENE_LIST_URL,
			tableObj:".table-sort",
			columnsSetting:columnsSetting,
			columnsJson:[0,4,5,6]
		},
		templateParams:templateParams		
	};

$(function(){
	publish.renderParams = $.extend(true,publish.renderParams,mySetting);
	publish.init();
});

/**********************************************************************************************************************/
/**
 * 改变验证规则方法
 */
function changeValidateRule(sceneId, ruleFlag, callback) {
	$.get(SCENE_CHANGE_VALIDATE_RULE_URL,{messageSceneId:sceneId, validateRuleFlag:ruleFlag},function(data){
		if(data.returnCode == 0) {				
			refreshTable();	
			layer.msg("操作成功!", {icon:1, time:1500});
		}else{
			layer.alert(data.msg,{icon:5});
		}
	});
}

/**
 * 保存验证内容
 * 全文验证或者关键字验证
 */
function saveValidateJson(){	
	var sendData = {};
	if ($("#parameterName").length > 0) {
		var parameterName = '{"LB":"' + ($("#LB").val()).replace(/\"/g, "\\\"") + '","RB":"' + ($("#RB").val()).replace(/\"/g, "\\\"") + '","ORDER":"' + $("#ORDER").val() + '"}';
		sendData.parameterName = parameterName;
	}	
	
	sendData.validateValue = $("#validateValue").val();
	sendData.validateId = $("#validateId").val();
	
	$.post(VALIDATE_FULL_EDIT_URL, sendData, function(data){
		if(data.returnCode == 0){
			layer.closeAll('page');
			layer.msg('已保存!', {icon:1, time:1500});
		} else {
			layer.alert(data.msg,{icon:5});
		}
	});		
}

/**
 * 场景测试页面渲染
 */
function renderSceneTestPage() {
	var index = layer.load(2, {shade:0.35});
	$.get(SCENE_GET_TEST_OBJECT_URL, {messageSceneId:messageSceneId}, function(data){					
		if(data.returnCode == 0){
			var selectUrl=$("#selectUrl");
			var selectData=$("#selectData");
			selectUrl.html('');			
			selectData.html('');
			$(".textarea").val('');
			$.each(data.urls,function(i,n){
				selectUrl = selectUrl.append("<option value='"+n+"'>"+n+"</option>");
			});				
			
			if (data.testData.length > 0) {
				$.each(data.testData,function(i,n){
					selectData = selectData.append("<option data-id='" + n.dataId + "' value='"+i+"'>"+n.dataDiscr+"</option>");
				});
				$(".textarea").val(data.testData[0].dataJson);
				$("#selectData").change(function(){
					var p1 = $(this).children('option:selected').val();
					$(".textarea").val(data.testData[p1].dataJson);
				});
			}																		
		} else {
			layer.alert(data.msg, {icon:5});
		}
		layer.close(index);
	});
}

/**
 * 场景测试
 */
function sceneTest() {
	var requestUrl=$("#selectUrl").val();
	var requestMessage=$(".textarea").val();
		
	if(requestUrl == "" || requestUrl == null || requestMessage == "" || requestMessage == null){
		layer.msg('请选择正确的接口地址和测试数据',{icon:2, time:1500});
		return;
	}
	var dataId = $("#selectData > option:selected").attr("data-id");
	var index = layer.load(2, {shade:0.35});
	$.post(TEST_SCENE_URL, {messageSceneId:messageSceneId, dataId:dataId, requestUrl:requestUrl, requestMessage:requestMessage},function(data){
		if(data.returnCode==0){
			renderSceneTestPage();
			layer.close(index);
			var color="";
			var flag="";
			if (data.result.runStatus == "0") {
				color="green";
				flag="SUCCESS";
			} else if (data.result.runStatus == "1"){
				color="red";
				flag="FAIL";
			} else {
				color="red";
				flag="STOP";
			}
			
			
			var resultData = {
				color:color,
				flag:flag,
				useTime:data.result.useTime,
				statusCode:data.result.statusCode,
				responseMessage:(data.result.responseMessage == "null") ? "" : data.result.responseMessage,
				mark:data.result.mark
			};			
			
			parent.layer.open({
				  title: '测试结果',
				  shade: 0,
				  type: 1,
				  skin: 'layui-layer-rim', //加上边框		
				  area: ['700px', '400px'], //宽高
				  content: resultTemplate(resultData)
				});
		}else{
			layer.close(index);
			layer.alert(data.msg, {icon:5});
		}
	});
}

/**
 * 展示不同的类型验证规则的编辑页面
 * @param type
 */
function showValidatRulePage(type, sceneName) {
	if (type == "0") {
		layer_show(sceneName + '-关键字关联验证', validateKeywordHtml, '820', '360', 1, function() {
			$.get(VALIDATE_GET_URL, {messageSceneId:messageSceneId, validateMethodFlag:"0"},function(data){
				if(data.returnCode == 0) {
					if (data.parameterName != "") {
						var relevanceObject = JSON.parse(data.parameterName);
						$("#ORDER").val(relevanceObject.ORDER);
						$("#LB").val(relevanceObject.LB);
						$("#RB").val(relevanceObject.RB);
						$("#objectSeqText").text(relevanceObject.ORDER);
					}
					$("#parameterName").val(data.parameterName);
					$("#validateValue").val(data.validateValue);
					$("#validateId").val(data.validateId);
				} else {
					layer.alert(data.msg,{icon:5});
				}
			});
		});
	}
	
	if (type == "1") {
		var index = layer.open({
            type: 2,
            title: sceneName + "-节点验证管理",
            content: 'validateParameters.html?messageSceneId=' + messageSceneId
        });
		layer.full(index);
	}
	
	if (type == "2") {
		layer_show(sceneName + '-全文验证管理', validateFullJsonHtml, '800', '520', 1, function() {
			$.get(VALIDATE_GET_URL, {messageSceneId:messageSceneId, validateMethodFlag:"2"},function(data){
				if(data.returnCode == 0) {
					$("#validateValue").val(data.validateValue);
					$("#validateId").val(data.validateId);
					validateValueOriginal = data.validateValue;
				} else {
					layer.alert(data.msg,{icon:5});
				}
			});
		});
	}
	
}

/**
 * 显示场景备注
 */
function showSceneMark(sceneName) {
	layer.prompt({
		formType: 2,
		value: currSceneMark,
		title: sceneName + '-备注',
		area: ['500px', '300px']}, 
		function(value, index, elem){
			layer.close(index);
		});
}

/**
 * 关键字验证
 * 取值顺序页面按键  减一
 */
function reduceSeq() {
	var seq=$("#objectSeqText").text();
	if(seq==1){
		return;
	}
	$("#objectSeqText").text(seq-1);
	$("#ORDER").val(seq-1);
}

/**
 * 关键字验证
 * 取值顺序页面按键  加一
 */
function addSeq() {
	var seq=$("#objectSeqText").text();
	$("#objectSeqText").text(parseInt(seq)+1);
	$("#ORDER").val(parseInt(seq)+1);
}