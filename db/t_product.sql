BEGIN;

CREATE TABLE IF NOT EXISTS public.t_product (
	pid bigint DEFAULT nextval('t_product_seq'::regclass) NOT NULL,
	chno integer,
	url text NOT NULL,
	title text NOT NULL,
	price integer,
	base text,
	std_price integer,
	PRIMARY KEY(pid)
);

COMMIT;
