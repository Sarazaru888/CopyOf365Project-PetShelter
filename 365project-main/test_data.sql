INSERT INTO users (username, password, email, role) VALUES 
('JohnDoe', 'password123', 'john.doe@example.com', 'user'),
('JaneSmith', 'password321', 'jane.smith@example.com', 'shelter_employee'),
('AdminUser', 'adminpassword', 'admin@example.com', 'admin');

INSERT INTO shelters (shelter_name, shelter_address, contact_info, capacity) VALUES 
('Happy Tails Shelter', '123 Bark St, Woofville', '123-456-7890', 50),
('Purrfect Companions', '456 Meow Ln, Cat Town', '987-654-3210', 30);

INSERT INTO pets (name, type, age, description, status, shelter_id) VALUES 
('Buddy', 'Dog', 3, 'Friendly and energetic Labrador Retriever.', 'available', 1),
('Whiskers', 'Cat', 2, 'Calm and loving Siamese.', 'available', 2),
('Max', 'Dog', 5, 'Loyal German Shepherd.', 'adopted', 1);

INSERT INTO adoptions (user_id, pet_id, adoption_date) VALUES 
(1, 3, '2023-11-20');

