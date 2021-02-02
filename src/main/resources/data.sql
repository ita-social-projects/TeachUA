insert into roles(name) values ('ROLE_ADMIN'), ('ROLE_USER');

insert into users(email, password, name, role_id) values
('admin@gmail.com', '$2y$12$iod5PRHZaYrIO6L3onnnk.Mhx9Hc1lb2ehBi0hRvPDD83u6OM/b66', 'admin', 1),
('user@gmail.com', '$2y$12$aDvzOnearRd4eulVJID3pOufutAIXVU5i1GKhgpXuvyVmktuSAmqe', 'user', 2),
('user2@gmail.com', '$2y$12$aDvzOnearRd4eulVJID3pOufutAIXVU5i1GKhgpXuvyVmktuSAmqe', 'user2', 2);

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

insert into categories(name,url_logo, background_color) values
('спортивні секції', 'assets/images/categories/sport.svg', '#1890FF'),
('танці, хореографія', 'assets/images/categories/dance.svg', '#531DAB'),
('студії раннього розвитку', 'assets/images/categories/improvement.svg', '#73D13D'),
('програмування, робототехніка, STEM', 'assets/images/categories/programming.svg', '#597EF7'),
('художні студії, мистецтво, дизайн', 'assets/images/categories/art.svg', '#9254DE'),
('вокальна студії, музика, музичні інструменти', 'assets/images/categories/music.svg', '#FF7A45'),
('акторська майстерність, театр', 'assets/images/categories/theatre.svg', '#FF4D4F'),
('особистісний розвиток', 'assets/images/categories/self-improvement.svg', '#FADB14'),
('журналістика, дитяче телебачення, монтаж відео, влогів', 'assets/images/categories/tv.svg', '#13C2C2'),
('інше', 'assets/images/categories/other.svg', '#FFA940'),
('центр розвитку', 'assets/images/categories/center.svg', '#F759AB');

insert into centers (name, email, address, phones, social_links, description,
                      latitude, longitude, url_logo, url_web, user_id) values
('center1', 'center1@gameil.com', 'center_address1', '+380000000001', 'some_links', 'center1_description',
49.73259434488975, 23.997036169252326, '#', '#', 2),
('center2', 'center2@gameil.com', 'center_address2', '+380000000002', 'some_links', 'center2_description',
49.23259434488972, 23.297036169252322, '#', '#', 3);

insert into news(title, description) values
('title1', 'description1'),
('title2', 'description2');

insert into clubs(age_from, age_to, name, address, url_logo, url_web, work_time, latitude, longitude,
                  city_id, center_id, user_id, description) values
(6, 9, 'ГУРТОК 1', 'club_address1', '#', '#', '09:00-16:00', 49.73259434488975, 23.997036169252326, 1, 2, 3, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...'),
(7, 10, 'гурток 2', 'club_address2', '#', '#', '09:00-16:00', 49.63259434488875, 23.997036168252326, 2, 2, 3, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...'),
(11, 16, 'гурток 3', 'club_address3', '#', '#', '09:00-16:00', 49.83259434488775, 23.997036167252326, 3, 1, 2, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...'),
(6, 9, 'гурток 4', 'club_address4', '#', '#', '09:00-16:00', 49.83259434488675, 23.997036166252326, 4, null, 2, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...'),
(5, 10, 'гурток 5', 'club_address5', '#', '#', '09:00-16:00', 49.83259434488575, 23.997036165252326, 5, null, 2, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...'),
(5, 10, 'гурток 6', 'club_address6', '#', '#', '09:00-16:00', 49.83259434488475, 23.997036164252326, 6, null, 3, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...'),
(5, 10, 'гурток 7', 'club_address7', '#', '#', '09:00-16:00', 49.83259434488475, 23.997036164252326, 7, null, 2, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...'),
(5, 10, 'гурток 8', 'club_address8', '#', '#', '09:00-16:00', 49.83259434488475, 23.997036164252326, 8, null, 2, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...'),
(5, 10, 'гурток 9', 'club_address9', '#', '#', '09:00-16:00', 49.83259434488175, 23.997036161252326, 9, 1, 2, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...'),
(5, 10, 'гурток 10', 'club_address10', '#', '#', '09:00-16:00', 49.83817131443633, 24.029413132472404, 1, null, 3, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ...');


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
