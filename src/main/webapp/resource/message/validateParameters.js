//遮罩层覆盖区域
var $wrapper = $('#div-table-container');

var messageSceneId; //当前正在操作的sceneId
var currIndex;//当前正在操作的layer窗口的index

/**
 * ajax地址
 */
var VALIDATE_RULE_LIST_URL = "validate-getValidates"; //获取数据列表
var VALIDATE_RULE_EDIT_URL = "validate-edit";
var VALIDATE_RULE_GET_URL = "validate-get";
var VALIDATE_RULE_DEL_URL = "validate-del";

var VALIDATE_RULE_UPDATE_STATUS = "validate-updateValidateStatus";
var DB_LIST_URL = "db-listAll";

var selectGetValueMethodTig = {
		"0":["字符串", "请输入用于比对该参数值的字符串,如18655036394", "可用字符串"],
		"1":["入参节点值", "请输入正确的入参节点路径,程序将会自动化来获取该路径下的值,区分大小写,请参考接口信息中的参数管理", "入参节点路径"],
		"2":["数据库取值", "请输入用于查询的SQL语句。在SQL语句中,你同样可以使用节点路径:#ROOT.DATA.PHONE_NO#来表示表示入参节点数据", "查询SQL语句"]
	};


var index_selectDb;

var templateParams = {
		tableTheads:["节点路径", "预期值取值方式", "预期值", "状态", "备注", "操作"],
		btnTools:[{
			type:"primary",
			size:"M",
			id:"add-object",
			iconFont:"&#xe600;",
			name:"添加节点验证规则"
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
			label:"规则ID",  	
			objText:"validateIdText",
			input:[{	
				hidden:true,
				name:"validateId"
				}]
		},
		{
			required:true,
			label:"节点路径",  
			input:[{	
				name:"parameterName",
				placeholder:"例如:ROOT.OPR_INFO.USER.ID"
				}]
		},
		{	
			required:true,
			label:"预期值取值方式",  
			button:[{
				style:"primary",
				value:"选择",
				name:"select-get-value-method"
			}]
		},
		{
			required:true,
			label:"预期比对值",  
			input:[{	
				name:"validateValue"
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
			name:"messageScene.messageSceneId",
		},
		{
			name:"validateMethodFlag",
			value:"1"
		},
		{
			name:"getValueMethod"
		},
		{
			label:"备注",  
			textarea:[{	
				name:"mark",
				placeholder:"验证说明"
			}]
		}
		]		
	};

var columnsSetting = [
                      {
                      	"data":null,
                      	"render":function(data, type, full, meta){                       
                              return checkboxHmtl(data.parameterName, data.validateId, "selectValidate");
                          }},
                      {"data":"validateId"},
                      ellipsisData("parameterName"),
                      {
                    	"data":null,
                    	"render":function(data) {
                    		var option = {
                    			"0":{
                    				btnStyle:"primary",
            		  				status:"字符串"
                    			},
                    			"1":{
                    				btnStyle:"primary",
            		  				status:"入参节点值"
                    			},
                    			"default":{
                    				btnStyle:"primary",
            		  				status:"数据库"
                    			}
                    		};
                    		
                    		return labelCreate(data.getValueMethod, option);
                    	}
                      },
                      ellipsisData("validateValue"),
                      {
                    	  "data":null,
                    	  "render":function(data) {
                    		  var checked = '';
                    		  if(data.status == "0") {checked= 'checked';}                  	
                    		  return '<div class="switch size-MINI" data-on-label="可用" data-off-label="禁用"><input type="checkbox" ' + checked + ' value="' + data.validateId + '"/></div>';                                                   							
                    	  }
                      }, 
                      ellipsisData("mark"),
                      {
                          "data":null,
                          "render":function(data, type, full, meta){
                            var context = [{
                	    		title:"规则编辑",
                	    		markClass:"object-edit",
                	    		iconFont:"&#xe6df;"
                	    	},{
                	    		title:"规则删除",
                	    		markClass:"object-del",
                	    		iconFont:"&#xe6e2;"
                	    	}];                           
                          	return btnIconTemplate(context);
                          }}
                  ];

var eventList = {
		"#add-object":function() {
			publish.renderParams.editPage.modeFlag = 0;					
			currIndex = layer_show("增加验证规则", editHtml, "700", "500", 1, function() {
				addEditPageHtml();
			});
			//layer.full(index);
			publish.init();			
		},
		"#batch-del-object":function() {
			var checkboxList = $(".selectValidate:checked");
			batchDelObjs(checkboxList,VALIDATE_RULE_DEL_URL);
		},
		".object-edit":function() {
			var data = table.row( $(this).parents('tr') ).data();
			publish.renderParams.editPage.modeFlag = 1;
			publish.renderParams.editPage.objId = data.validateId;
			layer_show("编辑规则信息", editHtml, "700", "530",1, function() {
				addEditPageHtml();
			});
			publish.init();	
		},
		".object-del":function() {
			var data = table.row( $(this).parents('tr') ).data();
			delObj("确定删除此验证规则？请慎重操作!",VALIDATE_RULE_DEL_URL, data.validateId, this);
		},
		"#select-get-value-method":function() {  //选择预期值取值方式
			layer.confirm('请选择预期比对数据获取方式',{title:'提示',btn:['字符串','入参节点值','数据库取值'],
				btn3:function(index) {
					$.ajax({
						type:"POST",
						url:DB_LIST_URL,
						success:function(data) {
							if(data.returnCode == 0){
								if(data.data.length < 1){
									layer.alert('没有可用的数据库连接信息,请在系统设置模块添加可用的数据库信息', {icon:5});
									return false;
								}
		
								var selectHtml = '<div class="row cl" style="width:340px;margin:15px;"><div class="form-label col-xs-2"><input type="button" class="btn btn-primary radius" onclick="selectDB();" value="选择"/></div><div class="formControls col-xs-10"><span class="select-box radius mt-0"><select class="select" size="1" name="selectDb" id="selectDb">';
								$.each(data.data, function(i,n) {
									selectHtml += '<option value="' + n.dbId + '">' + n.dbMark + "-" + n.dbName + '</option>';									
								});
								selectHtml += '</select></span></div></div>';
								index_selectDb = layer.open({
							        type: 1,
							        title: "选择数据库",
							        area: ['355px', '110px'],
							        content:selectHtml
							    });
							}else{
								layer.alert(data.msg,{icon:5});
							}
						}							
					});			
				}}
				,function(index){
					changeTigs("0");
					layer.close(index);
				}
				,function(index){
					changeTigs("1");
					layer.close(index);
				});
		}
		
};

var mySetting = {
		eventList:eventList,
		templateCallBack:function(df){
			messageSceneId = GetQueryString("messageSceneId");
			publish.renderParams.listPage.listUrl = VALIDATE_RULE_LIST_URL + "?messageSceneId=" + messageSceneId;			
			
			df.resolve();			   		 	
   	 	},
		editPage:{
			editUrl:VALIDATE_RULE_EDIT_URL,
			getUrl:VALIDATE_RULE_GET_URL,
			rules:{
				parameterName:{
					required:true,
					minlength:1,
					maxlength:255			
				},
				validateValue:{
					required:true,
					minlength:1,
					maxlength:1000
				},
				getValueMethod:{
					required:true
				}				
			},
			beforeInit:function(df){
				$("#messageScene\\.messageSceneId").val(messageSceneId);
       		 	df.resolve();
       	 	},
       	 	renderCallback:function(obj){
       	 		changeTigs(obj.getValueMethod);
       	 	}
		},		
		listPage:{
			listUrl:VALIDATE_RULE_LIST_URL,
			tableObj:".table-sort",
			columnsSetting:columnsSetting,
			columnsJson:[0, 6, 7],
			dtOtherSetting:{
				"serverSide": false,
				"initComplete":function() {
					$('.switch')['bootstrapSwitch']();
	            	$('.switch input:checkbox').change(function(){
	            		var flag = $(this).is(':checked');
	            		var validateId = $(this).attr('value');
	            		updateStatus(validateId, flag, this);
	            	});
				}
			},
			dtAjaxCallback:function() {
				$('.switch')['bootstrapSwitch']();
            	$('.switch input:checkbox').change(function(){
            		var flag = $(this).is(':checked');
            		var validateId = $(this).attr('value');
            		updateStatus(validateId, flag, this);
            	});
			}
		},
		templateParams:templateParams		
	};

$(function(){
	publish.renderParams = $.extend(true, publish.renderParams,mySetting);
	publish.init();
});

/*******************************************************************************************************/

/**
 * 实时改变状态
 */
function updateStatus(validateId, flag, obj) {
	var status = '1';
	if(flag == true){
		status = '0';
	}
	$.post(VALIDATE_RULE_UPDATE_STATUS, {validateId:validateId, status:status}, function(json) {
		if(json.returnCode != 0){
			$(obj).click();
			layer.alert(json.msg, {icon:5});
		}
	});
}

/**
 * 选择查询数据库
 */
function selectDB() {
	changeTigs($("#selectDb").val());
	layer.close(index_selectDb);
	
}

function changeTigs(type) {
	
	$("#getValueMethod").val(type);
	
	if (type != "1" && type != "0") {
		type = "2"; 		
		selectGetValueMethodTig[type][2] = "数据库  "+$("#selectDb option:selected").text();
	}
		
	$("#validateValue").attr("placeholder", selectGetValueMethodTig[type][0]);
	$("#tipMsg").text(selectGetValueMethodTig[type][1]);
	$("#getValueMethodText").text(selectGetValueMethodTig[type][2]);
}

function addEditPageHtml() {
	$("#select-get-value-method").before('<strong><span id="getValueMethodText"></span></strong>&nbsp;&nbsp;');
	$("#form-edit").append('<div class="row cl"><div class="col-xs-8 col-sm-9 col-xs-offset-4' 
			+ ' col-sm-offset-3"><span id="tipMsg" style="color:red;"></span></div></div>');
}