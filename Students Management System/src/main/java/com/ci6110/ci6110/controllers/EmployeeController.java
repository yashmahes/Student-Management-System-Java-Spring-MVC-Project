/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ci6110.ci6110.controllers;

import com.ci6110.ci6110.Ci6110Application;
import com.ci6110.ci6110.entities.Employee;
import com.ci6110.ci6110.repositories.EmployeeRepository;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author pk
 */
@Controller
public class EmployeeController {

    @Autowired
    private EmployeeRepository StudentsRepo;

    @GetMapping("/")

    public String showPage(Model model, Pageable pageable) {
        model.addAttribute("data", StudentsRepo.findAll(pageable));

        List<Employee> students = new ArrayList<>();
        StudentsRepo.findAll().forEach(students::add);
        float sum = 0;
        for (Employee e : students) {
            sum += e.getScore();
        }
        float average = sum / students.size();
        String average1 = String.format("%.2f", average);
        model.addAttribute("avg", average1);

        Employee element = Collections.max(students, Comparator.comparingDouble(Employee::getScore));
        float max = element.getScore();
        String max1 = String.format("%.2f", max);
        model.addAttribute("max", max1);

        return "index";
    }

    @PostMapping("/save")
    public String save(Employee e) throws FileNotFoundException, UnsupportedEncodingException {
        StudentsRepo.save(e);

        Ci6110Application.students_list.add(e);
        Ci6110Application.writeFile();

        return "redirect:/";
    }

    @GetMapping("/delete")
    public String deleteEmployee(Integer id) throws FileNotFoundException, UnsupportedEncodingException {
        StudentsRepo.deleteById(id);

        for (Employee e : Ci6110Application.students_list) {
            if (Objects.equals(e.getId(), id)) {
                Ci6110Application.students_list.remove(e);
                break;
            }
        }

        Ci6110Application.writeFile();

        return "redirect:/";
    }

    @GetMapping("/findOne")
    @ResponseBody
    public Optional<Employee> getEmployee(Integer id) {

        return StudentsRepo.findById(id);

    }

//iteration 1 is below. Can be accessed by localhost:8080/addemployee
    List<Employee> oneemployees = new ArrayList<>();

    @GetMapping("/addemployee")
    public String greetingForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "addemployee";
    }

    @PostMapping("/result")
    public String addEmployee(Model model, @ModelAttribute Employee e) {
        String student_score = String.format("%.2f", e.getScore());

        oneemployees.add(e);
        float sum = 0;
        for (Employee newe : oneemployees) {
            sum += newe.getScore();
        }
        float average = sum / oneemployees.size();
        String average1 = String.format("%.1f", average);

        Employee element = Collections.max(oneemployees, Comparator.comparingDouble(Employee::getScore));
        float max = element.getScore();
        String max1 = String.format("%.1f", max);

        model.addAttribute("empsal", student_score);
        model.addAttribute("avg", average1);
        model.addAttribute("max", max1);

        return "result";
    }

}
