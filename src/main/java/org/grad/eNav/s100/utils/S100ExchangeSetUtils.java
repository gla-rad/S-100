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

import javax.xml.bind.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The S-100 Exchange Set Utility Class.
 *
 * A static utility function class that allows easily manipulation of the S-100
 * Exchange Sets.
 *
 * @author Nikolaos Vastardis (email: Nikolaos.Vastardis@gla-rad.org)
 */
public class S100ExchangeSetUtils {

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
    public static CodeListValueType generateCodeListValueType(String list, String space, String code, String input) {
        final CodeListValueType codeListValueType = new CodeListValueType();
        codeListValueType.setCodeList(list);
        codeListValueType.setCodeSpace(space);
        codeListValueType.setCodeListValue(code);
        codeListValueType.setValue(input);
        return codeListValueType;
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
