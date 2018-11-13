package com.nfsindustries.utils;

import com.nfsindustries.model.*;

import javax.xml.bind.*;
import java.io.InputStream;

public class Camt053Parser {

    /**
     * Parse a CAMT.053 formatted bank statement from the given input stream.
     *
     * @param inputStream input stream containing the CAMT.053 formatted bank statement
     * @return document holding CAMT.053 parsed bank statement
     * @throws JAXBException
     */
    public Document parse(InputStream inputStream) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(ObjectFactory.class);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Document camt053Document = ((JAXBElement<Document>) unmarshaller.unmarshal(inputStream)).getValue();

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        //marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "camt053.xml");
        //marshaller.marshal(camt053Document, System.out);

        return camt053Document;
    }
}