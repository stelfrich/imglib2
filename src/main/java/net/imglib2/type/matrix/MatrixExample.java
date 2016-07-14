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

import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <>
 */
public class MatrixExample
{
	public static void main( final String[] args )
	{
		final Img< DoubleMatrix2DType > img =
				new ArrayImgFactory< DoubleMatrix2DType >().create(
						new int[] { 10 }, new DoubleMatrix2DType( 2, 2 ) );

		double x = 0;
		double y = 100;
		double z = 200;
		double u = 300;

		for ( final DoubleMatrix2DType vector : img )
		{
			vector.setComponent( 0, 0, x );
			vector.setComponent( 0, 1, y );
			vector.setComponent( 1, 0, z );
			vector.setComponent( 1, 1, u );
			x += 1;
			y += 10;
			z += 20;
			u += 30;
		}

		final RandomAccess< DoubleMatrix2DType > a = img.randomAccess();
		a.setPosition( new int[] { 2 } );
		final DoubleMatrix2DType vector = a.get();
		System.out.println( vector.getComponent( 0, 0 ) );
		System.out.println( vector.getComponent( 0, 1 ) );
		System.out.println( vector.getComponent( 1, 0 ) );
		System.out.println( vector.getComponent( 1, 1 ) );

		a.setPosition( new int[] { 5 } );
		final DoubleMatrix2DType vector2 = a.get();
		System.out.println( vector2.getComponent( 0, 0 ) );
		System.out.println( vector2.getComponent( 0, 1 ) );
		System.out.println( vector2.getComponent( 1, 0 ) );
		System.out.println( vector2.getComponent( 1, 1 ) );
	}
}
