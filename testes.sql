CREATE TABLE teste (
	id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	nome VARCHAR(128) NOT NULL,
	senha VARCHAR(128) NOT NULL,
	email VARCHAR(64) UNIQUE NOT NULL,
	isAdmin BOOLEAN NOT NULL
);

DROP TABLE teste;

SELECT * FROM teste;

SELECT tablename FROM pg_tables WHERE schemaname = 'public';

SELECT * FROM information_schema.columns WHERE table_name = 'teste';

INSERT INTO teste (nome, senha, email, isAdmin) VALUES ('nome qualquer', '12345', 'email@email.com', true); 