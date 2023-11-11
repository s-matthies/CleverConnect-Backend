package com.example.api.student;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentController {

    private final StudentRepository repository;

    StudentController(StudentRepository repository){
        this.repository = repository;
    }

    @GetMapping("/students")
    List<Student> all() {
        return repository.findAll();
    }

    @PostMapping("/students")
    Student newStudent(@RequestBody Student newStudent){
        return repository.save(newStudent);
    }

    // Single item

    @GetMapping("/students/{id}")
    Student one(@PathVariable Long id){
        return repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

   @PutMapping("/students/{id}")
    Student replaceStudent(@RequestBody Student newStudent, @PathVariable Long id) {
        return repository.findById(id).map(student -> {
                    student.setFirstName(newStudent.getFirstName());
                    student.setLastName(newStudent.getLastName());
                    student.setMail(newStudent.getMail());
                    return repository.save(student);
                })
                .orElseGet(() -> {
                    newStudent.setId(id);
                    return repository.save(newStudent);
        });
    }

    /*
    @PutMapping("/students/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
        Student existingStudent = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Update the existing person with the new data
        existingStudent.setFirstName(updatedStudent.getFirstName());
        existingStudent.setLastName(updatedStudent.getLastName());
        existingStudent.setMail(updatedStudent.getMail());
        return repository.save(existingStudent);
    } */

    @DeleteMapping("/students/{id}")
    void deleteStudent(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
