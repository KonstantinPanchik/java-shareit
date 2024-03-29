CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);
CREATE TABLE IF NOT EXISTS requests (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  description VARCHAR(512) NOT NULL,
  requestor BIGINT ,
  created TIMESTAMP WITHOUT TIME ZONE,
  CONSTRAINT pk_requests PRIMARY KEY (id),
  CONSTRAINT FK_requests FOREIGN KEY (requestor) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS items (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(512) NOT NULL,
  available BOOLEAN NOT NULL,
  user_id BIGINT,
  request_id BIGINT,
  CONSTRAINT FK_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT FK_requests_id FOREIGN KEY (request_id) REFERENCES requests(id) ON DELETE SET NULL,
  CONSTRAINT pk_item PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS bookings (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  started TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  ended TIMESTAMP WITHOUT TIME ZONE NOT NULL CHECK (started<ended),
  status VARCHAR(8) NOT NULL,
  item_id BIGINT,
  booker BIGINT,
  CONSTRAINT pk_book PRIMARY KEY (id),
  CONSTRAINT FK_item_id FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
  CONSTRAINT FK_booker FOREIGN KEY (booker) REFERENCES users(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS comments (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  text VARCHAR(512) NOT NULL,
  item_id BIGINT,
  author BIGINT,
  CONSTRAINT pk_comments PRIMARY KEY (id),
  CONSTRAINT FK_item_id_comments FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
  CONSTRAINT FK_author FOREIGN KEY (author) REFERENCES users(id) ON DELETE CASCADE
);