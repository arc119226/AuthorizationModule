package com.arcare.oauth.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
 * @author ptx48
 *
 */
@Controller
@RequestMapping(value = "/clientInfo")
public class ClientInfoController {

	@Autowired
	private ClientInfoService clientInfoService;
	
	/**
	 * init state list
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/initlist", method = RequestMethod.GET)
	public String initlist(Model model,Integer pageNo,Integer pageSize,
			HttpSession session) {
		model.addAttribute("objList", clientInfoService.findPageByClientStatus("init", pageNo, pageSize));
		return "clientInfo/initlist";
	}
	
	/**
	 * reviewing state list
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/reviewinglist", method = RequestMethod.GET)
	public String reviewinglist(Model model,Integer pageNo,Integer pageSize,HttpSession session) {
		model.addAttribute("objList", clientInfoService.findPageByClientStatus("reviewing", pageNo, pageSize));
		return "clientInfo/reviewinglist";
	}
	
	/**
	 * ready state list
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/readylist", method = RequestMethod.GET)
	public String readylist(Model model,Integer pageNo,Integer pageSize,HttpSession session) {
		model.addAttribute("objList", clientInfoService.findPageByClientStatus("ready", pageNo, pageSize));
		return "clientInfo/readylist";
	}
	
	/**
	 * deny state list
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/denylist", method = RequestMethod.GET)
	public String denylist(Model model,Integer pageNo,Integer pageSize,HttpSession session) {
		model.addAttribute("objList", clientInfoService.findPageByClientStatus("deny", pageNo, pageSize));
		return "clientInfo/denylist";
	}
	/**
	 * stop state list
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/stoplist", method = RequestMethod.GET)
	public String stoplist(Model model,Integer pageNo,Integer pageSize,HttpSession session) {
		model.addAttribute("objList", clientInfoService.findPageByClientStatus("stop", pageNo, pageSize));
		return "clientInfo/stoplist";
	}
	
	/**
	 * publish state list
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/publishlist", method = RequestMethod.GET)
	public String publishlist(Model model,Integer pageNo,Integer pageSize,HttpSession session) {
		model.addAttribute("objList", clientInfoService.findPageByClientStatus("publish", pageNo, pageSize));
		return "clientInfo/publishlist";
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
		List<Scope> scopeList = clientInfoService.findAllScope();
		Map<String, String> clientStateList = clientInfoService.getClientStateMap();
		
		model.addAttribute("clientStateList", clientStateList);
		model.addAttribute("clientTypeList", clientTypeList);
		model.addAttribute("scopeList", scopeList);
		
		model.addAttribute("form", client);	
		
		return "clientInfo/form";
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
	public String saveOrUpdateAction(@ModelAttribute("form") @Validated ClientInfo clientInfo, BindingResult result, Model model,final RedirectAttributes redirectAttributes,HttpSession session) {
		if (result.hasErrors()) {
			return "clientInfo/form";
		} else {
			if (clientInfo.isNew()) {
				
			} else {
				Object obj=session.getAttribute("user");
				Developer dev=Developer.class.cast(obj);

				ClientInfo originClientInfo=clientInfoService.findById(clientInfo.getId());
				
				originClientInfo.setLastUpdated(new Date());
				originClientInfo.setLastUpdateUser(dev.getUsername());
				originClientInfo.setRedirectUri(clientInfo.getRedirectUri());
				originClientInfo.setScope(clientInfo.getScope());
				originClientInfo.setClientType(clientInfo.getClientType());
				originClientInfo.setNote(clientInfo.getNote());
				
				clientInfoService.saveOrUpdate(originClientInfo);
				redirectAttributes.addFlashAttribute("css", "success");
				redirectAttributes.addFlashAttribute("msg", "資料更新成功!");
			}
			return "redirect:/clientInfo/" + clientInfo.getId();
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
		ClientInfo clientInfo = clientInfoService.findById(id);
		if(clientInfo!=null){

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
			return "clientInfo/show";
		}else {
			model.addAttribute("css", "danger");
			model.addAttribute("msg", "查無資料");
			return "redirect:/clientInfo/initlist";
		}
	}
	
	/**
	 * deny -> init
	 * stop -> init
	 * 重新編輯 action
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/{id}/init", method = RequestMethod.POST)
	public String initAction(@PathVariable("id") int id, final RedirectAttributes redirectAttributes,HttpSession session) {
		Object obj=session.getAttribute("user");
		Developer dev=Developer.class.cast(obj);
		ClientInfo clientInfo=clientInfoService.findById(id);
		if(clientInfo!=null && (clientInfo.getClientState().equals("stop") || clientInfo.getClientState().equals("deny"))){
			
			String beforeStatus=clientInfo.getClientState();
			
			clientInfo.setClientState("init");
			clientInfo.setLastUpdated(new Date());
			clientInfo.setLastUpdateUser(dev.getUsername());
			clientInfoService.saveOrUpdate(clientInfo);
			
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "回到編輯狀態!");
			
			return "redirect:/clientInfo/"+beforeStatus+"list";
		}else{
			redirectAttributes.addFlashAttribute("css", "fail");
			redirectAttributes.addFlashAttribute("msg", "操作失敗!");
			return "redirect:/clientInfo/initlist";
		}
		
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
	public String reviewingAction(@PathVariable("id") int id, final RedirectAttributes redirectAttributes,HttpSession session) {
		Object obj=session.getAttribute("user");
		Developer dev=Developer.class.cast(obj);	
		ClientInfo clientInfo=clientInfoService.findById(id);
		if(clientInfo!=null && clientInfo.getClientState().equals("init")){
			String beforeStatus=clientInfo.getClientState();
			
			clientInfo.setClientState("reviewing");
			clientInfo.setLastUpdated(new Date());
			clientInfo.setLastUpdateUser(dev.getUsername());
			clientInfoService.saveOrUpdate(clientInfo);
			
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "送入審核區!");	
			return "redirect:/clientInfo/"+beforeStatus+"list";
		}else{
			redirectAttributes.addFlashAttribute("css", "fail");
			redirectAttributes.addFlashAttribute("msg", "操作失敗!");
			return "redirect:/clientInfo/initlist";
		}
	}
	
	/**
	 * 
	 * reviewing -> deny
	 * 送審 action
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/{id}/deny", method = RequestMethod.POST)
	public String denyAction(@PathVariable("id") int id, final RedirectAttributes redirectAttributes,String denyReason,HttpSession session) {
		Object obj=session.getAttribute("user");
		Developer dev=Developer.class.cast(obj);	
		ClientInfo clientInfo=clientInfoService.findById(id);
		if(clientInfo!=null && clientInfo.getClientState().equals("reviewing")){
			String beforeStatus=clientInfo.getClientState();
			
			clientInfo.setClientState("deny");
			clientInfo.setReason(denyReason);
			clientInfo.setLastUpdated(new Date());
			clientInfo.setLastUpdateUser(dev.getUsername());
			clientInfoService.saveOrUpdate(clientInfo);
			
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "送入審額失敗區!");	
			return "redirect:/clientInfo/"+beforeStatus+"list";
		}else{
			redirectAttributes.addFlashAttribute("css", "fail");
			redirectAttributes.addFlashAttribute("msg", "操作失敗!");
			return "redirect:/clientInfo/reviewinglist";
		}
	}
	
	/**
	 * 
	 * reviewing -> ready
	 * 通過 action
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/{id}/ready", method = RequestMethod.POST)
	public String readyAction(@PathVariable("id") int id, final RedirectAttributes redirectAttributes,HttpSession session) {
		Object obj=session.getAttribute("user");
		Developer dev=Developer.class.cast(obj);	
		ClientInfo clientInfo=clientInfoService.findById(id);
		if(clientInfo!=null && clientInfo.getClientState().equals("reviewing")){
			String beforeStatus=clientInfo.getClientState();
			
			clientInfo.setClientState("ready");
			clientInfo.setLastUpdated(new Date());
			clientInfo.setLastUpdateUser(dev.getUsername());
			clientInfoService.saveOrUpdate(clientInfo);
			
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "送入審核成功區!");	
			return "redirect:/clientInfo/"+beforeStatus+"list";
		}else{
			redirectAttributes.addFlashAttribute("css", "fail");
			redirectAttributes.addFlashAttribute("msg", "操作失敗!");
			return "redirect:/clientInfo/reviewinglist";
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
	public String pbulishAction(@PathVariable("id") int id, final RedirectAttributes redirectAttributes,HttpSession session) {
		Object obj=session.getAttribute("user");
		Developer dev=Developer.class.cast(obj);
		
		ClientInfo clientInfo=clientInfoService.findById(id);
		if(clientInfo!=null && (clientInfo.getClientState().equals("ready")||clientInfo.getClientState().equals("stop"))){
			String beforeStatus=clientInfo.getClientState();
			
			clientInfo.setClientState("publish");
			clientInfo.setLastUpdated(new Date());
			clientInfo.setLastUpdateUser(dev.getUsername());
			clientInfoService.saveOrUpdate(clientInfo);
			
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "發佈!");
			return "redirect:/clientInfo/"+beforeStatus+"list";
		}else{
			redirectAttributes.addFlashAttribute("css", "fail");
			redirectAttributes.addFlashAttribute("msg", "操作失敗!");
			return "redirect:/myClientInfo/initlist";
		}
	}
	
	/**
	 * ready -> stop
	 * publish -> stop
	 * 發佈 action
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/{id}/stop", method = RequestMethod.POST)
	public String stopAction(@PathVariable("id") int id, final RedirectAttributes redirectAttributes,HttpSession session) {
		Object obj=session.getAttribute("user");
		Developer dev=Developer.class.cast(obj);
		
		ClientInfo clientInfo=clientInfoService.findById(id);
		if(clientInfo!=null && (clientInfo.getClientState().equals("ready")||clientInfo.getClientState().equals("publish"))){
			String beforeStatus=clientInfo.getClientState();

			clientInfo.setClientState("stop");
			clientInfo.setLastUpdated(new Date());
			clientInfo.setLastUpdateUser(dev.getUsername());
			
			clientInfoService.saveOrUpdate(clientInfo);

			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "停用!");
			return "redirect:/clientInfo/"+beforeStatus+"list";
		}else{
			redirectAttributes.addFlashAttribute("css", "fail");
			redirectAttributes.addFlashAttribute("msg", "操作失敗!");
			return "redirect:/myClientInfo/initlist";
		}
	}
}
