CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    role ENUM('user', 'shelter_employee', 'admin') DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE shelters (
    shelter_id INT AUTO_INCREMENT PRIMARY KEY,
    shelter_name VARCHAR(255) NOT NULL,
    shelter_address VARCHAR(255) NOT NULL,
    contact_info VARCHAR(255),
    capacity INT
);

CREATE TABLE pets (
    pet_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,  -- e.g., Dog, Cat, etc.
    age INT,
    description TEXT,
    status ENUM('available', 'adopted') DEFAULT 'available',
    shelter_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (shelter_id) REFERENCES shelters(shelter_id)
);

CREATE TABLE adoptions (
    adoption_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    pet_id INT,
    adoption_date DATE,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (pet_id) REFERENCES pets(pet_id)
);


