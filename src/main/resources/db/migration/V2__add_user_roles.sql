-- Creating the 'user_roles' table to represent the many-to-many relationship between users and roles
CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role_id BIGINT NOT NULL,
    -- Assuming you have a Role table with BIGINT as the primary key
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);
-- Adding a trigger to set the created_at and updated_at fields
-- (If needed, PostgreSQL can handle this using default values or triggers)
-- Example: CREATE TRIGGER set_created_at BEFORE INSERT ON users FOR EACH ROW EXECUTE FUNCTION now();