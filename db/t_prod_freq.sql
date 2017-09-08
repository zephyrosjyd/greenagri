BEGIN;

CREATE TABLE IF NOT EXISTS public.t_prod_freq (
	sampleid bigint DEFAULT nextval('t_prod_freq_sampleid_seq'::regclass) NOT NULL,
	bid bigint,
	pid bigint,
	freq integer,
	PRIMARY KEY(sampleid)
);

COMMIT;
