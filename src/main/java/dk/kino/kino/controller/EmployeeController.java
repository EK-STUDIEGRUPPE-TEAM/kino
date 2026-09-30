package dk.kino.kino.controller;

import dk.kino.kino.model.Employee;
import dk.kino.kino.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping
    List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    @PostMapping("/addEmployee")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee){
        System.out.println("Employee created");
        return ResponseEntity.ok(employeeRepository.save(employee));
    }

    @PutMapping("/editEmployee/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Employee> editEmployee(@PathVariable Long id, @RequestBody Employee employee){
        Optional<Employee> orgEmployee = employeeRepository.findById(id);

        if (orgEmployee.isPresent()) {
            employee.setId(id);
            Employee updatedEmployee = employeeRepository.save(employee);
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("deleteEmployee/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id){
        Optional<Employee> orgEmployee = employeeRepository.findById(id);

        if (orgEmployee.isPresent()){
            employeeRepository.deleteById(id);
            return ResponseEntity.ok("Employee is deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }




}
