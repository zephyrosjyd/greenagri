BEGIN;

CREATE TABLE IF NOT EXISTS public.t_path_weight (
	sampleid bigint DEFAULT nextval('t_path_weight_sampleid_seq'::regclass) NOT NULL,
	chno integer,
	wt double precision,
	PRIMARY KEY(sampleid)
);

COMMIT;

insert into t_path_weight (chno, wt) values (1, 0.388);
insert into t_path_weight (chno, wt) values (2, 0.218);
insert into t_path_weight (chno, wt) values (3, 0.519);
