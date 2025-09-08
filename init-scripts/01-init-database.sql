-- Academic Management Service Database Initialization Script
-- This script creates the necessary tables for the academic management system

-- Create grades table
CREATE TABLE IF NOT EXISTS grades (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    level INTEGER NOT NULL UNIQUE CHECK (level > 0),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create student_grades table
CREATE TABLE IF NOT EXISTS student_grades (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    grade_id BIGINT NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_student_grades_grade_id FOREIGN KEY (grade_id) REFERENCES grades(id),
    CONSTRAINT chk_student_grades_end_date CHECK (end_date IS NULL OR end_date >= start_date)
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_grades_name ON grades(name);
CREATE INDEX IF NOT EXISTS idx_grades_level ON grades(level);
CREATE INDEX IF NOT EXISTS idx_grades_is_active ON grades(is_active);

CREATE INDEX IF NOT EXISTS idx_student_grades_student_id ON student_grades(student_id);
CREATE INDEX IF NOT EXISTS idx_student_grades_grade_id ON student_grades(grade_id);
CREATE INDEX IF NOT EXISTS idx_student_grades_is_active ON student_grades(is_active);
CREATE INDEX IF NOT EXISTS idx_student_grades_start_date ON student_grades(start_date);
CREATE INDEX IF NOT EXISTS idx_student_grades_end_date ON student_grades(end_date);

-- Insert initial grade data
INSERT INTO grades (name, description, level) VALUES
('PRESCHOOL', 'Preschool education for children aged 3-5', 0),
('KINDERGARTEN', 'Kindergarten for children aged 5-6', 1),
('FIRST', 'First grade - Elementary school', 2),
('SECOND', 'Second grade - Elementary school', 3),
('THIRD', 'Third grade - Elementary school', 4),
('FOURTH', 'Fourth grade - Elementary school', 5),
('FIFTH', 'Fifth grade - Elementary school', 6),
('SIXTH', 'Sixth grade - Elementary school', 7),
('SEVENTH', 'Seventh grade - Middle school', 8),
('EIGHTH', 'Eighth grade - Middle school', 9),
('NINTH', 'Ninth grade - High school', 10),
('TENTH', 'Tenth grade - High school', 11),
('ELEVENTH', 'Eleventh grade - High school', 12),
('TWELFTH', 'Twelfth grade - High school', 13)
ON CONFLICT (name) DO NOTHING;

-- Create function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers to automatically update updated_at
CREATE TRIGGER update_grades_updated_at 
    BEFORE UPDATE ON grades 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_student_grades_updated_at 
    BEFORE UPDATE ON student_grades 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Grant permissions
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO postgres;
