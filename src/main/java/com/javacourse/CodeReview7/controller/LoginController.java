package com.javacourse.CodeReview7.controller;

import com.javacourse.CodeReview7.model.Student;
import com.javacourse.CodeReview7.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private StudentService studentService;

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        Student student = new Student();
        modelAndView.addObject("student", student);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value="/registration", method = RequestMethod.POST)
    public ModelAndView createNewStudent(@Valid Student student, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Student studentExists = studentService.findStudentByEmail(student.getMail());
        if (studentExists != null) {
            bindingResult.rejectValue("email", "error.student", "There is already a student registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            studentService.saveStudent(student);
            modelAndView.addObject("successMessage", "Student has been registered successfully");
            modelAndView.addObject("student", new Student());
        }
        return modelAndView;
    }
    @RequestMapping(value="/admin/home", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Student student = studentService.findStudentByEmail(auth.getName());
        modelAndView.addObject("studentName","Welcome " + student.getName() + " " + student.getLastName() + " (" + student.getEmail() + ")");
        modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }
}
