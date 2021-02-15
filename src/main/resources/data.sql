insert into roles(name)
values ('ROLE_ADMIN'),
       ('ROLE_USER');

insert into users(email, password, name, role_id)
values ('admin@gmail.com', '$2y$12$iod5PRHZaYrIO6L3onnnk.Mhx9Hc1lb2ehBi0hRvPDD83u6OM/b66', 'admin', 1),
       ('user@gmail.com', '$2y$12$aDvzOnearRd4eulVJID3pOufutAIXVU5i1GKhgpXuvyVmktuSAmqe', 'user', 2),
       ('user2@gmail.com', '$2y$12$aDvzOnearRd4eulVJID3pOufutAIXVU5i1GKhgpXuvyVmktuSAmqe', 'user2', 2);

insert into cities(name)
values ('Київ'),
       ('Харків'),
       ('Дніпро'),
       ('Одеса'),
       ('Запоріжжя'),
       ('Луганськ'),
       ('Донецьк'),
       ('Львів'),
       ('Рівне');

insert into categories(name, url_logo, background_color, tag_background_color, tag_text_color)
values ('спортивні секції', '/static/images/categories/sport.svg', '#1890FF', '#1890FF', '#fff'),
       ('Танці', '/static/images/categories/dance.svg', '#531DAB', '#F9F0FF', '#531DAB'),
       ('студії раннього розвитку', '/static/images/categories/improvement.svg', '#73D13D', '#73D13D', '#fff'),
       ('програмування, робототехніка, STEM', '/static/images/categories/programming.svg', '#597EF7', '#597EF7',
        '#fff'),
       ('художні студії, мистецтво, дизайн', '/static/images/categories/art.svg', '#9254DE', '#9254DE', '#fff'),
       ('вокальна студії, музика, музичні інструменти', '/static/images/categories/music.svg', '#FF7A45', '#FF7A45',
        '#fff'),
       ('акторська майстерність, театр', '/static/images/categories/theatre.svg', '#FF4D4F', '#FF4D4F', '#fff'),
       ('особистісний розвиток', '/static/images/categories/self-improvement.svg', '#FADB14', '#FFF9D4', '#D46B08'),
       ('журналістика, дитяче телебачення, монтаж відео, влогів', '/static/images/categories/tv.svg', '#13C2C2',
        '#13C2C2', '#fff'),
       ('інше', '/static/images/categories/other.svg', '#FFA940', '#FFA940', '#fff'),
       ('центр розвитку', '/static/images/categories/center.svg', '#F759AB', '#F759AB', '#fff');

insert into centers (name, email, address, phones, social_links, description,
                     latitude, longitude, url_logo, url_web, user_id)
values ('center1', 'center1@gameil.com', 'center_address1', '+380000000001', 'some_links', 'center1_description',
        49.73259434488975, 23.997036169252326, 'https://www.logodesign.net/images/minimal-logo.png', '#', 2),
       ('Творчий край', 'center2@gameil.com', 'center_address2', '+380000000002', 'some_links', 'center2_description',
        49.23259434488972, 23.297036169252322, 'https://www.logodesign.net/images/illustration-logo.png', '#', 3);

insert into news(title, description)
values ('title1', 'description1'),
       ('title2', 'description2');

insert into districts(name, city_id)
values ('No District', 1),
       ('No District', 2),
       ('No District', 3),
       ('No District', 4),
       ('No District', 5),
       ('No District', 6),
       ('No District', 7),
       ('No District', 8),
       ('No District', 9);

insert into stations(name, city_id, district_id)
values ('No Station', 1, 1),
       ('No Station', 2, 2),
       ('No Station', 3, 3),
       ('No Station', 4, 4),
       ('No Station', 5, 5),
       ('No Station', 6, 6),
       ('No Station', 7, 7),
       ('No Station', 8, 8),
       ('No Station', 9, 9);

insert into clubs(age_from, age_to, name, address, url_logo, url_web, url_background, work_time, latitude, longitude,
                  station_id, district_id, city_id, center_id, user_id, description, rating)
values (6, 9, 'ГУРТОК 1', 'club_address1', '#', '#', '/static/images/club/bg_2.png', '09:00-16:00', 49.73259434488975,
        23.997036169252326, 1, 1, 1, 2, 3,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut', 2),
       (7, 10, 'гурток 2', 'club_address2', '#', '#', '/static/images/club/bg_1.jpg', '09:00-16:00', 49.63259434488875,
        23.997036168252326, 2, 2, 2, 2, 3,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...', 3),
       (11, 16, 'гурток 3', 'club_address3', '#', '#', '/static/images/club/bg_3.jpg', '09:00-16:00', 49.83259434488775,
        23.997036167252326, 3, 3, 3, 1, 2,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...', 1),
       (6, 9, 'гурток 4', 'club_address4', '#', '#', '/static/images/club/bg_4.jpg', '09:00-16:00', 49.83259434488675,
        23.997036166252326, 4, 4, 4, null, 2,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...', 5),
       (5, 10, 'гурток 5', 'club_address5', '#', '#', '/static/images/club/bg_2.png', '09:00-16:00', 49.83259434488575,
        23.997036165252326, 5, 5, 5, null, 2,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...', 1),
       (5, 10, 'гурток 6', 'club_address6', '#', '#', '/static/images/club/bg_2.png', '09:00-16:00', 49.83259434488475,
        23.997036164252326, 6, 6, 6, null, 3,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...', 3),
       (5, 10, 'гурток 7', 'club_address7', '#', '#', '/static/images/club/bg_2.png', '09:00-16:00', 49.83259434488475,
        23.997036164252326, 7, 7, 7, null, 2,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...', 1),
       (5, 10, 'гурток 8', 'club_address8', '#', '#', '/static/images/club/bg_2.png', '09:00-16:00', 49.83259434488475,
        23.997036164252326, 8, 8, 8, null, 2,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...', 3),
       (5, 10, 'гурток 9', 'club_address9', '#', '#', '/static/images/club/bg_2.png', '09:00-16:00', 49.83259434488175,
        23.997036161252326, 9, 9, 9, 1, 2,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...', 5),
       (5, 10, 'гурток 10', 'club_address10', '#', '#', '/static/images/club/bg_2.png', '09:00-16:00',
        49.83817131443633, 24.029413132472404, 1, 1, 1, null, 3,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut', 1);

insert into feedbacks(rate, date, text, user_id, club_id)
values (5, '2021-02-15 16:06:36.21', 'nice club', 1, 1),
       (3, '2021-02-15 16:06:36.21', ' ', 1, 1),
       (5, '2021-02-15 16:06:36.21', 'nice club', 2, 2),
       (4, '2021-02-15 16:06:36.21', 'nice club', 3, 3),
       (5, '2021-02-15 16:06:36.21', 'nice club', 1, 4),
       (4, '2021-02-15 16:06:36.21', 'nice club', 2, 5),
       (5, '2021-02-15 16:06:36.21', 'nice club', 3, 6),
       (4, '2021-02-15 16:06:36.21', 'nice club', 1, 7),
       (5, '2021-02-15 16:06:36.21', 'nice club', 2, 8),
       (2, '2021-02-15 16:06:36.21', 'bad club', 3, 9),
       (1, '2021-02-15 16:06:36.21', 'bad club', 1, 10);

insert into club_category(club_id, category_id)
VALUES (1, 2),
       (1, 8),
       (1, 3),
       (2, 2),
       (3, 2),
       (4, 3),
       (5, 1),
       (6, 4),
       (7, 5),
       (8, 9),
       (9, 1),
       (10, 1);
