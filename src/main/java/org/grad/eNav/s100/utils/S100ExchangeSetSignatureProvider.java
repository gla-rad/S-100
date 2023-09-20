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

import _int.iho.s100.catalog._5_0.S100SEDigitalSignature;

/**
 * The S-100 Exchange Set Signature Provider Interface.
 *
 * This interface is utilised by the S-100 Exchange Set builders to generate
 * signatures while building the exchange set and its metadata.
 *
 * @author Nikolaos Vastardis (email: Nikolaos.Vastardis@gla-rad.org)
 */
public interface S100ExchangeSetSignatureProvider {

    S100SEDigitalSignature generateSignature(Object id, String algorithm, byte[] payload);

}
