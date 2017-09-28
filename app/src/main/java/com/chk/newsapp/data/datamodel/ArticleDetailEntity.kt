package com.chk.newsapp.data.datamodel

import me.angrybyte.goose.images.Image
import org.jsoup.nodes.Element
import java.util.*

/**
 * Created by chira on 17-08-2017.
 */

class ArticleDetailEntity {

    /**
     * Holds the title of the web page
     */
    private val title: String? = null

    private val publishDate: Date? = null

    /**
     * holds the meta description meta tag in the html doc
     */
    private val metaDescription: String? = null

    /**
     * holds the clean text after we do strip out everything but the text and wrap it up in a nice package this is the guy you probably
     * want, just pure text
     */
    private val cleanedArticleText: String? = null

    /**
     * holds the original unmodified HTML that goose retrieved from the URL
     */
    private val rawHtml: String? = null

    /**
     * holds the meta keywords that would in the meta tag of the html doc
     */
    private val metaKeywords: String? = null

    /**
     * holds the meta data canonical link that may be place in the meta tags of the html doc
     */
    private val canonicalLink: String? = null

    /**
     * holds the domain of where the link came from. http://techcrunch.com/article/testme would be tech crunch.com as the domain
     */
    private val domain: String? = null

    /**
     * this represents the jSoup element that we think is the big content dude of this page we can use this node to start grabbing text,
     * images, etc.. around the content
     */
    private val topNode: Element? = null

    /**
     * if image extractor is enable this would hold the image we think is the best guess for the web page
     */
    private val topImage: Image? = null

    /**
     * holds an array of the image candidates we thought might perhaps we decent images related to the content
     */
    private val imageCandidates = ArrayList<String>()

    /**
     * holds a list of elements that related to youtube or vimeo movie embeds
     */
    private val movies: ArrayList<Element>? = null

    /**
     * holds a list of tags extracted from the article
     */
    private val tags: Set<String>? = null

    private val additionalData: Map<String, String>? = null
}
