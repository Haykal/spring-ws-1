/*
 * Copyright 2006 the original author or authors.
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

package org.springframework.ws.soap.saaj;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.springframework.util.Assert;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapHeaderException;
import org.springframework.ws.soap.saaj.support.SaajUtils;

/**
 * Internal class that uses SAAJ 1.2 to implement the <code>SoapHeaderElement</code> interface.
 *
 * @author Arjen Poutsma
 */
class Saaj12SoapHeaderElement implements SoapHeaderElement {

    private final SOAPHeaderElement saajHeaderElement;

    Saaj12SoapHeaderElement(SOAPHeaderElement saajHeaderElement) {
        Assert.notNull(saajHeaderElement, "No saajHeaderElement given");
        this.saajHeaderElement = saajHeaderElement;
    }

    public QName getName() {
        return SaajUtils.toQName(saajHeaderElement.getElementName());
    }

    public Source getSource() {
        return new DOMSource(saajHeaderElement);
    }

    public String getActorOrRole() {
        return saajHeaderElement.getActor();
    }

    public void setActorOrRole(String role) {
        saajHeaderElement.setActor(role);
    }

    public boolean getMustUnderstand() {
        return saajHeaderElement.getMustUnderstand();
    }

    public void setMustUnderstand(boolean mustUnderstand) {
        saajHeaderElement.setMustUnderstand(mustUnderstand);
    }

    public Result getResult() {
        return new DOMResult(saajHeaderElement);
    }

    public void addAttribute(QName name, String value) throws SoapHeaderException {
        try {
            Name saajName = SaajUtils.toName(name, saajHeaderElement, getEnvelope());
            saajHeaderElement.addAttribute(saajName, value);
        }
        catch (SOAPException ex) {
            throw new SaajSoapHeaderException(ex);
        }
    }

    private SOAPEnvelope getEnvelope() {
        return (SOAPEnvelope) saajHeaderElement.getParentElement().getParentElement();
    }

}