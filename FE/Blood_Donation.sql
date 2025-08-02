USE master;
GO

IF EXISTS (SELECT name FROM sys.databases WHERE name = N'Blood_Donation')
  DROP DATABASE Blood_Donation;
GO

CREATE DATABASE Blood_Donation;
GO

USE Blood_Donation;
GO

CREATE TABLE Roles (
  id INT PRIMARY KEY IDENTITY,
  name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE Users (
  id INT PRIMARY KEY IDENTITY,
  username VARCHAR(100) UNIQUE NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255),
  role_id INT,
  enable BIT,
  created_at DATETIME,
  updated_at DATETIME,
  FOREIGN KEY (role_id) REFERENCES Roles(id)
);

CREATE TABLE UserProfile (
  user_id INT PRIMARY KEY,
  first_name VARCHAR(100),
  last_name VARCHAR(100),
  dob DATE,
  gender VARCHAR(10),
  blood_type VARCHAR(3),
  address VARCHAR(255),
  phone VARCHAR(20),
  last_donation_date DATE,
  recovery_time INT,
  location VARCHAR(255),
  latitude FLOAT,
  longitude FLOAT,
  FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE BloodTypes (
  type VARCHAR(3) PRIMARY KEY,
  description VARCHAR(100) NOT NULL
);

CREATE TABLE BloodComponents (
  id INT PRIMARY KEY IDENTITY,
  name VARCHAR(50)
);

CREATE TABLE CompatibilityRules (
  id INT PRIMARY KEY IDENTITY,
  donor_type VARCHAR(3),
  recipient_type VARCHAR(3),
  component_id INT,
  is_compatible BIT,
  FOREIGN KEY (donor_type) REFERENCES BloodTypes(type),
  FOREIGN KEY (recipient_type) REFERENCES BloodTypes(type),
  FOREIGN KEY (component_id) REFERENCES BloodComponents(id)
);

CREATE TABLE DonationRegistrations (
  id INT PRIMARY KEY IDENTITY,
  user_id INT,
  ready_date DATE,
  status VARCHAR(50),
  FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE Donations (
  id INT PRIMARY KEY IDENTITY,
  user_id INT,
  registration_id INT,
  donation_date DATE,
  blood_type VARCHAR(3),
  component_id INT,
  volume_ml INT,
  location VARCHAR(255),
  notes TEXT,
  FOREIGN KEY (user_id) REFERENCES Users(id),
  FOREIGN KEY (registration_id) REFERENCES DonationRegistrations(id),
  FOREIGN KEY (blood_type) REFERENCES BloodTypes(type),
  FOREIGN KEY (component_id) REFERENCES BloodComponents(id)
);

CREATE TABLE BloodUnits (
  id INT PRIMARY KEY IDENTITY,
  blood_type VARCHAR(3),
  component_id INT,
  donation_id INT,
  quantity_ml INT,
  expiration_date DATE,
  status VARCHAR(50),
  stored_at DATETIME,
  FOREIGN KEY (blood_type) REFERENCES BloodTypes(type),
  FOREIGN KEY (component_id) REFERENCES BloodComponents(id),
  FOREIGN KEY (donation_id) REFERENCES Donations(id)
);

CREATE TABLE BloodInventory (
  id INT PRIMARY KEY IDENTITY,
  blood_type VARCHAR(3),
  component_id INT,
  total_quantity_ml INT,
  last_updated DATETIME,
  FOREIGN KEY (blood_type) REFERENCES BloodTypes(type),
  FOREIGN KEY (component_id) REFERENCES BloodComponents(id)
);

CREATE TABLE BloodRequests (
  id INT PRIMARY KEY IDENTITY,
  requester_id INT,
  blood_type VARCHAR(3),
  component_id INT,
  quantity_ml INT,
  urgency_level VARCHAR(20),
  is_urgent BIT DEFAULT 0,
  status VARCHAR(20),
  created_at DATETIME,
  FOREIGN KEY (requester_id) REFERENCES Users(id),
  FOREIGN KEY (component_id) REFERENCES BloodComponents(id)
);

CREATE TABLE Transfusions (
  id INT PRIMARY KEY IDENTITY,
  recipient_id INT,
  request_id INT,
  blood_unit_id INT,
  transfusion_date DATETIME,
  status VARCHAR(20),
  notes TEXT,
  FOREIGN KEY (recipient_id) REFERENCES Users(id),
  FOREIGN KEY (request_id) REFERENCES BloodRequests(id),
  FOREIGN KEY (blood_unit_id) REFERENCES BloodUnits(id)
);

CREATE TABLE Blogs (
  id INT PRIMARY KEY IDENTITY,
  title VARCHAR(255),
  author_id INT,
  content TEXT,
  created_at DATETIME,
  status VARCHAR(20),
  FOREIGN KEY (author_id) REFERENCES Users(id)
);

CREATE TABLE Notifications (
  id INT PRIMARY KEY IDENTITY,
  user_id INT,
  content TEXT,
  sent_at DATETIME,
  [read] BIT,
  FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE VnPayPayments (
  id INT PRIMARY KEY IDENTITY,
  user_id INT,
  amount DECIMAL(10,2),
  payment_time DATETIME,
  transaction_code VARCHAR(100),
  status VARCHAR(20),
  FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- Roles
INSERT INTO Roles (name) VALUES ('Admin'), ('Staff'), ('User');

-- Users
-- Users (dùng bcrypt hash)
INSERT INTO Users (username, email, password, role_id, enable, created_at, updated_at) VALUES
('admin', 'admin@blood.com', '$2b$12$JqrfVuw1OjElUK78h17GaOd9muSPzcsL8KSKp04KwiMnJGC2mZZJu', 1, 1, GETDATE(), GETDATE()),
('staff1', 'staff1@blood.com', '$2b$12$FQkrLN6U7xOV3rd3HSE7puVg4ioYW0fZkweI27gad9zchch1OOh3S', 2, 1, GETDATE(), GETDATE()),
('user1', 'user1@blood.com', '$2b$12$lzbGN.Ywn4lC08ngWfS9Fe3i2df/X1y8ljSq/xGSVRgxw5TZE4JXC', 3, 1, GETDATE(), GETDATE());


-- UserProfile
INSERT INTO UserProfile (user_id, first_name, last_name, dob, gender, blood_type, address, phone, last_donation_date, recovery_time, location, latitude, longitude) VALUES
(3, 'John', 'Smith', '1990-01-01', 'Male', 'O+', 'Hanoi', '0123456789', '2025-04-01', 90, 'Hanoi Hospital', 21.0285, 105.8544);

-- BloodTypes
INSERT INTO BloodTypes (type, description) VALUES 
('O+', 'O Positive'), ('A+', 'A Positive'), ('B+', 'B Positive'), ('AB+', 'AB Positive');

-- BloodComponents
INSERT INTO BloodComponents (name) VALUES ('Plasma'), ('Red Cells'), ('Platelets');

-- CompatibilityRules
INSERT INTO CompatibilityRules (donor_type, recipient_type, component_id, is_compatible) VALUES
('O+', 'O+', 1, 1), ('O+', 'A+', 1, 1), ('A+', 'A+', 2, 1);

-- DonationRegistrations
INSERT INTO DonationRegistrations (user_id, ready_date, status) VALUES
(3, '2025-06-10', 'Approved');

-- Donations
INSERT INTO Donations (user_id, registration_id, donation_date, blood_type, component_id, volume_ml, location, notes) VALUES
(3, 1, '2025-06-11', 'O+', 2, 450, 'Hanoi Hospital', 'First donation');

-- BloodUnits
INSERT INTO BloodUnits (blood_type, component_id, donation_id, quantity_ml, expiration_date, status, stored_at) VALUES
('O+', 2, 1, 450, '2025-09-10', 'Stored', GETDATE());

-- BloodInventory
INSERT INTO BloodInventory (blood_type, component_id, total_quantity_ml, last_updated) VALUES
('O+', 2, 450, GETDATE());

-- BloodRequests
INSERT INTO BloodRequests (requester_id, blood_type, component_id, quantity_ml, urgency_level, status, created_at) VALUES
(3, 'O+', 2, 300, 'High', 'Pending', GETDATE());

-- Transfusions
INSERT INTO Transfusions (recipient_id, request_id, blood_unit_id, transfusion_date, status, notes) VALUES
(3, 1, 1, GETDATE(), 'Completed', 'No complications');

-- Blogs
INSERT INTO Blogs (title, author_id, content, created_at, status) VALUES
('Benefits of Donating Blood', 1, 'Donating blood can save lives...', GETDATE(), 'Published');

-- Notifications
INSERT INTO Notifications (user_id, content, sent_at, [read]) VALUES
(3, 'Thank you for your blood donation!', GETDATE(), 0);

-- VnPayPayments
INSERT INTO VnPayPayments (user_id, amount, payment_time, transaction_code, status) VALUES
(3, 100000, GETDATE(), 'TXN123456', 'Success');
