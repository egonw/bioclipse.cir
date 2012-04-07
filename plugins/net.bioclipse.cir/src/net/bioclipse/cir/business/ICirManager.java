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

import net.bioclipse.cdk.domain.ICDKMolecule;
import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;

@PublishedClass(
    value="Manager that allows using the NCI/CADD Chemical Indentifier Resolver web service."
)
public interface ICirManager extends IBioclipseManager {

    @Recorded
    @PublishedMethod(
        methodSummary = "Gets a IMolecule by name, or null if the name is not recognized.",
        params="String name"
    )
    public ICDKMolecule getByName(String name) throws BioclipseException;

}
