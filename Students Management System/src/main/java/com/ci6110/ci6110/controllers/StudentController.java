
package com.ci6110.ci6110.controllers;

import com.ci6110.ci6110.Ci6110Application;
import com.ci6110.ci6110.entities.Student;
import com.ci6110.ci6110.repositories.StudentRepository;
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
 * @author khaled
 */
@Controller
public class StudentController {

    @Autowired
    private StudentRepository StudentsRepo;

    @GetMapping("/")

    public String showPage(Model model, Pageable pageable) {
        model.addAttribute("data", StudentsRepo.findAll(pageable));

        List<Student> students = new ArrayList<>();
        StudentsRepo.findAll().forEach(students::add);
        float sum = 0;
        for (Student e : students) {
            sum += e.getScore();
        }
        float average = sum / students.size();
        String average1 = String.format("%.1f", average);
        model.addAttribute("avg", average1);

        Student element = Collections.max(students, Comparator.comparingDouble(Student::getScore));
        float max = element.getScore();
        String max1 = String.format("%.1f", max);
        model.addAttribute("max", max1);

        return "index";
    }

    @PostMapping("/save")
    public String save(Student e) throws FileNotFoundException, UnsupportedEncodingException {
        StudentsRepo.save(e);
        for (Student s : Ci6110Application.students_list) {
            if (Objects.equals(e.getId(), s.getId())) {
                Ci6110Application.students_list.remove(s);
                break;
            }
        }
        Ci6110Application.students_list.add(e);
        Ci6110Application.writeFile();

        return "redirect:/";
    }

    @GetMapping("/delete")
    public String deleteEmployee(Integer id) throws FileNotFoundException, UnsupportedEncodingException {
        StudentsRepo.deleteById(id);

        for (Student e : Ci6110Application.students_list) {
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
    public Optional<Student> getStudent(Integer id) {

        return StudentsRepo.findById(id);

    }

//iteration 1 is below. Can be accessed by localhost:8080/addstudent
    List<Student> one_student = new ArrayList<>();

    @GetMapping("/addstudent")
    public String greetingForm(Model model) {
        model.addAttribute("employee", new Student());
        return "addstudent";
    }

    @PostMapping("/result")
    public String addStudent(Model model, @ModelAttribute Student e) {
        String student_score = String.format("%.2f", e.getScore());

        one_student.add(e);
        float sum = 0;
        for (Student newe : one_student) {
            sum += newe.getScore();
        }
        float average = sum / one_student.size();
        String average1 = String.format("%.1f", average);

        Student element = Collections.max(one_student, Comparator.comparingDouble(Student::getScore));
        float max = element.getScore();
        String max1 = String.format("%.1f", max);

        model.addAttribute("empsal", student_score);
        model.addAttribute("avg", average1);
        model.addAttribute("max", max1);

        return "result";
    }

}
