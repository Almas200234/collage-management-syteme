CREATE DATABASE college_db;
USE college_db;

CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    department VARCHAR(50),
    enrollment_no VARCHAR(50),
    contact VARCHAR(15)
);
select* from students