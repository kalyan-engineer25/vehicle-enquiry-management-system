CREATE DATABASE IF NOT EXISTS vehicle_db;
USE vehicle_db;

CREATE TABLE Vehicle (
    vehicle_id INT AUTO_INCREMENT PRIMARY KEY,
    brand      VARCHAR(50)  NOT NULL,
    model      VARCHAR(50)  NOT NULL,
    year       INT          NOT NULL,
    price      DECIMAL(10,2) NOT NULL,
    km_driven  INT,
    fuel_type  VARCHAR(20),
    status     VARCHAR(20) DEFAULT 'Available'
);

CREATE TABLE Enquiry (
    enquiry_id    INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id    INT NOT NULL,
    customer_name VARCHAR(60) NOT NULL,
    phone         VARCHAR(15) NOT NULL,
    message       VARCHAR(200),
    enquiry_date  DATE DEFAULT (CURRENT_DATE),
    status        VARCHAR(20) DEFAULT 'Pending',
    FOREIGN KEY (vehicle_id) REFERENCES Vehicle(vehicle_id) ON DELETE CASCADE
);
