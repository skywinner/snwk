package util

import groovy.time.TimeCategory
import spock.lang.Specification

class StringUtilSpec extends Specification {

    void "a random string is returned of the right length"() {

        given: "expectations of the result"
        def characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        def maxLength = 255

        when: "a string is generated"
        String generated = ''
        (1..maxLength).each { len ->
            generated = StringUtil.randomString(len)

            then: "the length is correct"
            generated.size() == len
            generated.chars.each { Character character ->

                then: "the character is included in expected result"
                characters.contains(character.toString())
            }
        }

        then: "everything is done"
        generated.size() == maxLength
    }

    void "capitalization of words"() {

        given: "expectations of the result"
        def expected = []
        expected.add(['jesper', 'Jesper'])
        expected.add(['', ''])
        expected.add([null, null])
        expected.add(['JESPER', 'JESPER'])
        expected.add(['1234///niZZe', '1234///niZZe'])

        when: "capitalization occurs"
        String after = ''
        expected.each { String given, String exp ->
            after = StringUtil.capitalizeFirstLetter(given)
            then: "the result comes out as expected"
            assert exp == after
        }

        then: 'done'
        expected

    }

    void "capitalization and lower of words"() {

        given: "expectations of the result"
        def expected = []
        expected.add(['jesper', 'Jesper'])
        expected.add(['', ''])
        expected.add([null, null])
        expected.add(['JESPER', 'Jesper'])
        expected.add(['1234///niZZe', '1234///nizze'])

        when: "capitalization occurs"
        String after = ''
        expected.each { String given, String exp ->
            after = StringUtil.capitalizeFirstLetterAndTheRestLower(given)
            then: "the result comes out as expected"
            assert exp == after
        }

        then: 'done'
        expected

    }

    void "rightmost substring of strings"() {

        given: "expectations of the result"
        def expected = []
        expected.add(['jesper', 'per'])
        expected.add(['', ''])
        expected.add([null, null])
        expected.add(['as', 'as'])
        expected.add(['1234', '234'])
        expected.add(['I am a walrus', 'rus'])

        when: "substring occurs"
        String after = ''
        expected.each { String given, String exp ->
            after = StringUtil.subRight(given, 3)
            then: "the result comes out as expected"
            assert exp == after
        }

        then: 'done'
        expected

    }

    void "first part of strings and then dots if long string"() {
        given: "expectations of the result"
        def expected = []
        expected.add(['jesper', null, 'jesper'])
        expected.add(['jesper', 3, 'j...'])
        expected.add(['jesper', 4, 'j...'])
        expected.add(['jesper', 5, 'je...'])
        expected.add(['jesper', 6, 'jesper'])
        expected.add(['jesper', 7, 'jesper'])
        expected.add(['A123456789B123456789c123456789d123456789e123456789f123456789', null, 'A123456789B123456789c123456789d123456...'])
        expected.add(['A123456789B123456789c123456789d123456789', null, 'A123456789B123456789c123456789d123456789'])
        expected.add(['A123456789B123456789c123456789d123456789e', null, 'A123456789B123456789c123456789d123456...'])

        when: "first part is derived"
        String after = ''
        expected.each { String given, Integer max, String exp ->
            after = StringUtil.firstPartThenDots(given, max)
            then: "the result comes out as expected"
            assert exp == after
        }

        then: 'done'
        expected
    }

    void "range for a Long value"() {
        given: "expectations of the result"
        def expected = []
        expected.add([0L, 1L, 7L, 1L])
        expected.add([3L, 1L, 7L, 3L])
        expected.add([9L, 1L, 7L, 7L])
        expected.add([null, 1L, 7L, 1L])
        expected.add([-20L, -10L, 100L, -10L])
        expected.add([0L, -10L, 100L, 0L])
        expected.add([101L, -10L, 100L, 100L])
        expected.add([null, -10L, 100L, 0L])

        when: "range is applied"
        Long after
        expected.each { Long value, Long minValue, Long maxValue, Long exp ->
            after = StringUtil.range(value, minValue, maxValue)
            then: "the result comes out as expected"
            assert exp == after
        }

        then: 'done'
        expected
    }

}
