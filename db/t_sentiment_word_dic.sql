BEGIN;

CREATE TABLE IF NOT EXISTS public.t_sentiment_word_dic (
	sampleid bigint DEFAULT nextval('t_sentiment_word_dic_sampleid_seq'::regclass) NOT NULL,
	word text NOT NULL,
	sentiment integer NOT NULL,
	PRIMARY KEY(sampleid)
);

COMMIT;

insert into t_sentiment_word_dic (word, sentiment) values ('좋네', 1);
--insert into t_sentiment_word_dic (word, sentiment) values ('맛있어', 1);
insert into t_sentiment_word_dic (word, sentiment) values ('좋아', 1);
insert into t_sentiment_word_dic (word, sentiment) values ('좋은', 1);
insert into t_sentiment_word_dic (word, sentiment) values ('썩은', -1);
insert into t_sentiment_word_dic (word, sentiment) values ('비추', -1);
insert into t_sentiment_word_dic (word, sentiment) values ('맛있', 1);
--insert into t_sentiment_word_dic (word, sentiment) values ('맛있게', 1);
--insert into t_sentiment_word_dic (word, sentiment) values ('맛있습니다', 1);
insert into t_sentiment_word_dic (word, sentiment) values ('실망', -1);
insert into t_sentiment_word_dic (word, sentiment) values ('아쉽', -1);
insert into t_sentiment_word_dic (word, sentiment) values ('좋습', 1);
insert into t_sentiment_word_dic (word, sentiment) values ('강추', 1);
insert into t_sentiment_word_dic (word, sentiment) values ('감사', 1);
insert into t_sentiment_word_dic (word, sentiment) values ('풍부', 1);
insert into t_sentiment_word_dic (word, sentiment) values ('짜증', -1);
insert into t_sentiment_word_dic (word, sentiment) values ('반품', -1);
insert into t_sentiment_word_dic (word, sentiment) values ('않아', -1);
insert into t_sentiment_word_dic (word, sentiment) values ('만족', 1);
insert into t_sentiment_word_dic (word, sentiment) values ('깨끗', 1);
insert into t_sentiment_word_dic (word, sentiment) values ('기대', 1);
--insert into t_sentiment_word_dic (word, sentiment) values ('맛있는', 1);
insert into t_sentiment_word_dic (word, sentiment) values ('좋고', 1);
insert into t_sentiment_word_dic (word, sentiment) values ('질좋은', 1);
insert into t_sentiment_word_dic (word, sentiment) values ('최고', 1);
insert into t_sentiment_word_dic (word, sentiment) values ('못한', -1);
--insert into t_sentiment_word_dic (word, sentiment) values ('실망스런', -1);
--insert into t_sentiment_word_dic (word, sentiment) values ('실망했어요', -1);
--insert into t_sentiment_word_dic (word, sentiment) values ('맛있게', 1);
insert into t_sentiment_word_dic (word, sentiment) values ('난감', -1);
insert into t_sentiment_word_dic (word, sentiment) values ('냄새', -1);
insert into t_sentiment_word_dic (word, sentiment) values ('믿고', 1);
insert into t_sentiment_word_dic (word, sentiment) values ('맛나', 1);
insert into t_sentiment_word_dic (word, sentiment) values ('신선', 1);

