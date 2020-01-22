create database FastShopping;
use FastShopping;

create table Loja (IdLoja int not null auto_increment,
                   NomeLoja varchar(40) not null,
                   Coordenadas varchar(50) not null,
                   Imagem longblob not null,
                   Proprietario varchar(25) not null,
                   primary key (IdLoja));
                   
create table EstiloLoja (IdEstilo int not null auto_increment,
                         NomeEstilo varchar(25) not null,
                         primary key (IdEstilo));       
       
create table Utilizador (IdUtilizador int not null auto_increment,
						 NomeUtilizador varchar(40) not null,
                         IsAdmin boolean not null,
                         primary key (IdUtilizador));
			
create table Favoritos (fav_IdUtilizador int not null,
                        fav_IdLoja int not null,
                        foreign key (fav_IdUtilizador) references Utilizador (IdUtilizador),
						foreign key (fav_IdLoja) references Loja(IdLoja) );
			
create table Produto (IdProduto int not null auto_increment,
					  NomeProduto varchar(45) not null,
                      Imagem longblob not null,
                      primary key (IdProduto));
                      
create table Categoria (IdCategoria int not null auto_increment,
						nomeCategoria varchar(30) not null,
                        primary key (IdCategoria));
                        
create table Comentario (IdComentario int not null auto_increment,
                         Texto longtext not null,
                         Loja_IdLoja int not null,
                         Utilizador_IdUtilizador int not null,
                         primary key (IdComentario),
						 foreign key (Loja_IdLoja) references Loja(IdLoja),
					     foreign key (Utilizador_IdUtilizador) references Utilizador(IdUtilizador));
                                              
                         
create table Produto_has_Categoria (ProdutoId_has_Categoria int not null auto_increment,
                                    Categoria_IdCategoria int not null,
                                    Produto_IdProduto int not null,
                                    primary key (ProdutoId_has_Categoria),
                                    foreign key (Categoria_IdCategoria) references Categoria(IdCategoria),
									foreign key (Produto_IdProduto) references Produto(IdProduto));

create table Loja_has_Produto (LojaId_has_Produto int not null auto_increment,
							   Loja_IdLoja int not null,
                               Produto_IdProduto int not null,
                               Preco float not null,
                               primary key (LojaId_has_Produto),
                               foreign key (Loja_IdLoja) references Loja(IdLoja),
                               foreign key (Produto_IdProduto) references Produto(IdProduto));

create table Loja_has_Estilo (LojaId_has_Estilo int not null auto_increment,
                              Loja_IdLoja int not null,
                              Estilo_IdEstilo int not null,
                              primary key (LojaId_has_Estilo),
                              foreign key (Loja_IdLoja) references Loja(IdLoja),
                              foreign key (Estilo_IdEstilo) references EstiloLoja(IdEstilo));		
                              
create table Encomenda (IdEncomenda int not null auto_increment,
                        NumEncomenda int not null,
						Produto_IdProduto int not null,
						Loja_IdLoja int not null,
                        Utilizador_IdUtilizador int not null,
                        DataEncomenda date not null,
                        DataEntrega date not null,
                        primary key (IdEncomenda),
                        foreign key (Loja_IdLoja) references Loja(IdLoja),
                        foreign key (Utilizador_IdUtilizador) references Utilizador(IdUtilizador),
                        foreign key (Produto_IdProduto) references Produto(IdProduto));
