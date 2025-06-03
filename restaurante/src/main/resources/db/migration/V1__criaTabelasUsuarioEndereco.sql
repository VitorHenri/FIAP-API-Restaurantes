
create table usuario(
    id serial not null,
    nome varchar(250) not null,
    email varchar(250) not null,
    login varchar(250) not null unique,
    senha varchar(250) not null,
    data_ultima_alteracao date,
    is_admin boolean default 'N',
    primary key(id)
);

create table endereco(
    id serial not null,
    logradouro varchar(250),
    numero varchar(15),
    bairro varchar(50),
    complemento varchar(250),
    cep varchar(20),
    cidade varchar(30),
    estado varchar(15),
    usuario_id int not null,
    primary key(id),
    foreign key(usuario_id) references usuario(id) ON DELETE CASCADE
);



