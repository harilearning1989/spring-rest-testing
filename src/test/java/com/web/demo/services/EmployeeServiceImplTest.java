package com.web.demo.services;

import com.web.demo.dtos.EmployeeDTO;
import com.web.demo.exceptions.EmployeeNotFoundException;
import com.web.demo.models.Employee;
import com.web.demo.repos.EmployeeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepo employeeRepo;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1);
        employee.setEmpId(101);
        employee.setEmpName("Hari Duddukunta");
        employee.setDesignation("Software Engineer");
    }


    @Test
    void testFindAll() {
        when(employeeRepo.findAll()).thenReturn(Collections.singletonList(employee));
        List<Employee> employees = employeeService.findAll();
        assertNotNull(employees);
        assertEquals(1, employees.size());
    }

    @Test
    void testFindAllByEmpId() {
        when(employeeRepo.findAllByEmpId(101)).thenReturn(Optional.of(employee));
        Optional<Employee> found = employeeService.findByEmpId(101);
        assertTrue(found.isPresent());
        assertEquals("Hari Duddukunta", found.get().getEmpName());
    }

    @Test
    void testCreateEmployee() {
        when(employeeRepo.save(employee)).thenReturn(employee);
        Employee created = employeeService.createEmployee(employee);
        assertNotNull(created);
        assertEquals("Hari Duddukunta", created.getEmpName());
    }

    @Test
    void testFindById() {
        when(employeeRepo.findByEmpId(101)).thenReturn(Optional.of(employee));
        Optional<Employee> found = employeeService.findById(101);
        assertTrue(found.isPresent());
        assertEquals(101, found.get().getEmpId());
    }

    @Test
    void testDeleteById() {
        doNothing().when(employeeRepo).deleteById(101);
        employeeService.deleteById(101);
        verify(employeeRepo, times(1)).deleteById(101);
    }

    @Test
    void testDeleteAll() {
        doNothing().when(employeeRepo).deleteAll();
        employeeService.deleteAll();
        verify(employeeRepo, times(1)).deleteAll();
    }

    @Test
    void testUpdateEmp() {
        Employee updatedEmployee = new Employee();
        updatedEmployee.setEmpName("Hari Duddukunta");

        when(employeeRepo.findByEmpId(101)).thenReturn(Optional.of(employee));
        when(employeeRepo.save(any(Employee.class))).thenReturn(updatedEmployee);

        Employee result = employeeService.updateEmp(101, updatedEmployee);
        assertNotNull(result);
        assertEquals("Hari Duddukunta", result.getEmpName());
    }

    @Test
    void testFindAllUnderManager() {
        employee.setManager_id(200);
        when(employeeRepo.findAll()).thenReturn(Collections.singletonList(employee));
        List<EmployeeDTO> employees = employeeService.findAllUnderManager(200);
        assertNotNull(employees);
        assertEquals(1, employees.size());
    }

    @Test
    void testHelloWorld() {
        assertEquals("Hello World", employeeService.helloWorld());
    }

    @Test
    void testUpdateFindAll() {
        when(employeeRepo.findAll()).thenReturn(getMockEmployees());
        List<EmployeeDTO> employeeDTOS = employeeService.updateFindAll();

        assertNotNull(employeeDTOS);
        assertEquals(3, employeeDTOS.size());

        // Check the designation assignment
        assertNotNull(employeeDTOS.get(1).getDesignation());
    }

    @Test
    void testGetEmployeeById_EmployeeExists() {
        int empId = 101;
        when(employeeRepo.findByEmpId(empId)).thenReturn(Optional.of(employee));

        // Act
        Employee result = employeeService.getEmployeeById(empId);

        // Assert
        assertNotNull(result);
        assertEquals(empId, result.getEmpId());
        assertEquals("Hari Duddukunta", result.getEmpName());
        assertEquals("Software Engineer", result.getDesignation());

        // Verify repository was called once
        verify(employeeRepo, times(1)).findByEmpId(empId);
    }

   @Test
    void testGetEmployeeById_EmployeeNotFound() {
       int empId = 101;
       when(employeeRepo.findByEmpId(empId)).thenReturn(Optional.empty());

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeById(empId));
        assertEquals("Employee not found with id: " + empId, exception.getMessage());
        verify(employeeRepo, times(1)).findByEmpId(empId);
    }

    private List<Employee> getMockEmployees() {
        List<Employee> employeeList = new ArrayList<>();
        Employee employee = new Employee();
        employee.setId(12);
        employee.setEmpId(112);
        employee.setEmpName("Hari Duddukunta");
        employee.setManager_id(10);
        employeeList.add(employee);

        employee = new Employee();
        employee.setId(13);
        employee.setEmpId(113);
        employee.setEmpName("Deekshith Duddukunta");
        employee.setManager_id(8);
        employeeList.add(employee);

        employee = new Employee();
        employee.setId(14);
        //employee.setEmpId(114);
        employee.setEmpName("Rohan Duddukunta");
        employeeList.add(employee);

        return employeeList;
    }
}

