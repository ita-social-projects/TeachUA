insert into roles(name) values ('ROLE_ADMIN'), ('ROLE_USER');

insert into users(email, password, name, role_id) values
('user@gmail.com', '$2y$12$aDvzOnearRd4eulVJID3pOufutAIXVU5i1GKhgpXuvyVmktuSAmqe', 'user', 1),
('admin@gmail.com', '$2y$12$iod5PRHZaYrIO6L3onnnk.Mhx9Hc1lb2ehBi0hRvPDD83u6OM/b66', 'admin', 2);

insert into cities(name) values
('Київ'),
('Харків'),
('Дніпро'),
('Одеса'),
('Запоріжжя'),
('Луганськ'),
('Донецьк'),
('Львів'),
('Рівне');

insert into categories(name,url_logo) values
('спортивні секції', '#'),
('танці, хореографія', '#'),
('студії раннього розвитку', '#'),
('програмування, робототехніка, STEM', '#'),
('художні студії, мистецтво, дизайн', '#'),
('вокальна студії, музика, музичні інструменти', '#'),
('акторська майстерність, театр', '#'),
('особистісний розвиток', '#'),
('журналістика, дитяче телебачення, монтаж відео, влогів', '#'),
('інше', '#'),
('центр розвитку', '#');

insert into studios(name) values
('studio1'),
('studio2');

insert into news(title,description) values
('title1', 'description1'),
('title2', 'description2');

insert into clubs(age_from, age_to, name, url_logo, url_web, work_time,
                  city_id, studio_id, user_id) values
(6, 9, 'гурток 1', '#', '#', '09:00-16:00', 1, null, 2),
(7, 10, 'гурток 2', '#', '#', '09:00-16:00', 2, null, 2),
(11, 16, 'гурток 3', '#', '#', '09:00-16:00', 3, null, 2),
(6, 9, 'гурток 4', '#', '#', '09:00-16:00', 4, null, 2),
(5, 10, 'гурток 5', '#', '#', '09:00-16:00', 5, null, 2),
(5, 10, 'гурток 6', '#', '#', '09:00-16:00', 6, null, 2),
(5, 10, 'гурток 7', '#', '#', '09:00-16:00', 7, null, 2),
(5, 10, 'гурток 8', '#', '#', '09:00-16:00', 8, null, 2),
(5, 10, 'гурток 9', '#', '#', '09:00-16:00', 9, null, 2),
(5, 10, 'гурток 10', '#', '#', '09:00-16:00', 1, null, 2);


insert into feedbacks(rate, text, user_name, club_id) values
(5, 'nice club', 'John Smith', 1),
(3, ' ', 'Olia The', 1),
(5, 'nice club', 'John Smith', 2),
(4, 'nice club', 'John Smith', 3),
(5, 'nice club', 'John Smith', 4),
(4, 'nice club', 'John Smith', 5),
(5, 'nice club', 'John Smith', 6),
(4, 'nice club', 'John Smith', 7),
(5, 'nice club', 'John Smith', 8),
(2, 'bad club', 'John Smith', 9),
(1, 'bad club', 'John Smith', 10);

insert into coordinates(latitude, longitude) values
(49.73259434488975, 23.997036169252326),
(49.63259434488875, 23.997036168252326),
(49.83259434488775, 23.997036167252326),
(49.83259434488675, 23.997036166252326),
(49.83259434488575, 23.997036165252326),
(49.83259434488475, 23.997036164252326),
(49.83259434488375, 23.997036163252326),
(49.83259434488275, 23.997036162252326),
(49.83259434488175, 23.997036161252326),
(49.83817131443633, 24.029413132472404);

insert into club_category(club_id, category_id) VALUES
(1,1),
(2,2),
(3,2),
(4,3),
(5,1),
(6,4),
(7,5),
(8,9),
(9,1),
(10,11);

insert into club_coordinates(club_id, coordinates_id) values
(1,1),
(2,2),
(3,2),
(4,3),
(5,5),
(6,6),
(7,7),
(8,8),
(9,9),
(10,10);