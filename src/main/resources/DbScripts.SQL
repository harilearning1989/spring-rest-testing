CREATE TABLE employee_data(
    id          NUMBER PRIMARY KEY,
    EMP_ID       NUMBER UNIQUE,
    EMP_NAME     VARCHAR2(100),
    FATHER_NAME  VARCHAR2(100),
    gender      VARCHAR2(10),
    category    VARCHAR2(10),
    manager_id  NUMBER,
    designation VARCHAR2(50),
    salary      NUMBER
);

SELECT e1.emp_id emp_id, e1.emp_name emp_name,
       e2.emp_id manager_id, e2.emp_name AS manager_name
FROM   EMPLOYEE_DATA e1, EMPLOYEE_DATA e2
where e1.manager_id  = e2.emp_id order by e2.emp_id FETCH FIRST 10000 ROWS ONLY ;