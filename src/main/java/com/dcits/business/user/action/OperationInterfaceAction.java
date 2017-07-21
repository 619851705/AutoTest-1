package com.dcits.business.user.action;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.base.action.BaseAction;
import com.dcits.business.user.bean.OperationInterface;
import com.dcits.business.user.service.OperationInterfaceService;
import com.dcits.constant.ReturnCodeConsts;
import com.dcits.constant.SystemConsts;

/**
 * 系统操作接口Action
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */

@Controller
@Scope("prototype")
public class OperationInterfaceAction extends BaseAction<OperationInterface> {

	private static final long serialVersionUID = 1L;
		
	/**
	 * 操作接口类型
	 */
	private Integer opType;
	
	private OperationInterfaceService operationInterfaceService;
	
	@Autowired
	public void setOperationInterfaceService(OperationInterfaceService operationInterfaceService) {
		super.setBaseService(operationInterfaceService);
		this.operationInterfaceService = operationInterfaceService;
	}
	
	
	/**
	 * 根据传入opType查找不同类型的操作接口信息
	 * @return
	 */
	public String listOp() {
		Integer opId = 0;
		switch (opType) {
		case 1:
			opId = SystemConsts.MESSAGE_OP_ID;
			break;
		case 2:
			opId = SystemConsts.WEB_OP_ID;
			break;
		case 3:
			jsonMap.put("data", new HashSet<OperationInterface>());
			jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
			
			return SUCCESS;
		case 4:
			opId = SystemConsts.SYSTEM_OP_ID;
			break;
		case 5:
			opId = SystemConsts.USER_OP_ID;
			break;
		default:
			break;
		}
		Set<OperationInterface> ops = operationInterfaceService.get(opId).getAllOis();
		for (OperationInterface op:ops) {
			op.setParentOpName();
		}
		
		jsonMap.put("data", ops);
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		
		return SUCCESS;
	}
	
	/**************************************************************************/
	
	public void setOpType(Integer opType) {
		this.opType = opType;
	}
	
	
}
