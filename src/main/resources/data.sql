-- -- ADICIONANDO PRIMEIRO USUÁRIO
INSERT INTO usuario (nome, sobrenome, email, telefone, endereco, numero, complemento, bairro, cidade, uf, password) VALUES ('Visitante', '01', 'visitante@email.com', '1234567890', 'Avenida Brasil', '40', 'Apto. 21', 'Jardim Brasil', 'São Paulo', 'SP', '$2a$10$A99hY9zxzxIozEwKykUZoebG5uOEp5qXGngJp26TbJIdRkcyOBR1a'); -- senha 123

-- ADICIONANDO CATEGORIAS
DELETE FROM categoria; --APAGA TODAS AS CATEGORIAS, CASO EXISTAM

INSERT INTO categoria (nome, slug) VALUES ('Informática', 'informatica');
INSERT INTO categoria (nome, slug) VALUES ('Veículos', 'veiculos');
INSERT INTO categoria (nome, slug) VALUES ('Roupas e Acessórios', 'roupas-acessorios');
INSERT INTO categoria (nome, slug) VALUES ('Eletrodomésticos', 'eletrodomesticos');
INSERT INTO categoria (nome, slug) VALUES ('Imóveis', 'imoveis');
INSERT INTO categoria (nome, slug) VALUES ('Artigos para o lar', 'artigos-lar');
INSERT INTO categoria (nome, slug) VALUES ('Moveis', 'moveis');
INSERT INTO categoria (nome, slug) VALUES ('Telefonia', 'telefonia');
INSERT INTO categoria (nome, slug) VALUES ('Materiais de construção', 'materiais-construcao');
INSERT INTO categoria (nome, slug) VALUES ('Segurança', 'seguranca');
INSERT INTO categoria (nome, slug) VALUES ('Serviços', 'servicos');
