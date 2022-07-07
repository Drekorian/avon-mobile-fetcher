package cz.drekorian.avonmobilefetcher.resources

/**
 * This interface denotes the common behavior for resource classes.
 *
 * @author Marek Osvald
 */
interface Resources {

    /**
     * Returns localizes string resource key.
     *
     * @param key resource string key
     */
    operator fun get(key: String): String = "key_not_found<$key>"
}
