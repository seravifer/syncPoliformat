package data.network.scrapping

import pl.droidsonroids.jspoon.annotation.Selector
import utils.Utils

class SubjectsList {
    @Selector("div.upv_listasimple > p:not(.upv_listanon)") lateinit var subjects: List<Subject>
}

class Subject {
    @Selector("a") lateinit var _name: String
    @Selector("a span") lateinit var id: String

    val name: String by lazy {
        _name.takeWhile { it != '(' }.trim()
    }
    val graId: String by lazy {
        val nId = id.substring(1 until id.indexOf(','))
        "GRA_${nId}_${Utils.curso}"
    }
}