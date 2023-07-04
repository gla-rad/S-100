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

import _int.iho.s100.catalog._5_0.*;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class S100ExchangeSetUtilsTest {

    // Test Variables
    private S100ExchangeCatalogue s100ExchangeCatalogue;
    private String s100ExchangeSetXml;


    // Fixed Variables
    private org.iso.standards.iso._19115.__3.gco._1.ObjectFactory gcoObjectFactory = new org.iso.standards.iso._19115.__3.gco._1.ObjectFactory();
    private org.iso.standards.iso._19115.__3.lan._1.ObjectFactory lanObjectFactory = new org.iso.standards.iso._19115.__3.lan._1.ObjectFactory();
    private String isoType = "ISO 19103:2015";
    private DateTimeFormatter dateFormat = DateTimeFormatter.ISO_DATE;
    private DateTimeFormatter dateTimeFormat = DateTimeFormatter.ISO_DATE_TIME;

    /**
     * Common setup for all the tests.
     */
    @BeforeEach
    void setup() throws IOException, ParseException {
        final InputStream in = ClassLoader.getSystemResourceAsStream("s100-exchange-set.xml");
        this.s100ExchangeSetXml = IOUtils.toString(in, StandardCharsets.UTF_8.name());

        // Initialise the Dataset
        this.s100ExchangeCatalogue = new S100ExchangeCatalogue();
        this.s100ExchangeCatalogue.setDataServerIdentifier("Test Exchange Set");
        this.s100ExchangeCatalogue.setExchangeCatalogueDescription(S100ExchangeSetUtils.createCharacterString("Test Exchange Set Description."));
        this.s100ExchangeCatalogue.setExchangeCatalogueComment(S100ExchangeSetUtils.createCharacterString("Test Exchange Set Comment."));

        //====================================================================//
        //           The S100 Exchange Catalogue Identifier                   //
        //====================================================================//
        S100ExchangeCatalogueIdentifier s100ExchangeCatalogueIdentifier = new S100ExchangeCatalogueIdentifier();
        s100ExchangeCatalogueIdentifier.setIdentifier(UUID.fromString("2d7c8116-75a9-4fb8-b1b3-7a698d416b97").toString());
        s100ExchangeCatalogueIdentifier.setDateTime(LocalDateTime.parse("2001-01-01T00:00:00.000Z", this.dateTimeFormat));
        this.s100ExchangeCatalogue.setIdentifier(s100ExchangeCatalogueIdentifier);

        //====================================================================//
        //             The S100 Exchange Catalogue Contact                    //
        //====================================================================//
        S100CataloguePointofContact s100CataloguePointofContact = new S100CataloguePointofContact();
        s100CataloguePointofContact.setOrganization(S100ExchangeSetUtils.createCharacterString("GRAD"));
        S100AddressType s100AddressType = new S100AddressType();
        s100AddressType.setUuid(UUID.fromString("40267999-1ee4-4633-b32e-899031207e1f").toString());
        s100AddressType.setIsoType(this.isoType);
        s100AddressType.setId("Test Address");
        s100AddressType.setCity(S100ExchangeSetUtils.createCharacterString("Harwich"));
        s100AddressType.setCountry(S100ExchangeSetUtils.createCharacterString("UK"));
        s100AddressType.setAdministrativeArea(S100ExchangeSetUtils.createCharacterString("England and Wales"));
        s100CataloguePointofContact.setAddress(s100AddressType);
        S100PhoneType s100PhoneType = new S100PhoneType();
        s100PhoneType.setIsoType(this.isoType);
        s100PhoneType.setId("Test Phone");
        s100PhoneType.setUuid(UUID.fromString("4f92d85a-fd3d-4438-8983-dac15c14d435").toString());
        s100PhoneType.setNumber(S100ExchangeSetUtils.createCharacterString("01255245000"));
        s100CataloguePointofContact.setPhone(s100PhoneType);
        this.s100ExchangeCatalogue.setContact(s100CataloguePointofContact);

        //====================================================================//
        //         The S100 Exchange Catalogue product Specification          //
        //====================================================================//
        S100ProductSpecification s100ProductSpecification = new S100ProductSpecification();
        s100ProductSpecification.setName("S-125");
        s100ProductSpecification.setProductIdentifier("S-125");
        s100ProductSpecification.setNumber(BigInteger.ONE);
        s100ProductSpecification.setDate(LocalDate.parse("2022-10-22", this.dateFormat));
        s100ProductSpecification.setCompliancyCategory(S100CompliancyCategory.CATEGORY_1);
        this.s100ExchangeCatalogue.setProductSpecifications(Collections.singletonList(s100ProductSpecification));


        //====================================================================//
        //           The S100 Exchange Catalogue Discovery Metadata           //
        //====================================================================//
        S100ExchangeCatalogue.DatasetDiscoveryMetadata datasetDiscoveryMetadata = new S100ExchangeCatalogue.DatasetDiscoveryMetadata();
        this.s100ExchangeCatalogue.setDatasetDiscoveryMetadata(datasetDiscoveryMetadata);
    }

    /**
     * Test that we can create (marshall) and XML based on an S-125 Dataset type
     * object.
     *
     * @throws JAXBException a JAXB exception thrown during the marshalling operation
     */
    @Test
    void testMarchallS125() throws JAXBException {
        String xml = S100ExchangeSetUtils.marshalS125(this.s100ExchangeCatalogue);
        assertNotNull(xml);
        assertEquals(this.s100ExchangeSetXml, xml);
    }

    /**
     * Test that we can generate (unmarshall) an S-125 POJO based on a valid
     * XML S-125 Dataset.
     *
     * @throws IOException any IO exceptions while reading the input XML file
     * @throws JAXBException a JAXB exception thrown during the unmarshalling operation
     */
    @Test
    void testUnmarshalS125() throws JAXBException {
        // Unmarshall it to a G1128 service instance object
        S100ExchangeCatalogue result = S100ExchangeSetUtils.unmarshallS125(this.s100ExchangeSetXml);

        // Assert all information is correct
        assertNotNull(result);
        assertEquals(this.s100ExchangeCatalogue.getDataServerIdentifier(), result.getDataServerIdentifier());
        assertNotNull( result.getExchangeCatalogueDescription());
        assertNotNull( result.getExchangeCatalogueDescription().getCharacterString());
        assertEquals(this.s100ExchangeCatalogue.getExchangeCatalogueDescription().getCharacterString().getValue(), result.getExchangeCatalogueDescription().getCharacterString().getValue());
        assertNotNull( result.getExchangeCatalogueComment());
        assertNotNull( result.getExchangeCatalogueComment().getCharacterString());
        assertEquals(this.s100ExchangeCatalogue.getExchangeCatalogueComment().getCharacterString().getValue(), result.getExchangeCatalogueComment().getCharacterString().getValue());
    }

}