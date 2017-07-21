var table;
//遮罩层覆盖区域
var $wrapper = $('#div-table-container');
var templateParams = {
		tableTheads:["角色组","角色名","权限","备注","操作"],
		btnTools:[{
			type:"primary",
			size:"M",
			markClass:"add-object",
			iconFont:"&#xe600;",
			name:"添加角色"
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
			required:false,
			label:"&nbsp;&nbsp;ID",  	
			objText:"roleIdText",
			input:[{	
				hidden:true,
				name:"roleId"
				}]
		},
		{
			edit:false,
			required:true,
			label:"角色组",  			
			select:[{
				name:"roleGroup",
				option: [
				         {
				        	 value:"性能测试组",
				        	 text:"性能测试组",
				        	 selected:true 
				         },
				         {
				        	 value:"CRM测试组",
				        	 text:"性能测试组", 
				         },
				         {
				        	 value:"BOSS测试组",
				        	 text:"BOSS测试组", 
				         },
				         {
				        	 value:"电渠测试组",
				        	 text:"电渠测试组", 
				         },
				         {
				        	 value:"自动化测试组",
				        	 text:"自动化测试组", 
				         },
				         {
				        	 value:"测试开发组",
				        	 text:"测试开发组", 
				         },
				         {
				        	 value:"思特奇开发",
				        	 text:"思特奇开发", 
				         },
				         {
				        	 value:"安徽移动",
				        	 text:"安徽移动", 
				         }]
				}]
		},
		{
			edit:false,
			required:true,
			label:"角色名",  			
			input:[{	
				hidden:false,
				name:"roleName",
				placeholder:"4至20个英文字母"
				}]
		},
		{
			edit:false,
			required:true,
			label:"&nbsp;&nbsp;备注",  			
			textarea:[{
				placeholder:"角色备注,角色说明",
				name:"mark"				
			}]
		}]		
	};

var columnsSetting = [
    {
    	"data":null,
    	"render":function(data, type, full, meta){
		  		return checkboxHmtl(data.roleName,data.roleId,"selectRole");
	           }},
	{"data":"roleId"},{"data":"roleGroup"},{"data":"roleName"},                                  
	{
	   "data":null,
	   "render":function(data, type, full, meta ){
		   var context = [{
			   type:"default",
			   size:"S",
			   markClass:"show-role-power",  
			   iconFont:"", 
			   name:data.oiNum
		   }];
		   return btnTextTemplate(context);		   
		   }
	},
	{
		"data":"mark",
		"className":"ellipsis",
	    "render":CONSTANT.DATA_TABLES.COLUMNFUN.ELLIPSIS
	},
	{
		"data":null,
	    "render":function(data, type, full, meta){	    	
	    	var context = [
	    	    {
		    		title:"编辑",
		    		markClass:"role-edit",
		    		iconFont:"&#xe6df;"
	    	    },{
		    		title:"删除",
		    		markClass:"role-del",
		    		iconFont:"&#xe6e2;"
	    	    }
	    	];	    		
	    	return btnIconTemplate(context);
	    }
	}
];

var eventList = {
		".batch-del-object":function(){
			var checkboxList = $(".selectRole:checked");
			batchDelObjs(checkboxList,"role-del");
		},
		".add-object":function(){
			publish.renderParams.editPage.modeFlag = 0;					
			layer_show("增加角色", editHtml, "600", "400",1);
			publish.init();			
		},	
		".show-role-power":function(){
			var data = table.row( $(this).parents('tr') ).data();
			initCheckOpId = [];
			currDelCheckOpId = [];
			currAddCheckOpId = [];	
			roleId = data.roleId;
			layer_show("角色权限编辑",rolePowerHtml, "400", "660",1,function(){
				$("#rolePowerTable").spinModal();
				$.get("role-getNodes?roleId="+roleId,function(data){
					if(data.returnCode==0){
						var nodes=data.interfaces;						
						$.each(nodes,function(i,n){
							if(n.isParent=="true"){
								n["open"]="true";
							}
							if(n.isOwn==true){
								initCheckOpId.push(n.opId);
							}
						});
						var t = $("#treeDemo");
						t = $.fn.zTree.init(t, zTreeSetting, nodes);
						$("#rolePowerTable").spinModal(false);
					}else{
						layer.alert(data.msg,{icon:5});
					}		
				});
			});
	
		},
		".save-role-power":function(){
			saveChange();
		},
		".role-edit":function(){
			var data = table.row( $(this).parents('tr') ).data();
			if(data.roleName=="admin" || data.roleName=="default"){
	  			layer.msg('不能修改预置管理员角色或者默认角色信息!',{time:1500});
	  		}else{
	  			publish.renderParams.editPage.modeFlag = 1;	
	  			publish.renderParams.editPage.objId = data.roleId;
				layer_show("编辑用户信息", editHtml, "600", "430",1);
				publish.init();	
	  		}
		},
		".role-del":function(){
			var data = table.row( $(this).parents('tr') ).data();
			if(data.roleName=="admin" || data.roleName=="default"){
				layer.msg('不能删除预置管理员角色或者默认角色信息!',{time:1500});
			}else{
				delObj("确认要删除此角色信息吗？","role-del",data.roleId,this);
			}
			
		}
};

var mySetting = {
		eventList:eventList,
		templateCallBack:function(df){
			$("#rolePower-page").load("_role-Power.htm",function(){
				rolePowerHtml = $("#rolePower-page").html();
				$("#rolePower-page").html('');
				df.resolve();
			});
		},
		editPage:{
			editUrl:"role-edit",
			getUrl:"role-get",
			rules:{roleName:{isEnglish:true,minlength:4,maxlength:20},roleGroup:{required:true}},
		},
		listPage:{
			listUrl:"role-list",
			tableObj:".table-sort",
			columnsSetting:columnsSetting,
			columnsJson:[0,4,5]
		},
		templateParams:templateParams		
	};

$(function(){			
	publish.renderParams = $.extend(true,publish.renderParams,mySetting);
	publish.init();
});

/**************************************************************************/
//当前编辑权限的roleId
var roleId;
//权限编辑页面html
var rolePowerHtml;
//初始的被checked的opId的数组
var initCheckOpId;
//操作的被取消或者删除的op
var currDelCheckOpId;
//操作的增加的op
var currAddCheckOpId;

var zTreeSetting = {
		view: {showIcon: false},
		check: {
			enable: true,
			chkboxType:  { "Y" : "s", "N" : "ps" },
			autoCheckTrigger: true
			},
		data: {
			simpleData: {
				enable:true,
				idKey: "opId",
				pIdKey: "parentOpId",
				rootPId: 1
			},
			key: {
				name:"opName",
				title:"mark",
				url:"callName",
				checked:"isOwn"
			}
		},
		callback:{
			onCheck:zTreeOnCheck
		}
	};

//Ztree中checkBox被选中或者取消时的回调
function zTreeOnCheck(event, treeId, treeNode) {
	//判断是否是根节点
	if(treeNode.isParent=="false"||treeNode.isParent==false){
		//判断是被勾选还是取消勾选
		if(treeNode.isOwn){
			//被勾选,判断是否为初始的数据		
			if(initCheckOpId.indexOf(treeNode.opId)==-1){
				currAddCheckOpId.push(treeNode.opId);
			}else{
				currDelCheckOpId.splice(currDelCheckOpId.indexOf(treeNode.opId),1);
			}	
		}else{
			//取消勾选
			if(initCheckOpId.indexOf(treeNode.opId)==-1){
				currAddCheckOpId.splice(currAddCheckOpId.indexOf(treeNode.opId),1);
			}else{
				currDelCheckOpId.push(treeNode.opId);
			}
		}
	}	
}


//保存信息发送到服务端
function saveChange(){	
	//判断是否需要发送更新请求到后台
	if(currDelCheckOpId.length<1&&currAddCheckOpId.length<1){
		layer.closeAll('page');
		return;
	}
	var sendData = {"roleId":roleId};
	if(currDelCheckOpId.length>0){
		sendData["delOpIds"]=currDelCheckOpId.join(",");
	}
	if(currAddCheckOpId.length>0){
		sendData["addOpIds"]=currAddCheckOpId.join(",");
	}
	$.get("role-updateRolePower",sendData,function(data){
		if(data.returnCode==0){
			refreshTable();
			layer.closeAll('page');
		}else{
			layer.alert(data.msg,{icon:5});
		}
	});
}