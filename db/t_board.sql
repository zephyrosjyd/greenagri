BEGIN;

CREATE TABLE IF NOT EXISTS public.t_board (
	bid bigint DEFAULT nextval('t_board_seq'::regclass) NOT NULL,
	chno integer,
	url text NOT NULL,
	postno character varying(32),
	wdate timestamp without time zone,
	contents text NOT NULL,
	emotion double precision,
	similarity double precision,
	PRIMARY KEY(bid)
);

COMMIT;
