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
package org.eclipse.birt.data.engine.impl.document.viewing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 * Utility for expression metadata.
 */
public class ExprMetaUtil
{
	//
	public  final static String POS_NAME 	= "_$$_dte_inner_row_id_$$_";	
	
	// an instance
	private static ExprMetaUtil instance = new ExprMetaUtil( );
	
	// binding name set
	private Set nameSet;
	
	/**
	 * @throws DataException
	 * @throws DataException
	 * @throws IOException
	 */
	public static void saveExprMetaInfo( IBaseQueryDefinition queryDefn,
			Set nameSet, OutputStream outputStream ) throws DataException
	{
		instance.nameSet = nameSet;
		
		List exprMetaList = instance.prepareQueryDefn( queryDefn );
		ExprMetaInfo[] exprMetas = (ExprMetaInfo[]) exprMetaList.toArray( new ExprMetaInfo[]{} );
		
		DataOutputStream dos = new DataOutputStream( outputStream );
		try
		{
			int size = exprMetas.length;
			IOUtil.writeInt( dos, size );

			for ( int i = 0; i < size; i++ )
			{
				ExprMetaInfo exprMeta = exprMetas[i];
				IOUtil.writeString( dos, exprMeta.getName( ) );
				IOUtil.writeInt( dos, exprMeta.getGroupLevel( ) );
				IOUtil.writeInt( dos, exprMeta.getDataType( ) );
				IOUtil.writeInt( dos, exprMeta.getType( ) );
				IOUtil.writeString( dos, exprMeta.getJSText( ) );
			}
			dos.close( );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR,
					"expression metadata" );
		}
	}
	
	/**
	 * Extract the expression information from query definition
	 * 
	 * @return queryDefn
	 */
	private List prepareQueryDefn( IBaseQueryDefinition queryDefn )
	{
		List exprMetaList = new ArrayList( );

		prepareGroup( queryDefn, exprMetaList );
	
		return exprMetaList;
	}
	
	/**
	 * Extract the expression information from group definition
	 * 
	 * @param trans
	 * @param groupLevel
	 * @param exprMetaList
	 * @throws DataException
	 */
	private void prepareGroup( IBaseQueryDefinition trans, List exprMetaList )
	{
		Map exprMap = trans.getResultSetExpressions( );
		if ( exprMap == null )
			return;
		
		Iterator it = exprMap.keySet( ).iterator( );
		while ( it.hasNext( ) )
		{
			String exprName = (String) it.next( );
			if ( nameSet.contains( exprName ) == false )
				continue;
			
			IBaseExpression baseExpr = (IBaseExpression) exprMap.get( exprName );

			ExprMetaInfo exprMeta = new ExprMetaInfo( );
			exprMeta.setDataType( baseExpr.getDataType( ) );
			exprMeta.setGroupLevel( 0 );
			exprMeta.setName( exprName );
			
			if ( baseExpr instanceof IScriptExpression )
			{
				exprMeta.setType( ExprMetaInfo.SCRIPT_EXPRESSION );
				exprMeta.setJSText( ((IScriptExpression)baseExpr).getText( ) );
			}

			exprMetaList.add( exprMeta );
		}
	}
	
	/**
	 * @param inputStream
	 * @throws DataException
	 */
	public static ExprMetaInfo[] loadExprMetaInfo( InputStream inputStream )
			throws DataException
	{
		DataInputStream dis = new DataInputStream( inputStream );
		try
		{
			int size = IOUtil.readInt( dis );
			ExprMetaInfo[] exprMetas = new ExprMetaInfo[size];
			for ( int i = 0; i < size; i++ )
			{
				exprMetas[i] = new ExprMetaInfo( );
				exprMetas[i].setName( IOUtil.readString( dis ) );
				exprMetas[i].setGroupLevel( IOUtil.readInt( dis ) );
				exprMetas[i].setDataType( IOUtil.readInt( dis ) );
				exprMetas[i].setType( IOUtil.readInt( dis ) );
				exprMetas[i].setJSText( IOUtil.readString( dis ) );
			}
			dis.close( );
			return exprMetas;
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_LOAD_ERROR, e );
		}
	}
	
	/**
	 * @param inExprMetas
	 * @return
	 */
	public static ExprMetaInfo[] buildExprDataMetaInfo(
			ExprMetaInfo[] inExprMetas )
	{
		ExprMetaInfo[] exprMetas = null;
		if ( isBasedOnRD( inExprMetas ) == false )
		{
			exprMetas = new ExprMetaInfo[inExprMetas.length + 1];
			System.arraycopy( inExprMetas, 0, exprMetas, 0, inExprMetas.length );

			ExprMetaInfo[] tempExprMetaInfo = getAssistExprMetaInfo( );
			System.arraycopy( tempExprMetaInfo,
					0,
					exprMetas,
					inExprMetas.length,
					tempExprMetaInfo.length );
		}
		else
		{
			exprMetas = inExprMetas;
		}
		return exprMetas;
	}
	
	/**
	 * @param inputStreamMeta
	 * @return
	 * @throws DataException 
	 * @throws DataException
	 */
	public static IResultClass buildExprDataResultClass(
			ExprMetaInfo[] exprMetas ) throws DataException
	{
		List newProjectedColumns = new ArrayList( );
		for ( int i = 0; i < exprMetas.length; i++ )
		{
			String name = exprMetas[i].getName( );
			Class clazz = DataType.getClass( exprMetas[i].getDataType( ) );
			ResultFieldMetadata metaData = new ResultFieldMetadata( 0,
					name,
					name,
					clazz,
					clazz == null ? null : clazz.toString( ),
					i == exprMetas.length - 1 ? true : false );
			newProjectedColumns.add( metaData );
		}

		return new ResultClass( newProjectedColumns );
	}
	
	/**
	 * @param exprMetas
	 * @return
	 */
	public static boolean isBasedOnRD( ExprMetaInfo[] exprMetas )
	{
		boolean isBasedOnSecondRD = false;
		for ( int i = 0; i < exprMetas.length; i++ )
		{
			if ( exprMetas[i].getName( ).equals( ExprMetaUtil.POS_NAME ) )
			{
				isBasedOnSecondRD = true;
				break;
			}
		}
		return isBasedOnSecondRD;
	}
	
	/**
	 * @param resultClass
	 * @return
	 */
	public static boolean isBasedOnRD( IResultClass resultClass )
	{
		return getIdColumnPos( resultClass ) >= 0;
	}
	
	/**
	 * @param resultClass
	 * @return
	 */
	public static int getIdColumnPos( IResultClass resultClass )
	{
		int columnPos = -1;
		for ( int i = 0; i < resultClass.getFieldCount( ); i++ )
		{
			try
			{
				if ( resultClass.getFieldName( i + 1 )
						.equals( ExprMetaUtil.POS_NAME ) )
				{
					columnPos = i + 1;
					break;
				}
			}
			catch ( DataException e )
			{
				// impossible, ignore it
			}
		}
		return columnPos;
	}
	
	/**
	 * @return
	 */
	private static ExprMetaInfo[] getAssistExprMetaInfo( )
	{
		ExprMetaInfo[] exprMetas = new ExprMetaInfo[1];

		exprMetas[0] = new ExprMetaInfo( );
		exprMetas[0].setName( POS_NAME );
		exprMetas[0].setGroupLevel( 0 );
		exprMetas[0].setDataType( DataType.INTEGER_TYPE );
		exprMetas[0].setType( ExprMetaInfo.SCRIPT_EXPRESSION );
		exprMetas[0].setJSText( "dataSetRow._rowPosition" );

		return exprMetas;
	}
	
}
