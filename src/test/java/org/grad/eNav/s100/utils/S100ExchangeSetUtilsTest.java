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

import _int.iho.s100.catalog._5_0.*;
import org.apache.commons.io.IOUtils;
import org.grad.eNav.s100.enums.MaintenanceFrequency;
import org.grad.eNav.s100.enums.RoleCode;
import org.grad.eNav.s100.enums.SecurityClassification;
import org.grad.eNav.s100.enums.TelephoneType;
import org.iso.standards.iso._19115.__3.cit._2.*;
import org.iso.standards.iso._19115.__3.gco._1.CharacterStringPropertyType;
import org.iso.standards.iso._19115.__3.gco._1.CodeListValueType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class S100ExchangeSetUtilsTest {

    // Test Variables
    private S100ExchangeCatalogueBuilder s100ExchangeCatalogueBuilder;
    private S100DatasetDiscoveryMetadataBuilder s100DatasetDiscoveryMetadataBuilder;
    private S100ExchangeCatalogue s100ExchangeCatalogue;
    private String s100ExchangeSetXml;


    // Fixed Variables
    private org.iso.standards.iso._19115.__3.gco._1.ObjectFactory gcoObjectFactory = new org.iso.standards.iso._19115.__3.gco._1.ObjectFactory();
    private org.iso.standards.iso._19115.__3.lan._1.ObjectFactory lanObjectFactory = new org.iso.standards.iso._19115.__3.lan._1.ObjectFactory();
    private String isoType = "ISO 19103:2015";
    private DateTimeFormatter timeFormat = DateTimeFormatter.ISO_TIME;
    private DateTimeFormatter dateFormat = DateTimeFormatter.ISO_DATE;
    private DateTimeFormatter dateTimeFormat = DateTimeFormatter.ISO_DATE_TIME;

    /**
     * Common setup for all the tests.
     */
    @BeforeEach
    void setup() throws IOException, CertificateException, JAXBException {
        // Create an exchange set catalogue builder
        this.s100ExchangeCatalogueBuilder = new S100ExchangeCatalogueBuilder((id, algorithm, payload) -> {
            S100SEDigitalSignature s100SEDigitalSignature = new S100SEDigitalSignature();
            s100SEDigitalSignature.setId(id.toString());
            s100SEDigitalSignature.setCertificateRef("ref");
            s100SEDigitalSignature.setValue("signature".getBytes());
            return s100SEDigitalSignature;
        });

        // Create the medatata file builders
        this.s100DatasetDiscoveryMetadataBuilder = new S100DatasetDiscoveryMetadataBuilder((id, algorithm, payload) -> {
            S100SEDigitalSignature s100SEDigitalSignature = new S100SEDigitalSignature();
            s100SEDigitalSignature.setId(id.toString());
            s100SEDigitalSignature.setCertificateRef("ref");
            s100SEDigitalSignature.setValue("signature".getBytes());
            return s100SEDigitalSignature;
        });

        //====================================================================//
        //              Load a test Exchange Set Catalogue XML                //
        //====================================================================//
        final InputStream inES = ClassLoader.getSystemResourceAsStream("s100-exchange-set.xml");
        this.s100ExchangeSetXml = IOUtils.toString(inES, StandardCharsets.UTF_8);
        //====================================================================//

        //====================================================================//
        //                   Load a test X.509 Certificate                    //
        //====================================================================//
        final InputStream inCert = ClassLoader.getSystemResourceAsStream("test.pem");
        final String inString = new String(inCert.readAllBytes(), StandardCharsets.UTF_8);
        X509Certificate certificate = this.s100ExchangeCatalogueBuilder.getCertFromPem(inString.getBytes());
        //====================================================================//

        //====================================================================//
        //         The S100 Exchange Catalogue product Specification          //
        //====================================================================//
        final S100ProductSpecification s100ProductSpecification = new S100ProductSpecification();
        s100ProductSpecification.setName("S-125");
        s100ProductSpecification.setProductIdentifier("S-125");
        s100ProductSpecification.setNumber(BigInteger.ONE);
        s100ProductSpecification.setDate(LocalDate.parse("2022-10-22", this.dateFormat));
        s100ProductSpecification.setCompliancyCategory(S100CompliancyCategory.CATEGORY_1);
        //====================================================================//

        // Initialise the Dataset
        this.s100ExchangeCatalogue = this.s100ExchangeCatalogueBuilder
                .setIdentifier("Test Exchange Set")
                .setDataServerIdentifier("2d7c8116-75a9-4fb8-b1b3-7a698d416b97")
                .setOrganization("GRAD")
                .setElectronicMailAddresses(Collections.singletonList("test@gla-rad.org"))
                .setPhone("+44 1255 245000")
                .setPhoneType(TelephoneType.VOICE)
                .setCity("Harwich")
                .setPostalCode("postalCode")
                .setCountry("UK and Ireland")
                .setLocales(Collections.singletonList(Locale.UK))
                .setAdministrativeArea("England, Wales, Scotland and the whole of Ireland")
                .setDescription("Test Exchange Set Description.")
                .setComment("Test Exchange Set Comment.")
                .setProductSpecification(Collections.singletonList(s100ProductSpecification))
                .setCertificates(Collections.singletonMap("CRT1", certificate))
                .addDatasetMetadata(this.s100DatasetDiscoveryMetadataBuilder
                        .setFileName("file:/dataset.XML")
                        .setFileContent("dataset".getBytes())
                        .setDatasetID("urn:mrn:gla:grad:s125:datasets:XXXX")
                        .setDescription("description")
                        .setCompressionFlag(false)
                        .setDataProtection(false)
                        .setProtectionScheme(S100ProtectionScheme.S_100_P_15)
                        .setCopyright(true)
                        .setClassification(SecurityClassification.UNCLASSIFIED)
                        .setPurpose(S100Purpose.NEW_DATASET)
                        .setNotForNavigation(true)
                        .setSpecificUsage("testing")
                        .setEditionNumber(BigInteger.ONE)
                        .setUpdateNumber(BigInteger.ZERO)
                        .setUpdateApplicationDate(LocalDate.parse("2023-01-01", this.dateFormat))
                        .setIssueDate(LocalDate.parse("2023-01-02", this.dateFormat))
                        .setIssueTime(LocalTime.parse("00:00:00", this.timeFormat))
                        .setProductSpecification(null)
                        .setProducingAgency("producingAgency")
                        .setProducingAgencyRole(RoleCode.ORIGINATOR)
                        .setProducerCode("producerCode")
                        .setEncodingFormat(S100EncodingFormat.GML)
                        .setDataCoverages(null)
                        .setComment("comment")
                        .setMetadataDateStamp(LocalDate.parse("2023-01-03", this.dateFormat))
                        .setReplacedData(false)
                        .setNavigationPurposes(Collections.singletonList(S100NavigationPurpose.OVERVIEW))
                        .setMaintenanceFrequency(MaintenanceFrequency.CONTINUAL)
                        .setMaintenanceDate(LocalDate.parse("2023-01-03", this.dateFormat))
                        .setMaintenancePeriod(Duration.ofDays(100))
                        .build())
                .build();
    }

    /**
     * Test that we can successfully create a character string from a simple
     * Java string.
     */
    @Test
    void testCreateCharacterString() {
        CharacterStringPropertyType cspt = S100ExchangeSetUtils.createCharacterStringPropertyType("test");

        // Assert that the CharacterStringPropertyType is not empty and seems valid
        assertNotNull(cspt);
        assertNotNull(cspt.getCharacterString());
        assertEquals("test", cspt.getCharacterString().getValue());
    }

    /**
     * Test that for a null string the CharacterStringPropertyType generation
     * method will return an empty but valid object.
     */
    @Test
    void testCreateCharacterStringNull() {
        CharacterStringPropertyType cspt = S100ExchangeSetUtils.createCharacterStringPropertyType(null);

        // Assert that the CharacterStringPropertyType is not empty and seems valid
        assertNotNull(cspt);
        assertNotNull(cspt.getCharacterString());
        assertNull(cspt.getCharacterString().getValue());
    }

    /**
     * Test that we can successfully create a list of character strings from a
     * simple list of Java strings.
     */
    @Test
    void testCreateCharacterStringList() {
        List<CharacterStringPropertyType> cspts = S100ExchangeSetUtils.createCharacterStringPropertyTypeList(Collections.singletonList("test"));

        // Assert that the CharacterStringPropertyType is not empty and seems valid
        assertNotNull(cspts);
        assertNotNull(cspts.get(0));
        assertEquals(1, cspts.size());
        assertNotNull(cspts.get(0).getCharacterString());
        assertEquals("test", cspts.get(0).getCharacterString().getValue());
    }

    /**
     * The that for a null list, the CharacterStringPropertyType list generation
     * method will return an empty list respectively.
     * method will return an empty list respectively.
     */
    @Test
    void testCreateCharacterStringListNull() {
        List<CharacterStringPropertyType> cspts = S100ExchangeSetUtils.createCharacterStringPropertyTypeList(null);

        // Assert that the CharacterStringPropertyType is not empty and seems valid
        assertNotNull(cspts);
        assertEquals(0, cspts.size());
    }

    /**
     * Test that we can create (marshall) and XML based on an S-125 Dataset type
     * object.
     *
     * @throws JAXBException a JAXB exception thrown during the marshalling operation
     */
    @Test
    void testMarchallS125() throws JAXBException {
        String xml = S100ExchangeSetUtils.marshalS100ExchangeSetCatalogue(this.s100ExchangeCatalogue);

        // Assert the XML is not empty and seems valid
        assertNotNull(xml);

        // The marshalling operation ofter messes up the namespace order so
        // we might as well remove it before we continue with the one-to-one
        // matching
        String s100ExchangeSetXmlWithoutNamespaces = this.s100ExchangeSetXml
                .replaceAll("S100_ExchangeCatalogue .+>","S100_ExchangeCatalogue>")
                .replaceAll("ns\\d+:","");
        String xmlWithoutNamespaces = xml
                .replaceAll("S100_ExchangeCatalogue .+>","S100_ExchangeCatalogue>")
                .replaceAll("ns\\d+:","");
        assertEquals(s100ExchangeSetXmlWithoutNamespaces, xmlWithoutNamespaces);
    }

    /**
     * Test that we can generate (unmarshall) an S-125 POJO based on a valid
     * XML S-125 Dataset.
     *
     * @throws JAXBException a JAXB exception thrown during the unmarshalling operation
     */
    @Test
    void testUnmarshalS125() throws JAXBException {
        // Unmarshall it to a G1128 service instance object
        S100ExchangeCatalogue result = S100ExchangeSetUtils.unmarshallS100ExchangeSetCatalogue(this.s100ExchangeSetXml);

        // Assert all information is correct
        assertNotNull(result);
        assertEquals(this.s100ExchangeCatalogue.getDataServerIdentifier(), result.getDataServerIdentifier());
        assertNotNull( result.getExchangeCatalogueDescription());
        assertNotNull( result.getExchangeCatalogueDescription().getCharacterString());
        assertEquals(this.s100ExchangeCatalogue.getExchangeCatalogueDescription().getCharacterString().getValue(), result.getExchangeCatalogueDescription().getCharacterString().getValue());
        assertNotNull( result.getExchangeCatalogueComment());
        assertNotNull( result.getExchangeCatalogueComment().getCharacterString());
        assertEquals(this.s100ExchangeCatalogue.getExchangeCatalogueComment().getCharacterString().getValue(), result.getExchangeCatalogueComment().getCharacterString().getValue());
        assertNotNull(result.getDatasetDiscoveryMetadata());
        assertNotNull(result.getDatasetDiscoveryMetadata().getS100DatasetDiscoveryMetadatas());
        assertEquals(1, result.getDatasetDiscoveryMetadata().getS100DatasetDiscoveryMetadatas().size());

        // Now verify the dataset discovery metadata
        final S100DatasetDiscoveryMetadata metadata = result.getDatasetDiscoveryMetadata().getS100DatasetDiscoveryMetadatas().get(0);
        assertEquals(this.s100ExchangeCatalogue.getDatasetDiscoveryMetadata().getS100DatasetDiscoveryMetadatas().get(0).getFileName(), metadata.getFileName());
        assertEquals(this.s100ExchangeCatalogue.getDatasetDiscoveryMetadata().getS100DatasetDiscoveryMetadatas().get(0).getDatasetID(), metadata.getDatasetID());
        assertNotNull(metadata.getDescription().getCharacterString());
        assertNotNull(metadata.getClassification());
        assertEquals(this.s100ExchangeCatalogue.getDatasetDiscoveryMetadata().getS100DatasetDiscoveryMetadatas().get(0).getPurpose(), metadata.getPurpose());
        assertTrue(metadata.isNotForNavigation());
        assertEquals(this.s100ExchangeCatalogue.getDatasetDiscoveryMetadata().getS100DatasetDiscoveryMetadatas().get(0).getEditionNumber(), metadata.getEditionNumber());
        assertEquals(this.s100ExchangeCatalogue.getDatasetDiscoveryMetadata().getS100DatasetDiscoveryMetadatas().get(0).getUpdateNumber(), metadata.getUpdateNumber());
        assertEquals(this.s100ExchangeCatalogue.getDatasetDiscoveryMetadata().getS100DatasetDiscoveryMetadatas().get(0).getUpdateApplicationDate(), metadata.getUpdateApplicationDate());
        assertEquals(this.s100ExchangeCatalogue.getDatasetDiscoveryMetadata().getS100DatasetDiscoveryMetadatas().get(0).getIssueDate(), metadata.getIssueDate());
        assertNull(metadata.getBoundingBox());
        assertNull(metadata.getTemporalExtent());
        assertNotNull(metadata.getProducingAgency());
        assertEquals(this.s100ExchangeCatalogue.getDatasetDiscoveryMetadata().getS100DatasetDiscoveryMetadatas().get(0).getProducerCode(), metadata.getProducerCode());
        assertEquals(this.s100ExchangeCatalogue.getDatasetDiscoveryMetadata().getS100DatasetDiscoveryMetadatas().get(0).getMetadataDateStamp(), metadata.getMetadataDateStamp());
        assertEquals(this.s100ExchangeCatalogue.getDatasetDiscoveryMetadata().getS100DatasetDiscoveryMetadatas().get(0).isReplacedData(), metadata.isReplacedData());
    }

}