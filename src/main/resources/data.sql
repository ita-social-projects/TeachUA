insert into roles(name)
values ('ROLE_ADMIN'),
       ('ROLE_USER');

insert into users(email, password, name, role_id)
values ('admin@gmail.com', '$2y$12$iod5PRHZaYrIO6L3onnnk.Mhx9Hc1lb2ehBi0hRvPDD83u6OM/b66', 'admin', 1),
       ('user@gmail.com', '$2y$12$aDvzOnearRd4eulVJID3pOufutAIXVU5i1GKhgpXuvyVmktuSAmqe', 'user', 2),
       ('user2@gmail.com', '$2y$12$aDvzOnearRd4eulVJID3pOufutAIXVU5i1GKhgpXuvyVmktuSAmqe', 'user2', 2);

insert into cities(name, latitude, longitude)
values ('Київ', 50.4501, 30.5234),
       ('Харків', 49.9935, 36.2304),
       ('Дніпро', 48.479512881488375, 35.00721554865378),
       ('Одеса', 46.4825, 30.7233),
       ('Запоріжжя', 47.8228900, 35.1903100),
       ('Луганськ', 48.5740, 39.3078),
       ('Донецьк', 48.0159, 37.8028),
       ('Львів', 49.8397, 24.0297),
       ('Рівне', 50.6199, 26.2516);

insert into categories(name, url_logo, background_color, tag_background_color, tag_text_color)
values ('спортивні секції', '/dev/static/images/categories/sport.svg', '#1890FF', '#1890FF', '#fff'),
       ('Танці', '/dev/static/images/categories/dance.svg', '#531DAB', '#F9F0FF', '#531DAB'),
       ('студії раннього розвитку', '/dev/static/images/categories/improvement.svg', '#73D13D',
        '#73D13D',
        '#fff'),
       ('програмування, робототехніка, STEM', '/dev/static/images/categories/programming.svg', '#597EF7',
        '#597EF7',
        '#fff'),
       ('художні студії, мистецтво, дизайн', '/dev/static/images/categories/art.svg', '#9254DE',
        '#9254DE',
        '#fff'),
       ('вокальна студії, музика, музичні інструменти', '/dev/static/images/categories/music.svg',
        '#FF7A45',
        '#FF7A45',
        '#fff'),
       ('акторська майстерність, театр', '/dev/static/images/categories/theatre.svg', '#FF4D4F',
        '#FF4D4F',
        '#fff'),
       ('особистісний розвиток', '/dev/static/images/categories/self-improvement.svg', '#FADB14',
        '#FFF9D4',
        '#D46B08'),
       ('журналістика, дитяче телебачення, монтаж відео, влогів',
        '/dev/static/images/categories/tv.svg',
        '#13C2C2',
        '#13C2C2', '#fff'),
       ('інше', '/dev/static/images/categories/other.svg', '#FFA940', '#FFA940', '#fff'),
       ('центр розвитку', '/dev/static/images/categories/center.svg', '#F759AB', '#F759AB', '#fff');

insert into centers (name, email, address, phones, social_links, description,
                     latitude, longitude, url_logo, url_web, user_id)
values ('center1', 'center1@gameil.com', 'center_address1', '+380000000001', 'some_links',
        'center1_description',
        49.73259434488975, 23.997036169252326, 'https://www.logodesign.net/images/minimal-logo.png', '#', 2),
       ('Творчий край', 'center2@gameil.com', 'center_address2', '+380000000002', 'some_links',
        'center2_description',
        49.23259434488972, 23.297036169252322, 'https://www.logodesign.net/images/illustration-logo.png', '#',
        3);

insert into news(title, description, date, url_title_logo)
values ('title1', 'description1', '2021-02-15 16:06:36.21',
        'https://vechirniy.kyiv.ua/data/news/full/58cbc15d9f4cb.jpg'),
       ('title2', 'description2', '2021-02-15 16:06:36.21', 'https://cpo.in.ua/articles/technik/DSC00014.JPG'),
       ('title3', 'description3', '2021-02-15 16:06:36.21',
        'https://fti.dp.ua/dsit/wp-content/uploads/sites/2/2020/02/sci-tech-talks-1-1080x608.jpg');

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
                  station_id, district_id, city_id, center_id, user_id, description, rating, is_approved)
values (6, 9, 'Довкілля крізь призму української мови 1', 'вул. Університетська 52', '#', '#', 'dev/static/images/club/bg_2.png', '09:00-16:00', 50.4501,
        30.5234, 1, 1, 1, 2, 3,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut', 2,true),
       (7, 10, 'Довкілля крізь призму української мови 2', 'вул. Університетська 52', '#', '#', 'dev/static/images/club/bg_1.jpg', '09:00-16:00', 49.988377487943026,
        36.178415917152016, 2, 2, 2, 2, 3,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...', 3, true),
       (11, 16, 'Довкілля крізь призму української мови 3', 'вул. Університетська 52', '#', '#', 'dev/static/images/club/bg_3.jpg', '09:00-16:00', 48.428760792192556,
        35.0136847650432, 3, 3, 3, 1, 2,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...', 1,true),
       (6, 9, 'Довкілля крізь призму української мови 4', 'вул. Університетська 52', '#', '#', 'dev/static/images/club/bg_4.jpg', '09:00-16:00', 46.492841277008196,
        30.746685697230422
       , 4, 4, 4, null, 2,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...', 5,true),
       (5, 10, 'Довкілля крізь призму української мови 5', 'вул. Університетська 52', '#', '#', 'dev/static/images/club/bg_2.png', '09:00-16:00', 47.830640332833454,
        35.17341638260339, 5, 5, 5, null, 2,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...', 1,true),
       (5, 10, 'Довкілля крізь призму української мови 6', 'вул. Університетська 52', '#', '#', 'dev/static/images/club/bg_2.png', '09:00-16:00', 48.535458078035944,
        39.28420328747561, 6, 6, 6, null, 3,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...', 3,true),
       (5, 10, 'Довкілля крізь призму української мови 7', 'вул. Університетська 52', '#', '#', 'dev/static/images/club/bg_2.png', '09:00-16:00',48.0574323833658,
        37.776618035813655, 7, 7, 7, null, 2,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...', 1,true),
       (5, 10, 'Довкілля крізь призму української мови 8', 'вул. Університетська 52', '#', '#', 'dev/static/images/club/bg_2.png', '09:00-16:00', 50.61060477065451,
        26.261240125246683, 8, 8, 8, null, 2,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...', 3.,true),
       (5, 10, 'Довкілля крізь призму української мови 9', 'вул. Університетська 52', '#', '#', 'dev/static/images/club/bg_2.png', '09:00-16:00', 49.83483090260284,
        24.032436728841876, 9, 9, 9, 1, 2,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...', 5,true),
       (5, 10, 'Довкілля крізь призму української мови 10', 'вул. Університетська 52', '#', '#', 'dev/static/images/club/bg_2.png', '09:00-16:00',
        50.47768137708023, 30.63419898789332, 1, 1, 1, null, 3,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut', 1,true);


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
       (10, 3);

