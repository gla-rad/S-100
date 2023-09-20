/*
 * Copyright (c) 2023 GLA Research and Development Directorate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.grad.eNav.s100.utils;

import _int.iho.s100.catalog._5_0.S100ExchangeCatalogue;
import org.iso.standards.iso._19115.__3.gco._1.CharacterStringPropertyType;
import org.iso.standards.iso._19115.__3.gco._1.CodeListValueType;
import org.iso.standards.iso._19115.__3.gco._1.ObjectFactory;
import org.iso.standards.iso._19115.__3.lan._1.CountryCodePropertyType;
import org.iso.standards.iso._19115.__3.lan._1.LanguageCodePropertyType;
import org.iso.standards.iso._19115.__3.lan._1.MDCharacterSetCodePropertyType;
import org.iso.standards.iso._19115.__3.lan._1.PTLocaleType;

import javax.xml.bind.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * The S-100 Exchange Set Utility Class.
 * <p/>
 * A static utility function class that allows easily manipulation of the S-100
 * Exchange Sets.
 *
 * @author Nikolaos Vastardis (email: Nikolaos.Vastardis@gla-rad.org)
 */
public class S100ExchangeSetUtils {

    /**
     * The Language Code namespace/list for use in the language codes.
     */
    public static final String LANGUAGE_NAMESPACE = "ISO 639-2/T";
    public static final String LANGUAGE_LIST = null;

    /**
     * The Country Code namespace/list for use in the country codes.
     */
    public static final String COUNTRY_NAMESPACE = "ISO 3166-2";
    public static final String COUNTRY_LIST = null;

    /**
     * The Character Set Encoding namespace/list for use in the character set encoding codes.
     */
    public static final String CHARACTER_ENCODING_NAMESPACE = null;
    public static final String CHARACTER_ENCODING_LIST = "http://www.iana.org/assignments/character-sets";


    /**
     * A helper function to easily generate character string property types
     * that are used by the ISO standards.
     *
     * @param characterString The string to be transformed into a character string property type
     * @return The character string property type
     */
    public static CharacterStringPropertyType createCharacterStringPropertyType(String characterString) {
        final ObjectFactory objectFactory = new ObjectFactory();
        final CharacterStringPropertyType cspt = new CharacterStringPropertyType();
        cspt.setCharacterString(objectFactory.createCharacterString(characterString));
        return cspt;
    }

    /**
     * A helper function to easily generate a list of character string property
     * types that are used by the ISO standards.
     *
     * @param characterStringList  The list of string to be transformed into a list of character string property type
     * @return The list of character string property type
     */
    public static List<CharacterStringPropertyType> createCharacterStringPropertyTypeList(List<String> characterStringList) {
        return Optional.ofNullable(characterStringList)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(S100ExchangeSetUtils::createCharacterStringPropertyType)
                .toList();
    }

    /**
     * For a given code list type class, a code list value and the actual input
     * this helper function will generate a code list value object.
     *
     * @param list the list location of the code list value type
     * @param space the space of the code list value type
     * @param code the code of the code list value type
     * @param input the actual value of the code list value type
     * @return the populated code list value type object
     */
    public static CodeListValueType createCodeListValueType(String list, String space, String code, String input) {
        final CodeListValueType codeListValueType = new CodeListValueType();
        codeListValueType.setCodeList(list);
        codeListValueType.setCodeSpace(space);
        codeListValueType.setCodeListValue(code);
        codeListValueType.setValue(input);
        return codeListValueType;
    }

    /**
     * Based on the provided Java Locale this function will generate the S-100
     * PTLocaleType object and return it.
     *
     * @param locale the java locale object
     * @return the S-100 respective PTLocaleType object
     */
    public static PTLocaleType createPTLocaleType(Locale locale) {
        // Sanity check
        if(Objects.isNull(locale)) {
            return null;
        }

        // Otherwise continue
        final PTLocaleType ptLocaleValue = new PTLocaleType();
        LanguageCodePropertyType languageCodePropertyType = new LanguageCodePropertyType();
        languageCodePropertyType.setLanguageCode(S100ExchangeSetUtils.createCodeListValueType(
                LANGUAGE_LIST,
                LANGUAGE_NAMESPACE,
                locale.getISO3Language(),
                locale.getDisplayLanguage()));
        ptLocaleValue.setLanguage(languageCodePropertyType);
        CountryCodePropertyType countryCodePropertyType = new CountryCodePropertyType();
        countryCodePropertyType.setCountryCode(S100ExchangeSetUtils.createCodeListValueType(
                COUNTRY_LIST,
                COUNTRY_NAMESPACE,
                locale.getISO3Country(),
                locale.getDisplayCountry()));
        ptLocaleValue.setCountry(countryCodePropertyType);
        MDCharacterSetCodePropertyType mdCharacterSetCodePropertyType = new MDCharacterSetCodePropertyType();
        mdCharacterSetCodePropertyType.setMDCharacterSetCode(S100ExchangeSetUtils.createCodeListValueType(
                CHARACTER_ENCODING_LIST,
                CHARACTER_ENCODING_NAMESPACE,
                StandardCharsets.UTF_8.name(),
                StandardCharsets.UTF_8.displayName()));
        ptLocaleValue.setCharacterEncoding(mdCharacterSetCodePropertyType);
        return ptLocaleValue;
    }

    /**
     * Overloading the S100ExchangeSet marshalling operation to easily perform
     * the task with the formatting turned on by default.
     *
     * @param s100ExchangeCatalogue the S100 Exchange Set Catalogue object
     * @return the marshalled S100 Exchange Set Catalogue XML representation
     * @throws JAXBException for errors in the marshalling operation
     */
    public static String marshalS100ExchangeSetCatalogue(S100ExchangeCatalogue s100ExchangeCatalogue) throws JAXBException {
        return marshalS100ExchangeSetCatalogue(s100ExchangeCatalogue, Boolean.TRUE);
    }

    /**
     * Using the S100ExchangeSet utilities we can marshall S100ExchangeCatalogue
     * objects to the XML representation.
     *
     * @param s100ExchangeCatalogue the S100 Exchange Set Catalogue object
     * @param format whether to format the XML string
     * @return the marshalled S100 Exchange Set Catalogue XML representation
     * @throws JAXBException for errors in the marshalling operation
     */
    public static String marshalS100ExchangeSetCatalogue(S100ExchangeCatalogue s100ExchangeCatalogue, Boolean format) throws JAXBException {
        // Create the JAXB objects
        JAXBContext jaxbContext = JAXBContext.newInstance(S100ExchangeCatalogue.class.getPackageName(), S100ExchangeCatalogue.class.getClassLoader());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);

        // Transform the S100 Exchange Set Catalogue object to an output stream
        ByteArrayOutputStream xmlStream = new ByteArrayOutputStream();
        jaxbMarshaller.marshal(s100ExchangeCatalogue, xmlStream);

        // Return the XML string
        return xmlStream.toString();
    }

    /**
     * The character string input object contains the XML content of the S100
     * Exchange Set Catalogue. We can easily translate that into an
     * S100ExchangeCatalogue object so that it can be accessed more efficiently.
     *
     * @param s100ExchangeCatalogue the S100 Exchange Set Catalogue XML content
     * @return The unmarshalled 100 Exchange Set Catalogue object
     * @throws JAXBException for errors in the unmarshalling operation
     */
    public static S100ExchangeCatalogue unmarshallS100ExchangeSetCatalogue(String s100ExchangeCatalogue) throws JAXBException {
        // Create the JAXB objects
        JAXBContext jaxbContext = JAXBContext.newInstance(S100ExchangeCatalogue.class.getPackageName(), S100ExchangeCatalogue.class.getClassLoader());
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        // Transform the S100 Exchange Set Catalogue context into an input stream
        ByteArrayInputStream is = new ByteArrayInputStream(s100ExchangeCatalogue.getBytes());

        // And translate
        return (S100ExchangeCatalogue) JAXBIntrospector.getValue(jaxbUnmarshaller.unmarshal(is));
    }

}
