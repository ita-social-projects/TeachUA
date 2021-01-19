insert into city(city) values
('Львів'),('Київ'),('Харків'),('Одеса'),('Вінниця'),
('Рівне'),('Луцьк'),('Донецьк'),('Луганськ');


insert into club_coordinates(latitude,longitude)
values
(49.8288091391771, 24.05856514044121),
(49.81303597220556, 24.012239860287306),
(49.83259434488775, 23.997036169252326),
(49.83259434488775, 23.997036169252326),
(49.83259434488775, 23.997036169252326),
(49.83259434488775, 23.997036169252326),
(49.83259434488775, 23.997036169252326),
(49.83259434488775, 23.997036169252326),
(49.83259434488775, 23.997036169252326),
(49.83259434488775, 23.997036169252326),
(49.83259434488775, 23.997036169252326),
(49.83259434488775, 23.997036169252326),
(49.83259434488775, 23.997036169252326),
(49.83259434488775, 23.997036169252326),
(49.83259434488775, 23.997036169252326),
(49.83259434488775, 23.997036169252326),
(49.83259434488775, 23.997036169252326),
(49.83259434488775, 23.997036169252326);


insert into children_center(address,city_id,coordinates_id)
VALUES('street 1',1,1),('street 2', 1,10),
      ('street 3',2,2),('street 4',2,11),
      ('street 4', 3,3),('street 5',3,12),
      ('street 6', 4,4),('street 7',4,13),
      ('street 8', 5,5),('street 9',5,14),
      ('street 10', 6,6),('street 11', 6,15),
      ('street 12', 7,7),('street 13', 7,16),
      ('street 14', 8,8),('street 15', 8,17),
      ('street 16', 9,9),('street 17', 9,18);

-- insert into children_center( coordinates_id)
-- values
-- (1),(2),(3),(4),(5),
-- (6),(7),(8),(9),(10),
-- (11),(12),(13),(14),(15),
-- (16),(17),(18);



INSERT into activities(activity)
VALUES
('спорт'),('музика'),('танці'),('наука'), ('робототехніка'),
('конструювння');

insert into club(children_center_id,club_name,
                 general_description_of_activities,
                 general_description_of_club,
                 link_in_social_networks, logo_url, web_site_url)
VALUES
(1,'гурток 1','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 1 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(1,'гурток 2','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 2 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(1,'гурток 3','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 3 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(1,'гурток 4','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 4 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(1,'гурток 5','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 5 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(1,'гурток 6','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 6 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(1,'гурток 7','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 7 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(1,'гурток 8','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 8 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(1,'гурток 9','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 9 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(1,'гурток 10','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 10 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),



(2,'гурток 11','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 11 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(2,'гурток 12','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 12 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(2,'гурток 13','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 13 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(2,'гурток 14','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 14 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(2,'гурток 15','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 15 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(2,'гурток 16','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 16 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(2,'гурток 17','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 17 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(2,'гурток 18','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 18 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(2,'гурток 19','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 19 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(2,'гурток 20','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 20 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),


(3,'гурток 21','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 21 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(3,'гурток 22','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 22 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(3,'гурток 23','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 23 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(3,'гурток 24','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 24 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(3,'гурток 25','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 25 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(3,'гурток 26','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 26 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(3,'гурток 27','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 27 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(3,'гурток 28','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 28 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(3,'гурток 29','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 29 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(3,'гурток 30','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 30 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),



(4,'гурток 31','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 31 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(4,'гурток 32','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 32 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(4,'гурток 33','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 33 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(4,'гурток 34','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 34 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(4,'гурток 35','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 35 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(4,'гурток 36','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 36 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(4,'гурток 37','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 37 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(4,'гурток 38','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 38 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(4,'гурток 39','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 39 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(4,'гурток 40','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 40 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),



(5,'гурток 41','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 41 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(5,'гурток 42','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 42 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(5,'гурток 43','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 43 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(5,'гурток 44','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 44 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(5,'гурток 45','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 45 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(5,'гурток 46','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 46 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(5,'гурток 47','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 47 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(5,'гурток 48','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 48 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(5,'гурток 49','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 49 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(5,'гурток 50','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 50 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),



(6,'гурток 51','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 51 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(6,'гурток 52','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 52 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(6,'гурток 53','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 53 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(6,'гурток 54','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 54 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(6,'гурток 55','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 55 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(6,'гурток 56','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 56 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(6,'гурток 57','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 57 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(6,'гурток 58','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 58 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(6,'гурток 59','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 59 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(6,'гурток 60','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 60 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),



(7,'гурток 61','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 61 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(7,'гурток 62','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 62 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(7,'гурток 63','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 63 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(7,'гурток 64','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 64 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(7,'гурток 65','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 65 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(7,'гурток 66','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 66 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(7,'гурток 67','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 67 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(7,'гурток 68','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 68 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/'),
(7,'гурток 69','загальний опис діяльності гуртка 1 ',
 ' опис гуртка 69 ','https://www.facebook.com/','/images/logo-social.png','https://www.google.com/'),
(7,'гурток 70','загальний опис діяльності гуртка 2 ',
 ' опис гуртка 70 ','https://www.facebook.com/','/images/star-logo.jpg','https://www.google.com/');








insert into club_activities(activities_id, club_id)values
(1,1),(2,2),(3,3),(4,4),(5,5),(6,6),
(1,7),(2,8),(3,9),(4,10),(5,11),(6,12),
(1,13),(2,14),(3,15),(4,16),(5,17),(6,18),
(1,19),(2,20),(3,21),(4,22),(5,23),(6,24),
(1,25),(2,26),(3,27),(4,28),(5,29),(6,30),
(1,31),(2,32),(3,33),(4,34),(5,35),(6,36),
(1,37),(2,38),(3,39),(4,40),(5,41),(6,42),
(1,43),(2,44),(3,45),(4,46),(5,47),(6,48),
(1,49),(2,50),(3,51),(4,52),(5,53),(6,54),
(1,55),(2,56),(3,57),(4,58),(5,59),(6,60),
(1,61),(2,62),(3,63),(4,64),(5,65),(6,66),
(1,67),(2,68),(3,69),(4,70);

