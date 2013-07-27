package org.eclipse.birt.report.model.parser;


import java.util.Map;

import org.eclipse.birt.core.template.TemplateParser;
import org.eclipse.birt.core.template.TextTemplate;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.InvalidParentException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.XmlComment;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ITextItemModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.util.BoundDataColumnUtil;
import org.eclipse.birt.report.model.util.ElementStructureUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class XmlCommentState extends ReportItemState {

	/**
	 * The xml comment being created.
	 */

	protected XmlComment element;

	/**
	 * Constructs the xml comment state with the design parser handler, the
	 * container element and the container slot of the text item.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public XmlCommentState( ModuleParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
	}
	

	/**
	 * Constructs the xml comment state with the design parser handler, the
	 * container element and the container property name of the report element.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param prop
	 *            the slot in which this element appears
	 */

	public XmlCommentState( ModuleParserHandler handler,
			DesignElement theContainer, String prop )
	{
		super( handler, theContainer, prop );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		element = new XmlComment( );
		initElement( attrs, false );
	}
	
	/**
	 * Initializes a report element with "name" and "extends" property.
	 * 
	 * @param attrs
	 *            the SAX attributes object
	 * @param nameRequired
	 *            true if this element requires a name, false if the name is
	 *            optional.
	 * 
	 * @see #initSimpleElement(Attributes)
	 */

	protected void initElement( Attributes attrs, boolean nameRequired )
	{
		DesignElement element = getElement( );

		initElement( attrs );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement( )
	{
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.ReportItemState#end()
	 */

	public void end( ) throws SAXException
	{
		super.end( );
	}
	
	
	public void setComment(String comment) {
		((XmlComment) element).setComment(comment);		
	}


	@Override
	public String toString() {
		return "XmlCommentState [element=" + element + ", container="
				+ container + ", slotID=" + slotID + ", containmentPropName="
				+ containmentPropName + ", handler=" + handler + ", context="
				+ context + ", elementName=" + elementName + ", text=" + text
				+ ", getHandler()="
				+ getHandler() + ", jumpTo()=" + jumpTo() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
	
	


}
