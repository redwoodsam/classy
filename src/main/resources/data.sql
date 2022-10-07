INSERT INTO usuario (nome, sobrenome, email, telefone, endereco, numero, complemento, bairro, cidade, uf, password) VALUES ('Samuel', 'Araujo', 'samuel@email.com', '1234567890', 'Avenida Brasil', '40', 'Apto. 21', 'Jardim Brasil', 'Cuiabá', 'MT', '$2a$10$H8nZm5E/lKa1ROOfO8a5uuqnAMLT49/ColSMfBzFFK79GH/mE99Q6
');
INSERT INTO anuncio (nome, descricao, valor, usuario_id) VALUES ('Notebook CCE', 'Produto em ótima qualidade', '500', 1);
INSERT INTO foto (nome, path, formato) VALUES ('notebook-cce', 'https://img.olx.com.br/images/77/774908098012476.jpg', 'jpeg');
INSERT INTO foto (nome, path, formato) VALUES ('notebook-cce-2', 'https://img.olx.com.br/images/77/775908093063981.jpg', 'jpeg');
INSERT INTO foto (nome, path, formato) VALUES ('notebook-cce-fechado', 'https://img.olx.com.br/images/77/770908096096018.jpg', 'jpeg');
INSERT INTO foto_anuncio (anuncio_id, foto_id) VALUES (1, 1);
INSERT INTO foto_anuncio (anuncio_id, foto_id) VALUES (1, 2);
INSERT INTO foto_anuncio (anuncio_id, foto_id) VALUES (1, 3);

