CREATE TABLE nexd_collection (
  id INTEGER NOT NULL PRIMARY KEY,
  pid INTEGER,
  type SMALLINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  CONSTRAINT parentId FOREIGN KEY (pid) REFERENCES collection (id) ON DELETE CASCADE
);

INSERT INTO nexd_collection (id, pid, type, name) VALUES (0, NULL, 0, 'db');
INSERT INTO nexd_collection (id, pid, type, name) VALUES (1, 0, 0, 'docs');
INSERT INTO nexd_collection (id, pid, type, name) VALUES (2, 1, 0, 'foo');
INSERT INTO nexd_collection (id, pid, type, name) VALUES (3, 1, 0, 'articles');

CREATE TABLE nexd_resource (
  id INTEGER NOT NULL PRIMARY KEY,
  cid INTEGER NOT NULL,
  type SMALLINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  FOREIGN KEY (cid) REFERENCES nexd_collection (id) ON DELETE CASCADE,
  UNIQUE (cid, name)
);

CREATE TABLE nexd_sequence (
  name VARCHAR(255) NOT NULL PRIMARY KEY,
  id INTEGER NOT NULL
);

INSERT INTO nexd_sequence (name, id) VALUES ('nexd_collection', 3);
INSERT INTO nexd_sequence (name, id) VALUES ('nexd_resource', 0);
INSERT INTO nexd_sequence (name, id) VALUES ('nexd_dom_node', 0);

CREATE TABLE nexd_schema (
  cid INTEGER NOT NULL PRIMARY KEY,
  type TINYINT NOT NULL,
  schema VARCHAR NOT NULL,
  FOREIGN KEY (cid) REFERENCES nexd_collection (id) ON DELETE CASCADE
);

CREATE TABLE nexd_dom_node (
  id INTEGER NOT NULL PRIMARY KEY,
  lft INTEGER NOT NULL,
  rgt INTEGER NOT NULL,
  type SMALLINT NOT NULL,
  rid INTEGER NOT NULL,
  UNIQUE (id, lft, rgt),
  FOREIGN KEY (rid) REFERENCES nexd_resource (id) ON DELETE CASCADE
);

CREATE TABLE nexd_dom_document (
  nid INTEGER NOT NULL PRIMARY KEY,
  version VARCHAR(4) NOT NULL,
  encoding VARCHAR(20),
  standalone BOOLEAN,
  FOREIGN KEY (nid) REFERENCES nexd_dom_node (id) ON DELETE CASCADE
);
CREATE TABLE nexd_dom_element (
  nid INTEGER NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  FOREIGN KEY (nid) REFERENCES nexd_dom_node (id) ON DELETE CASCADE
);
CREATE TABLE nexd_dom_entityref (
  nid INTEGER NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  FOREIGN KEY (nid) REFERENCES nexd_dom_node (id) ON DELETE CASCADE
);
CREATE TABLE nexd_dom_pi (
  nid INTEGER NOT NULL PRIMARY KEY,
  target VARCHAR(255) NOT NULL,
  data VARCHAR,
  FOREIGN KEY (nid) REFERENCES nexd_dom_node (id) ON DELETE CASCADE
);

CREATE TABLE nexd_dom_attr (
  nid INTEGER NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  value VARCHAR,
  FOREIGN KEY (nid) REFERENCES nexd_dom_node (id) ON DELETE CASCADE
);

CREATE TABLE nexd_dom_text (
  nid INTEGER NOT NULL PRIMARY KEY,
  value VARCHAR,
  FOREIGN KEY (nid) REFERENCES nexd_dom_node (id) ON DELETE CASCADE
);

CREATE TABLE nexd_dom_doctype (
  nid INTEGER NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  publicId VARCHAR,
  systemId VARCHAR,
  subset VARCHAR,
  FOREIGN KEY (nid) REFERENCES nexd_dom_node (id) ON DELETE CASCADE
);
COMMIT;