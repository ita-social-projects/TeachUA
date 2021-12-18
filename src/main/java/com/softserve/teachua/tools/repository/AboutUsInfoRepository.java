package com.softserve.teachua.tools.repository;

import com.softserve.teachua.dto.about_us_item.AboutUsItemProfile;

import java.util.Arrays;
import java.util.List;

public class AboutUsInfoRepository {
    public static List<AboutUsItemProfile> getAboutUsInfo() {
        return Arrays.asList(
                AboutUsItemProfile.builder()
                        .text("Про ініціативу")
                        .picture(null)
                        .video(null)
                        .type(1L)
                        .number(1L).build(),
                AboutUsItemProfile.builder()
                        .text("<p>Ініціатива “Навчай українською” - це небайдужі громадяни, які об'єдналися, щоб популяризувати українську мову у сфері освіти. Ви можете про нас більше дізнатися з нашої Facebook-сторінки.</p> ")
                        .picture(null)
                        .video(null)
                        .type(2L)
                        .number(2L).build(),
                AboutUsItemProfile.builder()
                        .text("<span class=\"chapter\">Наталка Федечко</span>" +
                                "<p><span class=\"highlight\">Співзасновниця та координаторка Ініціативи \"Навчай українською\"</span></p>" +
                                "<p>Активна учасниця групи захисту мовного закону і права україномовних на послуги українською мовою в Україні. Ініціювала розпорядження департаменту культури КМДА про викладання у мистецьких школах Києва українською мовою. Експертка з комунікацій та менеджерка соціальних, освітніх та культурних проєктів. Конкретніше: консультантка з розробки та створення телевізійного шоу про нові об’єднані територіальні громади, головний редактор програми журналістських розслідувань «Брат за брата», редактор розважальних програм Новий канал, впродовж десяти років журналістка та ведуча програми новин «Репортер» Новий канал. Хочу дізнатись, що зміниться, якщо в Україні всі розмовлятимуть українською? Чи змінить це ставлення громадян до своєї країни? Чи стане молодь більше поважати свій край?</p>")
                        .picture("data_for_db/aboutUs/profile_222.jpg")
                        .video(null)
                        .type(3L)
                        .number(3L).build(),
                AboutUsItemProfile.builder()
                        .text("<span class=\"chapter\">Іванна Кобєлєва</span>" +
                                "<p><span class=\"highlight\">Комунікаційна менеджерка Ініціативи \"Навчай українською\"</span></p>" +
                                "<p>Комунікаційна менеджерка Ініціативи \"Навчай українською\". Редакторка волонтерського проєкту Портал мовної політики. Учасниця спільноти \"Мова об'єднує\", яка займається адвокацією закону \"Про забезпечення функціонування української мови як державної\". Мріє, щоб ті, хто говорить українською, не почували себе дискримінованими.</p>")
                        .picture("data_for_db/aboutUs/profile_111.jpg")
                        .video(null)
                        .type(4L)
                        .number(5L).build(),
                AboutUsItemProfile.builder()
                        .text("Ініціатива \"Навчай українською\" закликає викладачів спортивних секцій, тренерів навчати дітей українською мовою.")
                        .picture(null)
                        .video(null)
                        .type(1L)
                        .number(6L).build(),
                AboutUsItemProfile.builder()
                        .text("<p>Із 16 січня 2021 набуває чинності стаття 30 закону “Про забезпечення функціонування української мови як державної” про державну мову у сфері обслуговування споживачів. З 16 січня всі надавачі послуг, незалежно від форми власності, зобов’язані обслуговувати споживачів і надавати інформацію про товари і послуги державною мовою. Громадяни мають право отримати освітні послуги українською мовою у закладах позашкільної освіти.</p>")
                        .picture(null)
                        .video(null)
                        .type(2L)
                        .number(7L).build(),
                AboutUsItemProfile.builder()
                        .text(null)
                        .picture(null)
                        .video("<a>https://www.youtube.com/watch?v=i3umBFqDznQ&t=3s</a>")
                        .type(5L)
                        .number(8L).build(),
                AboutUsItemProfile.builder()
                        .text("Амбасадори проєкту")
                        .picture(null)
                        .video(null)
                        .type(1L)
                        .number(9L).build(),
                AboutUsItemProfile.builder()
                        .text("<p>\"Я сьогодні бачу в своїй стрічці багато суму через бар’єри, які доводиться долати, аби виховувати дітей рідною мовою. І дуже вірю, що скоро одним викликом, як то мова позашкільної освіти, стане менше! Повірте, батьки дуже люблять, коли їхніх дітей хтось вчить українською мовою, коли вони мають цю можливість. Попит на те, щоб ви викладали українською мовою, величезний і дуже сильно недооцінений. Думаю, керівники гуртків це відчують, коли почнуть переходити на українську мову”, - сказала ведуча “1+1” Марічка Падалко під час онлайн-зустрічі “Українська історія зірок”. Ця зустріч була стартом першого 21-денного челенджу “Навчай українською” для викладачів дитячих гуртків, секцій та студій, які переходять на українську мову викладання. </p>")
                        .picture("data_for_db/aboutUs/Marichka_Padalkooo.png")
                        .video(null)
                        .type(3L)
                        .number(10L).build(),
                AboutUsItemProfile.builder()
                        .text("<p>«Я прошу всіх викладачів, хто не байдужий до долі цієї країни і дітей переходити на українську. Бо діти будуть обмежені в своєму розвитку, якщо закон України про мову не буде виконуватися. Не можна жити в Швеції не знаючи шведської мови, не можна жити в ОАЕ не знаючи арабської мови. Можливо, раніше можна було жити в Україні і спілкуватись тільки російською, але ті часи давно пройшли. До того ж, слава Богу, ми не росіяни», - закликав освітян викладати дітям українською продюсер і шоумен Ігор Кондратюк.</p>")
                        .picture("data_for_db/aboutUs/about_img_11.jpg")
                        .video(null)
                        .type(4L)
                        .number(11L).build(),
                AboutUsItemProfile.builder()
                        .text("<p>Лідер гурту \"Антитіла\" Тарас Тополя закликав освітян викладати дітям українською: «Коли ми говоримо про людину як соціальну істоту, яка кодується, – ми маємо думати про те, як буде закодоване майбутнє покоління. І тут мова має визначне значення. Якщо б ми використовували латинку, а не кирилицю нам би було в тисячу разів легше протистояти російській інформаційній війні, яка ведеться проти нас. Звісно, ми не маємо права ображати людей, які користуються російською мовою, але ми маємо право і етичне зобов’язання перед майбутнім поколінням закодувати його на українську мову. Щоб вона стала потрібною і цікавою для нього»</p>")
                        .picture("data_for_db/aboutUs/about_img_22.jpg")
                        .video(null)
                        .type(3L)
                        .number(12L).build(),
                AboutUsItemProfile.builder()
                        .text("<p>Тенісист Сергій Стаховський закликав освітян викладати дітям українською: «З 2014 року я всі свої інтерв’ю даю українською мовою, бо вважаю, що ми маємо нею пишатися. Українська мова є нашою ідентичністю. Мені хотілося б, щоб результатом цього челенджу було те, що діти, які займаються в гуртках і, в тому числі, спортивних секціях у майбутньому представляли свою країну в світі українською мовою, бо це майбутні чемпіони».</p>")
                        .picture("data_for_db/aboutUs/about_img_33.png")
                        .video(null)
                        .type(4L)
                        .number(13L).build(),
                AboutUsItemProfile.builder()
                        .text("Відгуки учасників челенджу")
                        .picture(null)
                        .video(null)
                        .type(1L)
                        .number(14L).build(),
                AboutUsItemProfile.builder()
                        .text("<p>\uD83C\uDF31 Я змогла взяти для себе чимало важливих професійних порад щодо ведення занять рідною мовою, доречного вживання фахової термінології тощо. Дуже сподобалося, що модератори змогли створити легку невимушену атмосферу нашого навчання, що ми могли спілкуватися безпосередньо з нашими професійними наставниками та колегами. Вікторія Оленяк, керівниця ансамблю народного танцю “Дружба”, Центр творчості дітей та юнацтва Олександрівського району Запорізької міськради.</p>" +
                                "<p>\uD83C\uDF31 Станція юних техніків №3 міста Харкова висловлює щиру подяку модераторам челенджу \"Навчай українською\". Ми отримали корисний досвід спілкування українською, поглибили знання з мови та познайомилися з чудовими людьми. Дарина Щипанова, м. Харків.</p>" +
                                "<p>\uD83C\uDF31Дякую організаторам за чудовий челендж! Страху не було, були очікування, які цілком виправдались. Нові знання та знайомства, безліч корисних посилань, доброзичлива атмосфера спілкування. Отримані знання (особливо ігри) застосовував при роботі з дітьми, і їм дуже сподобалось. Вже самі просили ще раз провести ігри \uD83D\uDE0A Щиро дякую! Олександр Ємець, м. Дніпро.</p>" +
                                "<p>\uD83C\uDF31 За час челенджу я удосконалила проведення занять українською мовою. Почала в чаті писати дітям домашнє завдання українською та спілкуватися з друзями, дивитись мультфільми та фільми, читаю дитячі вірші та казки. Стала впевнено себе почувати: зараз спілкуюсь у супермаркеті та аптеці. Переклала професійні терміни, поліпшила знання з правопису. Дякую за плідну працю та надання матеріалів для роботи, за корисні вебінари. Хочу ще. Юлія Матвеєнко, м. Харків, Центр позашкільної освіти \"Мрія\".</p>")
                        .picture(null)
                        .video(null)
                        .type(2L)
                        .number(15L).build()

        );
    }
}
