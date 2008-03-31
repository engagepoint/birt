/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.content.impl.LabelContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.dom.DOMReportItemExecutor;
import org.eclipse.birt.report.engine.layout.ILineStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.content.LineStackingExecutor;
import org.eclipse.birt.report.engine.layout.pdf.util.HTML2Content;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

public class LayoutContextFactory
{

	private ContentVisitor visitor = new ContentVisitor( );

	ContainerLayout layoutContext = null;

	ContainerLayout parent = null;

	private LayoutEngineContext context = null;

	protected IReportItemExecutor executor;

	public LayoutContextFactory( LayoutEngineContext context )
	{
		this.context = context;
	}

	public ContainerLayout createLayoutManager( ContainerLayout parent,
			IContent content )
	{
		this.parent = parent;
		if ( content == null )
		{
			return new LineLayout( context, parent );
		}
		else
		{
			return (ContainerLayout) content.accept( visitor, null );
		}
	}

	private class ContentVisitor implements IContentVisitor
	{

		public Object visit( IContent content, Object value )
		{
			return visitContent( content, value );
		}

		public Object visitContent( IContent content, Object value )
		{
			boolean isInline = PropertyUtil.isInlineElement( content );
			if ( isInline )
			{
				return new InlineBlockLayout( context, parent, content );
			}
			else
			{
				return new BlockStackingLayout( context, parent, content);
			}
		}

		public Object visitPage( IPageContent page, Object value )
		{
			return new PageLayoutContext(context, parent, page);
		}

		public Object visitContainer( IContainerContent container, Object value )
		{
			boolean isInline = PropertyUtil.isInlineElement( container );
			if ( isInline )
			{
				return new InlineBlockLayout( context, parent, container);
			}
			else
			{
				return new BlockStackingLayout( context, parent, container);
			}
		}

		public Object visitTable( ITableContent table, Object value )
		{
			return new TableLayout( context, parent, table );
		}

		public Object visitTableBand( ITableBandContent tableBand, Object value )
		{
			return new BlockStackingLayout( context, parent, tableBand);
		}

		public Object visitRow( IRowContent row, Object value )
		{
			return new RowLayout( context, parent, row );
		}

		public Object visitCell( ICellContent cell, Object value )
		{
			return new CellLayout( context, parent, cell );
		}

		public Object visitText( ITextContent text, Object value )
		{
			// FIXME
			return handleText( text );
		}

		public Object visitLabel( ILabelContent label, Object value )
		{
			return handleText( label );
		}

		public Object visitData( IDataContent data, Object value )
		{
			return handleText( data );
		}

		public Object visitImage( IImageContent image, Object value )
		{
			return new ImageLayout( context, parent, image );
		}

		public Object visitForeign( IForeignContent foreign, Object value )
		{
			assert(false);
			return null;
		}

		public Object visitAutoText( IAutoTextContent autoText, Object value )
		{
			String originalPageNumber = autoText.getText( );
			NumberFormatter nf = new NumberFormatter( );
			String patternStr = autoText.getComputedStyle( )
					.getNumberFormat( );
			nf.applyPattern( patternStr );
			try
			{
				autoText.setText( nf.format( Integer
						.parseInt( originalPageNumber ) ) );
			}
			catch ( NumberFormatException nfe )
			{
				autoText.setText( originalPageNumber );
			}
			return handleText( autoText );
		}

		private Object handleText( ITextContent content )
		{
			boolean isInline = parent instanceof ILineStackingLayoutManager;
			if ( isInline )
			{
				return new InlineTextLayout( context, parent, content );
			}
			else
			{
				String text = content.getText( );
				if ( text == null || "".equals( text ) ) //$NON-NLS-1$
				{
					content.setText( " " ); //$NON-NLS-1$
				}
				return new BlockTextLayout( context, parent, content);
			}
		}

		public Object visitList( IListContent list, Object value )
		{
			return new BlockStackingLayout( context, parent, list );

		}

		public Object visitListBand( IListBandContent listBand, Object value )
		{
			assert ( false );
			return null;
			// return new PDFListBandLM(context, parent, listBand, emitter,
			// executor);

		}

		public Object visitListGroup( IListGroupContent group, Object value )
		{
			return new BlockStackingLayout( context, parent, group );
		}

		public Object visitTableGroup( ITableGroupContent group, Object value )
		{
			return new BlockStackingLayout( context, parent, group );
		}

		public Object visitGroup( IGroupContent group, Object value )
		{
			return new BlockStackingLayout( context, parent, group );
		}

	}

}
