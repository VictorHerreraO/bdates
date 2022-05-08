package com.soyvictorherrera.bdates.modules.eventList.data.datasource

import org.json.JSONArray

fun JSONArray.forEach(action: (Any) -> Unit) {
    for (i in 0 until this.length()) {
        action(this.get(i))
    }
}
