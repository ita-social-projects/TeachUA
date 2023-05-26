package com.softserve.teachua.tools.repository;

import com.softserve.teachua.tools.transmodel.ChallengeTransfer;
import java.util.Collections;
import java.util.List;

public class ChallengeInfoRepository {
    private ChallengeInfoRepository() {
    }

    @SuppressWarnings("checkstyle:LineLength")
    public static List<ChallengeTransfer> getChallengesList() {
        return Collections.singletonList(
                ChallengeTransfer.builder().name("Клуб української мови \"Розмовляй\"").isActive(true)
                        .sortNumber(4L).picture("data_for_db/challengeImage/marathon.png")
                        .title("Клуб української мови \"Розмовляй\"").registrationLink("/speakingclub/registration")
                        .description("""
                                <h1><strong>Клуб української мови "Розмовляй"</strong></h1>\s
                                <h3><strong><em>Клуб української мови "Розмовляй"</em></strong></h3>\s
                                <p>Клуб української мови "Розмовляй" допоможе опанувати мовні практики, здолати мовні бар’єри, створити середовище підтримки та обміну досвідом між батьками дошкільнят, здобути необхідну лексичну базу українською мовою для повсякденного спілкування з дітьми.</p>\s
                                <p><br></p>\s
                                <p>Організатори - Ініціатива "Навчай українською" за підтримки Міністерства молоді та спорту України</p>\s
                                <p><br></p>\s
                                <p><a href="https://speak-ukrainian.org.ua/" rel="noopener noreferrer" target="_blank"> https://speak-ukrainian.org.ua/ </a></p>\s
                                <p><a href="https://www.facebook.com/teach.in.ukrainian" rel="noopener noreferrer" target="_blank"> https://www.facebook.com/teach.in.ukrainian </a></p>\s
                                <p><a href="mailto:teach.in.ukrainian@gmail.com" rel="noopener noreferrer" target="_blank"> teach.in.ukrainian@gmail.com </a></p>\s
                                <p>Українська гуманітарна платформа</p>""")
                        .build());
    }
}
