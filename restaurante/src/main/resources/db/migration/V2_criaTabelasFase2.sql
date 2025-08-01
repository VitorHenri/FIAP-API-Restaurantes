create table tipo_usuario (
    id serial primary key,
    nome varchar(100) not null
);

alter table usuario
add column tipo_usuario_id int,
add foreign key (tipo_usuario_id) references tipo_usuario(id);

create table restaurante (
    id serial primary key,
    nome varchar(150) not null,
    tipo_cozinha varchar(100),
    horario_funcionamento varchar(100),
    usuario_id int not null,
    endereco_id int,
    foreign key (usuario_id) references usuario(id),
    foreign key (endereco_id) references endereco(id)
);

create table item_cardapio (
    id serial primary key,
    nome varchar(150) not null,
    descricao text,
    preco numeric(10,2) not null,
    apenas_local boolean default false,
    caminho_foto varchar(255),
    restaurante_id int not null,
    foreign key (restaurante_id) references restaurante(id) on delete cascade
);
