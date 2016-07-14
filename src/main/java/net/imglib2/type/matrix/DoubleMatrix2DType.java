/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2016 Tobias Pietzsch, Stephan Preibisch, Stephan Saalfeld,
 * John Bogovic, Albert Cardona, Barry DeZonia, Christian Dietz, Jan Funke,
 * Aivar Grislis, Jonathan Hale, Grant Harris, Stefan Helfrich, Mark Hiner,
 * Martin Horn, Steffen Jaensch, Lee Kamentsky, Larry Lindsey, Melissa Linkert,
 * Mark Longair, Brian Northan, Nick Perry, Curtis Rueden, Johannes Schindelin,
 * Jean-Yves Tinevez and Michael Zinsmaier.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imglib2.type.matrix;

import net.imglib2.img.NativeImg;
import net.imglib2.img.NativeImgFactory;
import net.imglib2.img.basictypeaccess.DoubleAccess;
import net.imglib2.img.basictypeaccess.array.DoubleArray;
import net.imglib2.type.NativeType;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.util.Fraction;

/**
 * {@link Type} for storing two-dimensional matrices with double entries.
 * 
 * @author Tobias Pietzsch
 * @author Stefan Helfrich
 */
public class DoubleMatrix2DType implements NativeType< DoubleMatrix2DType >, NumericType< DoubleMatrix2DType >
{
	/** Number of elements in this matrix. Only used internally. */
	final private int numElements;

	/** Number of rows of this matrix. */
	final protected int rows;

	/** Number of columns of this matrix. */
	final protected int cols;

	final protected NativeImg< DoubleMatrix2DType, ? extends DoubleAccess > img;

	/** {@link DoubleAccess} that stores the matrix entries */
	protected DoubleAccess dataAccess;

	/** Index into {@link #dataAccess} */
	private int idx = 0;

	/**
	 * TODO Documentation
	 * 
	 * @param width
	 * @param height
	 */
	public DoubleMatrix2DType( final int width, final int height )
	{
		this.cols = width;
		this.rows = height;

		this.numElements = width * height;
		this.img = null;
	}

	/**
	 * This is the constructor if you want it to be a variable
	 * 
	 * TODO Documentation
	 * 
	 * @param values
	 * @param cols
	 */
	public DoubleMatrix2DType( final double[] values, final int cols )
	{
		assert ( values.length % 2 == 0 );

		this.img = null;

		this.numElements = values.length;

		this.cols = cols;
		this.rows = numElements / cols;

		dataAccess = new DoubleArray( numElements );

		for ( int i = 0; i < cols; ++i )
			for ( int j = 0; j < rows; ++j )
				setComponent( i, j, values[ i * j ] );
	}

	/**
	 * TODO Documentation
	 * 
	 * @param values
	 */
	public DoubleMatrix2DType( final double[][] values )
	{
		this.img = null;

		this.cols = values.length;
		this.rows = values[ 0 ].length;

		this.numElements = cols * rows;

		dataAccess = new DoubleArray( numElements );

		for ( int i = 0; i < cols; ++i )
			for ( int j = 0; j < rows; ++j )
				setComponent( i, j, values[ i ][ j ] );
	}

	/**
	 * This is the constructor if you want it to read from an array
	 * 
	 * TODO Documentation
	 * 
	 * @param width
	 * @param height
	 * @param container
	 */
	public DoubleMatrix2DType(
			final int width,
			final int height,
			final NativeImg< DoubleMatrix2DType, ? extends DoubleAccess > container )
	{
		this.cols = width;
		this.rows = height;

		this.numElements = width * height;

		img = container;
	}

	@Override
	public DoubleMatrix2DType createVariable()
	{
		return new DoubleMatrix2DType( new double[ cols ][ rows ] );
	}

	@Override
	public DoubleMatrix2DType copy()
	{
		final DoubleMatrix2DType t = createVariable();
		t.set( this );
		return t;
	}

	@Override
	public void set( final DoubleMatrix2DType c )
	{
		for ( int i = 0; i < cols; ++i )
			for ( int j = 0; j < rows; ++j )
				setComponent( i, j, c.getComponent( i, j ) );
	}

	@Override
	public boolean valueEquals( final DoubleMatrix2DType t )
	{
		for ( int i = 0; i < cols; ++i )
			for ( int j = 0; j < rows; ++j )
			{
				if ( t.getComponent( i, j ) != getComponent( i, j ) )
					return false;
			}
		return true;
	}

	@Override
	public Fraction getEntitiesPerPixel()
	{
		return new Fraction( numElements, 1 );
	}

	@Override
	public NativeImg< DoubleMatrix2DType, ? > createSuitableNativeImg(
			final NativeImgFactory< DoubleMatrix2DType > storageFactory,
			final long[] dim )
	{
		// create the container
		final NativeImg< DoubleMatrix2DType, ? extends DoubleAccess > container =
				storageFactory.createDoubleInstance( dim, getEntitiesPerPixel() );

		// create a Type that is linked to the container
		final DoubleMatrix2DType linkedType = new DoubleMatrix2DType( cols, rows, container );

		// pass it to the NativeContainer
		container.setLinkedType( linkedType );

		return container;
	}

	@Override
	public DoubleMatrix2DType duplicateTypeOnSameNativeImg()
	{
		return new DoubleMatrix2DType( cols, rows, img );
	}

	@Override
	public void updateContainer( final Object c )
	{
		dataAccess = img.update( c );
	}

	@Override
	public void updateIndex( final int i )
	{
		this.idx = i;
	}

	@Override
	public int getIndex()
	{
		return idx;
	}

	@Override
	public void incIndex()
	{
		++idx;
	}

	@Override
	public void incIndex( final int increment )
	{
		idx += increment;
	}

	@Override
	public void decIndex()
	{
		--idx;
	}

	@Override
	public void decIndex( final int decrement )
	{
		idx -= decrement;
	}

	/**
	 * TODO Documentations
	 * 
	 * @param i
	 *            column index
	 * @param j
	 *            row index
	 * @return value at position (i,j) in {@code this}
	 */
	public double getComponent( final int i, final int j )
	{
		return dataAccess.getValue( ( numElements * idx ) + ( j * cols + i ) );
	}

	/**
	 * TODO Documentation
	 * 
	 * @param i
	 *            column index
	 * @param j
	 *            row index
	 * @param value
	 */
	public void setComponent( final int i, final int j, final double value )
	{
		dataAccess.setValue( ( numElements * idx ) + ( j * cols + i ), value );
	}

	/**
	 * Element-wise addition
	 */
	@Override
	public void add( DoubleMatrix2DType c )
	{
		// TODO
//		assert ( c.getCols() == getCols() && c.getRows() == getRows() );
		for ( int i = 0; i < cols; ++i )
			for ( int j = 0; j < rows; ++j )
				setComponent( i, j, getComponent( i, j ) + c.getComponent( i, j ) );
	}

	/**
	 * Element-wise multiplication
	 */
	@Override
	public void mul( DoubleMatrix2DType c )
	{
		// TODO
//		assert ( c.getCols() == getCols() && c.getRows() == getRows() );
		for ( int i = 0; i < cols; ++i )
			for ( int j = 0; j < rows; ++j )
				setComponent( i, j, getComponent( i, j ) * c.getComponent( i, j ) );
	}

	/**
	 * Element-wise subtraction
	 */
	@Override
	public void sub( DoubleMatrix2DType c )
	{
		// TODO
//		assert ( c.getCols() == getCols() && c.getRows() == getRows() );
		for ( int i = 0; i < cols; ++i )
			for ( int j = 0; j < rows; ++j )
				setComponent( i, j, getComponent( i, j ) - c.getComponent( i, j ) );
	}

	/**
	 * Element-wise division
	 */
	@Override
	public void div( DoubleMatrix2DType c )
	{
		// TODO
//		assert ( c.getCols() == getCols() && c.getRows() == getRows() );
		for ( int i = 0; i < cols; ++i )
			for ( int j = 0; j < rows; ++j )
				setComponent( i, j, getComponent( i, j ) / c.getComponent( i, j ) );
	}

	@Override
	public void setOne()
	{
		for ( int i = 0; i < cols; ++i )
			for ( int j = 0; j < rows; ++j )
				setComponent( i, j, 1d );
	}

	@Override
	public void setZero()
	{
		for ( int i = 0; i < cols; ++i )
			for ( int j = 0; j < rows; ++j )
				setComponent( i, j, 1d );
	}

	@Override
	public void mul( float c )
	{
		for ( int i = 0; i < cols; ++i )
			for ( int j = 0; j < rows; ++j )
				setComponent( i, j, getComponent( i, j ) * c );	
	}

	@Override
	public void mul( double c )
	{
		for ( int i = 0; i < cols; ++i )
			for ( int j = 0; j < rows; ++j )
				setComponent( i, j, getComponent( i, j ) * c );
	}

}
