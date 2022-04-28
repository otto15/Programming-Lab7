package com.otto15.server.db;

public enum DBQuery {
    CREATE_PERSONS_TABLE("CREATE TABLE IF NOT EXISTS public.persons\n"
            + "(\n"
            + "    person_id BIGSERIAL,\n"
            + "    author_id bigint references users(user_id) on delete cascade,\n"
            + "    person_name character varying(1000) COLLATE pg_catalog.\"default\" NOT NULL,\n"
            + "    coordinates_x double precision NOT NULL,\n"
            + "    coordinates_y double precision NOT NULL,\n"
            + "    creation_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,\n"
            + "    height bigint NOT NULL,\n"
            + "    eye_color integer,\n"
            + "    hair_color integer,\n"
            + "    nationality integer NOT NULL,\n"
            + "    person_location integer NOT NULL,\n"
            + "    CONSTRAINT persons_pkey PRIMARY KEY (person_id),\n"
            + "    CONSTRAINT persons_eye_color_fkey FOREIGN KEY (eye_color)\n"
            + "        REFERENCES public.colors (color_id) MATCH SIMPLE\n"
            + "        ON UPDATE NO ACTION\n"
            + "        ON DELETE SET NULL,\n"
            + "    CONSTRAINT persons_hair_color_fkey FOREIGN KEY (hair_color)\n"
            + "        REFERENCES public.colors (color_id) MATCH SIMPLE\n"
            + "        ON UPDATE NO ACTION\n"
            + "        ON DELETE SET NULL,\n"
            + "    CONSTRAINT persons_nationality_fkey FOREIGN KEY (nationality)\n"
            + "        REFERENCES public.countries (country_id) MATCH SIMPLE\n"
            + "        ON UPDATE NO ACTION\n"
            + "        ON DELETE RESTRICT,\n"
            + "    CONSTRAINT persons_person_location_fkey FOREIGN KEY (person_location)\n"
            + "        REFERENCES public.locations (location_id) MATCH SIMPLE\n"
            + "        ON UPDATE NO ACTION\n"
            + "        ON DELETE RESTRICT\n"
            + ")"),
    CREATE_LOCATIONS_TABLE("CREATE TABLE IF NOT EXISTS public.locations\n"
            + "(\n"
            + "    location_id SERIAL,\n"
            + "    location_x double precision NOT NULL,\n"
            + "    location_y bigint NOT NULL,\n"
            + "    location_z real NOT NULL,\n"
            + "    unique(location_x, location_y, location_z),\n"
            + "    CONSTRAINT locations_pkey PRIMARY KEY (location_id)\n"
            + ")"),
    CREATE_COUNTRIES_TABLE("CREATE TABLE IF NOT EXISTS public.countries\n"
            + "(\n"
            + "    country_id SERIAl,\n"
            + "    country_name character varying(20),\n"
            + "    CONSTRAINT countries_pkey PRIMARY KEY (country_id),\n"
            + "    CONSTRAINT countries_country_name_key UNIQUE (country_name)\n"
            + ")"),
    CREATE_COLORS_TABLE("CREATE TABLE IF NOT EXISTS public.colors\n"
            + "(\n"
            + "    color_id SERIAL,\n"
            + "    color_name character varying(20),\n"
            + "    CONSTRAINT colors_pkey PRIMARY KEY (color_id),\n"
            + "    CONSTRAINT colors_color_name_key UNIQUE (color_name)\n"
            + ")"),
    CREATE_USERS_TABLE("CREATE TABLE IF NOT EXISTS public.users\n"
            + "(\n"
            + "    login character varying(200) NOT NULL check(length(login) >= 1),\n"
            + "    password character varying(40) NOT NULL check(length(password) >= 4),\n"
            + "    user_id BIGSERIAL,\n"
            + "    CONSTRAINT users_pkey PRIMARY KEY (user_id),\n"
            + "    CONSTRAINT constraint_name UNIQUE (login)\n"
            + ")"),
    INSERT_CONSTANT_ROW_TO_COLORS("INSERT INTO colors (color_name) VALUES(?) ON CONFLICT DO NOTHING"),
    INSERT_CONSTANT_ROW_TO_COUNTRIES("INSERT INTO countries (country_name) VALUES(?) ON CONFLICT DO NOTHING"),
    INSERT_USER("INSERT INTO users (login, password) values(?, ?) RETURNING user_id"),
    SELECT_USER_BY_LOGIN_AND_PASSWORD("SELECT user_id from users where login = ? and password = ?"),
    INSERT_PERSON("WITH i as (INSERT INTO locations (location_x, location_y, location_z)\n"
            + "   VALUES(?, ?, ?) ON CONFLICT DO NOTHING RETURNING location_id)\n"
            + "   INSERT INTO persons (person_name,    coordinates_x, coordinates_y, height, eye_color, hair_color,"
            + "   nationality, person_location, creation_date, author_id) VALUES(?, ?, ?, ?, (SELECT color_id from colors"
            + "   where color_name = ?),"
            + "   (SELECT color_id from colors where color_name = ?"
            + "   ), (SELECT country_id from countries WHERE country_name = ?),"
            + "   (select location_id from i union all (select location_id from\n"
            + "   locations where location_x = ? and location_y = ? and location_z = ?)), ?, ?)    RETURNING person_id"),
    UPDATE_PERSON("WITH i as (INSERT INTO locations (location_x, location_y, location_z) "
            + "   values(?, ?, ?) ON CONFLICT DO NOTHING RETURNING location_id)   UPDATE persons"
            + "   SET person_name = ?, coordinates_x = ?, coordinates_y = ?, height = ?,"
            + "   eye_color = (SELECT color_id from colors where color_name = ?"
            + "   ),   hair_color = (SELECT color_id from colors "
            + "   where color_name = ?),    "
            + "   nationality = (SELECT country_id from countries where country_name = ?),    "
            + "   person_location = (select location_id from i union all (select location_id from\n"
            + "   locations where location_x = ? and location_y = ? and location_z = ?))    WHERE person_id = ?"),
    SELECT_ALL_PERSONS("select person_id, login, person_name, coordinates_x, coordinates_y, creation_date,\n"
            + "    height, c1.color_name as eye_color, c2.color_name as hair_color, country_name, locations.location_x, locations.location_y, locations.location_z\n"
            + "    from persons\n"
            + "    join users on author_id = users.user_id\n"
            + "    join countries on nationality=countries.country_id\n"
            + "    join locations on location_id=persons.person_location\n"
            + "    left outer join colors as c1 on eye_color=c1.color_id\n"
            + "    left outer join colors as c2 on hair_color=c2.color_id"),
    DELETE_PERSONS_BY_AUTHOR("DELETE from persons where author_id = (select user_id from users where login=?)"),
    DELETE_PERSON_BY_ID("DELETE from persons where person_id = ?");

    private final String query;

    DBQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

}
