CREATE SEQUENCE APP_USER_ROLE_SEQ START WITH (select max(id) + 1 from app_user_role);
CREATE SEQUENCE APP_USER_SEQ START WITH (select max(id) + 1 from app_user);
CREATE SEQUENCE COMPANY_SEQ START WITH (select max(id) + 1 from company);
CREATE SEQUENCE COMPANY_FLEET_SEQ START WITH (select max(id) + 1 from company_fleet);