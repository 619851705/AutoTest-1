function $$(id){return document.getElementById(id);}
function setClass (obj,className){
	if((typeof obj).toLowerCase()=="string"){
		obj=$$(obj);
	}
	if(isIE){
		obj.className=className;
	}else{
		obj.setAttribute("class",className);
	}
}
function regButton(pid,funcs){
	if($$(pid)){
		var divs=$$(pid).getElementsByTagName("div");
		var dl=divs.length;
		for(var i=0;i<dl;i++){
			(function  (i){
				if(divs[i].innerHTML.indexOf("flash")==-1){
					setClass(divs[i],"flexbt");
					divs[i].onmouseover=function(){
						setClass(this,"flexbt_over");
					};
					divs[i].onmouseout=function(){
						setClass(this,"flexbt");
					};
					divs[i].onclick=function(e){
						if(funcs&&funcs[i]){
							funcs[i](e);
						}
					};
				}else{
					setClass(divs[i],"flexbt_flash");
				}
			})(i)
		}
	}
}
function T(id){
	return {
		show:function(){
			$$(id).style.display="";
			return this;
		},
		hid:function(){
			$$(id).style.display="none";
			return this;
		},
		css:function(param){
			var tobj=$$(id);
			for(var i in param){
				try{tobj.style[i]=param[i];}catch(e){}
			}
			return this;
		}
	};
}
function trim (str){
	return str.replace(/^\s+|\s+$/g,"");
}