CREATE TABLE app_user_role (
    id long NOT NULL AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE app_user (
    id long NOT NULL AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    email_address VARCHAR(200) NOT NULL,
    password VARBINARY(80) NOT NULL,
    role_id long NOT NULL,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (role_id) REFERENCES app_user_role(id)
);

CREATE TABLE company (
    id long NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE company_fleet (
    id long NOT NULL AUTO_INCREMENT,
    employee_id VARCHAR(100) NOT NULL,
    vehicle_type VARCHAR(100) NOT NULL,
    average_weekly_mileage VARCHAR(100) NOT NULL,
    company_id long NOT NULL,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (company_id) REFERENCES company(id)
);