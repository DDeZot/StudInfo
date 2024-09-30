CREATE TABLE IF NOT EXISTS "STUDENTS" (
    id BIGINT IDENTITY PRIMARY KEY,
    name VARCHAR(60) NOT NULL,
    surname VARCHAR(60) NOT NULL,
    patronymic VARCHAR(60),
    birth_date DATE NOT NULL,
    group_id BIGINT,

    FOREIGN KEY (group_id) REFERENCES GROUPS(ID)
                                    ON DELETE NO ACTION
)
