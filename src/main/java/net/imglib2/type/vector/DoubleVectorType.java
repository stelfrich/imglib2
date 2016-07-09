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

package net.imglib2.type.vector;

import net.imglib2.img.NativeImg;
import net.imglib2.img.NativeImgFactory;
import net.imglib2.img.basictypeaccess.DoubleAccess;
import net.imglib2.img.basictypeaccess.array.DoubleArray;
import net.imglib2.type.NativeType;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.util.Fraction;

/**
 * {@link Type} implementing vectors of double.
 * 
 * @author Tobias Pietzsch
 * @author Stefan Helfrich
 *
 */
public class DoubleVectorType implements NativeType< DoubleVectorType >, NumericType< DoubleVectorType >
{
	/** Length of vector */
	protected final int vlen;

	/** the DataAccess that holds the information */
	protected DoubleAccess dataAccess;

	/** Index in the underlying dataAccess */
	private int idx = 0;

	/** TODO Documentation */
	final protected NativeImg< DoubleVectorType, ? extends DoubleAccess > img;

	/**
	 * TODO Documentation
	 * 
	 * @param vlen
	 */
	public DoubleVectorType( final int vlen )
	{
		this.vlen = vlen;
		img = null;
	}

	/**
	 * this is the constructor if you want it to be a variable
	 * 
	 * TODO Documentation
	 * 
	 * @param values
	 */
	public DoubleVectorType( final double[] values )
	{
		img = null;
		vlen = values.length;
		dataAccess = new DoubleArray( vlen );
		for ( int d = 0; d < vlen; ++d )
			setComponent( d, values[ d ] );
	}

	/**
	 * this is the constructor if you want it to read from an array
	 */
	public DoubleVectorType(
			final int vlen,
			final NativeImg< DoubleVectorType, ? extends DoubleAccess > container )
	{
		this.vlen = vlen;
		img = container;
	}

	@Override
	public DoubleVectorType createVariable()
	{
		return new DoubleVectorType( new double[ vlen ] );
	}

	@Override
	public DoubleVectorType copy()
	{
		final DoubleVectorType t = createVariable();
		t.set( this );
		return t;
	}

	@Override
	public void set( final DoubleVectorType c )
	{
		for ( int d = 0; d < vlen; ++d )
			setComponent( d, c.getComponent( d ) );
	}

	@Override
	public boolean valueEquals( final DoubleVectorType t )
	{
		for ( int d = 0; d < vlen; ++d )
			if ( t.getComponent( d ) != getComponent( d ) )
				return false;
		return true;
	}

	@Override
	public Fraction getEntitiesPerPixel()
	{
		return new Fraction( vlen, 1 );
	}

	@Override
	public NativeImg< DoubleVectorType, ? > createSuitableNativeImg(
			final NativeImgFactory< DoubleVectorType > storageFactory,
			final long[] dim )
	{
		// create the container
		final NativeImg< DoubleVectorType, ? extends DoubleAccess > container =
				storageFactory.createDoubleInstance( dim, getEntitiesPerPixel() );

		// create a Type that is linked to the container
		final DoubleVectorType linkedType = new DoubleVectorType( vlen, container );

		// pass it to the NativeContainer
		container.setLinkedType( linkedType );

		return container;
	}

	@Override
	public DoubleVectorType duplicateTypeOnSameNativeImg()
	{
		return new DoubleVectorType( vlen, img );
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

	public double getComponent( final int d )
	{
		return dataAccess.getValue( vlen * idx + d );
	}

	public void setComponent( final int d, final double value )
	{
		dataAccess.setValue( vlen * idx + d, value );
	}

	@Override
	public void add( DoubleVectorType c )
	{
		for ( int d = 0; d < vlen; ++d )
			setComponent( d, getComponent( d ) + c.getComponent( d ) );
	}

	@Override
	public void mul( DoubleVectorType c )
	{
		for ( int d = 0; d < vlen; ++d )
			setComponent( d, getComponent( d ) * c.getComponent( d ) );
	}

	@Override
	public void sub( DoubleVectorType c )
	{
		for ( int d = 0; d < vlen; ++d )
			setComponent( d, getComponent( d ) - c.getComponent( d ) );
	}

	@Override
	public void div( DoubleVectorType c )
	{
		for ( int d = 0; d < vlen; ++d )
			setComponent( d, getComponent( d ) / c.getComponent( d ) );
	}

	@Override
	public void setOne()
	{
		for ( int d = 0; d < vlen; ++d )
			setComponent( d, 1 );
	}

	@Override
	public void setZero()
	{
		for ( int d = 0; d < vlen; ++d )
			setComponent( d, 0 );
	}

	@Override
	public void mul( float c )
	{
		for ( int d = 0; d < vlen; ++d )
			setComponent( d, getComponent( d ) * c );
	}

	@Override
	public void mul( double c )
	{
		for ( int d = 0; d < vlen; ++d )
			setComponent( d, getComponent( d ) * c );
	}
}
