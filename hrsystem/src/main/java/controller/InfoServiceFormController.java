package controller;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import hr.model.DepInfoBean;
import hr.model.DepInfoService;
import hr.model.EmpInfoBean;
import hr.model.EmpInfoService;
import hr.model.InfoSecurityLvBean;
import hr.model.InfoServiceFormBean;
import hr.model.InfoServiceFormService;
import hr.model.InfoServiceTypeBean;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

@Controller
@Scope("session")
@RequestMapping("/infoservice")
public class InfoServiceFormController implements Serializable{
	
	@Autowired
    private InfoServiceFormService infoServiceFormService;
	@Autowired
    private EmpInfoService empInfoService;
	
	
	@RequestMapping(value = "/get/applicantdep", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getApplicantDep(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();

		//暫時設成2
//		EmpInfoBean empInfo = new EmpInfoBean();
//		DepInfoBean depInfo = new DepInfoBean();
//		depInfo.setNo(2);
//		empInfo.setDepInfoBean(depInfo);
//		session.setAttribute("loginToken", empInfo);
		
		
		EmpInfoBean emp = (EmpInfoBean)session.getAttribute("loginToken");
		
		int depNo = emp.getDepInfoBean().getNo();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("applicantDepNo", Integer.toString(depNo));
		return jsonObject.toString();
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String list() throws Exception {
		return infoServiceFormService.iSFList().toString();
	}
	
	@RequestMapping(value = "/detail", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String findById(String num) throws Exception {
		int id = 0;
		try {
			id = Integer.parseInt(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infoServiceFormService.iSF(id).toString();
	}
	
	@RequestMapping(value = "/list/receiver", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String findByReceiver(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		//暫時設成14
//	    EmpInfoBean empInfo = new EmpInfoBean();
//		empInfo.setId(14);
//		session.setAttribute("loginToken", empInfo);
		
		EmpInfoBean emp = (EmpInfoBean)session.getAttribute("loginToken");
		int empId = emp.getId();
		return infoServiceFormService.iSFListByReceiver(empId).toString();
	}
	
//	@RequestMapping(value = "/list/stage/one", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
//    @ResponseBody
//    public String findByApplicantSupervisor() throws Exception {
//        //暫時塞14
//		return infoServiceFormService.iSFListByApplicantSupervisor(14).toString();
//	}
//	
//	@RequestMapping(value = "/list/stage/two", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
//    @ResponseBody
//    public String findByContractor() throws Exception {
////		int contractor = 0;
////		try {
////			contractor = Integer.parseInt(num);
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
//        //暫時塞14
//		return infoServiceFormService.iSFListByContractor(14).toString();
//	}
//	
//	@RequestMapping(value = "/list/stage/three", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
//    @ResponseBody
//    public String findByVerification() throws Exception {
//        //暫時塞14
//		return infoServiceFormService.iSFListByVerification(14).toString();
//	}
//	
//	@RequestMapping(value = "/list/stage/four", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
//    @ResponseBody
//    public String findByStage4() throws Exception {
//        //暫時塞14
//		return infoServiceFormService.iSFListByStage4(14).toString();
//	}
	
	@RequestMapping(value = "/list/stage/six", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String findByStage6() throws Exception {
        
		return infoServiceFormService.iSFListByStage(6.0).toString();
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
	public String save(String type, String applicant, String applicantDep, String contractorDep, String demand) throws Exception {

		
		InfoServiceFormBean bean = new InfoServiceFormBean();
		EmpInfoBean applicantBean = new EmpInfoBean();
		DepInfoBean applicantDepBean = new DepInfoBean();
		DepInfoBean contractorDepBean = new DepInfoBean();
		EmpInfoBean receiverBean = new EmpInfoBean();
		
		int applicantId = 0;
		try {
			applicantId = Integer.parseInt(applicant);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int applicantDepNo = 0;
		try {
			applicantDepNo = Integer.parseInt(applicantDep);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int contractorDepNo = 0;
		try {
			contractorDepNo = Integer.parseInt(contractorDep);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		applicantBean.setId(applicantId);
		applicantDepBean.setNo(applicantDepNo);
		contractorDepBean.setNo(contractorDepNo);
		receiverBean.setId(empInfoService.getSupervisorId(applicantDepNo));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = null;
		try {
			date = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		bean.setId(0);
		bean.setType(type);
		bean.setApplicationTime(date);
		bean.setApplicant(applicantBean);
		bean.setApplicantDep(applicantDepBean);
		bean.setContractorDep(contractorDepBean);
		bean.setDemand(demand);
		bean.setStage(1.0);
		bean.setReceiver(receiverBean);
		
		
		infoServiceFormService.insert(bean);
		
		return infoServiceFormService.iSFList().toString();
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
	public String update(String id,String applicationTime,String fileNo,String type,String applicantId,String applicantDepNo,String contractorId,String contractorDepNo,String demandContent,String infoServiceTypeNo,String event,String eventRemark,String securityLv,String processWay,String pStartTime,String pEndTime,String correction,String cEstimated,String cActual,String improvement,String iEstimated,String iActual,String verificationId,String returnReason,String remark,String stage) throws Exception {
        
        
		InfoServiceFormBean bean = new InfoServiceFormBean();
		EmpInfoBean applicantBean = new EmpInfoBean();
		DepInfoBean applicantDepBean = new DepInfoBean();
		EmpInfoBean contractorBean = new EmpInfoBean();
		DepInfoBean contractorDepBean = new DepInfoBean();
		InfoServiceTypeBean infoServiceTypeBean = new InfoServiceTypeBean();
		InfoSecurityLvBean infoSecurityLvBean = new InfoSecurityLvBean();
		EmpInfoBean verificationBean = new EmpInfoBean();
		EmpInfoBean receiverBean = new EmpInfoBean();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		//資料轉換
		int idNo = 0;
		try {
			idNo = Integer.parseInt(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Date applicationTimeSDF = sdf.parse(applicationTime);
		
		Integer applicantIdNo = null;
		try {
			applicantIdNo = Integer.parseInt(applicantId);
		} catch (Exception e) {
			applicantIdNo = null;
		}
		
		Integer applicantDepNum = null;
		try {
			applicantDepNum = Integer.parseInt(applicantDepNo);
		} catch (Exception e2) {
			applicantDepNum = null;
		}
		
		Integer contractorIdNo = null;
		try {  
			contractorIdNo = Integer.parseInt(contractorId);
		} catch (Exception e1) {
			contractorIdNo = null;
		}
		
		Integer contractorDepNum = null;
		try {
			contractorDepNum = Integer.parseInt(contractorDepNo);
		} catch (Exception e) {
			contractorDepNum = null;
		}
		
		Integer infoServiceTypeNum = null;
		try {
			infoServiceTypeNum = Integer.parseInt(infoServiceTypeNo);
		} catch (Exception e) {
			infoServiceTypeNum = null;
		}
		
		Integer securityLevel = null;
		try {
			securityLevel = Integer.parseInt(securityLv);
		} catch (Exception e) {
			securityLevel = null;
		}
		
		Double stageNo = null;
		try {
			stageNo = Double.parseDouble(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Date pStart = null;
		try {
			pStart = sdf.parse(pStartTime);
		} catch (NullPointerException e1) {
			if(stageNo==2 || stageNo==3 || stageNo==99) {
				pStart = null;
			}else {
				e1.printStackTrace();
			}
		}
		Date pEnd = null;
		try {
			pEnd = sdf.parse(pEndTime);
		} catch (NullPointerException e1) {
			if(stageNo==2 || stageNo==3 || stageNo==99) {
				pEnd = null;
			}else {
				e1.printStackTrace();
			}
		}
		long pTotal = 0;
		try {
			pTotal = (pEnd.getTime() - pStart.getTime())/(1000*60);
		} catch (NullPointerException e2) {
			pTotal = 0;
		}
		
		Date cEstComplete = null;
		try {
			cEstComplete = sdf.parse(cEstimated);
		} catch (NullPointerException e1) {
			if(stageNo==2 || stageNo==3 || stageNo==99) {
				cEstComplete = null;
			}else {
				e1.printStackTrace();
			}
		}
		Date cActComplete = null;
		try {
			cActComplete = sdf.parse(cActual);
		} catch (NullPointerException e1) {
			if(stageNo==2 || stageNo==3 || stageNo==99) {
				cActComplete = null;
			}else {
				e1.printStackTrace();
			}
		}
		long cTotal = 0;
		try {
			cTotal = (cActComplete.getTime() - cEstComplete.getTime())/(1000*60);
		} catch (NullPointerException e2) {
			cTotal = 0;
		}
		
		Date iEstComplete = null;
		try {
			iEstComplete = sdf.parse(iEstimated);
		} catch (NullPointerException e1) {
			if(stageNo==2 || stageNo==3 || stageNo==99) {
				iEstComplete = null;
			}else {
				e1.printStackTrace();
			}
		}
		Date iActComplete = null;
		try {
			iActComplete = sdf.parse(iActual);
		} catch (NullPointerException e1) {
			if(stageNo==2 || stageNo==3 || stageNo==99) {
				iActComplete = null;
			}else {
				e1.printStackTrace();
			}
		}
		long iTotal = 0;
		try {
			iTotal = (iActComplete.getTime() - iEstComplete.getTime())/(1000*60);
		} catch (NullPointerException e1) {
			iTotal = 0;
		}
		
		Integer verificationIdNo = null;
		try {
			verificationIdNo = Integer.parseInt(verificationId);
		} catch (Exception e) {
			verificationIdNo = null;
		}
		
		
		
		applicantBean.setId(applicantIdNo);
		applicantDepBean.setNo(applicantDepNum);
		try {
			contractorBean.setId(contractorIdNo);
		} catch (NullPointerException e3) {
			contractorBean = null;
		}
		contractorDepBean.setNo(contractorDepNum);
		try {
			infoServiceTypeBean.setNo(infoServiceTypeNum);
		} catch (NullPointerException e2) {
			infoServiceTypeBean = null;
		}
		try {
			infoSecurityLvBean.setLv(securityLevel);
		} catch (NullPointerException e1) {
			infoSecurityLvBean = null;
		}
		try {
			verificationBean.setId(verificationIdNo);
		} catch (NullPointerException e) {
			verificationBean = null;
		}
		//指定下一階段接收者
		if(stageNo==2.0) {
			receiverBean.setId(empInfoService.getSupervisorId(contractorDepNum));
		}else if(stageNo==3.0) {
			receiverBean.setId(contractorIdNo);
		}else if(stageNo==4.0) {
			receiverBean.setId(verificationIdNo);
		}else if(stageNo==4.5) {
			receiverBean.setId(contractorIdNo);
		}else if(stageNo==5.0) {
			//資訊服務申請單管理人員,目前是Dori(12)
			receiverBean.setId(12);
		}else {
			//跑完全部流程後,不會有接收人
			receiverBean = null;
		}
		
		
		
		bean.setId(idNo);
		bean.setApplicationTime(applicationTimeSDF);
		bean.setFileNo(fileNo);
		bean.setType(type);
		bean.setApplicant(applicantBean);
		bean.setApplicantDep(applicantDepBean);
		bean.setContractor(contractorBean);
		bean.setContractorDep(contractorDepBean);
		bean.setDemand(demandContent);
		bean.setInfoServiceTypeBean(infoServiceTypeBean);
		bean.setEventType(event);
		bean.setEventRemark(eventRemark);
		bean.setInfoSecurityLvBean(infoSecurityLvBean);
		bean.setProcessWay(processWay);
		bean.setpStartTime(pStart);
		bean.setpEndTime(pEnd);
		bean.setpTotalTime(pTotal);
		bean.setCorrection(correction);
		bean.setcEstimated(cEstComplete);
		bean.setcActual(cActComplete);
		bean.setcTotal(cTotal);
		bean.setImprovement(improvement);
		bean.setiEstimated(iEstComplete);
		bean.setiActual(iActComplete);
		bean.setiTotal(iTotal);
		bean.setVerification(verificationBean);
		bean.setReturnReason(returnReason);
		bean.setRemark(remark);
		bean.setStage(stageNo);
		bean.setReceiver(receiverBean);
		
		if(stageNo==99.0) {
			infoServiceFormService.delete(bean.getId());
			return infoServiceFormService.iSFList().toString();
		}
		
		infoServiceFormService.update(bean);
		
		return infoServiceFormService.iSFList().toString();
	}
}
