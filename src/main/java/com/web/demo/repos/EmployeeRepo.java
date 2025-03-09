package com.web.demo.repos;

import com.web.demo.models.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Integer> {
    Optional<Employee> findAllByEmpId(int empId);

    Optional<Employee> findByEmpId(int empId);

    Page<Employee> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM employees LIMIT 1000", nativeQuery = true)
    List<Employee> findTop1000Employees();

    @Transactional
    @Modifying
    @Query(value="update Employee u set u.emp_name = :empName where u.emp_id = :empId", nativeQuery = true)
    void updateEmpParamNative(@Param("empName") String empName, @Param("empId") Integer empId);

    @Modifying
    @Query("update Employee u set u.empName = :empName, u.fatherName = :fatherName where u.empId = :empId")
    void updateEmployeeParam(@Param("empName") String empName,
                             @Param("fatherName") String fatherName,
                             @Param("empId") Integer empId);

    @Modifying
    @Query("update Employee u set u.empName = ?1, u.fatherName = ?2 where u.empId = ?3")
    void updateEmployee(String empName, String fatherName, Integer empId);
}
