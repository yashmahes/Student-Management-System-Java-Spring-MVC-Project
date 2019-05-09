package com.ci6110.ci6110;

import com.ci6110.ci6110.entities.Student;
import com.ci6110.ci6110.repositories.StudentRepository;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//public class Ci6110Application implements CommandLineRunner {
public class Ci6110Application {

    @Autowired
    private StudentRepository studentRepository;
    public static ArrayList<Student> students_list = new ArrayList<>();

    public static ArrayList<String> readFile(String filename) {

        ArrayList<String> records = new ArrayList<>();
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    records.add(line);

                }
            }
            return records;
        } catch (IOException e) {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            return null;
        }

    }

    //write to a file
    public static void writeFile() throws FileNotFoundException, UnsupportedEncodingException {

        String filename = "data.txt";
        try ( //write to a file
                PrintWriter writer = new PrintWriter(filename, "UTF-8")) {
            for (Student s : students_list) {
                writer.println(s);
            }

            writer.close();
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(Ci6110Application.class, args);
    }

    // @Override
    //   public void run(String... args) throws Exception {
    @Bean
    public CommandLineRunner demo(StudentRepository repository) {
        ArrayList<String> data = readFile("data.txt");
        return (args) -> {
            for (String item : data) {
                String[] split = item.split(",");
                int id = Integer.parseInt(split[0].trim());
                String fname = split[1].trim();
                String lname = split[2].trim();
                double score = Double.parseDouble(split[3].trim());
                Student stud = new Student(fname, lname, (float) score);
                stud.setId(id);
                studentRepository.save(stud);
                students_list.add(stud);
            }

        };

    }

}
