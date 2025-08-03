create table tipo_usuario (
    id serial primary key,
    nome varchar(100) not null
);

alter table usuario
add column tipo_usuario_id int,
add foreign key (tipo_usuario_id) references tipo_usuario(id);

CREATE TABLE restaurante (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    tipo_cozinha VARCHAR(100),
    horario_funcionamento VARCHAR(100),
    usuario_id INT NOT NULL,
    endereco_id INT,

    CONSTRAINT fk_restaurante_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_restaurante_endereco
        FOREIGN KEY (endereco_id)
        REFERENCES endereco(id)
        ON DELETE SET NULL
);

CREATE TABLE item_cardapio (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(1000),
    preco NUMERIC(10, 2) NOT NULL,
    disponivel_somente_no_local BOOLEAN NOT NULL,
    caminho_foto VARCHAR(500),
    restaurante_id BIGINT NOT NULL,
    CONSTRAINT fk_item_restaurante FOREIGN KEY (restaurante_id) REFERENCES restaurante(id)
);


