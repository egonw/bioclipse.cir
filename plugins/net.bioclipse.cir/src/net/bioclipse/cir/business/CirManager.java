/*******************************************************************************
 * Copyright (c) 2012  Egon Willighagen <egonw@users.sf.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.cir.business;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.bioclipse.cdk.business.CDKManager;
import net.bioclipse.cdk.domain.ICDKMolecule;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.GetMethod;
import org.openscience.cdk.io.formats.IChemFormat;
import org.openscience.cdk.io.formats.MDLV2000Format;

public class CirManager implements IBioclipseManager {

    private static final String SERVICE = "http://cactus.nci.nih.gov/chemical/structure/";
    private final CDKManager cdk = new CDKManager();

    /**
     * Gives a short one word name of the manager used as variable name when
     * scripting.
     */
    public String getManagerName() {
        return "cir";
    }

    public ICDKMolecule getByName(String name) throws BioclipseException {
    	HttpClient client = new HttpClient();
    	String fullURL = SERVICE + name + "/sdf";
    	System.out.println(("URL: " + fullURL));
		GetMethod method = new GetMethod(fullURL);
		addMethodHeaders(method,
			new HashMap<String,String>() {{ put("Accept", "text/plain"); }}
		);
		try {
			client.executeMethod(method);
			String content = method.getResponseBodyAsString();
			if (content.contains("http://www.w3.org/1999/xhtml")) {
				throw new BioclipseException("HTML page returned: probably multiple search hits");
			}
			InputStream reader = method.getResponseBodyAsStream();
			return cdk.loadMolecule(reader, (IChemFormat)MDLV2000Format.getInstance(), null);
		} catch (HttpException exception) {
			throw new BioclipseException("Error while accessing CIR: " + exception.getMessage(), exception);
		} catch (IOException exception) {
			throw new BioclipseException("Error while reading the CIR response: " + exception.getMessage(), exception);
		}
    }

    private static HttpMethodBase addMethodHeaders(HttpMethodBase method,
			Map<String, String> extraHeaders) {
		// add other headers
		if (extraHeaders != null) {
        	for (String header : extraHeaders.keySet()) {
        		method.setRequestHeader(
        			header, extraHeaders.get(header)
        		);
        	}
        }
		return method;
	}}
