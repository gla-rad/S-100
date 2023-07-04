/*
 * Copyright (c) 2022 GLA Research and Development Directorate
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
import org.iso.standards.iso._19115.__3.gco._1.ObjectFactory;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * The S-100 Utility Class.
 *
 * A static utility function class that allows easily manipulation of the S-100
 * messages.
 *
 * @author Nikolaos Vastardis (email: Nikolaos.Vastardis@gla-rad.org)
 */
public class S100ExchangeSetUtils {

    /**
     * A helper function to easily generate characyer string property types
     * that are used by the ISO standards.
     *
     * @param characterString   The string to be tranformed into a character string property type
     * @return The character string property type
     */
    public static CharacterStringPropertyType createCharacterString(String characterString) {
        final ObjectFactory objectFactory = new ObjectFactory();
        final CharacterStringPropertyType cspt = new CharacterStringPropertyType();
        cspt.setCharacterString(objectFactory.createCharacterString(characterString));
        return cspt;
    }

    /**
     * Overloading the S-125 marshalling operation to easily perform the task
     * with the formatting turned on by default.
     *
     * @param s100ExchangeCatalogue the Service Instance object
     * @return the marshalled S125 message XML representation
     */
    public static String marshalS125(S100ExchangeCatalogue s100ExchangeCatalogue) throws JAXBException {
        return marshalS125(s100ExchangeCatalogue, Boolean.TRUE);
    }

    /**
     * Using the S125 utilities we can marshall back an S125 DatasetType
     * object it's XML view.
     *
     * @param s100ExchangeCatalogue the Service Instance object
     * @param format whether to format the XML string
     * @return the marshalled S125 message XML representation
     */
    public static String marshalS125(S100ExchangeCatalogue s100ExchangeCatalogue, Boolean format) throws JAXBException {
        // Create the JAXB objects
        JAXBContext jaxbContext = JAXBContext.newInstance(S100ExchangeCatalogue.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);

        // Transform the S-125 object to an output stream
        ByteArrayOutputStream xmlStream = new ByteArrayOutputStream();
        QName qName = new QName("_int.iho.s100.catalog._5_0", "S100ExchangeCatalogue");
        JAXBElement<S100ExchangeCatalogue> root = new JAXBElement<S100ExchangeCatalogue>(qName, S100ExchangeCatalogue.class, s100ExchangeCatalogue);
        jaxbMarshaller.marshal(s100ExchangeCatalogue, xmlStream);

        // Return the XML string
        return xmlStream.toString();
    }

    /**
     * The S125Node object contains the S125 XML content of the message. We
     * can easily translate that into an S125 DatasetType object so that it
     * can be accessed more efficiently.
     *
     * @param s100ExchangeCatalogue the S125 message content
     * @return The unmarshalled S125 DatasetType object
     * @throws JAXBException
     */
    public static S100ExchangeCatalogue unmarshallS125(String s100ExchangeCatalogue) throws JAXBException {
        // Create the JAXB objects
        JAXBContext jaxbContext = JAXBContext.newInstance(S100ExchangeCatalogue.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        // Transform the S125 context into an input stream
        ByteArrayInputStream is = new ByteArrayInputStream(s100ExchangeCatalogue.getBytes());

        // And translate
        return (S100ExchangeCatalogue) JAXBIntrospector.getValue(jaxbUnmarshaller.unmarshal(is));
    }

}
