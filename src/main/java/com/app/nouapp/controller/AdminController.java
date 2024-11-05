package com.app.nouapp.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.nouapp.dto.MaterialDto;
import com.app.nouapp.model.Enquiry;
import com.app.nouapp.model.Material;
import com.app.nouapp.model.Response;
import com.app.nouapp.model.StudentInfo;
import com.app.nouapp.service.EnquiryRepository;
import com.app.nouapp.service.MaterialRepository;
import com.app.nouapp.service.ResponseRepository;
import com.app.nouapp.service.StudentInfoRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin") 
public class AdminController {

	@Autowired
	StudentInfoRepository srepo;
	
	@Autowired
	EnquiryRepository erepo;
	
	@Autowired
	ResponseRepository rrepo;
	
	@Autowired
	MaterialRepository mrepo;
	
	@GetMapping("/adminhome")
		 public String showAdminHome(HttpSession session, HttpServletResponse response)
		 {
			try {
				response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
				if(session.getAttribute("adminid")!= null)
				{
					 return "/admin/adminhome";
				}
				else {
					return "redirect:/adminLogin";
				}
			}
	
			catch(Exception ex)
			{
				return "redirect:/adminLogin";
			}
		 }
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/adminLogin";
	}
	
	@GetMapping("/managestudents")
	 public String showStudents(HttpSession session, HttpServletResponse response, Model model)
	 {
		try {
			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
			if(session.getAttribute("adminid")!= null)
			{
				List<StudentInfo> silist= srepo.findAll();
				model.addAttribute("silist", silist);
				 return "/admin/managestudents";
			}
			else {
				return "redirect:/adminLogin";
			}
		}

		catch(Exception ex)
		{
			return "redirect:/adminLogin";
		}
	 }
	
	
	
	@GetMapping("/manageenquiries")
	 public String showEnqueries(HttpSession session, HttpServletResponse response, Model model)
	 {
		try {
			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
			if(session.getAttribute("adminid")!= null)
			{
				List<Enquiry> eqlist= erepo.findAll();
				model.addAttribute("eqlist", eqlist);
				 return "/admin/manageenquiries";
			}
			else {
				return "redirect:/adminLogin";
			}
		}

		catch(Exception ex)
		{
			return "redirect:/adminLogin";
		}
	 }
	
	@GetMapping("/delenq")
	 public String showAdminHome(HttpSession session, HttpServletResponse response, @RequestParam int id)
	 {
		try {
			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
			if(session.getAttribute("adminid")!= null)
			{
				Enquiry enq = erepo.getById(id);
				erepo.delete(enq);
				 return "redirect:/admin/manageenquiries";
			}
			else {
				return "redirect:/adminLogin";
			}
		}

		catch(Exception ex)
		{
			return "redirect:/adminLogin";
		}
	 }
	

	@GetMapping("/managefeedback")
		 public String showFeedback(HttpSession session, HttpServletResponse response,Model model)
		 {
			try {
				response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
				if(session.getAttribute("adminid")!= null)
				{
					List<Response> listfeedback = rrepo.findByResponsetype("feedback");
					model.addAttribute("listfeedback", listfeedback);
					 return "/admin/managefeedback";
				}
				else {
					return "redirect:/adminLogin";
				}
			}
	
			catch(Exception ex)
			{
				return "redirect:/adminLogin";
			}
		 }
	
	@GetMapping("/managecomplaint")
	 public String showComplaint(HttpSession session, HttpServletResponse response, Model model)
	 {
		try {
			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
			if(session.getAttribute("adminid")!= null)
			{
				List<Response> listcomplaint=rrepo.findByResponsetype("complaint");
				model.addAttribute("listcomplaint", listcomplaint);
				 return "/admin/managecomplaint";
			}
			else {
				return "redirect:/adminLogin";
			}
		}

		catch(Exception ex)
		{
			return "redirect:/adminLogin";
		}
	 }
	
	@GetMapping("/addstudymaterial")
	 public String showAddStudyMaterial(HttpSession session, HttpServletResponse response,Model model)
	 {
		try {
			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
			if(session.getAttribute("adminid")!= null)
			{
				MaterialDto dto = new MaterialDto();
				model.addAttribute("dto",dto);
				 return "/admin/addstudymaterial";
			}
			else {
				return "redirect:/adminLogin";
			}
		}

		catch(Exception ex)
		{
			return "redirect:/adminLogin";
		}
	 }
	
	@PostMapping("addstudymaterial")
	public String addStudyMaterial(HttpSession session, HttpServletResponse response, @ModelAttribute MaterialDto dto, RedirectAttributes attrib) {
	    try {
	        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
	        
	        if (session.getAttribute("adminid") != null) {
	            MultipartFile uploadfile = dto.getUploadfile();
	            
	            // Check if the file is not empty
	            if (uploadfile == null || uploadfile.isEmpty()) {
	                attrib.addFlashAttribute("error", "Please upload a file.");
	                return "redirect:/admin/addstudy";
	            }

	            String storageFileName = new Date().getTime() + "_" + uploadfile.getOriginalFilename();
	            String uploadDir = "public/mat/";
	            Path uploadPath = Paths.get(uploadDir);

	            if (!Files.exists(uploadPath)) {
	                Files.createDirectories(uploadPath);
	            }

	            try (InputStream inputStream = uploadfile.getInputStream()) {
	                Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
	            }

	            Material m = new Material();
	            m.setProgram(dto.getProgram());
	            m.setBranch(dto.getBranch());
	            m.setYear(dto.getYear());  
	            m.setMaterialtype(dto.getMaterialtype());
	            m.setSubject(dto.getSubject());
	            m.setTopic(dto.getTopic());

	        
	            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	            String posteddate = df.format(new Date());
	            m.setPosteddate(posteddate);
	            m.setFilename(storageFileName);
  
	            mrepo.save(m);
      
	            attrib.addFlashAttribute("msg", "Material is uploaded");
	            return "redirect:/admin/addstudymaterial";
	        } else {
	            return "redirect:/adminLogin";
	        }
	    } catch (IOException ex) {
	     
	        attrib.addFlashAttribute("error", "Failed to upload material due to: " + ex.getMessage());
	        return "redirect:/admin/addstudy";
	    } catch (Exception ex) {
	 
	        return "redirect:/adminLogin";
	    }
	}
	
	@GetMapping("/managestudymaterial")
	 public String showStudyMaterial(HttpSession session, HttpServletResponse response, Model model)
	 {
		try {
			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
			if(session.getAttribute("adminid")!= null)
			{
				List<Material> mat = mrepo.findAll();
				model.addAttribute("mat",mat);
				 return "/admin/managestudymaterial";
			}
			else {
				return "redirect:/adminLogin";
			}
		}

		catch(Exception ex)
		{
			return "redirect:/adminLogin";
		}
	 }
	
	@GetMapping("/delmat")
	 public String delMaterial(HttpSession session, HttpServletResponse response, @RequestParam int id)
	 {
		try {
			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
			if(session.getAttribute("adminid")!= null)
			{
				Material m = mrepo.findById(id).get();
				Path filePath=Paths.get("public/mat/"+m.getFilename());
				try {
					Files.delete(filePath);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				 mrepo.delete(m);
				 return "redirect:/admin/managestudymaterial";
			}
			else {
				return "redirect:/adminLogin";
			}
		}

		catch(Exception ex)
		{
			return "redirect:/adminLogin";
		}
	 }

	}

