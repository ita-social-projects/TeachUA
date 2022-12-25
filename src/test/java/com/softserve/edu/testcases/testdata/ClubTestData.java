package com.softserve.edu.testcases.testdata;

import com.softserve.edu.models.placeholder.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ClubTestData {

    public ClubTestData() {
        // Default constructor
    }

    public ClubResponseDto randomClub() {
        return ClubResponseDto.newBuilder()
                .withId(55)
                .withAgeFrom(4)
                .withAgeTo(16)
                .withName("Мистецька школа N12 м. Одеси")
                .withDescription("{\"blocks\":[{\"key\":\"etag9\",\"text\":\"\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"8lltb\",\"text\":\" \",\"type\":\"atomic\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[{\"offset\":0,\"length\":1,\"key\":0}],\"data\":{}},{\"key\":\"98dtl\",\"text\":\"\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"9q9dc\",\"text\":\"КЗПСО ''Мистецька школа N12 м. Одеси''. Напрямки: музичний, вокальний, хореографічний, театральний, декоративно-ужитковий. \",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}}],\"entityMap\":{\"0\":{\"type\":\"image\",\"mutability\":\"IMMUTABLE\",\"data\":{\"src\":\"https://linguapedia.info/wp-content/uploads/2015/05/history-of-ukrainian.jpg\",\"className\":\"edited-image edited-image-center\"}}}}")
                .withUrlWeb(null)
                .withUrlLogo("#")
                .withUrlBackground("/static/images/club/bg_2.png")
                .withUrlGallery(new ArrayList<>())
                .withWorkTime(null)
                .withCategories(new ArrayList<>(Arrays.asList(new CategoryDto()
                        .setId(6)
                        .setSortby(25)
                        .setName("Вокальна студія, музика, музичні інструменти")
                        .setDescription("Музична школа, хор, ансамбль, гра на музичних інструментах, звукорежисерський гурток та ін.")
                        .setUrlLogo("/static/images/categories/music.svg")
                        .setBackgroundColor("#FF7A45")
                        .setTagBackgroundColor("#FF7A45")
                        .setTagTextColor("#fff"), new CategoryDto()
                        .setId(7)
                        .setSortby(30)
                        .setName("Акторська майстерність, театр")
                        .setDescription("Театральна студія, ляльковий театр, акторська майстерність")
                        .setUrlLogo("/static/images/categories/theatre.svg")
                        .setBackgroundColor("#FF4D4F")
                        .setTagBackgroundColor("#FF4D4F")
                        .setTagTextColor("#fff"), new CategoryDto()
                        .setId(12)
                        .setSortby(33)
                        .setName("Вчіться, діти")
                        .setDescription("description aaa bbb")
                        .setUrlLogo("/static/images/categories/tv.svg")
                        .setBackgroundColor("#13C2C2")
                        .setTagBackgroundColor("#a34d4d")
                        .setTagTextColor("#fff"), new CategoryDto()
                        .setId(2)
                        .setSortby(5)
                        .setName("Танці, хореографія")
                        .setDescription("Класичні і народні танці, брейк-данс, степ, контемп, балет та ін.")
                        .setUrlLogo("/static/images/categories/dance.svg")
                        .setBackgroundColor("#a8aa1d")
                        .setTagBackgroundColor("#9cad14")
                        .setTagTextColor("#eeefe6"))))
                .withUser(null)
                .withCenter(null)
                .withRating(0.0)
                .withLocations(new ArrayList<>(Arrays.asList(new LocationResponseDto()
                        .setId(517)
                        .setName("club_loc_!!!")
                        .setAddress("Одеса, вул.Балківська 42/1")
                        .setCityName("Одеса")
                        .setDistrictName("Суворовський")
                        .setStationName(null)
                        .setLocationCity(new CityResponseDto()
                                .setId(4)
                                .setName("Одеса")
                                .setLatitude(46.4825)
                                .setLongitude(30.7233))
                        .setCityId(4)
                        .setDistrictId(27)
                        .setStationId(null)
                        .setClubId(55)
                        .setCoordinates(null)
                        .setLongitude(30.705851128807083)
                        .setLatitude(46.48663036237369)
                        .setPhone(null), new LocationResponseDto()
                        .setId(114)
                        .setName("club_loc_!!!")
                        .setAddress("Одеса, вул.Балківська 42/1")
                        .setCityName("Одеса")
                        .setDistrictName("Суворовський")
                        .setStationName(null)
                        .setLocationCity(new CityResponseDto()
                                .setId(4)
                                .setName("Одеса")
                                .setLatitude(46.4825)
                                .setLongitude(30.7233))
                        .setCityId(4)
                        .setDistrictId(27)
                        .setStationId(null)
                        .setClubId(55)
                        .setCoordinates(null)
                        .setLongitude(30.705851128807083)
                        .setLatitude(46.48663036237369)
                        .setPhone(null), new LocationResponseDto()
                        .setId(860)
                        .setName("club_loc_!!!")
                        .setAddress("Одеса, вул.Балківська 42/1")
                        .setCityName("Одеса")
                        .setDistrictName("Суворовський")
                        .setStationName(null)
                        .setLocationCity(new CityResponseDto()
                                .setId(4)
                                .setName("Одеса")
                                .setLatitude(46.4825)
                                .setLongitude(30.7233))
                        .setCityId(4)
                        .setDistrictId(27)
                        .setStationId(null)
                        .setClubId(55)
                        .setCoordinates(null)
                        .setLongitude(30.705851128807083)
                        .setLatitude(46.48663036237369)
                        .setPhone(null))))
                .withIsApproved(null)
                .withIsOnline(null)
                .withFeedbackCount(0)
                .withContacts(new ArrayList<>(Arrays.asList(new ContactDataResponseDto()
                        .setContactType(new ContactTypeDto()
                                .setId(1)
                                .setName("Телефон")
                                .setUrlLogo("/static/images/contacts/phone.svg"))
                        .setContactData("0487322633"), new ContactDataResponseDto()
                                .setContactType(new ContactTypeDto()
                                .setId(6)
                                .setName("Contact")
                                .setUrlLogo("/static/images/contacts/website-link-icon.svg"))
                        .setContactData("http://art-school-12.com/"))))
                .build();
    }

    public CenterForClubDto certainCenter() {
        return new CenterForClubDto()
                .setId(11)
                .setName("INVENTOR СТЕМ-Школа")
                .setUrlBackgroundPicture(null)
                .setEmail(null)
                .setPhones(null)
                .setDescription("Ми розвиваємо у дітях вміння вирішувати конкретні задачі на уроці. Природничі науки, технології, інженерія, математика поєднуються в одному занятті для дітей уже з 3 років. Учні досліджують проблему, шукають шляхи її розв’язання, конструюють, програмують, проводять розрахунки та експерименти, роблять висновки та презентують отримані результати. Мета STEM-школи INVENTOR — допомогти кожному учневі знайти і розвинути власні таланти та нахили. Професійні педагоги не вирішують за дитину, а стимулюють її самостійно обрати заняття до душі сьогодні, завтра або в майбутньому. Заняття у STEM-школі IVENTOR містять чотири освітні складові: взаємозв’язок, конструювання, рефлексія та розвиток. Кожен курс у STEM-школі INVENTOR розвиває основні компетенції: креативність, критичне мислення, комунікабельність та науково-технічну грамотність.")
                .setUrlWeb("#")
                .setUrlLogo("https://www.logodesign.net/images/illustration-logo.png")
                .setSocialLinks(null)
                .setUser(null)
                .setContacts(null);
    }

    public ClubResponseDto clubMandatoryFields() {
        return ClubResponseDto.newBuilder()
                .withId(11)
                .withAgeFrom(6)
                .withAgeTo(16)
                .withName("Художнє відділення")
                .withDescription("{\"blocks\":[{\"key\":\"etag9\",\"text\":\"\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"8lltb\",\"text\":\" \",\"type\":\"atomic\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[{\"offset\":0,\"length\":1,\"key\":0}],\"data\":{}},{\"key\":\"98dtl\",\"text\":\"\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"9q9dc\",\"text\":\"Художнє відділення. Рисунок, живопис, композиція, скульптура, декоративно - прикладне мистецтво  \",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}}],\"entityMap\":{\"0\":{\"type\":\"image\",\"mutability\":\"IMMUTABLE\",\"data\":{\"src\":\"https://linguapedia.info/wp-content/uploads/2015/05/history-of-ukrainian.jpg\",\"className\":\"edited-image edited-image-center\"}}}}")
                .withCategories(new ArrayList<>(Collections.singletonList(new CategoryDto()
                        .setId(12)
                        .setSortby(33)
                        .setName("Вчіться, діти")
                        .setDescription("description aaa bbb")
                        .setUrlLogo("/static/images/categories/tv.svg")
                        .setBackgroundColor("#13C2C2")
                        .setTagBackgroundColor("#a34d4d")
                        .setTagTextColor("#fff"))))
                .withCenter(certainCenter())
                .withContacts(new ArrayList<>(Collections.singletonList(new ContactDataResponseDto()
                        .setContactType(new ContactTypeDto()
                                .setId(1)
                                .setName("Телефон")
                                .setUrlLogo("/static/images/contacts/phone.svg"))
                        .setContactData("3.80974404988E11"))))
                .build();
    }

}
