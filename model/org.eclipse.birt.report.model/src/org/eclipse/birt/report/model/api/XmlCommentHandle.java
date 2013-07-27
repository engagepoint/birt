package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ITextItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IXmlCommentModel;

public class XmlCommentHandle extends ReportElementHandle implements IXmlCommentModel {

	public XmlCommentHandle(Module module, DesignElement element) {
		super(module, element);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Gets the text of this comment element.
	 * 
	 * @return the text to display with the element, if this property value is
	 *         not set, return <code>null</code>.
	 */

	public String getContent( )
	{
		// TODO get comment content
		return getStringProperty( IXmlCommentModel.CONTENT_PROP );
	}

	
	/**
	 * Sets the text for the comment element.
	 * 
	 * @param value
	 *            the new content of the text item
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setContent( String value ) throws SemanticException
	{
		setStringProperty( IXmlCommentModel.CONTENT_PROP, value );
	}

}
