package dk.kino.kino.controller;

import dk.kino.kino.model.Employee;
import dk.kino.kino.repository.EmployeeRepository;
import dk.kino.kino.service.EmployeeService;
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
    EmployeeService employeeService;

    @GetMapping
    List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @PostMapping("/addEmployee")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee){

        System.out.println("Employee created");

        Employee createdEmployee = employeeService.addEmployee(employee);

        return ResponseEntity.ok(createdEmployee);
    }

    @PutMapping("/editEmployee/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Employee> editEmployee(@PathVariable Long id, @RequestBody Employee employee){

        System.out.println("Employee updated");

        Employee updatedEmployee = employeeService.editEmployee(id, employee);

        return ResponseEntity.ok(updatedEmployee);

    }

    @DeleteMapping("deleteEmployee/{id}")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id){

        employeeService.deleteEmployee(id);

        return ResponseEntity.ok("Employee is deleted");

    }




}
