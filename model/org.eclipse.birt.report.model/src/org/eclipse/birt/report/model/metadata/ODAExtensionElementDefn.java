/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;

/**
 * Represents the extension element definition for ODA.
 */

public final class ODAExtensionElementDefn extends ExtensionElementDefn
{

	private List hidePrivateProps = null;

	/**
	 * Constructs the add-on extension element definition with element
	 * definition name and base element definition.
	 * 
	 * @param baseElementDefn
	 *            definition of the base element, from which this extension
	 *            element definition extends.
	 */

	public ODAExtensionElementDefn( IElementDefn baseElementDefn )
	{
		assert baseElementDefn != null;

		this.name = baseElementDefn.getName( );
		this.displayNameKey = (String) baseElementDefn.getDisplayNameKey( );
		this.nameOption = MetaDataConstants.REQUIRED_NAME;
		this.allowExtend = false;
		this.extendsFrom = baseElementDefn.getName( );
	}

	/**
	 * Builds element definition.
	 * 
	 * @throws MetaDataException
	 *             if error occurs in building
	 */

	public void buildDefinition( ) throws MetaDataException
	{
		build( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.ElementDefn#build()
	 */

	protected void build( ) throws MetaDataException
	{
		if ( isBuilt )
			return;

		buildDefn( );

		// Cache data for properties defined here. Note, done here so
		// we don't repeat the work for any style properties copied below.

		buildProperties( );

		buildPrivateDriverProperties( );

		// build validation trigger
		buildTriggerDefnSet( );

		isBuilt = true;
	}

	/**
	 * If the visibility of an ODA property is "hidden", it is treated as
	 * private driver property.
	 * 
	 */

	private void buildPrivateDriverProperties( )
	{
		if ( propVisibilites == null )
			return;

		Iterator propNames = propVisibilites.keySet( ).iterator( );
		while ( propNames.hasNext( ) )
		{
			String propName = (String) propNames.next( );
			IElementPropertyDefn propDefn = getProperty( propName );

			if ( propDefn.getValueType( ) != IPropertyDefn.ODA_PROPERTY )
				continue;

			String visibility = (String) propVisibilites.get( propName );
			if ( !DesignChoiceConstants.PROPERTY_MASK_TYPE_HIDE
					.equalsIgnoreCase( visibility ) )
				continue;

			if ( hidePrivateProps == null )
				hidePrivateProps = new ArrayList( );

			hidePrivateProps.add( getProperty( propName ) );
			cachedProperties.remove( propName );
		}
	}

	/**
	 * Returns names of properties that are ODA defined private driver
	 * properties in ODA plug.xml. If the visibility of an ODA property is
	 * "hidden", it is treated as private driver property.
	 * 
	 * 
	 * @return a list containing private driver property names
	 */

	public List getODAPrivateDriverPropertyNames( )
	{
		if ( hidePrivateProps == null )
			return Collections.EMPTY_LIST;

		List retList = new ArrayList( );
		for ( int i = 0; i < hidePrivateProps.size( ); i++ )
		{
			IElementPropertyDefn propDefn = (IElementPropertyDefn) hidePrivateProps
					.get( i );
			retList.add( propDefn.getName( ) );
		}
		return Collections.unmodifiableList( retList );
	}
}