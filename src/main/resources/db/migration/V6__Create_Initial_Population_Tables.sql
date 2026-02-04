INSERT INTO artista (id, artista_id, nome, tipo)
VALUES (1, '550e8400-e29b-41d4-a716-446655440001', 'Daft Punk', 'DUPLA'),
       (2, '550e8400-e29b-41d4-a716-446655440002', 'Michael Jackson', 'CANTOR'),
       (3, '550e8400-e29b-41d4-a716-446655440003', 'Pink Floyd', 'BANDA'),
       (4, '550e8400-e29b-41d4-a716-446655440004', 'The Weeknd', 'CANTOR');

INSERT INTO album (id, album_id, titulo, data_lancamento)
VALUES (1, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Discovery', '2001-03-12'),
       (2, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Random Access Memories', '2013-05-17'),
       (3, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'Thriller', '1982-11-30'),
       (4, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'The Dark Side of the Moon', '1973-03-01'),
       (5, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'After Hours', '2020-03-20');

INSERT INTO artista_album (album_id, artista_id)
VALUES (1, 1), -- Discovery -> Daft Punk
       (2, 1), -- RAM -> Daft Punk
       (3, 2), -- Thriller -> Michael Jackson
       (4, 3), -- Dark Side -> Pink Floyd
       (5, 4); -- After Hours -> The Weeknd

SELECT setval('artista_seq', (SELECT MAX(id) FROM artista));
SELECT setval('album_seq', (SELECT MAX(id) FROM album));