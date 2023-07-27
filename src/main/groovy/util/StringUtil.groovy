package util

import grails.gorm.transactions.Transactional
import java.security.SecureRandom

@Transactional(readOnly = true)
class StringUtil {

    def static randomString(Integer length) {
        String alphabet = (('A'..'Z') + ('0'..'9')).join('')
        new SecureRandom().with {
            (1..length).collect { alphabet[nextInt(alphabet.length())] }.join('')
        }
    }

    static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1)
    }

    static String capitalizeFirstLetterAndTheRestLower(String original) {
        if (original == null || original.length() == 0) {
            return original
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase()
    }

    static String firstPartThenDots(String original, Integer max = null) {
        Integer maxLen = Math.max((max ?: 40), 4)
        String firstPartString = original
        if (original && original.length() > maxLen) {
            firstPartString = firstPartString.substring(0, (maxLen - 3)) + '...'
        }
        return firstPartString
    }

    // return the rightmost part of a string
    static String subRight(String original, Integer length) {
        String rightString = original

        if (original && length && original.length() >= length) {
            rightString = original.reverse().substring(0, length).reverse()
        }

        return rightString

    }

    static Long range(Long value, Long minValue, Long maxValue) {
        // Zero if null
        value = value ?: 0
        // At least this value
        value = Math.max(value, minValue)
        // No higher than this value
        value = Math.min(value, maxValue)
        return value
    }

}
