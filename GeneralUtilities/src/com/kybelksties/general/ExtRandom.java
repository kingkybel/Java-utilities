
/*
 * Copyright (C) 2016 Dieter J Kybelksties
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * @date: 2016-01-07
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.general;

import java.util.logging.Logger;

/**
 * Extension of the java.util.ExtRandom class that can get random values within
 * lower and upper boundaries.
 *
 * @author Dieter J Kybelksties
 */
public class ExtRandom extends java.util.Random
{

    private static final String CLASS_NAME = ExtRandom.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    /**
     * Default construct.
     */
    public ExtRandom()
    {
        super();
    }

    /**
     * Construct with a seed.
     *
     * @param seed seed for the pseudo-random generator
     */
    public ExtRandom(long seed)
    {
        super(seed);
    }

    /**
     * Retrieve an integer value between low and high values. The interval is
     * always interpreted between the actually lower value to the actually
     * greater value. The order in which these are given in the parameter list
     * is not relevant. The greater value of the two is exclusive of the
     * result-interval, the lower value will be inclusive.
     *
     * @param low  the lower bound
     * @param high the upper bound
     * @return a random value from the interval
     */
    public int nextInt(Integer low, Integer high)
    {
        if (low == null)
        {
            low = 0;
        }
        if (high == null)
        {
            high = 0;
        }
        if (high < low)
        {
            Integer tmp = high;
            high = low;
            low = tmp;
        }
        int reval = low.equals(high) ? low : nextInt(high - low) + low;

        return reval;

    }

    /**
     * Retrieve a long value between 0 and high value. The high value is
     * exclusive of the result-interval, 0 value will be inclusive.
     *
     * @param high the upper bound
     * @return a random value from the interval
     */
    public long nextLong(Long high)
    {
        if (high == 0L)
        {
            return 0L;
        }
        long reval = nextLong();
        if (high == null)
        {
            return reval;
        }
        if (reval < 0L)
        {
            reval *= -1L;
        }
        if (high != 0L)
        {
            reval %= high;
        }
        return reval;
    }

    /**
     * Retrieve a long value between low and high values. The interval is always
     * interpreted between the actually lower value to the actually greater
     * value. The order in which these are given in the parameter list is not
     * relevant. The greater value of the two is exclusive of the
     * result-interval, the lower value will be inclusive.
     *
     * @param low  the lower bound
     * @param high the upper bound
     * @return a random value from the interval
     */
    public long nextLong(Long low, Long high)
    {
        if (low == null)
        {
            low = 0L;
        }
        if (high == null)
        {
            high = 0L;
        }
        if (high < low)
        {
            Long tmp = high;
            high = low;
            low = tmp;
        }
        long reval = low.equals(high) ? low : nextLong(high - low) + low;

        return reval;

    }

    /**
     * Retrieve a double value between 0 and high value. The high value is
     * exclusive of the result-interval, 0 value will be inclusive.
     *
     * @param high the upper bound
     * @return a random value from the interval
     */
    public double nextDouble(Double high)
    {
        if (high == 0L)
        {
            return 0L;
        }
        double reval = nextLong();
        if (high == null)
        {
            return reval;
        }
        if (reval < 0.0)
        {
            reval *= -1.0;
        }
        if (high != 0.0)
        {
            reval %= high;
        }
        return reval;
    }

    /**
     * Retrieve a double value between low and high values. The interval is
     * always interpreted between the actually lower value to the actually
     * greater value. The order in which these are given in the parameter list
     * is not relevant. The greater value of the two is exclusive of the
     * result-interval, the lower value will be inclusive.
     *
     * @param low  the lower bound
     * @param high the upper bound
     * @return a random value from the interval
     */
    public double nextDouble(Double low, Double high)
    {
        if (low == null)
        {
            low = 0.0;
        }
        if (high == null)
        {
            high = 0.0;
        }
        if (high < low)
        {
            Double tmp = high;
            high = low;
            low = tmp;
        }
        double reval = low.equals(high) ? low : nextDouble(high - low) + low;

        return reval;

    }

    /**
     * Retrieve a float value between 0 and high value. The high value is
     * exclusive of the result-interval, 0 value will be inclusive.
     *
     * @param high the upper bound
     * @return a random value from the interval
     */
    public float nextFloat(Float high)
    {
        if (high == 0L)
        {
            return 0L;
        }
        float reval = nextLong();
        if (high == null)
        {
            return reval;
        }
        if (reval < 0.0F)
        {
            reval *= -1.0F;
        }
        if (high != 0.0F)
        {
            reval %= high;
        }
        return reval;
    }

    /**
     * Retrieve a float value between low and high values. The interval is
     * always interpreted between the actually lower value to the actually
     * greater value. The order in which these are given in the parameter list
     * is not relevant. The greater value of the two is exclusive of the
     * result-interval, the lower value will be inclusive.
     *
     * @param low  the lower bound
     * @param high the upper bound
     * @return a random value from the interval
     */
    public float nextFloat(Float low, Float high)
    {
        if (low == null)
        {
            low = 0.0F;
        }
        if (high == null)
        {
            high = 0.0F;
        }
        if (high < low)
        {
            Float tmp = high;
            high = low;
            low = tmp;
        }
        float reval = low.equals(high) ? low : nextFloat(high - low) + low;

        return reval;

    }

}
