package com.example.api.Config;

import com.example.api.Entitys.Student;
import com.example.api.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class StudentConfig {

    // wir verknüpfen mit dem StudentRepository
    @Autowired
    private final StudentRepository studentRepository;

    public StudentConfig(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    //wird beim Start des Programms ausgeführt
    @Bean
    CommandLineRunner commandLineRunner(){
        return args -> {
            Student student1 = new Student(
                    "Anna",
                    "Bertram",
                    "meineMail@mail.de",
                    "12345"
            );

            Student student2 = new Student(
                    "Anna",
                    "Bertram",
                    "meineMail@mail.de",
                    "12345"
            );

            studentRepository.saveAll(
                    List.of(student1, student2)
            );
        };
    }
}
