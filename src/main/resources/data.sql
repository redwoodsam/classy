-- -- ADICIONANDO PRIMEIRO USUÁRIO
-- INSERT INTO usuario (nome, sobrenome, email, telefone, endereco, numero, complemento, bairro, cidade, uf, password) VALUES ('Samuel', 'Araujo', 'samuel@email.com', '1234567890', 'Avenida Brasil', '40', 'Apto. 21', 'Jardim Brasil', 'Cuiabá', 'MT', '$2a$10$H8nZm5E/lKa1ROOfO8a5uuqnAMLT49/ColSMfBzFFK79GH/mE99Q6');

-- ADICIONANDO CATEGORIAS
DELETE FROM categoria; --DELETE TODAS AS CATEGORIAS, CASO EXISTAM

INSERT INTO categoria (nome, slug) VALUES ('Informática', 'informatica');
INSERT INTO categoria (nome, slug) VALUES ('Veículos', 'veiculos');
INSERT INTO categoria (nome, slug) VALUES ('Eletrodomésticos', 'eletrodomesticos');
INSERT INTO categoria (nome, slug) VALUES ('Imóveis', 'imoveis');
INSERT INTO categoria (nome, slug) VALUES ('Artigos para o lar', 'artigos-lar');
INSERT INTO categoria (nome, slug) VALUES ('Moveis', 'moveis');
INSERT INTO categoria (nome, slug) VALUES ('Telefonia', 'telefonia');
INSERT INTO categoria (nome, slug) VALUES ('Materiais de construção', 'materiais-construcao');
INSERT INTO categoria (nome, slug) VALUES ('Segurança', 'seguranca');
INSERT INTO categoria (nome, slug) VALUES ('Serviços', 'servicos');
