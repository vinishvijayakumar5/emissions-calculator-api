INSERT INTO app_user_role(name) values('ROLE_ADMIN');

INSERT INTO company(name, type, city, country, created_on)
values('NETFLIX', 'entertainment', 'NJ', 'US', '2023-11-23 01:48:34.20823');

INSERT INTO company_fleet(employee_id, vehicle_type, average_weekly_mileage, company_id, created_on)
values('12345', 'Car', '450.45', 1, '2023-11-23 01:48:34.20823');