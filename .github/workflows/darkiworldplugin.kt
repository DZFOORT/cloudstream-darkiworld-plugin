package com.cloudstream.darkiworld

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.Jsoup

class DarkiWorldPlugin : MainAPI() {
    override var mainUrl = "https://darkiworld2.com"
    override var name = "DarkiWorld"
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    // Fonction pour rechercher des films/séries
    override suspend fun getSearchResults(query: String): List<SearchResponse> {
        val document = Jsoup.connect("$mainUrl/search?q=$query").get()
        val results = mutableListOf<SearchResponse>()

        // Exemple : Extraire les résultats de recherche
        document.select("div.movie-item").forEach { element ->
            val title = element.select("h2").text()
            val url = element.select("a").attr("href")
            val poster = element.select("img").attr("src")

            results.add(
                MovieSearchResponse(
                    name = title,
                    url = "$mainUrl$url",
                    apiName = this.name,
                    type = TvType.Movie,
                    posterUrl = poster
                )
            )
        }

        return results
    }

    // Fonction pour charger les détails d'un film/série
    override suspend fun load(url: String): LoadResponse {
        val document = Jsoup.connect(url).get()

        // Exemple : Extraire les détails
        val title = document.select("h1.title").text()
        val plot = document.select("div.plot").text()
        val poster = document.select("img.poster").attr("src")
        val year = document.select("span.year").text().toIntOrNull()

        // Extraire les liens de streaming
        val videoLinks = extractVideoLinks(document)

        return MovieLoadResponse(
            name = title,
            url = url,
            apiName = this.name,
            plot = plot,
            posterUrl = poster,
            year = year,
            episodes = listOf(
                // Ajouter un épisode avec les liens de streaming
                Episode(
                    name = "Episode 1",
                    data = videoLinks.joinToString("\n"), // Stocker les liens dans `data`
                )
            )
        )
    }

    // Fonction pour extraire les liens de streaming
    private fun extractVideoLinks(document: org.jsoup.nodes.Document): List<String> {
        val links = mutableListOf<String>()

        // Exemple : Extraire les liens des balises <iframe>
        document.select("iframe").forEach { iframe ->
            val src = iframe.attr("src")
            if (src.isNotEmpty()) {
                links.add(src)
            }
        }

        // Exemple : Extraire les liens des balises <video>
        document.select("video source").forEach { source ->
            val src = source.attr("src")
            if (src.isNotEmpty()) {
                links.add(src)
            }
        }

        return links
    }
}