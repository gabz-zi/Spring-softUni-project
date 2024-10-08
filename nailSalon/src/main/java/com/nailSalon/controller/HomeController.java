package com.nailSalon.controller;

import com.nailSalon.model.NailSalonUserDetails;
import com.nailSalon.model.dto.DesignHomeDTO;
import com.nailSalon.model.entity.Appointment;
import com.nailSalon.model.view.MyAppointmentView;
import com.nailSalon.service.AppointmentService;
import com.nailSalon.service.DesignService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class HomeController {
    private final DesignService designService;
    private final AppointmentService appointmentService;

    public HomeController(DesignService designService, AppointmentService appointmentService) {
        this.designService = designService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/")
    public String nonLoggedIndex() {

        return "index";
    }

    @GetMapping("/my-appointments")
    @Transactional
    public ModelAndView home(@AuthenticationPrincipal UserDetails userDetails) {

        ModelAndView modelAndView = new ModelAndView("my-appointments");
        DesignHomeDTO designsForHomePage = designService.getDesignsForHomePage(userDetails.getUsername());
        List<MyAppointmentView> myAppointments = appointmentService.getAppointmentsOfUser(userDetails.getUsername());

        if (userDetails instanceof NailSalonUserDetails nailSalonUserDetails) {
            modelAndView.addObject("welcomeMessage", nailSalonUserDetails.getUsername());
        } else {
            modelAndView.addObject("welcomeMessage", "Anonymous");
        }

        modelAndView.addObject("designHomeDTO", designsForHomePage);
        modelAndView.addObject("myAppointments", myAppointments);

        return modelAndView;
    }

    @PostMapping("/appointments/remove/{id}")
    public String removeAppointment(RedirectAttributes redirectAttributes, @PathVariable Long id) {
        Appointment appointment = appointmentService.findById(id);
        appointment.setCancelled(true);
        appointmentService.save(appointment);
        redirectAttributes.addFlashAttribute("removedSuccessfully", true);
        return "redirect:/my-appointments";
    }
}
