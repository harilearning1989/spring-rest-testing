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