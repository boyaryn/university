package ua.in.boyaryn.practice.university.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomepageController {
    @RequestMapping({"/", "index"})
    public String courtReservation(Model model) {
	   	model.addAttribute("email", "bla-bla");
		return "index";
    }
}