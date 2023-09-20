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
import _int.iho.s100.catalog._5_0.ObjectFactory;
import org.grad.eNav.s100.enums.CodeListValueTypeProvider;
import org.grad.eNav.s100.enums.TelephoneType;
import org.iso.standards.iso._19115.__3.cit._2.CITelephoneTypeCodePropertyType;
import org.iso.standards.iso._19115.__3.lan._1.*;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * The S100 Exchange Catalogue Builder Class.
 *
 * This is a basic builder class that enables the generation of the S100
 * Exchange Set Catalogue file (named "CATALOG.XML") contents.
 *
 * @author Nikolaos Vastardis (email: Nikolaos.Vastardis@gla-rad.org)
 */
public class S100ExchangeCatalogueBuilder {

    /**
     * The Telephone Type namespace/list for use in the telephone type codes.
     */
    public static final String TELEPHONE_TYPE_NAMESPACE = "RFC7970";
    public static final String TELEPHONE_TYPE_LIST = "https://www.iana.org/assignments/iodef2/iodef2.xhtml#telephone-type";

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

    // Class Variables
    protected String identifier;
    protected String dataServerIdentifier;
    protected String organization;
    protected List<String> electronicMailAddresses;
    protected String phone;
    protected TelephoneType phoneType;
    protected String city;
    protected String postalCode;
    protected String country;
    protected List<Locale> locales;
    protected String administrativeArea;
    protected String description;
    protected String comment;
    protected List<S100ProductSpecification> productSpecifications;

    // Certificate Information
    protected Map<String, X509Certificate> certificateMap;

    // Metadata Lists
    protected final List<S100DatasetDiscoveryMetadata> datasetFileMetadataList;
    protected final List<S100SupportFileDiscoveryMetadata> supportFileMetadataList;
    protected final List<S100CatalogueDiscoveryMetadata> catalogueFileMetadataList;

    // Objects Factories
    private final ObjectFactory objectFactory;

    // Signature Provider
    private final S100ExchangeSetSignatureProvider signatureProvider;

    /**
     * Class Constructor.
     *
     * @param signatureProvider the signature provider for the exchange set
     */
    public S100ExchangeCatalogueBuilder(S100ExchangeSetSignatureProvider signatureProvider) {
        this.signatureProvider = signatureProvider;

        // Initialise the object factories
        this.objectFactory = new ObjectFactory();

        //Initialise the certificates map
        this.certificateMap = new HashMap<>();

        // Initialise the metadata lists
        this.datasetFileMetadataList = new ArrayList<>();
        this.supportFileMetadataList = new ArrayList<>();
        this.catalogueFileMetadataList = new ArrayList<>();
    }

    /**
     * Sets the exchange set identifier.
     *
     * @param identifier the exchange set identifier
     * @return the S100 exchange set catalogue builder
     */
    public S100ExchangeCatalogueBuilder setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    /**
     * Sets the exchange set data server identifier.
     *
     * @param dataServerIdentifier the exchange set data server identifier
     * @return the S100 exchange set catalogue builder
     */
    public S100ExchangeCatalogueBuilder setdataServerIdentifier(String dataServerIdentifier) {
        this.dataServerIdentifier = dataServerIdentifier;
        return this;
    }

    /**
     * Sets the exchange set organization.
     *
     * @param organization the exchange set organization
     * @return the S100 exchange set catalogue builder
     */
    public S100ExchangeCatalogueBuilder setOrganization(String organization) {
        this.organization = organization;
        return this;
    }

    /**
     * Sets the exchange set electronic mail addresses.
     *
     * @param electronicMailAddresses the exchange set electronic mail addresses
     * @return the S100 exchange set catalogue builder
     */
    public S100ExchangeCatalogueBuilder setElectronicMailAddresses(List<String> electronicMailAddresses) {
        this.electronicMailAddresses = electronicMailAddresses;
        return this;
    }

    /**
     * Sets the exchange set phone.
     *
     * @param phone the exchange set phone
     * @return the S100 exchange set catalogue builder
     */
    public S100ExchangeCatalogueBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    /**
     * Sets the exchange set phone type.
     *
     * @param phoneType the exchange set phone type
     * @return the S100 exchange set catalogue builder
     */
    public S100ExchangeCatalogueBuilder setPhoneType(TelephoneType phoneType) {
        this.phoneType = phoneType;
        return this;
    }

    /**
     * Sets the exchange set city.
     *
     * @param city the exchange set city
     * @return the S100 exchange set catalogue builder
     */
    public S100ExchangeCatalogueBuilder setCity(String city) {
        this.city = city;
        return this;
    }

    /**
     * Sets the exchange set postal code.
     *
     * @param postalCode the exchange set postal code
     * @return the S100 exchange set catalogue builder
     */
    public S100ExchangeCatalogueBuilder setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    /**
     * Sets the exchange set country.
     *
     * @param country the exchange set country
     * @return the S100 exchange set catalogue builder
     */
    public S100ExchangeCatalogueBuilder setCountry(String country) {
        this.country = country;
        return this;
    }

    /**
     * Sets the exchange set locales.
     *
     * @param locales the exchange set locales
     * @return the S100 exchange set catalogue builder
     */
    public S100ExchangeCatalogueBuilder setLocales(List<Locale> locales) {
        this.locales = locales;
        return this;
    }

    /**
     * Sets the exchange set administrative area.
     *
     * @param administrativeArea the exchange set administrative area
     * @return the S100 exchange set catalogue builder
     */
    public S100ExchangeCatalogueBuilder setAdministrativeArea(String administrativeArea) {
        this.administrativeArea = administrativeArea;
        return this;
    }

    /**
     * Sets the exchange set data server identifier.
     *
     * @param dataServerIdentifier the exchange set data server identifier
     * @return the S100 exchange set catalogue builder
     */
    public S100ExchangeCatalogueBuilder setDataServerIdentifier(String dataServerIdentifier) {
        this.dataServerIdentifier = dataServerIdentifier;
        return this;
    }

    /**
     * Sets the exchange set list of product specifications.
     *
     * @param productSpecifications the list of exchange set product specifications
     * @return the S100 exchange set catalogue builder
     */
    public S100ExchangeCatalogueBuilder setProductSpecification(List<S100ProductSpecification> productSpecifications) {
        this.productSpecifications = productSpecifications;
        return this;
    }

    /**
     * Sets the exchange set description information.
     *
     * @param description the exchange set description information
     * @return the S100 exchange set catalogue builder
     */
    public S100ExchangeCatalogueBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the exchange set comment information.
     *
     * @param comment the exchange set comment information
     * @return the S100 exchange set catalogue builder
     */
    public S100ExchangeCatalogueBuilder setComment(String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * Sets the exchange set certificates.
     *
     * @param certificateMap the certificates to be used
     * @return the S100 exchange set catalogue builder
     */
    public S100ExchangeCatalogueBuilder setCertificates(Map<String, X509Certificate> certificateMap) throws CertificateException {
        this.certificateMap.putAll(certificateMap);
        return this;
    }

    /**
     * Sets the exchange set certificates in PEM representation.
     *
     * @param certificateMap the certificates to be used
     * @return the S100 exchange set catalogue builder
     */
    public S100ExchangeCatalogueBuilder setCertificatesByPem(Map<String, byte[]> certificateMap) throws CertificateException {
        final Map<String, X509Certificate> temp = new HashMap<>();
        for(Map.Entry<String, byte[]> entry : certificateMap.entrySet()) {
            X509Certificate certificate = this.getCertFromPem(entry.getValue());
            temp.put(entry.getKey(), certificate);
        }
        return this.setCertificates(temp);
    }

    /**
     * Appends a new dataset metadata entry.
     *
     * @param datasetMetadata the dataset metadata entry to be appended
     * @return the S100 exchange set catalogue builder
     */
    public S100ExchangeCatalogueBuilder addDatasetMetadata(S100DatasetDiscoveryMetadata datasetMetadata) {
        this.datasetFileMetadataList.add(datasetMetadata);
        return this;
    }



    /**
     * Appends a new support file metadata entry.
     *
     * @param supportFileMetadata the support file metadata entry to be appended
     * @return the S100 exchange set catalogue builder
     */
    public S100ExchangeCatalogueBuilder addSupportFileMetadata(S100SupportFileDiscoveryMetadata supportFileMetadata) {
        this.supportFileMetadataList.add(supportFileMetadata);
        return this;
    }

    /**
     * Appends a new catalogue metadata entry.
     *
     * @param catalogueMetadata the catalogue metadata entry to be appended
     * @return the S100 exchange set catalogue builder
     */
    public S100ExchangeCatalogueBuilder addCatalogueMetadata(S100CatalogueDiscoveryMetadata catalogueMetadata) {
        this.catalogueFileMetadataList.add(catalogueMetadata);
        return this;
    }

    /**
     * The main function that builds the exchange  set catalog XML file.
     *
     * @return the exchange set catalogue XML file contents
     * @throws JAXBException for any exceptions while marshalling the XML content
     */
    public S100ExchangeCatalogue build() throws JAXBException, CertificateEncodingException {
        // Create a new exchange set catalogue
        final S100ExchangeCatalogue exchangeCatalogue  = new S100ExchangeCatalogue();

        // ================================================================== //
        //             Set the exchange set identifier information            //
        // ================================================================== //
        final S100ExchangeCatalogueIdentifier exchangeCatalogueIdentifier = new S100ExchangeCatalogueIdentifier();
        exchangeCatalogueIdentifier.setIdentifier(this.identifier);
        exchangeCatalogueIdentifier.setDateTime(LocalDateTime.now());
        exchangeCatalogue.setIdentifier(exchangeCatalogueIdentifier);

        // Create a UUID for this server from a constant string
        exchangeCatalogue.setDataServerIdentifier(this.dataServerIdentifier);
        // ================================================================== //

        // ================================================================== //
        //        Set the exchange set point of contact information           //
        // ================================================================== //
        final S100CataloguePointofContact cataloguePointOfContact = new S100CataloguePointofContact();
        cataloguePointOfContact.setOrganization(S100ExchangeSetUtils.createCharacterStringPropertyType(this.organization));

        // Set the address
        final S100AddressType addressType = new S100AddressType();
        addressType.setElectronicMailAddresses(S100ExchangeSetUtils.createCharacterStringPropertyTypeList(this.electronicMailAddresses));
        addressType.setAdministrativeArea(S100ExchangeSetUtils.createCharacterStringPropertyType(this.administrativeArea));
        addressType.setPostalCode(S100ExchangeSetUtils.createCharacterStringPropertyType(this.postalCode));
        addressType.setCity(S100ExchangeSetUtils.createCharacterStringPropertyType(this.city));
        addressType.setCountry(S100ExchangeSetUtils.createCharacterStringPropertyType(this.country));
        cataloguePointOfContact.setAddress(addressType);

        // Set the phone number
        final S100PhoneType phoneType = new S100PhoneType();
        phoneType.setNumber(S100ExchangeSetUtils.createCharacterStringPropertyType(this.phone));
        final CITelephoneTypeCodePropertyType ciTelephoneTypeCodePropertyType = new CITelephoneTypeCodePropertyType();
        ciTelephoneTypeCodePropertyType.setCITelephoneTypeCode(Optional.ofNullable(this.phoneType)
                .map(CodeListValueTypeProvider::getCodeListValueType)
                .orElse(null));
        phoneType.setNumberType(ciTelephoneTypeCodePropertyType);
        cataloguePointOfContact.setPhone(phoneType);

        // And set the point of content information
        exchangeCatalogue.setContact(cataloguePointOfContact);
        // ================================================================== //

        // ================================================================== //
        //             Set the Data Product Specification                     //
        // ================================================================== //
        exchangeCatalogue.setProductSpecifications(this.productSpecifications);
        // ================================================================== //

        // ================================================================== //
        //                 Set the Certificate Information                    //
        // ================================================================== //
        final S100SECertificateContainerType s100SECertificateContainerType = new S100SECertificateContainerType();
        final S100SECertificateContainerType.SchemeAdministrator schemeAdministrator = new S100SECertificateContainerType.SchemeAdministrator();
        schemeAdministrator.setId(this.organization);
        s100SECertificateContainerType.setSchemeAdministrator(schemeAdministrator);
        if(Objects.nonNull(this.certificateMap)) {
            for(Map.Entry<String, X509Certificate> certificateEntry : this.certificateMap.entrySet()) {
                final S100SECertificateType certificateType = new S100SECertificateType();
                certificateType.setId(certificateEntry.getKey());
                certificateType.setIssuer(certificateEntry.getValue().getIssuerX500Principal().getName());
                certificateType.setValue(this.getPemFromCert(certificateEntry.getValue()));
                s100SECertificateContainerType.getCertificates().add(certificateType);
            }
        }
        exchangeCatalogue.getCertificates().add(s100SECertificateContainerType);
        // ================================================================== //

        // ================================================================== //
        //                  Set the Locale Information                        //
        // ================================================================== //
        for (Locale locale : this.locales) {
            PTLocalePropertyType ptLocaleProperty = new PTLocalePropertyType();
            PTLocaleType ptLocaleValue = new PTLocaleType();
            LanguageCodePropertyType languageCodePropertyType = new LanguageCodePropertyType();
            languageCodePropertyType.setLanguageCode(S100ExchangeSetUtils.generateCodeListValueType(
                    LANGUAGE_LIST,
                    LANGUAGE_NAMESPACE,
                    locale.getISO3Language(),
                    locale.getDisplayLanguage()));
            ptLocaleValue.setLanguage(languageCodePropertyType);
            CountryCodePropertyType countryCodePropertyType = new CountryCodePropertyType();
            countryCodePropertyType.setCountryCode(S100ExchangeSetUtils.generateCodeListValueType(
                    COUNTRY_LIST,
                    COUNTRY_NAMESPACE,
                    locale.getISO3Country(),
                    locale.getDisplayCountry()));
            ptLocaleValue.setCountry(countryCodePropertyType);
            MDCharacterSetCodePropertyType mdCharacterSetCodePropertyType = new MDCharacterSetCodePropertyType();
            mdCharacterSetCodePropertyType.setMDCharacterSetCode(S100ExchangeSetUtils.generateCodeListValueType(
                    CHARACTER_ENCODING_LIST,
                    CHARACTER_ENCODING_NAMESPACE,
                    StandardCharsets.UTF_8.name(),
                    StandardCharsets.UTF_8.displayName()));
            ptLocaleValue.setCharacterEncoding(mdCharacterSetCodePropertyType);
            ptLocaleProperty.setPTLocale(new org.iso.standards.iso._19115.__3.lan._1.ObjectFactory().createPTLocale(ptLocaleValue));

            // Check if to add as the default or as another locale
            if (Objects.isNull(exchangeCatalogue.getDefaultLocale())) {
                exchangeCatalogue.setDefaultLocale(ptLocaleProperty);
            } else {
                exchangeCatalogue.getOtherLocales().add(ptLocaleProperty);
            }
        }
        // ================================================================== //

        // ================================================================== //
        //                  Set the Description/Comments                      //
        // ================================================================== //
        Optional.ofNullable(this.description)
                .map(S100ExchangeSetUtils::createCharacterStringPropertyType)
                .ifPresent(exchangeCatalogue::setExchangeCatalogueDescription);
        Optional.ofNullable(this.comment)
                .map(S100ExchangeSetUtils::createCharacterStringPropertyType)
                .ifPresent(exchangeCatalogue::setExchangeCatalogueComment);
        // ================================================================== //

        // ================================================================== //
        //                 Append the Metadata Information                    //
        // ================================================================== //
        // Initialise the metadata lists
        exchangeCatalogue.setDatasetDiscoveryMetadata(
                new S100ExchangeCatalogue.DatasetDiscoveryMetadata()
        );
        exchangeCatalogue.setSupportFileDiscoveryMetadata(
                new S100ExchangeCatalogue.SupportFileDiscoveryMetadata()
        );
        exchangeCatalogue.setCatalogueDiscoveryMetadata(
                new S100ExchangeCatalogue.CatalogueDiscoveryMetadata()
        );
        // Add the individual metadata lists
        exchangeCatalogue.getDatasetDiscoveryMetadata()
                .setS100DatasetDiscoveryMetadatas(this.datasetFileMetadataList);
        exchangeCatalogue.getSupportFileDiscoveryMetadata()
                .setS100SupportFileDiscoveryMetadatas(this.supportFileMetadataList);
        exchangeCatalogue.getCatalogueDiscoveryMetadata()
                .setS100CatalogueDiscoveryMetadatas(this.catalogueFileMetadataList);
        // ================================================================== //

        // And finally marshall to the XML output
        return exchangeCatalogue;
    }

    /**
     * This helper function will read the provided certificate and will generate
     * a minified version of its PEM representation as a string.
     *
     * @param cert the X.509 certificate to be read
     * @return the minified PEM representation of the certificate
     * @throws CertificateEncodingException if the provided PEM file is invalid
     */
    public byte[] getPemFromCert(X509Certificate cert) throws CertificateEncodingException {
        return Base64.getEncoder().encode(cert.getEncoded());
    }

    /**
     * This helper function will read the provided PEM file bytes and
     * reconstruct the valid X509 certificate.
     *
     * @param certificate the cerificate PEM bytes
     * @return the valid X509 certificate
     * @throws CertificateException if the provided PEM file is invalid
     */
    protected X509Certificate getCertFromPem(byte[] certificate) throws CertificateException {
        // Do the string conversion and reconstruct the X509Certificate object
        final ByteArrayInputStream ins = new ByteArrayInputStream(certificate);
        return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(ins);
    }

}
