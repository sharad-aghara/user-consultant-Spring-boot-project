package com.sharad.oc.controller;

import com.sharad.oc.repositories.AppointmentRepo;
import com.sharad.oc.repositories.UserRepo;
import com.sharad.oc.entity.User;
import com.sharad.oc.entity.Apointment;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    AppointmentRepo apointmentRepo;

    @GetMapping("/signin")
    public String login() {
        System.out.println("sign-in...");
        return "login";
    }

    @GetMapping("/about")
    public String about(Model model, Principal principal) {

        String email = principal.getName();
        User current = userRepo.findByEmail(email);

        if (current == null) {
            throw new UsernameNotFoundException("user not found");
        } else if (current.getRole().equals("ADMIN")) {

            List<User> allUsers = userRepo.findAll();
            model.addAttribute("allUsers", allUsers);

            return "admin-about";
        } else if (current.getRole().equals("USER")) {

            List<User> consultants = userRepo.findAll().stream().filter(user -> {
                return user.getRole().equals("CONSULTANT");
            }).collect(Collectors.toList());

            List<Apointment> myApointments = apointmentRepo.findAll().stream().filter(apointment -> {
                return apointment.getUserId() == current.getId();
            }).collect(Collectors.toList());

            model.addAttribute("consultants", consultants);
            model.addAttribute("myapointments", myApointments);

            return "user-about";
        } else {

            List<Apointment> apointments = apointmentRepo.findAll().stream().filter(apointment -> {
                return apointment.getConsultantId() == current.getId();
            }).collect(Collectors.toList());

            model.addAttribute("apointments", apointments);
            return "consultant-about";
        }
    }

    @GetMapping("/book-apointment/{id}")
    public String bookAppointmentById(@PathVariable("id") String consultantId, Model model) {

        model.addAttribute("consultantId", consultantId);
        return "book-apointment";
    }

    @PostMapping("/add-apointment/{id}")
    public String addApointment(@PathVariable("id") String consultantId, HttpServletRequest req, Principal principal) throws ParseException {

        Integer conId = Integer.parseInt(consultantId);
        User current = userRepo.findByEmail(principal.getName());

        Integer userId = current.getId();

        Integer id = Integer.parseInt(req.getParameter("appointmentId"));

        System.out.println(req.getParameter("startTime"));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        Date startTime = format.parse(req.getParameter("startTime"));
        Date endTime = format.parse(req.getParameter("endTime"));


        Apointment apointment = new Apointment(id, userId, conId, startTime, endTime, "scheduled");
        apointmentRepo.save(apointment);

        return "redirect:/about";
    }

    @GetMapping("/update-apointment/{id}")
    public String updateApointment(@PathVariable("id") String apointmentId) {
        Optional<Apointment> apointment = apointmentRepo.findById(Integer.parseInt(apointmentId));
        if (!apointment.isPresent()) {
            throw new UsernameNotFoundException("appointment not found");
        } else {
            apointment.get().setStatus("complete");
            apointmentRepo.save(apointment.get());
            return "redirect:/about";
        }
    }

    @GetMapping("/add-user")
    public String addUserPage() {
        return "add-user";
    }


    @PostMapping("/add-user")
    public String addUserToDb(HttpServletRequest req) {

        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String role = req.getParameter("role");

        User user = new User(Integer.parseInt(id), name, email, new BCryptPasswordEncoder().encode(password), role);
        userRepo.save(user);
        return "redirect:/about";
    }

    @GetMapping("/delete-user/{id}")
    public String deleteUserById(@PathVariable("id") String id) {
        userRepo.deleteById(Integer.parseInt(id));
        return "redirect:/about";
    }

    @GetMapping("/edit-user/{id}")
    public String editUserById(@PathVariable("id") String id, Model model) {
        Optional<User> usr = userRepo.findById(Integer.parseInt(id));
        if (!usr.isPresent()) {
            throw new UsernameNotFoundException("user not found");
        } else {
            model.addAttribute("user", usr.get());

        }

        return "edit-user";
    }

    @PostMapping("/update-user/{id}")
    public String updateUserById(@PathVariable("id") String id, HttpServletRequest req) {

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String role = req.getParameter("role");


        Optional<User> usr = userRepo.findById(Integer.parseInt(id));
        if (!usr.isPresent()) {
            throw new UsernameNotFoundException("user not found");
        } else {

            User old = usr.get();
            old.setName(name);
            old.setEmail(email);
            old.setPassword(new BCryptPasswordEncoder().encode(password));
            old.setRole(role);

            userRepo.save(old);
        }

        return "redirect:/about";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    @PostMapping("/register-user")
    public String register(HttpServletRequest req){

        System.out.println("register-user request");

        Integer id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String role = req.getParameter("role");

        User user = new User(id,name,email,new BCryptPasswordEncoder().encode(password),role);
        userRepo.save(user);
        return "redirect:/signin";
    }
}
