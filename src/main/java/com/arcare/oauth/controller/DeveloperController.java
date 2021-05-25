package com.arcare.oauth.controller;

import java.util.Date;

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

import com.arcare.oauth.po.Developer;
import com.arcare.oauth.service.DeveloperService;

/**
 * 
 * @author FUHSIANG_LIU
 *
 */
@Controller
@RequestMapping(value = "/developer")
public class DeveloperController {

	@Autowired
	private DeveloperService developerService;

	/**
	 * 存檔 新增 action
	 * @param developer
	 * @param result
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String saveOrUpdateAction(@ModelAttribute("form") @Validated Developer developer, 
			BindingResult result, Model model,
			final RedirectAttributes redirectAttributes,HttpSession session) {
		if (result.hasErrors()) {
			return "developer/form";
		} else {
			redirectAttributes.addFlashAttribute("css", "success");
			Developer originDeveloper=developerService.findById(developer.getId());
			if (developer.isNew()) {
				originDeveloper.setDateCreated(new Date());
				redirectAttributes.addFlashAttribute("msg", "developer added successfully!");
			} else {
				redirectAttributes.addFlashAttribute("msg", "developer updated successfully!");
			}
			Object obj=session.getAttribute("user");
			Developer dev=Developer.class.cast(obj);
			
			originDeveloper.setEmail(developer.getEmail());
			originDeveloper.setUserType(developer.getUserType());
			originDeveloper.setLastUpdated(new Date());
			originDeveloper.setLastUpdateUser(dev.getUsername());
			developerService.saveOrUpdate(originDeveloper);
			return "redirect:/developer/" + developer.getId();
		}
	}

	/**
	 * 刪除 action
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
	public String deleteAction(@PathVariable("id") int id, final RedirectAttributes redirectAttributes) {
		developerService.delete(id);
		redirectAttributes.addFlashAttribute("css", "success");
		redirectAttributes.addFlashAttribute("msg", "developer is deleted!");
		return "redirect:/developer/list";
	}

	/**
	 * 新增
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String showAdd(Model model) {
		Developer user = new Developer();
		// set default value
		// ignore
		model.addAttribute("form", user);
		return "developer/form";
	}
	
	/**
	 * 修改
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
	public String showEdit(@PathVariable("id") int id, Model model) {
		Developer developer = developerService.findById(id);
		model.addAttribute("form", developer);		
		return "developer/form";
	}
	
	/**
	 * 查詢
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model,Integer pageNo,Integer pageSize) {
		model.addAttribute("objList", developerService.findPagne(pageNo, pageSize));
		return "developer/list";
	}
	
	/**
	 * 查詢
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String showInfo(@PathVariable("id") int id, Model model) {
		Developer developer = developerService.findById(id);
		if (developer == null) {
			model.addAttribute("css", "danger");
			model.addAttribute("msg", "developer not found");
		}
		model.addAttribute("developer", developer);
		return "developer/show";
	}

}
