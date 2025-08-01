CREATE TABLE Patient (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    cnp CHAR(13),
    email VARCHAR(70),
    phone CHAR(10),
    birthDate DATE,
    isActive BOOLEAN
);

insert into Patient(firstName,lastName,cnp,email,phone,birthDate) values ('A','a','123','a@gmail.com','076','2023-11-01');