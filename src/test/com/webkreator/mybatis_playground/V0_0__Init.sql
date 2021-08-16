CREATE TABLE books
(
    isbn      TEXT    NOT NULL,

    title     TEXT    NOT NULL,

    author    JSONB   NOT NULL,

    editors   TEXT[]  NOT NULL,

    reviewers JSONB   NOT NULL,

    rating    INTEGER NOT NULL,

    PRIMARY KEY (isbn)
);

CREATE TABLE reviews
(
    review_id INTEGER NOT NULL
        GENERATED ALWAYS AS IDENTITY
            (START WITH 1),

    isbn      TEXT    NOT NULL REFERENCES books (isbn),

    rating    INTEGER NOT NULL,

    PRIMARY KEY (review_id)
);

CREATE TABLE streaming_data
(
    id INTEGER NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO streaming_data
VALUES (generate_series(1, 10000));