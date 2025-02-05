package com.cloudstream.darkiworld

import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.Qualities

class DarkiWorldExtractor : ExtractorApi() {
    override val name = "DarkiWorld Extractor"
    override val mainUrl = "https://darkiworld2.com"

    // Fonction pour extraire les liens de streaming
    override suspend fun getUrl(url: String, referer: String?): List<ExtractorLink> {
        val links = mutableListOf<ExtractorLink>()

        // Exemple : Extraire les liens de streaming
        val document = app.get(url).document
        document.select("iframe").forEach { iframe ->
            val src = iframe.attr("src")
            if (src.isNotEmpty()) {
                links.add(
                    ExtractorLink(
                        name = "DarkiWorld",
                        url = src,
                        referer = mainUrl,
                        quality = Qualities.Unknown.value,
                        isM3u8 = src.contains("m3u8")
                    )
                )
            }
        }

        return links
    }
}