var table;
//遮罩层覆盖区域
var $wrapper = $('#div-table-container');

//ajax地址
var SET_LIST_SCENE_URL = "set-listScenes";//展示存在测试集或者不存在于测试集的测试场景
var SET_OP_SCENE_URL = "set-opScene";//操作测试场景，添加到测试集或者从测试集删除

var setId;
var mode = 0;

var templateParams = {
		tableTheads:["接口", "报文", "场景", "操作"],
		btnTools:[{
			type:"primary",
			size:"M",
			id:"manger-scene",
			iconFont:"&#xe60c;",
			name:"管理场景"
		},{
			size:"M",
			id:"add-scene",
			iconFont:"&#xe600;",
			name:"添加场景"
		}]		
};

var columnsSetting = [
					{
					  	"data":null,
					  	"render":function(data, type, full, meta){                       
					          return checkboxHmtl(data.sceneName, data.messageSceneId, "selectScene");
					 }},
					 {"data":"messageSceneId"},
					 ellipsisData("interfaceName"),
					 ellipsisData("messageName"),
					 ellipsisData("sceneName"),
					 {
                         "data":null,
                         "render":function(data, type, full, meta){
                        	 
                           var context;
                           
                           //管理-删除
                           if (mode == 0) {
                        	   context = [{
	   	               	    		title:"删除",
	   	               	    		markClass:"op-scene",
	   	               	    		iconFont:"&#xe6e2;"
                  	    		}];
                           }
                           
                           //添加
                           if (mode == 1) {
                        	   context = [{
	   	               	    		title:"添加",
	   	               	    		markClass:"op-scene",
	   	               	    		iconFont:"&#xe600;"
                 	    		}];
                           }
                           
                           
                         	return btnIconTemplate(context);
                       }}										     
];


var eventList = {
		"#manger-scene":function() {
			var that = this;
			mode = 0;			
			refreshTable(SET_LIST_SCENE_URL + "?mode=" + mode + "&setId=" + setId, function(json) {
				$(that).addClass('btn-primary').siblings().removeClass('btn-primary');
			});
		},
		"#add-scene":function() {
			var that = this;
			mode = 1;
			refreshTable(SET_LIST_SCENE_URL + "?mode=" + mode + "&setId=" + setId, function(json) {
				$(that).addClass('btn-primary').siblings().removeClass('btn-primary');
			});
		},
		".op-scene":function() {
			var tip = '删除';
			
			if (mode == 1) {
				tip = "添加";
			}
						
			var data = table.row( $(this).parents('tr') ).data();
			var that = this;
			layer.confirm('确定要' + tip + '此场景吗?', {icon:0, title:'警告'}, function(index) {
				layer.close(index);
				$.get(SET_OP_SCENE_URL + "?mode=" + mode + "&setId=" + setId + "&messageSceneId=" + data.messageSceneId, function(json) {
					if (json.returnCode == 0) {
						table.row($(that).parents('tr')).remove().draw();
						layer.msg(tip + '成功!', {icon:1, time:1500});
					} else {
						layer.alert(json.msg, {icon:5});
					}
				});
			});						
		}	
};


var mySetting = {
		eventList:eventList,
		templateCallBack:function(df){
			setId = GetQueryString("setId");	
			publish.renderParams.listPage.listUrl = SET_LIST_SCENE_URL + "?mode=" + mode + "&setId=" + setId;
			df.resolve();			   		 	
   	 	},
		listPage:{
			tableObj:".table-sort",
			columnsSetting:columnsSetting,
			columnsJson:[0, 5],
			dtOtherSetting:{
				"serverSide": false
			}
		},
		templateParams:templateParams		
	};


$(function(){
	publish.renderParams = $.extend(true,publish.renderParams,mySetting);
	publish.init();
});