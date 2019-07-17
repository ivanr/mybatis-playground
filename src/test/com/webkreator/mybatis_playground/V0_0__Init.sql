CREATE TABLE books
(
  isbn      TEXT   NOT NULL,

  title     TEXT   NOT NULL,

  author    JSONB  NOT NULL,

  editors   TEXT[] NOT NULL,

  reviewers JSONB  NOT NULL,

  PRIMARY KEY (isbn)
);
