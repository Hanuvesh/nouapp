package com.app.nouapp.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Correct import
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.nou.api.SmsSender;
import com.app.nouapp.dto.AdminLoginDto;
import com.app.nouapp.dto.EnquiryDto;
import com.app.nouapp.dto.StudentInfoDto;
import com.app.nouapp.model.AdminLogin;
import com.app.nouapp.model.Enquiry;
import com.app.nouapp.model.StudentInfo;
import com.app.nouapp.service.AdminLoginRepository;
import com.app.nouapp.service.EnquiryRepository;
import com.app.nouapp.service.StudentInfoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	@Autowired
	EnquiryRepository erepo;
	
	@Autowired
	StudentInfoRepository srepo;
	
	@Autowired
	AdminLoginRepository adrepo;
	
    public String showIndex() {
        return "index";
    }

    @GetMapping("/aboutUs")
    public String showAboutUs() {
        return "aboutUs";
    }

    @GetMapping("/registration")
    public String showRegistration(Model model) {
    	StudentInfoDto dto = new StudentInfoDto();
    	model.addAttribute("dto",dto);
        return "registration";
    }

    @GetMapping("/stuLogin")
    public String showStuLogin(Model model) {
    	StudentInfoDto dto = new StudentInfoDto();
    	model.addAttribute("dto", dto);
        return "stuLogin";
    }

    @GetMapping("/adminLogin")
    public String showAdminLogin(Model model) {
    	AdminLoginDto dto = new AdminLoginDto();
    	model.addAttribute("dto",dto);
        return "adminLogin";
    }

    @GetMapping("/contactUs")
    public String showContactUs(Model model) { 
        EnquiryDto dto = new EnquiryDto();
        model.addAttribute("dto", dto);
        return "contactUs";
    }

    
    @PostMapping("/contactUs")
    public String saveEnquiry(@ModelAttribute EnquiryDto dto, RedirectAttributes attrib)
    {
    	Enquiry e = new Enquiry();
    	e.setName(dto.getName());
    	e.setContactno(dto.getContactno());
    	e.setEmailaddress(dto.getEmailaddress());
    	e.setEnquirytext(dto.getEnquirytext());
    	Date dt = new Date();
    	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    	String posteddate = df.format(dt);
    	e.setPosteddate(posteddate);
    	erepo.save(e);
    	SmsSender ss = new SmsSender();
    	ss.sendSms(dto.getContactno());
    	attrib.addFlashAttribute("msg", "Enquiry is saved");
    	return "redirect:/contactUs";
    }
    
    @PostMapping("/registration")
    public String doRegistration(@ModelAttribute StudentInfoDto dto, RedirectAttributes attrib)
    {
    	StudentInfo s = new StudentInfo();
    	s.setEnrollmentno(dto.getEnrollmentno());
    	s.setName(dto.getName());
    	s.setFname(dto.getFname());
    	s.setMname(dto.getMname());
    	s.setGender(dto.getGender());
    	s.setAddress(dto.getAddress());
    	s.setProgram(dto.getProgram());
    	s.setBranch(dto.getBranch());
    	s.setYear(dto.getYear());
    	s.setContactno(dto.getContactno());
    	s.setEmailaddress(dto.getEmailaddress());
    	s.setPassword(dto.getPassword());
    	Date dt = new Date();
    	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyu");
    	String regdate = df.format(dt);
    	s.setRegdate(regdate);
    	srepo.save(s);
    	attrib.addFlashAttribute("msg", "Registration is done");
    	return "redirect:/registration";
    }
    
    @PostMapping("/stuLogin")
    public String validateStudent(@ModelAttribute StudentInfoDto dto, RedirectAttributes attrib, HttpSession session) {
    	try {
			StudentInfo s = srepo.getById(dto.getEnrollmentno());	
    		if(s.getPassword().equals(dto.getPassword())) {
//    			attrib.addFlashAttribute("msg","valid user");
    			session.setAttribute("studentid", s.getEnrollmentno());
    			return "redirect:/student/studenthome";
    		}
    		else {
    			attrib.addFlashAttribute("msg","Invalid user");
    		}
    		return "redirect:/stuLogin";
    	}
    	catch(EntityNotFoundException ex) {
    		attrib.addFlashAttribute("msg","Student record not found");
    		return "redirect:/stuLogin";
    	}
		
    }
    
    @PostMapping("/adminLogin")
    public String validateAdmin(@ModelAttribute AdminLoginDto dto, RedirectAttributes attrib, HttpSession session) {
    	try {
    	   AdminLogin ad = adrepo.getById(dto.getUserid());
    	   if(ad.getPassword().equals(dto.getPassword()))
    	   {
               session.setAttribute("adminid", dto.getUserid());
    		   return "redirect:/admin/adminhome";
    	   }
    	   else {
    		   attrib.addFlashAttribute("msg","Invalid user");
    		   return "redirect:/adminLogin";
    	   }
    	}
    	catch(EntityNotFoundException ex){
    		attrib.addFlashAttribute("msg","Admin id is not found");
    		return "redirect:/adminLogin";
    	}
    }
}
