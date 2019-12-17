select * from Loja;
select * from utilizador;
select * from EstiloLoja;
select * from Produto;
select * from Categoria;
select * from Comentario;
select * from favoritos;
select * from Loja_has_Produto;
select * from Loja_has_Estilo;
select * from Produto_has_Categoria;

#Mostra a informação das lojas
select NomeEstilo, NomeLoja, IdLoja , Coordenadas, Proprietario, Imagem
from EstiloLoja E,Loja_has_estilo H, Loja L 
where L.IdLoja = H.Loja_IdLoja and H.Estilo_IdEstilo = E.IdEstilo
order by L.IdLoja asc;

#Mostra os produtos que pertencem ás lojas
select IdProduto,P.Imagem as "Imagem Produto", NomeProduto, Preco, IdLoja, NomeLoja
from Produto P, Loja_has_Produto H, Loja L
where L.IdLoja = H.Loja_IdLoja and H.Produto_IdProduto = P.IdProduto
order by P.IdProduto asc;

#Mostra as categorias dos produtos
select *
from Produto P, Produto_has_Categoria H, Categoria C
where P.IdProduto = H.Produto_IdProduto and H.Categoria_IdCategoria = C.IdCategoria
order by P.IdProduto asc;

#Mostra os favoritos dos utilizadores
select IdUtilizador, NomeUtilizador, IdLoja, NomeLoja
from Favoritos F, Utilizador U, Loja L
where  U.IdUtilizador = F.fav_IdUtilizador and L.IdLoJA = F.fav_IdLoja
order by L.IdLoja asc;

#Mostra os comentários que os utilizadores escrevem nas lojas
select IdComentario, Texto, IdLoja, NomeLoja, NomeUtilizador, IdUtilizador
from Comentario C, Loja L, Utilizador U 
where L.IdLoja = C.Loja_IdLoja and U.IdUtilizador = C.Utilizador_IdUtilizador;

#mostrar os comentarios do utilizador numa certa loja
SELECT C.Texto, U.NomeUtilizador 
FROM Comentario C, Utilizador U 
WHERE Loja_IdLoja = "variável na aplicação" and C.IdUtilizador = U.IdUtilizador;

 #Mostra os produtos de uma certa loja e de uma certa categoria
 select IdProduto, NomeProduto, P.Imagem, C.nomeCategoria, Preco
from Produto P, Produto_has_Categoria H, Loja L, Loja_has_Produto A, Categoria C
where L.IdLoja = "variavel escolhida pelo utilizador na aplicação" and A.Loja_IdLoja = "variavel escolhida pelo utilizador na aplicação" and P.IdProduto = H.Produto_IdProduto and P.IdProduto = A.Produto_IdProduto and C.nomeCategoria = "variavel escolhida pelo utilizador na aplicação" and H.Categoria_IdCategoria = C.IDCategoria;

# Mostra as categorias de uma certa loja
select distinct nomeCategoria
from Categoria C, Loja L, Loja_has_Produto P, Produto_has_Categoria A, Produto K
where L.IdLoja = "variavel escolhida pelo utilizador na aplicação" and P.Loja_IdLoja = "variavel escolhida pelo utilizador na aplicação" and C.IdCategoria = A.Categoria_IdCategoria and K.IdProduto = P.Produto_IdProduto and K.IdProduto = A.Produto_IdProduto;

#mostra os produtos das lojas
select L.NomeLoja as "Loja", P.NomeProduto as "Produto"
from Loja L
inner join Loja_has_Produto H on L.IdLoja = H.Loja_IdLoja
inner join Produto P on H.Produto_IdProduto = P.IdProduto;
