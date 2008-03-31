package org.eclipse.birt.report.engine.layout.pdf.emitter;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;
import org.eclipse.birt.report.engine.layout.pdf.PDFLayoutEngineContext;
import org.eclipse.birt.report.engine.layout.pdf.PDFStackingLM;
import org.eclipse.birt.report.engine.layout.pdf.PDFTableLM;


public class CellLayout extends BlockStackingLayout
{
	
	/**
	 * table layout manager of current cell
	 */
	protected TableLayout tableLayout;

	protected int columnWidth = 0;

	/**
	 * cell content
	 */
	private ICellContent cellContent;

	public CellLayout( LayoutEngineContext context,
			ContainerLayout parentContext, IContent content )
	{
		super( context, parentContext, content );
		tableLayout = getTableLayoutManager( );
		cellContent = (ICellContent) content;
		//tableLM.startCell( cellContent );

		// set max width constraint
		int startColumn = cellContent.getColumn( );
		int endColumn = startColumn + cellContent.getColSpan( );
		columnWidth = tableLayout.getCellWidth( startColumn, endColumn );

	}

	
	protected void createRoot( )
	{
		CellArea cell = AreaFactory.createCellArea( cellContent );
		cell.setRowSpan( cellContent.getRowSpan( ) );
		root = cell;
		int startColumn = cellContent.getColumn( );
		int endColumn = startColumn + cellContent.getColSpan( );
		columnWidth = tableLayout.getCellWidth( startColumn, endColumn );
		tableLayout.resolveBorderConflict( (CellArea)root, true);
		removeMargin( root.getStyle( ) );
		root.setWidth( columnWidth );
	}

	protected void initialize( )
	{
		createRoot( );
		offsetX = root.getContentX( );
		offsetY = root.getContentY( );
		maxAvaWidth = root.getContentWidth( );
		root.setAllocatedHeight( parent.getCurrentMaxContentHeight( ));
		maxAvaHeight = root.getContentHeight( );
	}

	protected void closeLayout( )
	{
		root.setHeight( currentBP
				+ offsetY
				+ getDimensionValue( root.getStyle( ).getProperty(
						StyleConstants.STYLE_PADDING_BOTTOM ) ) );

	}

}
