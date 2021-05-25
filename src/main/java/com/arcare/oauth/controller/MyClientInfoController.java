package com.arcare.oauth.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.arcare.oauth.po.ClientInfo;
import com.arcare.oauth.po.Developer;
import com.arcare.oauth.po.Scope;
import com.arcare.oauth.service.ClientInfoService;
/**
 * 
 * @author FUHSIANG_LIU
 * 我的服物
 */
@Controller
@RequestMapping(value = "/myClientInfo")
public class MyClientInfoController {

	@Autowired
	private ClientInfoService clientInfoService;
	/**
	 * 查詢
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model,Integer pageNo,Integer pageSize,
			HttpSession session) {
		Object obj=session.getAttribute("user");
		Developer dev=Developer.class.cast(obj);
		
		
		model.addAttribute("objList", clientInfoService.findPagneByDeveloper(dev,pageNo, pageSize));
		return "myClientInfo/list";
	}
	
	/**
	 * 修改
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
	public String showEdit(@PathVariable("id") int id, Model model) {
		ClientInfo client = clientInfoService.findById(id);
		
		Map<String, String> clientTypeList = clientInfoService.findAllClientType();
		model.addAttribute("clientTypeList", clientTypeList);
		
		List<Scope> scopeList = clientInfoService.findAllScope();
		model.addAttribute("scopeList", scopeList);
		model.addAttribute("form", client);	
		
		return "myClientInfo/form";
	}
	
	/**
	 * 新增
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String showAdd(Model model,
			HttpSession session) {
		ClientInfo client = new ClientInfo();
		Object obj=session.getAttribute("user");
		Developer dev=Developer.class.cast(obj);
		client.setDeveloper(dev);
		client.setClientId(UUID.randomUUID().toString());
		client.setClientSecret(UUID.randomUUID().toString());
		
		Map<String, String> clientTypeList = clientInfoService.findAllClientType();
		model.addAttribute("clientTypeList", clientTypeList);
		
		List<Scope> scopeList = clientInfoService.findAllScope();
		model.addAttribute("scopeList", scopeList);
		
		model.addAttribute("form", client);
		
		
		return "myClientInfo/form";
	}
	
	/**
	 * 存檔 新增 action
	 * @param clientInfo
	 * @param result
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String saveOrUpdateAction(@ModelAttribute("form") @Validated ClientInfo clientInfo, 
			BindingResult result, Model model,
			final RedirectAttributes redirectAttributes,
			HttpSession session) {
		if (result.hasErrors()) {
			return "myClientInfo/form";
		} else {
			redirectAttributes.addFlashAttribute("css", "success");
			if (clientInfo.isNew()) {
				Object obj=session.getAttribute("user");
				Developer dev=Developer.class.cast(obj);
				
				clientInfo.setDeveloper(dev);
				clientInfo.setClientState("init");
				clientInfo.setDateCreated(new Date());
				clientInfo.setLastUpdated(new Date());
				clientInfo.setLastUpdateUser(dev.getUsername());
				
				clientInfoService.saveOrUpdate(clientInfo);
				redirectAttributes.addFlashAttribute("msg", "新增成功!");
			} else {
				Object obj=session.getAttribute("user");
				Developer dev=Developer.class.cast(obj);
				ClientInfo originClientInfo=clientInfoService.findById(clientInfo.getId());
				if(originClientInfo.getClientState().equals("init") && //在編輯中狀態
				  originClientInfo.getDeveloper().getUsername().equals(dev.getUsername())) {
					
					originClientInfo.setLastUpdated(new Date());
					originClientInfo.setLastUpdateUser(dev.getUsername());
					
					originClientInfo.setRedirectUri(clientInfo.getRedirectUri());
					originClientInfo.setScope(clientInfo.getScope());
					originClientInfo.setClientType(clientInfo.getClientType());
					originClientInfo.setNote(clientInfo.getNote());
					
					redirectAttributes.addFlashAttribute("msg", "更新成功!");
					clientInfoService.saveOrUpdate(originClientInfo);
				}else {
					redirectAttributes.addFlashAttribute("msg", "操作失敗!");
				}
			}
			return "redirect:/myClientInfo/" + clientInfo.getId();
		}
	}
	
	/**
	 * 查詢
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String showInfo(@PathVariable("id") int id, Model model,HttpSession session) {
		
		Object obj=session.getAttribute("user");
		Developer dev=Developer.class.cast(obj);
		
		ClientInfo clientInfo = clientInfoService.findById(id);
		
		if(clientInfo!=null && dev.getUsername().equals(clientInfo.getDeveloper().getUsername())){
			
			//當前狀態
			Map<String, String> clientStateList = clientInfoService.getClientStateMap();
			String currentClientState = clientStateList.get(clientInfo.getClientState());
			model.addAttribute("currentClientState", currentClientState);
			//當前client type
			Map<String, String> clientTypeList = clientInfoService.findAllClientType();
			String currentClientType = clientTypeList.get(clientInfo.getClientType());
			model.addAttribute("currentClientType",currentClientType);
			
			List<Scope> scopeList = clientInfoService.findAllScope();
			model.addAttribute("scopeList", scopeList);

			model.addAttribute("clientInfo", clientInfo);
			return "myClientInfo/show";
		}else {
			model.addAttribute("css", "danger");
			model.addAttribute("msg", "查無資料");
			return "redirect:/myClientInfo/list";
		}
	}
	
	/**
	 * ready -> publish
	 * stop -> publish
	 * 發佈 action
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/{id}/publish", method = RequestMethod.POST)
	public String pbulishAction(@PathVariable("id") int id, 
			final RedirectAttributes redirectAttributes,
			HttpSession session) {
		Object obj=session.getAttribute("user");
		Developer dev=Developer.class.cast(obj);
		
		ClientInfo clientInfo=clientInfoService.findById(id);
		if(clientInfo!=null && 
			dev.getUsername().equals(clientInfo.getDeveloper().getUsername()) &&
			(clientInfo.getClientState().equals("stop")||
			clientInfo.getClientState().equals("ready"))
			){
			clientInfo.setClientState("publish");
			clientInfo.setLastUpdated(new Date());
			clientInfo.setLastUpdateUser(dev.getUsername());
			clientInfoService.saveOrUpdate(clientInfo);
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "發佈!");
		}else{
			redirectAttributes.addFlashAttribute("css", "fail");
			redirectAttributes.addFlashAttribute("msg", "操作失敗!");
		}
		
		return "redirect:/myClientInfo/list";
	}
	
	/**
	 * 
	 * publish -> stop
	 * ready -> stop
	 * 開通(ready) action
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/{id}/stop", method = RequestMethod.POST)
	public String stopAction(@PathVariable("id") int id, 
			final RedirectAttributes redirectAttributes,
			HttpSession session) {
		Object obj=session.getAttribute("user");
		Developer dev=Developer.class.cast(obj);
		
		ClientInfo clientInfo=clientInfoService.findById(id);
		if(clientInfo!=null && 
			dev.getUsername().equals(clientInfo.getDeveloper().getUsername())&&
			(clientInfo.getClientState().equals("publish")||
			 clientInfo.getClientState().equals("ready"))
			){
			clientInfo.setClientState("stop");
			clientInfo.setLastUpdated(new Date());
			clientInfo.setLastUpdateUser(dev.getUsername());
			clientInfoService.saveOrUpdate(clientInfo);
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "停用!");
		}else{
			redirectAttributes.addFlashAttribute("css", "fail");
			redirectAttributes.addFlashAttribute("msg", "操作失敗!");
		}
		return "redirect:/myClientInfo/list";
	}
	
	/**
	 * 
	 * init -> reviewing
	 * 送審 action
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/{id}/reviewing", method = RequestMethod.POST)
	public String reviewingAction(@PathVariable("id") int id, 
			final RedirectAttributes redirectAttributes,
			HttpSession session) {
		Object obj=session.getAttribute("user");
		Developer dev=Developer.class.cast(obj);
		
		ClientInfo clientInfo=clientInfoService.findById(id);
		if(clientInfo!=null && 
			dev.getUsername().equals(clientInfo.getDeveloper().getUsername())&&
			clientInfo.getClientState().equals("init")){
			clientInfo.setClientState("reviewing");
			clientInfo.setLastUpdated(new Date());
			clientInfo.setLastUpdateUser(dev.getUsername());
			clientInfoService.saveOrUpdate(clientInfo);
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "送審中!");
		}else{
			redirectAttributes.addFlashAttribute("css", "fail");
			redirectAttributes.addFlashAttribute("msg", "操作失敗!");
		}
		
		return "redirect:/myClientInfo/list";
	}
	
	/**
	 * deny -> init
	 * stop -> init
	 * 回到重新編輯的狀態 action
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/{id}/init", method = RequestMethod.POST)
	public String initAction(@PathVariable("id") int id, 
			final RedirectAttributes redirectAttributes,
			HttpSession session) {
		Object obj=session.getAttribute("user");
		Developer dev=Developer.class.cast(obj);
		
		ClientInfo clientInfo=clientInfoService.findById(id);
		if(clientInfo!=null && 
			dev.getUsername().equals(clientInfo.getDeveloper().getUsername()) &&
			(clientInfo.getClientState().equals("stop") ||
			 clientInfo.getClientState().equals("deny"))){
			clientInfo.setClientState("init");
			clientInfo.setLastUpdated(new Date());
			clientInfo.setLastUpdateUser(dev.getUsername());
			clientInfoService.saveOrUpdate(clientInfo);
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "回到編輯狀態!");
		}else{
			redirectAttributes.addFlashAttribute("css", "fail");
			redirectAttributes.addFlashAttribute("msg", "操作失敗!");
		}
		return "redirect:/myClientInfo/list";
	}

}
