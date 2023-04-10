CREATE TABLE users
(
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    email    TEXT UNIQUE NOT NULL,
    password TEXT        NOT NULL,
    role     TEXT default 'user' NOT NULL
);

CREATE TABLE routes
(
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    created_by_user INTEGER,
    title           TEXT    NOT NULL,
    description     TEXT,
    distance        INTEGER NOT NULL
        CONSTRAINT chk_positive_distance CHECK ( distance > 0 ),
    duration        INTEGER NOT NULL
        CONSTRAINT chk_positive_duration CHECK ( duration > 0 ),
    elevation       INTEGER NOT NULL
        CONSTRAINT chk_positive_elevation CHECK ( elevation > 0 ),
    creation_date   INTEGER    DEFAULT (strftime('%s', 'now', 'localtime')) NOT NULL,
    skill_level     TEXT       DEFAULT 'facil' NOT NULL
        CONSTRAINT valid_skilllevel CHECK ( skill_level in ('facil', 'media', 'dificil') ),
    blocked         INTEGER(1) DEFAULT 0
        CONSTRAINT is_boolean_value CHECK ( blocked in (0, 1) ),

    FOREIGN KEY (created_by_user) REFERENCES users (id) ON DELETE CASCADE
);

-- Table to store each possible category for a route
CREATE TABLE routecategories
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        TEXT UNIQUE NOT NULL,
    description TEXT
);

-- Insert into route categories' table default categories
INSERT INTO routecategories (name, description)
VALUES ('senderismo', 'La ruta puede ser completada a pie');
INSERT INTO routecategories (name, description)
VALUES ('carrera', 'La ruta puede ser completada corriendo');
INSERT INTO routecategories (name, description)
VALUES ('ciclismo', 'La ruta puede ser completada con cualquier tipo de bicicleta');

-- Maps route categories to routes. Each route can fit into one or multiple categories
CREATE TABLE routetocategoriesmapping
(
    route    INTEGER,
    category INTEGER,

    FOREIGN KEY (route) REFERENCES routes (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (category) REFERENCES routecategories (id) ON UPDATE CASCADE ON DELETE CASCADE,

    PRIMARY KEY (route, category)
);

-- Maps kudos given by an user to a route. Each user can either add or remove 1 kudo of each route, and
-- each route can have multiple kudos
CREATE TABLE routekudosregistry
(
    user            INTEGER,
    route           INTEGER,
    modifier        INTEGER NOT NULL
        CONSTRAINT valid_modifier CHECK ( modifier IN (-1, 1) ),
    submission_date INTEGER DEFAULT (strftime('%s', 'now', 'localtime')) NOT NULL,

    FOREIGN KEY (user) REFERENCES users (id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (route) REFERENCES routes (id) ON UPDATE CASCADE ON DELETE CASCADE,

    PRIMARY KEY (user, route)
);

-- Create an expanded view about routes with info about its categories and kudos balance
CREATE VIEW IF NOT EXISTS routes_expandedinfo
            (id, created_by_user, title, description, distance, duration, elevation, creation_date, skill_level,
             blocked, kudos, categories)
AS
SELECT r.id,
       r.created_by_user,
       r.title,
       r.description,
       r.distance,
       r.duration,
       r.elevation,
       r.creation_date,
       r.skill_level,
       r.blocked,
       (SELECT coalesce(sum(modifier), 0) FROM routekudosregistry WHERE route = r.id),
       group_concat(DISTINCT rc.name)
FROM routes r
         INNER JOIN routetocategoriesmapping rcm ON r.id = rcm.route
         INNER JOIN routecategories rc ON rcm.category = rc.id
GROUP BY r.id
ORDER BY r.id;

--- EXTRA REQUIREMENTS

-- Top routes of the month by kudo ratings submitted within the current month. This view doesn't take into account
-- routes with negative or 0 kudo balance

CREATE VIEW IF NOT EXISTS top_monthly_routes_by_kudos
            (id, created_by_user, title, description, distance, duration, elevation, creation_date, skill_level,
             blocked, kudos, categories)
AS
SELECT r.id,
       r.created_by_user,
       r.title,
       r.description,
       r.distance,
       r.duration,
       r.elevation,
       r.creation_date,
       r.skill_level,
       r.blocked,
       (SELECT coalesce(sum(modifier), 0)
        FROM routekudosregistry
        WHERE route = r.id
          AND date(submission_date, 'unixepoch') BETWEEN date('now', 'localtime', 'start of month') AND date('now', 'localtime')) as kudos,
       group_concat(DISTINCT rc.name)
FROM routes r
         INNER JOIN routetocategoriesmapping rcm ON r.id = rcm.route
         INNER JOIN routecategories rc ON rcm.category = rc.id
         INNER JOIN routekudosregistry rkr on r.id = rkr.route
WHERE kudos > 0
  AND date(rkr.submission_date, 'unixepoch') BETWEEN date('now', 'localtime', 'start of month') AND date('now', 'localtime')
GROUP BY r.id
ORDER BY kudos DESC;

-- Top users authors of top monthly routes ordered by descending number of top routes

CREATE VIEW IF NOT EXISTS top_users_by_top_monthly_routes(username, top_routes) AS
SELECT username,
       (SELECT count(DISTINCT tpr.id)
        FROM top_monthly_routes_by_kudos tpr
        WHERE u.id = tpr.created_by_user) AS top_routes
FROM users u
WHERE id IN (SELECT created_by_user FROM top_monthly_routes_by_kudos)
ORDER BY top_routes DESC;

-- Top users by average kudo ratings on their routes

CREATE VIEW IF NOT EXISTS top_users_by_top_avg_kudos(username, avg_kudos) AS
SELECT u.username, avg(rei.kudos) as avg_kudos
FROM routes_expandedinfo rei
         INNER JOIN users u ON rei.created_by_user = u.id
GROUP BY rei.created_by_user
ORDER BY avg_kudos DESC;