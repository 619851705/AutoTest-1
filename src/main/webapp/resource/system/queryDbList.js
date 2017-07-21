var table;
//遮罩层覆盖区域
var $wrapper = $('#div-table-container');
var testUrl = "db-testDB";
var templateParams = {
		tableTheads:["类型","数据库名","用户名","密码","地址","备注","操作"],
		btnTools:[{
			type:"primary",
			size:"M",
			markClass:"add-object",
			iconFont:"&#xe600;",
			name:"添加数据库信息"
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
			objText:"dbIdText",
			input:[{	
				hidden:true,
				name:"dbId"
				}]
		},
		{
			edit:false,
			required:true,
			label:"数据库类型",  			
			select:[{	
				name:"dbType",
				option:[{value:"mysql",text:"MYSQL",selected:true },
				        {value:"oracle",text:"ORACLE"},
				        {value:"redis",text:"REDIS"}]
				}]
		},
		{
			edit:false,
			required:true,
			label:"数据库主机地址",  			
			input:[{	
				hidden:false,
				name:"dbUrl",
				placeholder:"主机IP:端口号"
				}]
		},
		{
			edit:false,
			required:true,
			label:"数据库名",  			
			input:[{	
				hidden:false,
				name:"dbName"
				}]
		},
		{
			edit:false,
			required:true,
			label:"用户名",  			
			input:[{	
				hidden:false,
				name:"dbUsername"
				}]
		},
		{
			edit:false,
			required:true,
			label:"&nbsp;&nbsp;密码",  			
			input:[{	
				hidden:false,
				name:"dbPasswd"
				}]
		},
		{
			edit:false,
			label:"&nbsp;&nbsp;备注",
			textarea:[{
				placeholder:"数据库备注",
				name:"dbMark"	
			}]
		}	
		]		
	};

var columnsSetting = [{"data":null,
	 "render":function(data, type, full, meta){
		  		return checkboxHmtl(data.dbName,data.dbId,"selectDb");
	           }},
	{"data":"dbId"},{"data":"dbType"},{"data":"dbName"},{"data":"dbUsername"},                                       
	{
    "data":null,
    "render":function(data, type, full, meta ){
    	var htmlContent = '<a href="javascript:;" onclick="layer.alert(\''+data.dbPasswd+'\',{icon:4,title:\'数据库密码查看\'});">*******</a>';
        return htmlContent; 
    }
	},
	{
	    "data":"dbUrl",
	    "className":"ellipsis",
	    "render":CONSTANT.DATA_TABLES.COLUMNFUN.ELLIPSIS
	
	},
	{
	    "data":null,
	    "render":function(data, type, full, meta ){
	    	var context = '<a href="javascript:;" onclick="layer.alert(\''+data.dbMark+'\',{icon:0,title:\'数据库备注查看\'});" class="btn btn-success size-S radius">查看</a>';
            return context;
	    }
		},
	{
		"data":null,
	    "render":function(data, type, full, meta){	    	
	    	var context = [{
	    		title:"测试连接",
	    		markClass:"db-test",
	    		iconFont:"&#xe6f1;"
	    	},{
	    		title:"编辑",
	    		markClass:"db-edit",
	    		iconFont:"&#xe6df;"
	    	},{
	    		title:"删除",
	    		markClass:"db-del",
	    		iconFont:"&#xe6e2;"
	    	}
	    	];	    		
	    	return btnIconTemplate(context);	    	
	    }}];	

var eventList = {
		".add-object":function(){
			publish.renderParams.editPage.modeFlag = 0;					
			layer_show("增加数据库信息", editHtml, "800", "550",1);
			publish.init();
			
		},
		".batch-del-object":function(){
			var checkboxList = $(".selectDb:checked");
			batchDelObjs(checkboxList,"db-del");
		},
		".db-test":function(){
			var data = table.row( $(this).parents('tr') ).data();
			$wrapper.spinModal();
	  		$.get(testUrl,{id:data.dbId},function(data){
	  			$wrapper.spinModal(false);
	  			if(data.returnCode==0){ 				
	  				layer.alert("测试连接成功!", {icon: 1});
	  			}else{
	    			layer.alert(data.msg, {icon: 5});
	  			}			
	  		});
		},
		".db-edit":function(){
			var data = table.row( $(this).parents('tr') ).data();
			publish.renderParams.editPage.modeFlag = 1;	
  			publish.renderParams.editPage.objId = data.dbId;
			layer_show("编辑查询数据库信息", editHtml, "800", "550",1);
			publish.init();	
		},
		".db-del":function(){
			var data = table.row( $(this).parents('tr') ).data();
			delObj("确认要删除此查询数据库信息吗？","db-del",data.dbId,this);
		}
		
};

var mySetting = {
		eventList:eventList,
		editPage:{
			editUrl:"db-edit",
			getUrl:"db-get",
			rules:{
				dbName:{
					required:true,
					minlength:1,
					maxlength:200
				},
				dbUrl:{
					required:true
				},
				dbUsername:{
					required:true
				},
				dbPasswd:{
					required:true
				}
			},
		},
		listPage:{
			listUrl:"db-list",
			tableObj:".table-sort",
			columnsSetting:columnsSetting,
			columnsJson:[0,2,3,4,5,7]
		},
		templateParams:templateParams		
	};

$(function(){			
	publish.renderParams = $.extend(true,publish.renderParams,mySetting);
	publish.init();
});