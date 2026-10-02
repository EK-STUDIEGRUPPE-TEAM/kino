package dk.kino.kino.service;

import dk.kino.kino.model.Employee;
import dk.kino.kino.model.EmployeeType;
import dk.kino.kino.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;


    @Test
    void getAllEmployees() {
        //Arrange
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();

        List<Employee> employees = List.of(employee1, employee2);

        when(employeeRepository.findAll()).thenReturn(employees);

        //Act
        List<Employee> result = employeeService.getAllEmployees();

        //Assert
        assertEquals(employees, result);

        //Assert
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void addEmployee() {
        //Arrange
        Employee employee = new Employee(1L, "Kasper", EmployeeType.SALES);

        when(employeeRepository.save(employee)).thenReturn(employee);

        //Act
        Employee result = employeeService.addEmployee(employee);

        //Assert
        assertEquals(employee, result);

        verify(employeeRepository, times(1)).save(employee);

    }

    @Test
    void addEmployeeShouldThrowExceptionWhenNameIsEmpty() {

        // Arrange
        Employee employee =
                new Employee(1L, "", EmployeeType.SALES);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> employeeService.addEmployee(employee)
        );

        verify(employeeRepository, never())
                .save(any());
    }

    @Test
    void addEmployeeShouldThrowExceptionWhenNameIsNull() {

        // Arrange
        Employee employee =
                new Employee(1L, null, EmployeeType.SALES);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> employeeService.addEmployee(employee)
        );

        verify(employeeRepository, never())
                .save(any());
    }

    @Test
    void addEmployeeShouldThrowExceptionWhenTypeIsNull() {

        // Arrange
        Employee employee =
                new Employee(1L, "Kasper", null);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> employeeService.addEmployee(employee)
        );

        verify(employeeRepository, never())
                .save(any());
    }

    @Test
    void editEmployee() {
        // Arrange
        Employee existingEmployee =
                new Employee(1L, "Abbas", EmployeeType.SALES);

        Employee updatedData =
                new Employee(null, "Abbas Updated", EmployeeType.MOVIE_OPERATOR);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(existingEmployee));

        when(employeeRepository.save(existingEmployee))
                .thenReturn(existingEmployee);

        // Act
        Employee result =
                employeeService.editEmployee(1L, updatedData);

        // Assert
        assertEquals("Abbas Updated", result.getName());
        assertEquals(EmployeeType.MOVIE_OPERATOR, result.getType());

        verify(employeeRepository, times(1))
                .findById(1L);

        verify(employeeRepository, times(1))
                .save(existingEmployee);
    }

    @Test
    void deleteEmployee() {
        //Arrange
        Employee employee = new Employee(1L, "Kasper", EmployeeType.SALES);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        //Act
        employeeService.deleteEmployee(1L);

        //Assert
        verify(employeeRepository, times(1)).findById(1L);

        verify(employeeRepository, times(1)).delete(employee);
    }

    @Test
    void editEmployeeShouldThrowExceptionIfEmployeeDoesNotExist() {

        // Arrange
        when(employeeRepository.findById(99L))
                .thenReturn(Optional.empty());

        Employee updatedData =
                new Employee(null, "Test", EmployeeType.SALES);

        // Act + Assert
        assertThrows(
                RuntimeException.class,
                () -> employeeService.editEmployee(99L, updatedData)
        );

        verify(employeeRepository, never())
                .save(any());
    }

    @Test
    void deleteEmployeeShouldThrowExceptionIfEmployeeDoesNotExist() {

        // Arrange
        when(employeeRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                RuntimeException.class,
                () -> employeeService.deleteEmployee(99L)
        );

        verify(employeeRepository, never())
                .delete(any());
    }


    //Arrange

    //Act

    //Assert
}