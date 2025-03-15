package com.web.demo.services;

import com.web.demo.dtos.EmployeeDTO;
import com.web.demo.exceptions.EmployeeNotFoundException;
import com.web.demo.models.Employee;
import com.web.demo.repos.EmployeeRepo;
import com.web.demo.utils.EmployeeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Method;
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

    private List<Employee> mockEmployees;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1);
        employee.setEmpId(101);
        employee.setEmpName("Hari Duddukunta");
        employee.setDesignation("Software Engineer");

        // Creating a mock list of employees
        mockEmployees = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            Employee emp = new Employee();
            emp.setId(i);
            emp.setEmpId(100+i);
            emp.setEmpName("Hari "+i);
            emp.setDesignation("Software Engineer "+i);
            emp.setSalary(39500+i);
            mockEmployees.add(emp);
        }
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

    @Test
    void testGetTop1000Employees() {
        // Mocking repository call
        Pageable pageable = PageRequest.of(0, 1000);
        Page<Employee> mockPage = new PageImpl<>(mockEmployees);
        when(employeeRepo.findAll(pageable)).thenReturn(mockPage);
        // Calling the service method
        List<Employee> result = employeeService.getTop1000Employees();
        // Assertions
        assertNotNull(result);
        assertEquals(500, result.size());
        verify(employeeRepo, times(1)).findAll(pageable);
    }

    @Test
    void testSaveEmployee_InvalidName() {
        Employee invalidEmployee = new Employee();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.createEmployee(invalidEmployee);
        });

        assertEquals("Employee name cannot be empty", exception.getMessage());
        verify(employeeRepo, never()).save(any());
    }


    @Test
    void testValidateEmployee_UsingReflection() throws Exception {
        EmployeeServiceImpl service = new EmployeeServiceImpl(employeeRepo);
        Method method = EmployeeServiceImpl.class.getDeclaredMethod("validateEmployee", Employee.class);
        method.setAccessible(true);

        Employee validEmployee = new Employee();
        validEmployee.setEmpName("Hari");

        assertDoesNotThrow(() -> method.invoke(service, validEmployee));

        Employee invalidEmployee = new Employee();
        Exception exception = assertThrows(Exception.class, () -> method.invoke(service, invalidEmployee));

        assertInstanceOf(IllegalArgumentException.class, exception.getCause());
        assertEquals("Employee name cannot be empty", exception.getCause().getMessage());
    }

    @Test
    void testSaveEmployee_InvalidName_ShouldThrowException() {
        // Given an invalid employee with an empty name
        Employee invalidEmployee = new Employee();
        // When & Then: Expect IllegalArgumentException
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.createEmployee(invalidEmployee);
        });
        // Assert the exception message
        assertEquals("Employee name cannot be empty", exception.getMessage());
        // Verify that save() was never called due to validation failure
        verify(employeeRepo, never()).save(any());
    }

    @Test
    void testGetEmployeeLevel() {
        assertEquals("Senior", EmployeeUtils.getEmployeeLevel(5));
        assertEquals("Junior", EmployeeUtils.getEmployeeLevel(3));
    }

    @Test
    void testCalculateTax_Mocked() {
        try (MockedStatic<EmployeeUtils> mockedStatic = Mockito.mockStatic(EmployeeUtils.class)) {
            mockedStatic.when(() -> EmployeeUtils.calculateTax(50000)).thenReturn(10000.0);

            double tax = EmployeeUtils.calculateTax(50000);
            assertEquals(10000.0, tax);

            // Verify static method call
            mockedStatic.verify(() -> EmployeeUtils.calculateTax(50000), times(1));
        }
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

