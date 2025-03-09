package com.web.demo.controls;

import com.web.demo.dtos.EmployeeDTO;
import com.web.demo.models.Employee;
import com.web.demo.services.EmployeeServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeRestControllerTest {

    @InjectMocks
    EmployeeRestController employeeController;

    @Mock
    EmployeeServiceImpl employeeService;

    @Test
    public void helloWorldTest(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(employeeService.helloWorld()).thenReturn("Hello");

        ResponseEntity<String> responseEntity = employeeController.helloWorld();

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);
        assertThat(responseEntity.getBody().toString()).isEqualTo("Hello");
    }

    @Test
    public void findAllNotEmptyTest(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        List<Employee> employeeList = getManagerData();
        when(employeeService.findAll()).thenReturn(employeeList);

        ResponseEntity<List<Employee>> responseEntity = employeeController.findAll();

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);
        assertThat(responseEntity.getBody().size()).isEqualTo(employeeList.size());
    }

    @Test
    public void findAllEmptyTest(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        List<Employee> employeeList = new ArrayList<>();
        when(employeeService.findAll()).thenReturn(employeeList);

        ResponseEntity<List<Employee>> responseEntity = employeeController.findAll();

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    public void findAllThrowExceptionTest(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(employeeService.findAll()).thenReturn(null);

        ResponseEntity<List<Employee>> responseEntity = employeeController.findAll();

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(500);
    }

    @Test
    public void findAllUnderManagerTest(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        List<EmployeeDTO> employeeList = getManagerDataDto();
        when(employeeService.findAllUnderManager(76127)).thenReturn(employeeList);

        ResponseEntity<List<EmployeeDTO>> responseEntity = employeeController.findAllUnderManager(76127);

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);
        assertThat(responseEntity.getBody().size()).isEqualTo(employeeList.size());
    }

    @Test
    public void findAllUnderManagerEmptyTest(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        List<EmployeeDTO> employeeList = new ArrayList<>();
        when(employeeService.findAllUnderManager(76127)).thenReturn(employeeList);

        ResponseEntity<List<EmployeeDTO>> responseEntity = employeeController.findAllUnderManager(76127);

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    public void findAllUnderManagerExceptionTest(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(employeeService.findAllUnderManager(76127)).thenReturn(null);

        ResponseEntity<List<EmployeeDTO>> responseEntity = employeeController.findAllUnderManager(76127);

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(500);
    }

    @Test
    public void getEmpByIdTest(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Employee emp = new Employee();
        emp.setId(12);
        emp.setEmpName("Chandra");
        emp.setManager_id(76127);
        when(employeeService.findByEmpId(76127)).thenReturn(Optional.of(emp));

        ResponseEntity<Employee> responseEntity = employeeController.getEmpById(76127);

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    public void getEmpByIdEmptyTest(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(employeeService.findByEmpId(76127)).thenReturn(Optional.empty());

        ResponseEntity<Employee> responseEntity = employeeController.getEmpById(76127);

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    public void createEmployeeTest(){
        Employee empRequest = new Employee();
        empRequest.setEmpName("Chandra");
        empRequest.setManager_id(76127);

        Employee empResponse = new Employee();
        empResponse.setEmpId(11);
        empResponse.setEmpName("Chandra");
        empResponse.setManager_id(76127);

        when(employeeService.createEmployee(empRequest)).thenReturn(empResponse);
        ResponseEntity<Employee> responseEntity = employeeController.createEmployee(empRequest);
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(201);
    }

    @Test
    public void createEmployeeNullTest(){
        Employee empRequest = new Employee();
        empRequest.setEmpName("Chandra");
        empRequest.setManager_id(76127);
        when(employeeService.createEmployee(empRequest)).thenThrow(new RuntimeException());
        ResponseEntity<Employee> responseEntity = employeeController.createEmployee(empRequest);
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(500);
    }

    @Test
    void testUpdateEmployee_EmployeeFound() {
        // Given
        int empId = 1;
        Employee employee = new Employee();
        employee.setEmpId(1);
        employee.setEmpName("John Doe");
        employee.setDesignation("Developer");
        employee.setSalary(50000);
        Optional<Employee> optionalEmployee = Optional.of(employee);

        when(employeeService.findById(empId)).thenReturn(optionalEmployee);
        when(employeeService.createEmployee(employee)).thenReturn(employee);

        // When
        ResponseEntity<Employee> response = employeeController.updateEmployee(empId);

        // Then
        assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getEmpName());
        verify(employeeService, times(1)).findById(empId);
        verify(employeeService, times(1)).createEmployee(employee);
    }

    @Test
    void testGetEmployeeById_EmployeeFound() {
        // Given
        int empId = 1;
        Employee employee = new Employee();
        employee.setEmpId(empId);
        employee.setEmpName("John Doe");
        employee.setDesignation("Developer");
        employee.setSalary(50000);

        when(employeeService.getEmployeeById(empId)).thenReturn(employee);

        // When
        Employee result = employeeController.getEmployeeById(empId);

        // Then
        assertNotNull(result);
        assertEquals(empId, result.getEmpId());
        assertEquals("John Doe", result.getEmpName());
        verify(employeeService, times(1)).getEmployeeById(empId);
    }

    @Test
    void testGetEmployeeById_EmployeeNotFound() {
        // Given
        int empId = 2;
        when(employeeService.getEmployeeById(empId)).thenThrow(new RuntimeException("Employee not found"));

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> employeeController.getEmployeeById(empId));
        assertEquals("Employee not found", exception.getMessage());
        verify(employeeService, times(1)).getEmployeeById(empId);
    }

    @Test
    void testUpdateEmployee_EmployeeNotFound() {
        // Given
        int empId = 2;
        when(employeeService.findById(empId)).thenReturn(Optional.empty());

        // When
        ResponseEntity<Employee> response = employeeController.updateEmployee(empId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(employeeService, times(1)).findById(empId);
        verify(employeeService, never()).createEmployee(any());
    }

    @Test
    void testUpdateEmp_ValidEmployeeUpdate() {
        // Given
        int empId = 1;
        Employee employee = new Employee();
        employee.setEmpId(empId);
        employee.setEmpName("John Doe");
        employee.setDesignation("Developer");
        employee.setSalary(50000);
        when(employeeService.updateEmp(empId, employee)).thenReturn(employee);

        // When
        ResponseEntity<Employee> response = employeeController.updateEmp(empId, employee);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getEmpName());
        verify(employeeService, times(1)).updateEmp(empId, employee);
    }

    @Test
    void testUpdateEmp_EmployeeNotFound() {
        // Given
        int empId = 2;
        Employee employee = new Employee();
        employee.setEmpId(empId);
        employee.setEmpName("John Doe");
        employee.setDesignation("Developer");
        employee.setSalary(50000);
        when(employeeService.updateEmp(empId, employee)).thenThrow(new RuntimeException("Employee not found"));

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> employeeController.updateEmp(empId, employee));
        assertEquals("Employee not found", exception.getMessage());
        verify(employeeService, times(1)).updateEmp(empId, employee);
    }

    @Test
    void testUpdateEmp_NullRequestBody() {
        // Given
        int empId = 3;
        when(employeeService.updateEmp(empId, null)).thenReturn(null);

        // When
        ResponseEntity<Employee> response = employeeController.updateEmp(empId, null);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(employeeService, times(1)).updateEmp(empId, null);
    }

    @Test
    public void deleteEmployeeTest(){
        doNothing().when(employeeService).deleteById(76127);
        //verify(employeeService,times(1)).deleteById(76127);
        ResponseEntity<HttpStatus> responseEntity = employeeController.deleteEmployee(76127);
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    public void deleteEmployeeExceptionTest(){
        doThrow(new RuntimeException()).when(employeeService).deleteById(76127);
        ResponseEntity<HttpStatus> responseEntity = employeeController.deleteEmployee(76127);
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(500);
    }

    @Test
    public void deleteAllEmployeesTest(){
        doNothing().when(employeeService).deleteAll();
        //verify(employeeService,times(1)).deleteById(76127);
        ResponseEntity<HttpStatus> responseEntity = employeeController.deleteAllEmployees();
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    public void deleteAllEmployeesExceptionTest(){
        doThrow(new RuntimeException()).when(employeeService).deleteAll();
        ResponseEntity<HttpStatus> responseEntity = employeeController.deleteAllEmployees();
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(500);
    }

    @Test
    public void updateFindAllEmptyTest(){
        when(employeeService.updateFindAll()).thenReturn(new ArrayList<>());

        ResponseEntity<List<EmployeeDTO>> responseEntity = employeeController.updateFindAll();
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    public void updateFindAllTest(){
        List<EmployeeDTO> empDto = getManagerDataDto();
        when(employeeService.updateFindAll()).thenReturn(empDto);

        ResponseEntity<List<EmployeeDTO>> responseEntity = employeeController.updateFindAll();
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    public void updateFindAllExceptionTest(){
        when(employeeService.updateFindAll()).thenThrow(new RuntimeException());

        ResponseEntity<List<EmployeeDTO>> responseEntity = employeeController.updateFindAll();
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(500);
    }

    @Test
    public void readJsonTest(){
        List<EmployeeDTO> empDto = getManagerDataDto();
        when(employeeService.readJson()).thenReturn(empDto);

        ResponseEntity<List<EmployeeDTO>> responseEntity = employeeController.readJson();
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    public void readJsonEmptyTest(){
        when(employeeService.readJson()).thenReturn(new ArrayList<>());

        ResponseEntity<List<EmployeeDTO>> responseEntity = employeeController.readJson();
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    public void readJsonExceptionTest(){
        when(employeeService.readJson()).thenThrow(new RuntimeException());

        ResponseEntity<List<EmployeeDTO>> responseEntity = employeeController.readJson();
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(500);
    }


    private List<EmployeeDTO> getManagerDataDto() {
        List<EmployeeDTO> empList = new ArrayList<>();

        EmployeeDTO emp = new EmployeeDTO();
        emp.setId(12);
        emp.setEmpName("Chandra");
        emp.setManager_id(76127);
        empList.add(emp);

        emp = new EmployeeDTO();
        emp.setId(13);
        emp.setEmpName("Pramod");
        emp.setManager_id(76127);
        empList.add(emp);

        emp = new EmployeeDTO();
        emp.setId(14);
        emp.setEmpName("Bablu");
        emp.setManager_id(76128);
        empList.add(emp);

        emp = new EmployeeDTO();
        emp.setId(15);
        emp.setEmpName("Josh");
        emp.setManager_id(76129);
        empList.add(emp);

        emp = new EmployeeDTO();
        emp.setId(15);
        emp.setEmpId(76127);
        emp.setEmpName("Josh");
        emp.setManager_id(76129);
        empList.add(emp);

        return empList;
    }

    /*@Autowired
    private MockMvc mockMvc;

    @Test
    public void helloWorldTest1() throws Exception {
        when(employeeService.helloWorld()).thenReturn("Good Morning... !!");
        MockHttpServletRequestBuilder reqBuilder = MockMvcRequestBuilders.get("/emp/welcome");
        ResultActions perform = mockMvc.perform(reqBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        assertEquals(201, status);
    }*/

    private List<Employee> getManagerData() {
        List<Employee> empList = new ArrayList<>();

        Employee emp = new Employee();
        emp.setId(12);
        emp.setEmpName("Chandra");
        emp.setManager_id(76127);
        empList.add(emp);

        emp = new Employee();
        emp.setId(13);
        emp.setEmpName("Pramod");
        emp.setManager_id(76127);
        empList.add(emp);

        emp = new Employee();
        emp.setId(14);
        emp.setEmpName("Bablu");
        emp.setManager_id(76128);
        empList.add(emp);

        emp = new Employee();
        emp.setId(15);
        emp.setEmpName("Josh");
        emp.setManager_id(76129);
        empList.add(emp);

        emp = new Employee();
        emp.setId(15);
        emp.setEmpId(76127);
        emp.setEmpName("Josh");
        emp.setManager_id(76129);
        empList.add(emp);

        return empList;
    }
}
