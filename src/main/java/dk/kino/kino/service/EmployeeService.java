package dk.kino.kino.service;

import dk.kino.kino.exception.NotFoundException;
import dk.kino.kino.model.Employee;
import dk.kino.kino.model.EmployeeType;
import dk.kino.kino.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public Employee addEmployee(Employee employee){

        if (employee.getName() == null || employee.getName().isBlank()){
            throw new IllegalArgumentException("Employee must have a name");
        }

        if (employee.getType() == null){
            throw new IllegalArgumentException("Employee must have a type");
        }
        return employeeRepository.save(employee);
    }

    public Employee editEmployee(Long id, Employee newEmployee){

        Employee existingEmployee = employeeRepository.findById(id).
                orElseThrow(() ->
                        new NotFoundException("Employee with id " + id + " not found"));

        existingEmployee.setName(newEmployee.getName());
        existingEmployee.setType(newEmployee.getType());

        return employeeRepository.save(existingEmployee);

    }

    public void deleteEmployee(Long id){

        Employee employee = employeeRepository.findById(id).
                orElseThrow(() ->
                        new NotFoundException("Employee with id " + id + " not found"));

        employeeRepository.delete(employee);
    }
}
