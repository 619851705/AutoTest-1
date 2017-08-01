var table;
//遮罩层覆盖区域
var $wrapper = $('#div-table-container');

var interfaceId; //当前interfaceId
var messageId; //当前正在操作的messageid
var currIndex;//当前正在操作的layer窗口的index
var protocolJson;
var protocolType;

var addParameterTemplate;

/**
 * ajax地址
 */
var MESSAGE_LIST_URL = "message-list"; //获取接口列表
var MESSAGE_EDIT_URL = "message-edit";  //接口编辑
var MESSAGE_GET_URL = "message-get"; //获取指定接口信息
var MESSAGE_DEL_URL = "message-del"; //删除指定接口
var MESSAGE_FORMAT_URL = "message-format";//格式json串
var MESSAGE_VALIDATE_JSON_URL = "message-validateJson";//报文json串验证

var templateParams = {
		tableTheads:["接口","报文名", "类型", "创建时间", "状态", "创建用户", "最后修改", "入参", "场景", "操作"],
		btnTools:[{
			type:"primary",
			size:"M",
			markClass:"add-object",
			iconFont:"&#xe600;",
			name:"添加报文"
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
			label:"报文ID",  	
			objText:"messageIdText",
			input:[{	
				hidden:true,
				name:"messageId"
				}]
		},
		{
			required:true,
			label:"报文名称",  
			input:[{	
				name:"messageName"
				}]
		},
		{	
			required:true,
			label:"报文类型",  			
			select:[{	
				name:"messageType",
				option:[{
					value:"json",
					text:"JSON"
				},{
					value:"xml",
					text:"XML"
				},{
					value:"url",
					text:"URL"
				}]
				}]
		},
		{
			name:"interfaceInfo.interfaceId",
		},
		{
			required:true,
			label:"调用参数",
			input:[{
				hidden:true,
				name:"callParameter"
				}],
			button:[{
				style:"primary",
				value:"配置",
				markClass:"setting-call-parameter"
			}]
		},
		{
			required:true,
			label:"报文入参",  
			input:[{
				hidden:true,
				name:"parameterJson"
				}],
			button:[{
				style:"danger",
				value:"验证",
				markClass:"validate-parameter-json"
			},{
				style:"info",
				value:"格式化",
				embellish:"<br><br>",
				markClass:"format-parameter-json"
			}],
			textarea:[{
				placeholder:"输入报文入参"
			}]
		},
		{
			label:"请求地址",  
			input:[{	
				name:"requestUrl",
				placeholder:"请输入该报文指定的请求地址"
				}]
		},
		{	
			required:true,
			label:"报文状态",  			
			select:[{	
				name:"status",
				option:[{
					value:"0",
					text:"可用"
				},{
					value:"1",
					text:"禁用"
				}]
				}]
		},		
		{
			edit:true,			
			label:"创建日期",  
			objText:"createTimeText",
			input:[{	
				hidden:true,
				name:"createTime"
				}]
		},
		{
			edit:true,			
			label:"创建用户",  
			objText:"user.realNameText",
		},
		{
			edit:true,
			name:"user.userId"
							
		},
		{
			edit:true,			
			label:"最后修改",  
			objText:"lastModifyUserText",
			input:[{	
				hidden:true,
				name:"lastModifyUser"
				}]
		}
		]		
	};
//["报文名", "创建时间", "状态", "创建用户", "最后修改", "入参", "场景", "操作"],
var columnsSetting = [
                       {
                      	"data":null,
                      	"render":function(data, type, full, meta){                       
                              return checkboxHmtl(data.messageName,data.messageId,"selectMessage");
                          }},
                       {"data":"messageId"},
                       {
                         "className":"ellipsis",
               		     "data":"interfaceName",
                         "render":CONSTANT.DATA_TABLES.COLUMNFUN.ELLIPSIS
                       },                      
                       {
                      	"className":"ellipsis",
            		    "data":"messageName",
                      	"render":CONSTANT.DATA_TABLES.COLUMNFUN.ELLIPSIS
                      	},
                      	{
                     	   "data":null,
                     	   "render":function(data) {
                     		   return labelCreate((data.messageType).toUpperCase());
                     	   }
                         
                        },
                       {
                    	"className":"ellipsis",
              		    "data":"createTime",
              		    "render":CONSTANT.DATA_TABLES.COLUMNFUN.ELLIPSIS  
              		    },
                       {
                        	"data":null,
                        	"render":function(data, type, full, meta ){
                                return labelCreate(data.status);
                                }
              		    },
              		    {"data":"user.realName"},{"data":"lastModifyUser"},
              		    {
                            "data":null,
                            "render":function(data, type, full, meta){
                            	var context =
                            		[{
                          			type:"primary",
                          			size:"M",
                          			markClass:"get-params",
                          			name:"获取"
                          		}];
                                return btnTextTemplate(context);
                                }
              		    },
              		    {
              		    	"data":null,
                            "render":function(data, type, full, meta){
                            	var context =
                            		[{
                          			type:"default",
                          			size:"M",
                          			markClass:"show-scenes",
                          			name:data.sceneNum
                          		}];
                                return btnTextTemplate(context);
                                }
              		    },
              		    {
                            "data":null,
                            "render":function(data, type, full, meta){
                              var context = [{
	                  	    		title:"报文编辑",
	                  	    		markClass:"message-edit",
	                  	    		iconFont:"&#xe6df;"
                  	    		},{
	                  	    		title:"报文删除",
	                  	    		markClass:"message-del",
	                  	    		iconFont:"&#xe6e2;"
                  	    		}];
                            	return btnIconTemplate(context);
                            }}
              		    ];
var currentCallParamterSpan;
var eventList = {
		'.add-child-call-parameter':function() {
			
			var name = $(this).text();
			var context = {"key":"", "value":""};
			layer_show("添加参数-" + name, addParameterTemplate(context), '360', '240', 1, function(layero, index) {
				$("#save-new-call-parameter").attr("layer-index", index);
				$("#save-new-call-parameter").attr("parent-parameter-name", name);
				$("#save-new-call-parameter").attr("mode", "add");
			});			
		},
		'.edit-this-call-parameter':function() {
			currentCallParamterSpan = $(this);
			var name = $(this).parents('div').siblings('label').text();
			var keyValue = ($(this).text()).split("=");
			var context = {"key":keyValue[0], "value":keyValue[1]};
			layer_show("修改参数-" + name, addParameterTemplate(context), '360', '240', 1, function(layero, index) {
				$("#save-new-call-parameter").attr("layer-index", index);
				$("#save-new-call-parameter").attr("parent-parameter-name", name);
				$("#save-new-call-parameter").attr("mode", "edit");
			});			
		},
		'#save-new-call-parameter':function() {
			if ($(this).attr("mode") == "edit") {
				currentCallParamterSpan.text($("#call-parameter-name").val() + '=' +  $("#call-parameter-value").val());
			} else {
				$("label:contains('" + $(this).attr("parent-parameter-name") + "')").siblings('div')
					.append('<span class="label label-success radius edit-this-call-parameter appoint">' + $("#call-parameter-name").val() + '=' +  $("#call-parameter-value").val() + '</span><i class="Hui-iconfont del-this-call-parameter appoint" style="margin-right:8px;">&#xe6a6;</i>');
			}
			
			layer.close($(this).attr("layer-index"));
			layer.msg('保存成功!', {icon:1, time:1500});
		},
		'.del-this-call-parameter':function() {
			$(this).prev('span').remove();
			$(this).remove();
		},
		'#change-call-parameter':function() {
			var form = $(this).parents('form');
			var callParameter = {};
			$.each(form.children('.parameter'), function(i, n) {
				var parentKey = $(this).children('label').text();
				
				var params =  $(this).children('div');
				
				if (params.children('input').length > 0) {
					callParameter[parentKey] = params.children('input').val();
					return true;
				}	
				callParameter[parentKey] = {};
				if (params.children('span')) {				
					$.each(params.children('span'), function(i1, n1) {
						var keyValue = ($(this).text()).split("=");
						callParameter[parentKey][keyValue[0]] = keyValue[1];
					});
				}								
			});
			$("#callParameter").val(JSON.stringify(callParameter));
			layer.close($(this).attr("layer-index"));
			layer.msg('保存成功!', {icon:1, time:1500});
		},
		'.setting-call-parameter':function() {	
			
			if ($("#callParameter").val() == null || $("#callParameter").val() == "") {
				$("#callParameter").val(JSON.stringify(protocolJson[protocolType]));
			}
			
			var json = JSON.parse($("#callParameter").val());
			
			var callParameterViewHtml = '<article class="page-container"><form action="" method="" class="form form-horizontal">';
						
			if (json != null && !$.isEmptyObject(json)) {
				$.each(json, function(i, n) {
					callParameterViewHtml += '<div class="row cl parameter">'
						+ '<label class="form-label col-xs-4 col-sm-3';
					if (typeof n == 'object') {
						callParameterViewHtml += ' add-child-call-parameter" style="cursor: pointer;"';
					} else {
						callParameterViewHtml += '"';
					}
					callParameterViewHtml += '>' + i + '</label><div class="formControls col-xs-8 col-sm-9">';
					if (typeof n == 'object') {	
						if (!$.isEmptyObject(n)) {
							$.each(n, function(i1, n1) {
								callParameterViewHtml += '<span class="label label-success radius edit-this-call-parameter appoint">' + i1 + '=' + n1 + '</span><i class="Hui-iconfont del-this-call-parameter appoint" style="margin-right:8px;">&#xe6a6;</i>';							
							});
						}													
					} else {
						callParameterViewHtml += '<input type="text" class="input-text radius" value="' + n + '">';
					}
					callParameterViewHtml += '</div></div>';
				});
			} else {
				layer.alert("读取协议参数失败!", {icon:5});
				return false;
			}
			
			callParameterViewHtml += '<div class="row cl"><div class="col-xs-7 col-sm-8 col-xs-offset-4 col-sm-offset-3"><input class="btn btn-danger radius" type="button" value="&nbsp;&nbsp;保存更改&nbsp;&nbsp;" id="change-call-parameter"></div></div></form></article>';
			
			layer_show(protocolType + "调用参数设置", callParameterViewHtml, '700', '500', 1, function(layero, index) {
				$("#change-call-parameter").attr("layer-index", index);
			});
			
		},
		".get-params":function(){
			var data = table.row( $(this).parents('tr') ).data();
			messageId = data.messageId;
			var paramsHmtl = '<div class="page-container" id="parameter-json-textarea" style="padding:10px;">'
				+ '<div class="cl pd-5 bg-1 bk-gray"> <span class="l">'
				+ '<a href="javascript:;" id="copy-message-json" class="btn btn-primary radius">复制</a></span>'
				+ '&nbsp;<span class="r"><a href="javascript:;" onclick="getParameterJson();" class="btn btn-primary radius">刷新</a></span>'
				+ '</div><textarea class="textarea radius dct-message-json"></textarea></div>';
			
			layer_show(data.messageName + "-[入参报文]", paramsHmtl, '800', '500', 1, function(){
				getParameterJson();					
				$("#copy-message-json").zclip({
					path: "../../libs/ZeroClipboard.swf",
					copy: function(){
					return $(".textarea").val();
					},
					afterCopy:function(){/* 复制成功后的操作 */
						layer.msg('复制成功,CTRL+V粘贴',{icon:1,time:1500});
			        }
				});
			});
				
		},
		".show-scenes":function(){
			var data = table.row( $(this).parents('tr') ).data();			
			currIndex = layer_show(data.interfaceName + "-" + data.messageName + "-场景列表", "messageScene.html?messageId=" + data.messageId
					+ "&interfaceName=" + data.interfaceName + "&messageName=" + data.messageName
					, "" , "", 2);
			layer.full(currIndex);
		},
		".add-object":function() {
			publish.renderParams.editPage.modeFlag = 0;					
			currIndex = layer_show("增加报文", editHtml, "800", "560",1);
			//layer.full(index);
			publish.init();			
		},
		".batch-del-object":function() {
			var checkboxList = $(".selectMessage:checked");
			batchDelObjs(checkboxList,MESSAGE_DEL_URL);
		},
		".message-del":function() {
			var data = table.row( $(this).parents('tr') ).data();
			delObj("确定删除此报文？此操作同时会删除该报文下所有的场景及相关数据,请谨慎操作!",MESSAGE_DEL_URL,data.messageId,this);
		},
		".message-edit":function() {
			var data = table.row( $(this).parents('tr') ).data();
			messageId = data.messageId;
			publish.renderParams.editPage.modeFlag = 1;
			publish.renderParams.editPage.objId = messageId;
			layer_show("编辑报文信息", editHtml, "850", "720",1);
			publish.init();	
		},
		".validate-parameter-json":function() {
			var jsonStr = $(".textarea").val();
			var messageType = $("#messageType").val();
			if(jsonStr == null || jsonStr == "") {
				layer.msg('你还没有输入任何内容',{icon:5,time:1500});
				return false;
			}
			$.post(MESSAGE_VALIDATE_JSON_URL,{parameterJson:jsonStr,interfaceId:interfaceId, messageType:messageType, messageId:messageId},function(data){
				if(data.returnCode==0){
					layer.msg('验证通过,请执行下一步操作',{icon:1, time:1500});
					$("#parameterJson").val(jsonStr);
				}else{
					if (data.msg == null) {
						data.msg = "解析报文失败,请检查!";
					}
					layer.alert(data.msg,{icon:5});
				}
			});
		},
		".format-parameter-json":function(){
			var jsonStr = $(".textarea").val();
			var messageType = $("#messageType").val();
			if(jsonStr == null || jsonStr == "") {
				layer.msg('你还没有输入任何内容',{icon:5,time:1500});
				return false;
			}
			$.post(MESSAGE_FORMAT_URL,{parameterJson:jsonStr, messageType:messageType},function(data) {
				if(data.returnCode == 0){
					$(".textarea").val(data.returnJson);
				} else if(data.returnCode == 912) {
					layer.msg("格式化失败：不是指定的格式!",{icon:5,time:1500});
				}else {
					layer.alert(data.msg,{icon:5});
				}
			});
		}
};

var mySetting = {
		eventList:eventList,
		templateCallBack:function(df){
			interfaceId = GetQueryString("interfaceId");
			protocolType = GetQueryString("protocol");
			$.get("../../js/protocol.json", function(json) {
				protocolJson = json;
			});
			
			if (interfaceId != null) {
				publish.renderParams.listPage.listUrl = MESSAGE_LIST_URL + "?interfaceId=" + interfaceId;
			} else {
				publish.renderParams.listPage.listUrl = MESSAGE_LIST_URL;
				$(".add-object").hide();
			}		
			
			addParameterTemplate = Handlebars.compile($("#add-parameter-template").html());
			
   		 	df.resolve();
   	 	},
		editPage:{
			editUrl:MESSAGE_EDIT_URL,
			getUrl:MESSAGE_GET_URL,
			beforeInit:function(df){
				$("#interfaceInfo\\.interfaceId").val(interfaceId);
       		 	df.resolve();
       	 	},
			rules:{
				messageName:{
					required:true
				},				
				parameterJson:{
					required:true
				},
				status:{
					required:true
				},
				callParameter:{
					required:true
				}
			},
			messages:{
				messageName:"请输入报文名称",
				parameterJson:"请输入正确的报文 入参并点击验证",
				callParameter:"请点击配置按钮配置参数"
			},
			renderCallback:function(obj){
				//$("#parameterJson").val();
				$(".textarea").val(obj.parameterJson);				
				protocolType = obj.protocolType;								
			}
		},		
		listPage:{
			tableObj:".table-sort",
			columnsSetting:columnsSetting,
			columnsJson:[0, 9, 10, 11]
		},
		templateParams:templateParams		
	};

$(function(){	
	publish.renderParams = $.extend(true,publish.renderParams,mySetting);
	publish.init();
});

/**************************************************************************************/
function getParameterJson(){
	$("#parameter-json-textarea").spinModal();
	$.get("message-get",{id:messageId},function(data){
		if(data.returnCode==0){						
			$(".textarea").val(data.object.parameterJson);
			if (data.object.parameterJson == null || data.object.parameterJson == "") {
				$(".textarea").attr("placeholder","该报文没有设置入参json！");
			}
			$("#parameter-json-textarea").spinModal(false);
		}else{
			layer.alert(data.msg, {icon: 5});
		}
	});
}
