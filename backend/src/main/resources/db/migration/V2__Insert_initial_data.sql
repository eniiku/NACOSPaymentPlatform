INSERT INTO program (key, name, description)
VALUES ('CSC', 'Computer Science', 'Description for Computer Science'),
       ('DSC', 'Data Science', 'Description for Data Science'),
       ('IFS', 'Information Systems', 'Description for Information Systems'),
       ('IFT', 'Information Technology', 'Description for Information Technology'),
       ('ICT', 'Information and Communications Technology', 'Description for Information and Communications Technology'),
       ('SWE', 'Software Engineering', 'Study of software design and development'),
       ('CYS', 'Cybersecurity', 'Study of protecting systems, networks, and programs from digital attacks')
    ON CONFLICT (name) DO NOTHING;

INSERT INTO level (key, name, dues_amount)
VALUES ('L100', '100 Level', 5000.00),
       ('L200F', '200 Level - Fresher(DE)', 5000.00),
       ('L200', '200 Level', 3500.00),
       ('L300', '300 Level', 3500.00),
       ('L400', '400 Level', 3500.00);
ON CONFLICT (name) DO NOTHING;