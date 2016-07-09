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

package net.imglib2.outofbounds;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.util.Intervals;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the behavior of {@code definedBounds} for a tree of Views.
 * 
 * @author Stefan Helfrich
 */
public class DefinedBoundsTest
{

	final private long[] dim = new long[] { 5, 4, 3 };

	static private ArrayImg< IntType, ? > arrayImage;

	@Before
	public void setUp()
	{
		arrayImage = new ArrayImgFactory< IntType >().create( dim, new IntType() );

		int i = 0;
		for ( final IntType t : arrayImage )
			t.set( i++ );

		final int[] position = new int[ dim.length ];
		for ( final Cursor< IntType > c = arrayImage.cursor(); c.hasNext(); )
		{
			c.fwd();
			c.localize( position );

			i = 0;
			for ( int d = dim.length - 1; d >= 0; --d )
				i = i * ( int ) dim[ d ] + position[ d ];

			c.get().setInteger( i );
		}
	}

	/**
	 * Tests the behavior of {@code definedBounds} for a tree of Views.
	 */
	@Test
	public void testDefinedBounds()
	{
		ExtendedRandomAccessibleInterval< IntType, ? > extendZero = Views.extendZero( arrayImage );
		Interval definedBounds = extendZero.definedBounds();
		assertNull( definedBounds );

		IntervalView< IntType > interval = Views.interval( extendZero, Intervals.expand( arrayImage, 2 ) );
		definedBounds = interval.definedBounds();
		assertNull( definedBounds );

		IntervalView< IntType > expandedArrayImg = Views.interval( arrayImage, Intervals.expand( arrayImage, 2 ) );
		definedBounds = expandedArrayImg.definedBounds();
		assertEquals( definedBounds, arrayImage );

		IntervalView< IntType > expandedArrayImg2 = Views.interval( arrayImage, Intervals.expand( arrayImage, -1 ) );
		definedBounds = expandedArrayImg2.definedBounds();
		assertEquals( definedBounds, arrayImage );

		IntervalView< IntType > translate = Views.translate( arrayImage, new long[] { 10, 10, 10 } );
		definedBounds = translate.definedBounds();
		assertTrue( Intervals.equals( definedBounds, new FinalInterval( new long[] { 10, 10, 10 }, new long[] { 14, 13, 12 } ) ) );

		IntervalView< IntType > interval2 = Views.interval( translate, Intervals.expand( translate, -2 ) );
		definedBounds = interval2.definedBounds();
		assertTrue( Intervals.equals( definedBounds, new FinalInterval( new long[] { 10, 10, 10 }, new long[] { 14, 13, 12 } ) ) );

		ExtendedRandomAccessibleInterval< IntType, IntervalView< IntType > > extendZero2 = Views.extendZero( translate );
		definedBounds = extendZero2.definedBounds();
		assertNull( definedBounds );

	}

}
