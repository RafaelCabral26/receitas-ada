-- Para o H2, os nomes das tabelas e colunas são geralmente convertidos para maiúsculas.
-- A senha para ambos os usuários é 'password' (sem aspas). O hash é um Bcrypt válido para 'password'.
-- Usando IDs altos (a partir de 100) para evitar conflitos com a sequência de IDs gerada pelo Hibernate.
INSERT INTO USUARIO (id, email, username, password, role) VALUES (100, 'usuario1@exemplo.com', 'user1', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQPBEa7Y9mqphAx2Str56ioEg5/W', 'user');
INSERT INTO USUARIO (id, email, username, password, role) VALUES (101, 'usuario2@exemplo.com', 'user2', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQPBEa7Y9mqphAx2Str56ioEg5/W', 'user');

INSERT INTO RECEITA (id, titulo, descricao, ingredientes, pathImg, createdAt, updatedAt, autor_id) VALUES (100, 'Bolo de Chocolate', 'Um delicioso bolo de chocolate fofinho.', 'Farinha, ovos, chocolate em pó, açúcar, leite', 'https://s2-receitas.glbimg.com/wJmq1MqeOZZ-VSLlDxRLdL2zj60=/0x0:1280x800/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_1f540e0b94d8437dbbc39d567a1dee68/internal_photos/bs/2022/1/N/aQD0fhQs2qW7qlFw0bTA/bolo-de-chocolate-facil.jpg', NOW(), NOW(), 100);
INSERT INTO RECEITA (id, titulo, descricao, ingredientes, pathImg, createdAt, updatedAt, autor_id) VALUES (101, 'Pão de Queijo', 'A autêntica receita mineira.', 'Polvilho, queijo, ovos, leite, óleo', 'https://soubh.uai.com.br/wp-content/uploads/2025/08/yt-069_pao-de-queijo_receita.jpg', NOW(), NOW(), 101);

-- Adicionando votos de exemplo (valor 1 = voto positivo)
INSERT INTO VOTO (usuarioId, receitaId, valor, createdAt) VALUES (100, 101, 1, NOW());
INSERT INTO VOTO (usuarioId, receitaId, valor, createdAt) VALUES (101, 100, 1, NOW());