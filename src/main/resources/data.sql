insert into roles(name)
values ('ROLE_ADMIN'),
       ('ROLE_USER'),
       ('ROLE_MANAGER');

insert into users(email, password, url_logo, first_name, last_name, phone, role_id,provider,status)
values ('admin@gmail.com', '$2y$12$iod5PRHZaYrIO6L3onnnk.Mhx9Hc1lb2ehBi0hRvPDD83u6OM/b66',
        '/static/images/user/avatar/user1.png', 'Admin', 'Admin', '+38000000000', 1,'local',true),
       ('user@gmail.com', '$2y$12$aDvzOnearRd4eulVJID3pOufutAIXVU5i1GKhgpXuvyVmktuSAmqe',
        '/static/images/user/avatar/user2.png', 'user', 'user', '+38000000000', 2,'local',true);


insert into cities(name, latitude, longitude)
values ('Київ', 50.4501, 30.5234),
       ('Харків', 49.9935, 36.2304),
       ('Дніпро', 48.479512881488375, 35.00721554865378),
       ('Одеса', 46.4825, 30.7233),
       ('Запоріжжя', 47.8228900, 35.1903100);


insert into categories(sortby, name, description, url_logo, background_color, tag_background_color, tag_text_color)
values ('1', 'Спортивні секції', 'Футбол, бокс, хокей, гімнастика, плавання, бойові мистецтва тощо',
        '/static/images/categories/sport.svg', '#1890FF', '#1890FF', '#fff'),
       ('5', 'Танці, хореографія', 'Класичні і народні танці, брейк-данс, степ, контемп, балет та ін.',
        '/static/images/categories/dance.svg', '#531DAB', '#F9F0FF', '#531DAB'),
       ('10', 'Студії раннього розвитку', 'Центри раннього розвитку, заняття для малюків, розвиток мовлення',
        '/static/images/categories/improvement.svg', '#73D13D',
        '#73D13D',
        '#fff'),
       ('15', 'Програмування, робототехніка, STEM',
        'Вивчення природничих наук, технологій, інженерії та математики, STEM-освіта',
        '/static/images/categories/programming.svg', '#597EF7',
        '#597EF7',
        '#fff'),
       ('20', 'Художня студія, мистецтво, дизайн',
        'Образотворче мистецтво, дизайн, комп’ютерна графіка', '/static/images/categories/art.svg', '#9254DE',
        '#9254DE',
        '#fff'),
       ('25', 'Вокальна студія, музика, музичні інструменти',
        'Музична школа, хор, ансамбль, гра на музичних інструментах, звукорежисерський гурток та ін.',
        '/static/images/categories/music.svg',
        '#FF7A45',
        '#FF7A45',
        '#fff'),
       ('30', 'Акторська майстерність, театр', 'Театральна студія, ляльковий театр, акторська майстерність',
        '/static/images/categories/theatre.svg', '#FF4D4F',
        '#FF4D4F',
        '#fff'),
       ('35', 'Особистісний розвиток', 'Розвиток лідерських якостей, підприємництво для підлітків, фінансова грамотність',
        '/static/images/categories/self-improvement.svg', '#FADB14',
        '#FFF9D4',
        '#D46B08'),
       ('40', 'Журналістика, дитяче телебачення, монтаж відео',
        'Курси журналістики, дитяче телебачення і радіомовлення, монтаж відео, школа блогінгу',
        '/static/images/categories/tv.svg',
        '#13C2C2',
        '#13C2C2', '#fff'),
       ('45', 'Центр розвитку', 'підприємництво для підлітків, фінансова грамотність',
        '/static/images/categories/center.svg', '#F759AB', '#F759AB', '#fff'),
       ('50', 'Інше', 'Тут є цікаві гуртки, які не потрапили в інші категорії', '/static/images/categories/other.svg',
        '#FFA940', '#FFA940', '#fff');



insert into contact_type(id, name, url_logo)
values (1,'Телефон', '/static/images/contacts/phone.svg'),
       (2,'Facebook', '/static/images/contacts/facebook.svg'),
       (3,'WhatsApp', '/static/images/contacts/whats-app.svg'),
       (4,'Пошта', '/static/images/contacts/mail.svg'),
       (5,'Skype', '/static/images/contacts/skype.svg'),
       (6,'Site', '/static/images/contacts/website-link-icon.svg');



