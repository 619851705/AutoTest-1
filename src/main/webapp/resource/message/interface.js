var table;
//遮罩层覆盖区域
var $wrapper = $('#div-table-container');

var interfaceId; //当前正在编辑的interface的id
var currIndex;//当前正在操作的layer窗口的index

var parametersEditHmtl;

/**
 * ajax地址
 */
var PARAMS_GET_URL = "param-getParams"; //根据interfaceId来 获取parameters
var PARAM_SAVE_URL = "param-save";   //保存新增的接口参数
var PARAM_DEL_URL = "param-del";   //删除指定参数
var PARAM_EDIT_URL = "param-edit";  //编辑参数的指定属性
var PARAM_JSON_IMPORT_URL = "param-batchImportParams"; //导入json串

var INTERFACE_LIST_URL = "interface-list"; //获取接口列表
var INTERFACE_CHECK_NAME_URL = "interface-checkName"; //检查新增接口名是否重复
var INTERFACE_EDIT_URL = "interface-edit";  //接口编辑
var INTERFACE_GET_URL = "interface-get"; //获取指定接口信息
var INTERFACE_DEL_URL = "interface-del"; //删除指定接口

var templateParams = {
		tableTheads:["名称","中文名","类型","协议","创建时间","状态","创建用户","最后修改","参数","操作"],
		btnTools:[{
			type:"primary",
			size:"M",
			markClass:"add-object",
			iconFont:"&#xe600;",
			name:"添加接口"
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
			label:"接口ID",  	
			objText:"interfaceIdText",
			input:[{	
				hidden:true,
				name:"interfaceId"
				}]
		},
		{
			required:true,
			label:"接口名称",  
			input:[{	
				name:"interfaceName"
				}]
		},
		{
			required:true,
			label:"接口类型",  	
			select:[{	
				name:"interfaceType",
				option:[{
					value:"SL",
					text:"受理类"
				},{
					value:"CX",
					text:"查询类",
					selected:"selected"
				}]
				}]
		},
		{
			required:true,
			label:"协议类型",  	
			select:[{	
				name:"interfaceProtocol",
				option:[{
					value:"HTTP",
					text:"HTTP",
					selected:"selected"
				},{
					value:"WebService",
					text:"WebService",					
				},{
					value:"Socket",
					text:"Socket",					
				}]
				}]
		},
		{
			required:true,
			label:"中文名称",  	
			input:[{	
				name:"interfaceCnName"
				}]
		},
		{
			label:"模拟请求地址",  	
			input:[{	
				name:"requestUrlMock",
				placeholder:"输入完整的Url请求地址,带http://"
				}]
		},
		{	
			required:true,
			label:"真实请求地址",  	
			input:[{	
				name:"requestUrlReal",
				placeholder:"输入完整的Url请求地址,带http://"
				}]
		},
		{	
			required:true,
			label:"接口状态",  			
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

var columnsSetting = [
          {
          	"data":null,
          	"render":function(data, type, full, meta){                       
                  return checkboxHmtl(data.interfaceName+'-'+data.interfaceCnName,data.interfaceId,"selectInterface");
              }},
          {"data":"interfaceId"},
          {
          	"className":"ellipsis show-interface-messages",
		    "data":"interfaceName",
          	"render":CONSTANT.DATA_TABLES.COLUMNFUN.ELLIPSIS
          	},
          {
      		"className":"ellipsis",
		    "data":"interfaceCnName",
		    "render":CONSTANT.DATA_TABLES.COLUMNFUN.ELLIPSIS
          		},
          {
          	"data":null,
          	"render":function(data, type, full, meta ){
          		var option = {
          				"SL":{
          					btnStyle:"warning",
          					status:"受理类"
          					},
          				"CX":{
          					btnStyle:"success",
          					status:"查询类"
          				}
          		};                  
          		return labelCreate(data.interfaceType,option);
              }},
          {
            "data":null,
            "render":function(data) {
            	return labelCreate((data.interfaceProtocol).toUpperCase());
            }
          },
          {"data":"createTime","width":"120px"},
          {
          	"data":null,
          	"render":function(data, type, full, meta ){
                  return labelCreate(data.status);
              }},
          {"data":"user.realName"},{"data":"lastModifyUser"},
          {
              "data":null,
              "render":function(data, type, full, meta){
              	var context =
              		[{
            			type:"primary",
            			size:"M",
            			markClass:"edit-params",
            			name:"管理"
            		}];
                  return btnTextTemplate(context);
              }},
          {
              "data":null,
              "render":function(data, type, full, meta){
                var context = [{
    	    		title:"接口编辑",
    	    		markClass:"interface-edit",
    	    		iconFont:"&#xe6df;"
    	    	},{
    	    		title:"接口删除",
    	    		markClass:"interface-del",
    	    		iconFont:"&#xe6e2;"
    	    	}];
              	return btnIconTemplate(context);
              }}
      ];	

var eventList = {
		".show-interface-messages":function(){
			var data = table.row( $(this).parents('tr') ).data();			
			$(this).attr("data-title", data.interfaceName + "-" + data.interfaceCnName + " " + "报文管理");
			$(this).attr("_href", "resource/message/message.html?interfaceId=" + data.interfaceId + "&protocol=" + data.interfaceProtocol);
			Hui_admin_tab(this);			
		},
		".add-object":function(){
			publish.renderParams.editPage.modeFlag = 0;					
			layer_show("增加接口", editHtml, "850", "480",1);
			publish.init();
			
		},
		".batch-del-object":function(){
			var checkboxList = $(".selectInterface:checked");
			batchDelObjs(checkboxList,INTERFACE_DEL_URL);
		},
		".edit-params":function(){
			var data = table.row( $(this).parents('tr') ).data();
			interfaceId = data.interfaceId;
			layer_show(data.interfaceName+"-接口参数管理",parametersEditHmtl, "1000", "500",1,function(){
				initParameters();
			});	
		},
		".interface-edit":function(){
			var data = table.row( $(this).parents('tr') ).data();
			publish.renderParams.editPage.modeFlag = 1;
			publish.renderParams.editPage.objId = data.interfaceId;
			layer_show("编辑接口信息", editHtml, "850", "630",1);
			publish.init();	
		},
		".interface-del":function(){
			var data = table.row( $(this).parents('tr') ).data();
			delObj("确定删除此接口？此操作同时会删除该接口下所有的报文以及场景相关数据,请谨慎操作!",INTERFACE_DEL_URL,data.interfaceId,this);
		},
		"#add-parameter":function(){
			if($("#parameters-tbody").has('input').length>0 || $("#parameters-tbody").has('select').length>0){
				layer.msg('请先保存或者取消已修改的内容!', {icon: 2,time:1000});
				return false;
			};
			var html='';
			var btnS='<a href="javascript:;" onclick="saveParameter(this)" class="btn btn-success size-S radius">保存</a>&nbsp;<a href="javascript:;" onclick="cancelAddParameter(this)" class="btn btn-danger size-S radius">取消</a>';
			var selectS='<select><option value="Array">Array</option><option value="Map">Map</option><option value="String">String</option><option value="Number">Number</option><option value="List">List</option></select>';
			html+='<tr class="text-c">'+
				  '<td></td>'+
				  '<td><input type="text"/>'+'</td>'+
				  '<td><input type="text"/>'+'</td>'+
				  '<td><input type="text"/>'+'</td>'+
				  '<td>'+selectS+'</td>'+
				  '<td><input type="text" value="TopRoot."/>'+'</td>'+
				  '<td>'+btnS+'</td></tr>';
			$("#parameters-tbody").prepend(html);			
		},
		".param-edit-value":function(){
			editParameter($(this));
		},
		"#load-json-parameter":function(){			
			var showHtml = '<div class="page-container">'+
				'<div class="cl pd-5 bg-1 bk-gray mt-0"> <span class="l">'+
				'<a href="javascript:;" onclick="batchImportParams();" class="btn btn-danger radius">解析报文</a>'+
				'</span><span class="r">'+
				'<span class="select-box radius"><select class="select" size="1" id="messageType">'+
				'<option value="json" selected>JSON格式</option>'+
				'<option value="xml">XML格式</option>'+
				'<option value="url">URL格式</option>'+
				'</select></span>'+
				'</span></div><br><textarea style="height: 240px;" class="textarea radius" '+
				'id="jsonParams" placeholder="输入接口报文"></textarea></div>';
			currIndex = layer_show("导入报文", showHtml, "780", "400",1);
		},
		".parameter-del":function(){
			var that = $(this);
			var id = $(that.parent('td').siblings('td')[0]).attr("id");
			layer.confirm('确认要删除吗？',function(index){		
				layer.close(index);	
		    	$.post(PARAM_DEL_URL,{"id":id},function(data){
		    		if(data.returnCode==0){		    			
		    			if(that.parents('tr').siblings('tr').length<2){
		    				$("#no-parameter-tip").text('没有参数,你可以手动增加或者通过JSON报文导入');
		    			}
		    			that.parents("tr").remove();
		                layer.msg('已删除',{icon:1,time:1500});
		    		}else{
		    			layer.alert(data.msg, {icon: 5});
		    		}
		    	});		    		        
		    });
		}
		
};

var mySetting = {
		eventList:eventList,
		templateCallBack:function(df){
			$("#parameters-page").load("interface-parameters.htm",function(){
				parametersEditHmtl = $("#parameters-page").html();
				$("#parameters-page").html('');
				
				df.resolve();
			});
		},
		editPage:{
			editUrl:INTERFACE_EDIT_URL,
			getUrl:INTERFACE_GET_URL,
			rules:{
				interfaceName:{
					required:true,
					remote:{
						url:INTERFACE_CHECK_NAME_URL,
						type:"post",
						dataType: "json",
						data: {                   
					        interfaceName: function() {
					            return $("#interfaceName").val();
					        },
					        interfaceId:function(){
					        	return $("#interfaceId").val();
					        }
					}}
				},				
				interfaceType:{
					required:true,
				},
				interfaceCnName:{
					required:true,
					minlength:2,
					maxlength:100
				},
				requestUrlReal:{
					required:true,
				},
				status:{
					required:true,
				}			
			}

		},		
		listPage:{
			listUrl:INTERFACE_LIST_URL,
			tableObj:".table-sort",
			columnsSetting:columnsSetting,
			columnsJson:[0,8,9,10,11]
		},
		templateParams:templateParams		
	};

$(function(){			
	publish.renderParams = $.extend(true,publish.renderParams,mySetting);
	publish.init();
});

/******************************************************************************************************/
/**初始化接口参数数据*/
function initParameters(){
	$("#parameters-table").spinModal();
	$.get(PARAMS_GET_URL+"?interfaceId="+interfaceId,function(data){
		if(data.returnCode == 0){
			var html = '';
			$.each(data.data,function(i,n){
				var btnS = '<a href="javascript:;" class="btn btn-danger size-S radius parameter-del">删除</a>';							
				html += '<tr class="text-c">'+
						'<td class="param-id" id="'+n.parameterId+'">'+n.parameterId+'</td>'+
						'<td class="param-edit-value" name="parameterIdentify">'+n.parameterIdentify+'</td>'+
						'<td class="ellipsis param-edit-value" name="parameterName">'+ n.parameterName +'</td>'+
						'<td class="ellipsis param-edit-value" name="defaultValue">' + n.defaultValue +'</td>'+
						'<td class="param-edit-value" name="type">'+n.type+'</td>'+
						'<td class="param-edit-value" name="path">'+n.path+'</td>'+
						'<td class="param-btns">'+btnS+'</td></tr>';
			});
			$("#parameters-tbody").html(html);	
			$("#editTag").text("单击已修改参数属性");
			$("#no-parameter-tip").text('');
			$("#parameters-table").spinModal(false);
		}else if(data.returnCode==3){
			$("#no-parameter-tip").text("没有参数,你可以手动增加或者通过JSON报文导入");
			$("#parameters-table").spinModal(false);
		}else{
			layer.alert(data.msg, {icon: 5});
		}
	});	
}

/**编辑参数*/
function editParameter(tdObj){
	var tbobyP = $("#parameters-tbody");
	if(tbobyP.has('input').length>0 || tbobyP.has('select').length>0){
		//layer.msg('请先保存或者取消已修改的内容!', {icon: 2,time:1000});
		return false;
	};
	$("#editTag").text("Enter键提交更改,Esc取消更改");
	if (tdObj.children("input").length>0 || tdObj.children("select").length>0) {
        return false;
    }
	var text = tdObj.text();
	var currBtn = tdObj.siblings(".param-btns");
	var btnHtml = $(currBtn).html();
	var btnS='<a id="saveBtn" href="javascript:;" onclick="" class="btn btn-success size-S radius">保存</a>&nbsp;<a id="cancelBtn" href="javascript:;" onclick="" class="btn btn-danger size-S radius">取消</a>';
	$(currBtn).html(btnS);
	
	tdObj.html("");
	var inputObj;
	if(tdObj.attr("name")=="type"){
		var selectType = $("<select></select>").append("<option value='Array'>Array</option>")
						.append("<option value='Map'>Map</option>")
						.append("<option value='String'>String</option>")
						.append("<option value='Number'>Number</option>")
						.append("<option value='List'>List</option>");
		
		inputObj = selectType.css("font-size", tdObj.css("font-size"))
							 .css("background-color", tdObj.css("background-color"))
							 .width(tdObj.width()).val(text).appendTo(tdObj);
	}else{
		inputObj = $("<input type='text'>").css("font-size", tdObj.css("font-size"))
										   .css("background-color", tdObj.css("background-color"))
										   .width(tdObj.width()).val(text).appendTo(tdObj);       
        inputObj.trigger("focus").trigger("select");
	}
	
      inputObj.click(function () {
          return false;
      });
      
      
      var saveFn = function(){
	      var inputtext = inputObj.val();
	          //获取parameterId
	      var parameterId = tdObj.siblings(".param-id").attr("id");
	      var attrName = tdObj.attr("name");
	      
	      $.post(PARAM_EDIT_URL,{
		      id:parameterId,
		      attrName:attrName,
		      attrValue:inputtext
	      },
	      function(data){
	    	  if(data.returnCode==0){
	    		  layer.msg('更新成功',{icon:1,time:1500});
	    	  tdObj.html(inputtext); 
	      }else{
	    	  layer.alert(data.msg, {icon: 5});
	    	  tdObj.html(text);       			  
	      }
	      $(currBtn).html(btnHtml);
	      $("#editTag").text("单击以修改参数属性");
	        	  	
	          }); 
      };
      
      $("#saveBtn").click(saveFn);
      
      $("#cancelBtn").click(function(){
	      tdObj.html(text);
	      $(currBtn).html(btnHtml);
	      $("#editTag").text("单击以修改参数属性");
      });
      
	  $(document).keyup(function (event) {
	      var keycode = event.which;
	      //回车情况
	      if (keycode==13) {
	    	  saveFn();                                     
	      }
	      //ESC情况
	      if (keycode==27) {
	          tdObj.html(text);
	          $(currBtn).html(btnHtml);
	          $("#editTag").text("单击以修改参数属性");
	      }
	      
	  });       
}

/**取消增加参数*/
function cancelAddParameter(obj){
	$(obj).parents("tr").remove();
}


/**保存新增参数*/
function saveParameter(obj){
	var tdList = $(obj).parent('td').siblings();
	var parameterIdentify = $(tdList[1]).children().val();
	var parameterName = $(tdList[2]).children().val();
	var defaultValue = $(tdList[3]).children().val();
	var type = $(tdList[4]).children().val();
	var path = $(tdList[5]).children().val();
	$.post(PARAM_SAVE_URL,{
		parameterIdentify: parameterIdentify,
		parameterName: parameterName,
		defaultValue: defaultValue,
		type: type,
		"interfaceInfo.interfaceId": interfaceId
		},function(data){
			if(data.returnCode==0){
				var btnS='<a href="javascript:;" class="btn btn-danger size-S radius parameter-del">删除</a>';
				
				var  html = '<tr class="text-c">'+
					  '<td class="param-id" id="'+data.id+'">'+data.id+'</td>' +
					  '<td class="param-edit-value" name="parameterIdentify">'+parameterIdentify+'</td>'+
					  '<td class="param-edit-value" name="parameterName">'+parameterName+'</td>'+
					  '<td class="param-edit-value" name="defaultValue">'+defaultValue+'</td>'+
					  '<td class="param-edit-value" name="type">'+type+'</td>'+
					  '<td class="param-edit-value" name="path">'+path+'</td>'+
					  '<td class="param-btns">'+btnS+'</td></tr>';
				$(obj).parents("tr").remove();
				console.log(html);
				$("#parameters-tbody").prepend(html);
				layer.msg('增加成功',{icon:1,time:1500});	
				$("#editTag").text("单击已修改参数属性");
				$("#no-parameter-tip").text("");
			}else{
				layer.alert(data.msg, {icon: 5});
			}
	});
}

/**导入json*/
function batchImportParams(i){
	var paramsJson=$("#jsonParams").val();	
	if(paramsJson == '' || paramsJson == null){
		layer.msg('你还没有输入任何内容',{icon:2,time:1500});
		return false;
	}
	var messageType = $("#messageType").val();
	$.post(PARAM_JSON_IMPORT_URL,{interfaceId:interfaceId,paramsJson:paramsJson,messageType:messageType},function(data){
		if(data.returnCode==0){
			initParameters();
			layer.close(currIndex);
			layer.msg('导入成功',{icon:1,time:1500});
		}else if(data.returnCode == 912){
			layer.msg('无法解析报文或者解析失败,请检查!',{icon:2,time:1500});
		}else{
			layer.alert(data.msg, {icon: 5});
		}
	});
}
