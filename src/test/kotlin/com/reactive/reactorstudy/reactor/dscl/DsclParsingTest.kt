package com.reactive.reactorstudy.reactor.dscl

import mu.KotlinLogging
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test

private val log = KotlinLogging.logger {}

class DsclParsingTest {

    private val url = "https://dis.kofia.or.kr/websquare/index.jsp?w2xPath=/wq/fundann/DISFTimeAnnStut.xml&divisionId=MDIS01002002000000&serviceId=SDIS01002002000"

    @Test
    fun parsingTest() {
        val tempUrl = "https://blog.kakaopay.com/"
        // given
        val doc = Jsoup.connect(tempUrl).get()

        // when
        val element = doc.select("body > div > main > div > div.recent-posts.astro-ALDJ6TZS > ul > li > a > h3")
        log.info { element }

        // then
    }
}
