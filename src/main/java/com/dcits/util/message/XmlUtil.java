package com.dcits.util.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.dcits.util.message.JsonUtil.TypeEnum;

/**
 * 使用dom4j对xml报文进行解析
 * @author 
 * @version 1.0.0.0,2017.0719
 *
 */

public class XmlUtil {  
	
	/**
     * 根据输入的xml串获取指定的节点信息<br>
     * mode=1,返回包含所有节点名称的list<br>
     * mode=0,返回包含所有节点的类型的list<br>
     * mode=4，返回包含所有节点的路径的list<br>
     * mode=2,返回key为节点名称,value为节点值的map<br>
     * <br>
     * mode=3,返回包含所有信息的Array<br>
     * @param xml
     * @param mode
     * @return
     */
    public static Object getXmlList(String xml,int mode) throws Exception{
    	
    	//返回的都是子节点的map或者list
    	Map<String,String> jsonTreeMap = new HashMap<String,String>();
    	
    	List<String> jsonTreeList = new ArrayList<String>();
    	List<String> jsonTreeType = new ArrayList<String>();
    	List<String> jsonTreePath = new ArrayList<String>();
    	
		Map maps = Dom2Map(xml);
		
		JsonUtil.viewJsonTree(maps, jsonTreeMap, jsonTreeList, jsonTreeType, jsonTreePath, "TopRoot");
		
		if (mode == 1) {
			return jsonTreeList;
		} else if(mode == 2) {
			return jsonTreeMap;
		} else if (mode == 0) {
			return jsonTreeType;			
		} else if (mode == 4) {
			return jsonTreePath;
		} else {
			Object[] a = {jsonTreeList, jsonTreeType, jsonTreePath, jsonTreeMap};
			return a;
		}
        
    }
	
    private static int i = 0;
    /**
     * 复杂嵌套Xml获取Object数据
     * 
     * @param xmlStr
     * @param argsPath
     * @param argsType
     * @return
     */
	public static String getObjectByXml(String xmlStr, String argsPath, TypeEnum argsType) {
        if(argsPath == null || argsPath.equals("") || argsType == null){return null;}
         
        Object obj = null;
        try {
            Map maps = Dom2Map(xmlStr);
           
            //多层获取
            if(argsPath.indexOf(".") >= 0){
            	
                //类型自适应
                obj = JsonUtil.getObject(maps, argsPath, argsType);
            }else{ //第一层获取
                if(argsType == TypeEnum.string){
                    obj = maps.get(argsPath).toString();
                }else if(argsType == TypeEnum.map){
                    obj = maps.get(argsPath);
                }else if(argsType == TypeEnum.arrayList){
                    obj = maps.get(argsPath);
                }else if(argsType == TypeEnum.arrayMap){
                    obj = maps.get(argsPath);
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
            //System.out.println("###[Error] getObjectByJson() "+e.getMessage());
            return null;
        }
        i = 0;
        if (obj!=null) {
        	return obj.toString();
        } else {
        	return null;
        }
        
    }
	
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> Dom2Map(String docStr) throws Exception{ 
	  	Document doc = null;
	  	try {
			doc = DocumentHelper.parseText(docStr);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}
        Map<String, Object> map = new LinkedHashMap<String, Object>(); 
        if (doc == null) {
        	return map; 
        }
            
        Element root = doc.getRootElement(); 
        Map<String, Object> rootMap = new LinkedHashMap<String, Object>(); 
        
        map.put(root.getName(), rootMap);
        
        for (Iterator iterator = root.elementIterator(); iterator.hasNext();) { 
            Element e = (Element) iterator.next(); 
            List list = e.elements(); 
            if (list.size() > 0) { 
            	putList(rootMap, e.getName(), Dom2Map(e));
            } else { 
            	putList(rootMap, e.getName(), getElementText(e));
            }
            	
        } 
        return map; 
    } 
	
	/**
	 * 已经存在的key则转换成list
	 * @param map
	 * @param key
	 * @param value
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void putList(Map map, String key, Object value) {
		List mapList = new ArrayList();
		if (map.get(key) != null) {
			Object object = map.get(key);
			if (object.getClass().getName().equals("java.util.ArrayList")) {
				mapList = (List) object;
				mapList.add(value);
			}
			
			if (!object.getClass().getName().equals("java.util.ArrayList")) {
				mapList.add(map.get(key));
				mapList.add(value);
			}
			
			map.put(key, mapList);
			
		} else {
			map.put(key, value);
		}
	}
	
    @SuppressWarnings({ "rawtypes"})
	public static Object Dom2Map(Element e){ 
    	Map map = new LinkedHashMap(); 
        List list = e.elements(); 
        if (list.size() > 0) { 
            for (int i = 0;i < list.size(); i++) { 
                Element iter = (Element) list.get(i); 
                 
                if(iter.elements().size() > 0){                 	
                	putList(map, iter.getName(), Dom2Map(iter));               	                    
                } else { 
                	putList(map, iter.getName(), getElementText(iter));
                } 
            } 
        } else {
        	putList(map, e.getName(), getElementText(e));
        }        	 
        return map; 
    } 
    
    private static Object getElementText (Element e) {
    	String type = e.attributeValue("type");
    	String text = e.getTextTrim();
    	if (type == null) {
    		return text;
    	}
    	
    	switch (type.toUpperCase()) {
		case "INT":
		case "INTEGER":
			return Integer.parseInt(text);
		case "NUMBER":			
		case "LONG":
			return Long.parseLong(text);
		case "DOUBLE":
		case "FLOAT":
			return Double.parseDouble(text);
		default:
			return text;
		}
    }
}
