INSERT INTO program (name, description)
VALUES ('Computer Science', 'Description for Computer Science'),
       ('Data Science', 'Description for Data Science'),
       ('Information Systems', 'Description for Information Systems'),
       ('Information Technology', 'Description for Information Technology'),
       ('Information and Communications Technology', 'Description for Information and Communications Technology'),
       ('Software Engineering', 'Study of software design and development'),
       ('Cybersecurity', 'Study of protecting systems, networks, and programs from digital attacks')
ON CONFLICT (name) DO NOTHING;

INSERT INTO level (name, dues_amount)
VALUES ('100 Level', 5000.00),
       ('200 Level - Fresher(DE)', 5000.00),
       ('200 Level', 4000.00),
       ('300 Level', 4000.00),
       ('400 Level', 4000.00)
ON CONFLICT (name) DO NOTHING;