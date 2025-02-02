CREATE TYPE user_role AS ENUM ('Admin', 'User');
CREATE TYPE seat_section AS ENUM ('A', 'B');
CREATE TYPE gender AS ENUM ('Male', 'Female');

CREATE TABLE users (
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(255) UNIQUE NOT null,
    password VARCHAR(100) NOT NULL,
    gender gender,
    age INT,
    role user_role NOT NULL
);

CREATE TABLE train (
    train_id SERIAL PRIMARY KEY,
    train_number VARCHAR(10) NOT NULL,
    train_name VARCHAR(100),
    price DECIMAL(10,2) NOT NULL DEFAULT 20 CHECK (price >= 0),
    from_location VARCHAR(100) NOT NULL DEFAULT 'London',
    to_location VARCHAR(100) NOT NULL DEFAULT 'France'
);

CREATE TABLE seat (
    seat_id SERIAL PRIMARY KEY,
    train_id INT,
    section seat_section NOT NULL,
    seat_number VARCHAR(3) NOT NULL,
    availability_status BOOLEAN,
    version INTEGER DEFAULT 0,
    FOREIGN KEY (train_id) REFERENCES train(train_id),
    CONSTRAINT unique_train_seat UNIQUE (train_id, seat_number)
);

CREATE TABLE booking (
    booking_id SERIAL PRIMARY KEY,
    user_id UUID,
    train_id INT,
    booking_status VARCHAR(50) NOT NULL,
    booking_date DATE NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL CHECK (total_amount >= 0),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (train_id) REFERENCES train(train_id)
);

CREATE TABLE booking_details (
    booking_details_id SERIAL PRIMARY KEY,
    booking_id INT,
    seat_id INT,
    passenger_name VARCHAR(100) NOT NULL,
    passenger_age INT,
    passenger_gender gender,
    FOREIGN KEY (booking_id) REFERENCES booking(booking_id),
    FOREIGN KEY (seat_id) REFERENCES seat(seat_id),
    CONSTRAINT unique_booking_seat UNIQUE (booking_id, seat_id)
);

CREATE INDEX idx_booking_user_id ON booking (user_id);
CREATE INDEX idx_booking_details_booking_id ON booking_details (booking_id);
CREATE INDEX idx_booking_details_seat_id ON booking_details (seat_id);
CREATE INDEX idx_seat_section ON seat (section);
CREATE INDEX idx_seat_train_id_section ON seat (train_id, section);

INSERT INTO users (first_name, last_name, email, password, gender, age, role)
VALUES
    ('Mark', 'Waugh', 'mark.waugh@example.com', '5aa00315dfe5546f872c42b9e0ecb767', 'Male', 24, 'User'),
    ('Steve', 'Waugh', 'steve.waugh@example.com', '5aa00315dfe5546f872c42b9e0ecb767', 'Male', 24, 'User'),
    ('Matthew', 'Hayden', 'matthew.hayden@example.com', '5aa00315dfe5546f872c42b9e0ecb767', 'Male', 24, 'Admin');

INSERT INTO train (train_number, train_name, price)
VALUES
    ('T1001', 'EuroStar Express', 20.00),
    ('T1002', 'London-France Express', 20.00),
    ('T1003', 'French Flyer', 20.00),
    ('T1004', 'Royal Blue', 20.00),
    ('T1005', 'London Nightline', 20.00);

DO $$
DECLARE
    train_rec RECORD;
    seat_num INT;
BEGIN
    FOR train_rec IN (SELECT train_id FROM train) LOOP
        -- Insert 50 seats in Section A
        FOR seat_num IN 1..50 LOOP
            INSERT INTO seat (train_id, section, seat_number)
            VALUES (train_rec.train_id, 'A', 'A' || LPAD(seat_num::TEXT, 2, '0'));
        END LOOP;

        -- Insert 50 seats in Section B
        FOR seat_num IN 1..50 LOOP
            INSERT INTO seat (train_id, section, seat_number)
            VALUES (train_rec.train_id, 'B', 'B' || LPAD(seat_num::TEXT, 2, '0'));
        END LOOP;
    END LOOP;
END $$ LANGUAGE plpgsql;
