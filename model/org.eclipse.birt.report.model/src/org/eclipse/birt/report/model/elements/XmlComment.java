package org.eclipse.birt.report.model.elements;

import java.util.Arrays;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.XmlCommentHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.PropertySearchStrategy;
import org.eclipse.birt.report.model.elements.interfaces.IXmlCommentModel;
import org.eclipse.birt.report.model.elements.strategy.ReportItemPropSearchStrategy;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

public class XmlComment extends DesignElement implements IXmlCommentModel {

	private String comment; 
	
	/**
	 * Default constructor.
	 */

	public XmlComment( )
	{
		cachedPropStrategy = ReportItemPropSearchStrategy.getInstance( );
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt
	 * .report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitXmlComment( this );
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.XML_COMMENT;
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module
	 *            the report design
	 * @return an API handle for this element
	 */

	public XmlCommentHandle handle( Module module )
	{	
		if ( handle == null )
		{
			handle = new XmlCommentHandle( module, this );
		}
		return (XmlCommentHandle) handle;
	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.core.IDesignElement#getHandle(org.eclipse
	 * .birt.report.model.core.Module)
	 */

	public DesignElementHandle getHandle( Module module )
	{
		return handle( module );
	}


	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "XmlComment [comment=" + comment + ", name=" + name
				+ ", listeners=" + listeners + ", propValues=" + propValues
				+ ", userProperties=" + userProperties + ", containerInfo="
				+ containerInfo + ", slots=" + Arrays.toString(slots)
				+ ", extendsRef=" + extendsRef + ", derived=" + derived
				+ ", id=" + id + ", cachedDefn=" + cachedDefn + ", isValid="
				+ isValid + ", handle=" + handle + ", errors=" + errors
				+ ", baseId=" + baseId + ", encryptionMap=" + encryptionMap
				+ ", cachedPropStrategy=" + cachedPropStrategy
				+ ", getElementName()=" + getElementName() + ", getComment()="
				+ getComment() + ", getRoot()=" + getRoot()
				+ ", isVirtualElement()=" + isVirtualElement()
				+ ", getVirtualParent()=" + getVirtualParent()
				+ ", getStrategy()=" + getStrategy() + ", isStyle()="
				+ isStyle() + ", getDefn()=" + getDefn()
				+ ", getExtendsElement()=" + getExtendsElement()
				+ ", getExtendsName()=" + getExtendsName() + ", getName()="
				+ getName() + ", getFullName()=" + getFullName()
				+ ", getContainer()=" + getContainer() + ", getStyle()="
				+ getStyle() + ", getDescendents()=" + getDescendents()
				+ ", hasUserProperties()=" + hasUserProperties()
				+ ", hasLocalPropertyValues()=" + hasLocalPropertyValues()
				+ ", propertyWithLocalValueIterator()="
				+ propertyWithLocalValueIterator() + ", getUserProperties()="
				+ getUserProperties() + ", getLocalUserProperties()="
				+ getLocalUserProperties() + ", hasDerived()=" + hasDerived()
				+ ", getDisplayName()=" + getDisplayName()
				+ ", getDisplayNameID()=" + getDisplayNameID() + ", getID()="
				+ getID() + ", getElementSelectors()=" + getElementSelectors()
				+ ", getPropertyDefns()=" + getPropertyDefns()
				+ ", isRootIncludedByModule()=" + isRootIncludedByModule()
				+ ", getContainerInfo()=" + getContainerInfo()
				+ ", getObjectDefn()=" + getObjectDefn() + ", hasReferences()="
				+ hasReferences() + ", getDerived()=" + getDerived()
				+ ", isValid()=" + isValid() + ", getNameForDisplayLabel()="
				+ getNameForDisplayLabel() + ", getErrors()=" + getErrors()
				+ ", getIdentifier()=" + getIdentifier() + ", getBaseId()="
				+ getBaseId() + ", isManagedByNameSpace()="
				+ isManagedByNameSpace() + ", isInTemplateParameterDefinitionSlot()="
				+ isInTemplateParameterDefinitionSlot()
				+ ", getPropertySearchStrategy()="
				+ getPropertySearchStrategy() + ", getContents()="
				+ getContents() + ", isContainer()=" + isContainer()
				+ ", canDynamicExtends()=" + canDynamicExtends()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	

}
