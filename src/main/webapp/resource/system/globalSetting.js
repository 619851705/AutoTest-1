var editInfo = {};
var beforeSettings = {};

var eventList = {
		".save-setting":function(){
			if(getJsonLength(editInfo)>0){
				$.post(publish.renderParams.editPage.editUrl,editInfo,function(data){
					if(data.returnCode==0){				
						layer.msg("修改成功!",{icon:1,time:1500});
					}else{
						layer.alert(data.msg,{icon:5});
					}
				});
			}
		}
};



var mySetting = {
		eventList:eventList,
		userDefaultRender:false,    
   	 	userDefaultTemplate:false,
   	 	customCallBack:function(params){
	   	 	$.post(params.editPage.getUrl,function(data){
				if(data.returnCode==0){
					var o=data.data;
					var infoHtml = '';
					$.each(o,function(i,n){								
						infoHtml+='<div class="row cl">'+
						'<label class="form-label col-xs-4 col-sm-3">'+
						n.mark+'：</label>'+
						'<div class="formControls col-xs-8 col-sm-9">'+
						'<input type="text" class="input-text" id="'+n.settingName+'" name="'+n.settingName+'" value="'+n.settingValue+'" placeholder="'+n.defaultValue+'" /></div></div>';
						beforeSettings[n.settingName]=n.settingValue;
					});
					$("#setting-info").html(infoHtml);
					$("input").change(function(){
						if(beforeSettings[$(this).attr("name")]==$(this).val()){
							delete editInfo[$(this).attr("name")];
						}else{
							editInfo[$(this).attr("name")]=$(this).val();
						}
						
					});
				}else{
					layer.alert(data.msg,{icon:5});
				}
			});
   	 	},
		editPage:{
			editUrl:"global-edit",
			getUrl:"global-listAll"
		}	
	};


$(function(){			
	publish.renderParams = $.extend(true,publish.renderParams,mySetting);
	publish.init();
});