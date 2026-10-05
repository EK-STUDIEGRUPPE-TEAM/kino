package dk.kino.kino.controller;

import dk.kino.kino.model.Employee;
import dk.kino.kino.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @PostMapping("/addEmployee")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee){

        System.out.println("Employee created");

        Employee createdEmployee = employeeService.addEmployee(employee);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    @PutMapping("/editEmployee/{id}")
    public ResponseEntity<Employee> editEmployee(@PathVariable Long id, @RequestBody Employee employee){

        System.out.println("Employee updated");

        Employee updatedEmployee = employeeService.editEmployee(id, employee);

        return ResponseEntity.ok(updatedEmployee);

    }

    @DeleteMapping("deleteEmployee/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id){

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();

    }


}
