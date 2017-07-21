var table;
//遮罩层覆盖区域
var $wrapper = $('#div-table-container');

var templateParams = {
		tableTheads:["用户名","姓名","角色","当前状态","最近登录","创建时间","操作"],
		btnTools:[{
			type:"primary",
			size:"M",
			markClass:"add-object",
			iconFont:"&#xe600;",
			name:"添加用户"
		}],
		formControls:[
		{
			edit:true,
			required:false,
			label:"&nbsp;&nbsp;ID",  	
			objText:"userIdText",
			input:[{	
				hidden:true,
				name:"userId"
				}]
		},
		{
			edit:false,
			required:true,
			label:"用户名",  			
			input:[{	
				hidden:false,
				name:"username",
				placeholder:"2到20个字符"
				}]
		},
		{
			edit:false,
			required:true,
			label:"姓&nbsp;名",  			
			input:[{	
				hidden:false,
				name:"realName",
				placeholder:"2到20个字符"
				}]
		},
		{
			edit:false,
			required:true,
			label:"角&nbsp;色",  			
			select:[{
				name:"role.roleId"
				}]
		},
		{
			edit:true,
			required:false,
			label:"创建时间",  			
			objText:"createTimeText",
			input:[{	
				hidden:true,
				name:"createTime"
				}]
		},
		{
			edit:true,
			required:false,
			label:"当前状态",  			
			objText:"statusText",
			input:[{	
				hidden:true,
				name:"status"
				}]
		},
		{
			edit:true,
			required:false,
			label:"最近登录",  			
			objText:"lastLoginTimeText",
			input:[{	
				hidden:true,
				name:"lastLoginTime"
				}]
		},	
		]		
	};

var columnsSetting = [{"data":null,
	 "render":function(data, type, full, meta){
		  		return checkboxHmtl(data.username+'-'+data.realName,data.userId,"selectUser");
	           }},
	{"data":"userId"},
	{"data":"username"},
	{"data":"realName"}, 
	{"data":"role.roleName"},                                          
	{
    "data":null,
    "render":function(data, type, full, meta ){
    	var bstatus;
    	var btnstyle;
    	switch(data.status)
    	{
    	case "0":
       		bstatus = "正常";
       		btnstyle = "success";
    	  	break;
    	case "1":
    		bstatus = "锁定";
       		btnstyle = "danger";
    	  	break;               
    	}
        return htmlContent = '<span class="label label-'+btnstyle+' radius">'+bstatus+'</span>';
    }
	},
	{
	    "data":"lastLoginTime",
	    "className":"ellipsis",
	    "render":CONSTANT.DATA_TABLES.COLUMNFUN.ELLIPSIS
	
	},
	{
		"data":"createTime",
	    "className":"ellipsis",
	    "render":CONSTANT.DATA_TABLES.COLUMNFUN.ELLIPSIS
	},
	{
		"data":null,
	    "render":function(data, type, full, meta){
	    	var statusIcon='&#xe605;';
	    	if(data.status=="0"){
	    		statusIcon='&#xe60e;';
	    	}
	    	
	    	var context = [{
	    		title:"重置密码",
	    		markClass:"reset-pass",
	    		iconFont:"&#xe68f;"
	    	},{
	    		title:"编辑",
	    		markClass:"user-edit",
	    		iconFont:"&#xe6df;"
	    	},{
	    		title:"锁定",
	    		markClass:"user-lock",
	    		iconFont:statusIcon
	    	}
	    	];	    		
	    	return btnIconTemplate(context);
	    	
	    }}];	



var beforeEditInit = function(df){
	$.get("role-listAll",function(result){
		if(result.returnCode==0){
			var roles = result.data;
			var optionHtml='';
			for(var i=0;i<roles.length;i++){
				optionHtml+='<option value="'+roles[i].roleId+'">'+roles[i].roleName+'</option>';
			}
			$("#role\\.roleId").html(optionHtml);
			df.resolve();
		}
	});
};

var eventList = {
		".add-object":function(){
			publish.renderParams.editPage.modeFlag = 0;					
			layer_show("增加用户", editHtml, "600", "300",1);
			publish.init();
			
		},
		".batch-del":function(){
			var checkboxList = $(".selectUser:checked");
			batchDelObjs(checkboxList,"user-del");
		},
		".reset-pass":function(){
			//获取当前行数据
			var data = table.row( $(this).parents('tr') ).data();
			layer.confirm('确定要重置该用户的密码吗？',function(index){
				$.get("user-resetPwd",{userId:data.userId},function(data){
					if(data.returnCode==0){
		                layer.msg('密码已重置为111111',{icon:1,time:1000});
		    		}else{
		    			layer.alert(data.msg, {icon: 5});
		    		}
				});
				
			});
		},
		".user-edit":function(){
			var data = table.row( $(this).parents('tr') ).data();
			if(data.username=="admin"){
				layer.msg('不能修改预置管理员用户信息!',{time:1500});
			}else{
				publish.renderParams.editPage.modeFlag = 1;
				publish.renderParams.editPage.objId = data.userId;
				layer_show("编辑用户信息", editHtml, "600", "480",1);
				publish.init();	
				
			}
		},
		".user-lock":function(){
			var data = table.row( $(this).parents('tr') ).data();
			if(data.username=="admin"){
				layer.msg('不能锁定预置管理员用户!',{time:1500});
			}else{
				var mode = "0";
		    	var tipMsg = "确定需要解锁该用户吗?";
		    	var tipMsg1 = "该用户已解锁!";
		    	if(data.status=="0"){
		    		mode = "1";
		    		tipMsg = "确认要锁定该用户吗(请谨慎操作,被锁定的用户将不能登录)";
		    		tipMsg1 = "已锁定该用户,该用户将不能登录!";
		    	}   	
		    	layer.confirm(tipMsg,function(index){
		    		layer.close(index);
		        	$.get("user-lock",{userId:data.userId,username:data.username,mode:mode},function(data){
		        		if(data.returnCode==0){
		        			table.ajax.reload(null,false);
		                    layer.msg(tipMsg1,{icon:1,time:1000});
		        		}else{
		        			layer.alert(data.msg, {icon: 5});
		        		}
		        	});
		            
		        });
			} 
		}
		
};

var mySetting = {
		eventList:eventList,
		editPage:{
			beforeInit:beforeEditInit,
			editUrl:"user-edit",
			getUrl:"user-get",
			rules:{username:{required:true,minlength:2,maxlength:20},realName:{required:true,minlength: 2,maxlength: 20}},
			renderCallback:function(o){
				var statusMsg='';
				o.status=="0"?statusMsg="正常":statusMsg="锁定";
				$("#statusText").text(statusMsg);
			}
		},
		listPage:{
			listUrl:"user-list",
			tableObj:".table-sort",
			columnsSetting:columnsSetting,
			columnsJson:[0,7,8]
		},
		templateParams:templateParams		
	};

$(function(){			
	publish.renderParams = $.extend(true,publish.renderParams,mySetting);
	publish.init();
});