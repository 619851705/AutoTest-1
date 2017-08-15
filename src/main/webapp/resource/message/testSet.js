//遮罩层覆盖区域
var $wrapper = $('#div-table-container');
var configData;
var setId; //当前正在操作的set
var setName; //当前正在操作的setName
var currIndex;//当前正在操作的layer窗口的index

/**
 * ajax地址
 */
var SET_LIST_URL = "set-list"; //获取测试集列表
var SET_EDIT_URL = "set-edit";  //测试集信息编辑
var SET_GET_URL = "set-get"; //获取指定测试集信息
var SET_DEL_URL = "set-del"; //删除指定测试集
var SET_NAME_CHECK_URL = "set-checkName"; //验证测试集名称是否重复

var SET_RUN_SETTING_CONFIG_URL = "set-settingConfig";
var UPDATE_TEST_CONFIG_URL = "test-updateConfig";

var settingConfigViewTemplate;

var selectMode = "1";//是否为选择模式，只供选择0-是   1-不是

var templateParams = {
		tableTheads:["名称", "场景数", "状态", "创建用户", "创建时间", "运行时配置","操作"],
		btnTools:[{
			type:"primary",
			size:"M",
			id:"add-object",
			iconFont:"&#xe600;",
			name:"添加测试集"
		},{
			type:"danger",
			size:"M",
			id:"batch-del-object",
			iconFont:"&#xe6e2;",
			name:"批量删除"
		}],
		formControls:[
		{
			edit:true,
			label:"ID",  	
			objText:"setIdText",
			input:[{	
				hidden:true,
				name:"setId"
				}]
		},
		{
			required:true,
			label:"测试集名称",  
			input:[{	
				name:"setName",
				placeholder:"2-100字符"
				}]
		},
		{	
			required:true,
			label:"状态",  			
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
			label:"备注",  			
			textarea:[{
				name:"mark"
			}]
		},
		{
			edit:true,
			label:"创建用户",  	
			objText:"user.realNameText",
			
		},
		{
			edit:true,
			label:"创建时间",  	
			objText:"createTimeText",
			input:[{	
				hidden:true,
				name:"createTime"
			}]
		},
		{
			name:"user.userId",
		}]		
	};


var columnsSetting = [
                      {
                      	"data":null,
                      	"render":function(data, type, full, meta){                       
                              return checkboxHmtl(data.setName, data.setId, "selectSet");
                          }},
                      {"data":"setId"},
                      ellipsisData("setName"),
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
                    	  "render":function(data) {
                    		  	var option = {
                    		  			"0":{
                    		  				btnStyle:"success",
                    		  				status:"可用"
                    		  				},
                		  				"1":{
                		  					btnStyle:"danger",
                		  					status:"禁用"
                		  					}
                    		  	};	
                    		  	return labelCreate(data.status, option);							
                    	  }
                      },
                      {"data":"user.realName"},{"data":"createTime"},
                      {
                    	  "data":null,
                    	  "render":function(data) {
                    		  var context =
                            		[{
                          			type:"primary",
                          			size:"M",
                          			markClass:"run-setting-config",
                          			name:"配置"
                          		}];
                                return btnTextTemplate(context);
                    	  }
                      },
                      {
                          "data":null,
                          "render":function(data, type, full, meta){
                            var context;                            
                            if (selectMode == "0") {
                            	context = [{
                            		title:"选择测试集",
                    	    		markClass:"object-select",
                    	    		iconFont:"&#xe676;"
                            	}];
                            } else {
                            	context = [{
                    	    		title:"测试集编辑",
                    	    		markClass:"object-edit",
                    	    		iconFont:"&#xe6df;"
                    	    	},{
                    	    		title:"测试集删除",
                    	    		markClass:"object-del",
                    	    		iconFont:"&#xe6e2;"
                    	    	}]; 
                            }
                                                                                
                          	return btnIconTemplate(context);
                          }}
                  ];

var eventList = {
		"#add-object":function() {
			publish.renderParams.editPage.modeFlag = 0;					
			currIndex = layer_show("增加测试集", editHtml, "600", "400", 1);
			//layer.full(index);
			publish.init();			
		},
		"#batch-del-object":function() {
			var checkboxList = $(".selectSet:checked");
			batchDelObjs(checkboxList,SET_DEL_URL);
		},
		".object-edit":function() {
			var data = table.row( $(this).parents('tr') ).data();
			publish.renderParams.editPage.modeFlag = 1;
			publish.renderParams.editPage.objId = data.setId;
			layer_show("编辑测试集信息", editHtml, "600", "480",1);
			publish.init();	
		},
		".object-del":function() {
			var data = table.row( $(this).parents('tr') ).data();
			delObj("确定删除此测试集(删除测试集不会删除下属测试场景)？请慎重操作!", SET_DEL_URL, data.setId, this);
		},
		".show-scenes":function() { //打开场景页,可在此页添加删除场景
			var data = table.row($(this).parents('tr')).data();
			currIndex = layer_show(data.setName + "-测试场景", "setScene.html?setId=" + data.setId, "860", "680", 2, null, function() {
				refreshTable();
			});			
		},
		".run-setting-config":function() {
			var that = table.row($(this).parents('tr'));
			var data = that.data();
			var tip = "【自定义】,点击'默认'将会恢复为默认配置,点击'自定义'修改或者查看当前配置!"
			var mode = 1;
			if (data.config == null) {
				tip = "【默认】,点击'自定义'将会创建自定义的配置信息,点击'默认'返回!";
				mode = 0;
			}
			layer.confirm('当前选择的运行时配置为：' + tip, {icon: 0,title:'提示', btn:['默认', '自定义']}
				, function(index) {
					if (mode == 1) {
						settingConfig(data.setId, mode, function (json) {
							data.config = null;
							that.data(data);
							layer.close(index);
							layer.msg("修改成功!", {icon:1, time:1500});
						});
					} else {
						layer.close(index);
					}										
				}
				, function(index) {
					if (mode == 0) {
						settingConfig(data.setId, mode, function (json) {
							data.config = json.config;
							that.data(data);
							layer.close(index);
							viewRunSettingConfig(data,that);	
						});
					} else {					
						viewRunSettingConfig(data,that);	
					}
									
				});
		},
		"#update-option":function() {
			updateTestOptions();
		},
		"#reset-option":function() {
			resetOptions();
		},
		".object-select":function() {
			var data = table.row( $(this).parents('tr') ).data();
			parent.$("#relatedId").val(data.setId);
			parent.$("#choose-task-set").siblings("span").remove();
			parent.$("#choose-task-set").before('<span>' + data.setName + '&nbsp;</span>');
			parent.layer.close(parent.layer.getFrameIndex(window.name));
		}
};

var mySetting = {
		eventList:eventList,
		templateCallBack:function(df){
			selectMode = GetQueryString("selectMode");
			if (selectMode == "0") {
				$(".breadcrumb").hide();
				$("#btn-tools").parent('.cl').hide();
			}
			
			settingConfigViewTemplate = Handlebars.compile($("#setting-config-template").html());
			df.resolve();
		},
		editPage:{
			editUrl:SET_EDIT_URL,
			getUrl:SET_GET_URL,
			rules:{
				setName:{
					required:true,
					minlength:2,
					maxlength:100,
					remote:{
						url:SET_NAME_CHECK_URL,
						type:"post",
						dataType: "json",
						data: {                   
					        setName: function() {
					            return $("#setName").val();
					        },
					        setId:function(){
					        	return $("#setId").val();
					        }
					}}
				}				
			}
		},		
		listPage:{
			listUrl:SET_LIST_URL,
			tableObj:".table-sort",
			columnsSetting:columnsSetting,
			columnsJson:[0, 3, 7, 8]			
		},
		templateParams:templateParams		
	};

$(function(){
	publish.renderParams = $.extend(true,publish.renderParams,mySetting);
	publish.init();
});


/********************************************************************************************************/
function viewRunSettingConfig (rowData, obj) { 
	configData = rowData.config;
	layer_show(rowData.setName + "-运行时配置", settingConfigViewTemplate(rowData.config), '800', '480', 1, function() {
		resetOptions();
	}, function() {
		rowData.config = configData; 
		obj.data(rowData);
	});
}

function settingConfig(setId, mode, callback) {
	$.post(SET_RUN_SETTING_CONFIG_URL, {setId:setId, mode:mode}, function(json) {
		if (json.returnCode == 0) {			
			callback(json);
		} else {
			layer.alert(json.msg, {icon:5});
		}
	});
}

function resetOptions () {
	if (configData != null) {
		$("#requestUrlFlag").val(configData.requestUrlFlag);
		$("#connectTimeOut").val(configData.connectTimeOut);
		$("#readTimeOut").val(configData.readTimeOut);
		$("#checkDataFlag").val(configData.checkDataFlag);
		$("#configId").val(configData.configId);
		$("#customRequestUrl").val(configData.customRequestUrl);
	}
}

//更新配置信息
function updateTestOptions(){
	var updateConfigData=$("#form-setting-config").serializeArray();
	$.post(UPDATE_TEST_CONFIG_URL, updateConfigData, function(data){
		if(data.returnCode == 0){
			configData = data.config;
			layer.msg('更新成功',{icon:1, time:1500});
		} else {
			layer.alert("更新失败：" + data.msg, {icon:5});
		}
	});	
}