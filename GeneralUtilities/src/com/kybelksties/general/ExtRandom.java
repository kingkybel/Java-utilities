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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Extension of the java.util.ExtRandom class that can get random values within
 * lower and upper boundaries.
 *
 * @author Dieter J Kybelksties
 */
public class ExtRandom extends java.util.Random
{

    private static final Class CLAZZ = ExtRandom.class;
    private static final String CLASS_NAME = CLAZZ.getName();
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

    @Override
    protected int next(int bits)
    {
        return super.next(bits);
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

        return (reval / Long.MAX_VALUE) * high;
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

        return low.equals(high) ? low : nextDouble(high - low) + low;
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
            reval = (reval / Long.MAX_VALUE) * high;
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

        return low.equals(high) ? low : nextFloat(high - low) + low;

    }

    /**
     * Get a random enum-value given the enum-class.
     *
     * @param <T> template parameter for enum type
     * @param c   class of the enum
     * @return a random enum-value if c is indeed an enumeration, and null
     *         otherwise
     */
    public <T> T nextEnum(Class<T> c)
    {
        if (c.isEnum())
        {
            T[] values = c.getEnumConstants();
            return values[nextInt(values.length)];
        }
        return null;
    }

    /**
     * Get a random enum-value given an upper margin of enum-values (in terms of
     * its ordinal number).
     *
     * @param <T>  template parameter for enum type
     * @param high
     * @return a random enum-value if T is indeed an enumeration-type, and null
     *         otherwise
     */
    public <T> T nextEnum(T high)
    {
        if (high == null)
        {
            return null;
        }
        Class<T> c = (Class<T>) (high.getClass());
        if (c.isEnum())
        {
            T[] values = c.getEnumConstants();
            return values[nextInt(((Enum) high).ordinal())];
        }
        return null;
    }

    /**
     * Get a random enum-value given a lower and an upper margin of enum-values
     * (in terms of their ordinal number).
     *
     * @param <T>  template parameter for enum type
     * @param low  the lower margin
     * @param high the higher margin
     * @return a random enum-value if T is indeed an enumeration-type, and null
     *         otherwise
     */
    public <T> T nextEnum(T low, T high)
    {
        if (low == null && high == null)
        {
            return null;
        }
        Class<T> c = (Class<T>) (low != null ? low.getClass() : high.getClass());

        if (c.isEnum())
        {
            T[] values = c.getEnumConstants();
            if (low == null)
            {
                low = values[0];
            }
            else if (high == null)
            {
                high = values[values.length - 1];
            }
            if (((Enum) low).ordinal() > ((Enum) high).ordinal())
            {
                T tmp = low;
                low = high;
                high = tmp;
            }
            return values[nextInt(((Enum) low).ordinal(),
                                  ((Enum) high).ordinal())];
        }
        return null;
    }

    /**
     * Probabilities for mixed type enumerated objects.
     */
    public class DistinctValues
    {

        private ArrayList<Object> values;
        private ArrayList<Double> probabilities;

        /**
         * Default constructor.
         *
         * @param valueProb values and their probabilities given as array of
         *                  arrays, first element is the value second is
         *                  probability
         */
        public DistinctValues(Object[]... valueProb)
        {
            setData(valueProb);
        }

        /**
         * Construct with a value/probability map.
         *
         * @param valueProb values and their probabilities given as map
         */
        public DistinctValues(
                Map<Object, Double> valueProb)
        {
            setData(valueProb);
        }

        /**
         * Construct with a value/probability map.
         *
         * @param valueProb values as iterable container
         */
        public DistinctValues(Iterable valueProb)
        {
            setData(valueProb);
        }

        /**
         * Make sure that the probabilities meet probability requirements.
         */
        private void ensureProbability()
        {
            Double totalProb = 0.0;
            int vSize = values == null ? 0 : values.size();
            int pSize = probabilities == null ? 0 : probabilities.size();
            if (vSize != pSize)
            {
                if (vSize < pSize)
                {
                    for (int i = vSize; i < pSize; i++)
                    {
                        values.add(new Object());
                    }
                }
                else
                {
                    for (int i = pSize; i < vSize; i++)
                    {
                        probabilities.add(0.0);
                    }
                }
            }
            for (Double prob : probabilities)
            {
                if (prob == null || prob < 0.0)
                {
                    prob = 0.0;
                }
                totalProb += prob;
            }
            for (int i = 0; i < probabilities.size(); i++)
            {
                probabilities.set(i, probabilities.get(i) / totalProb);
            }

        }

        /**
         * Set the values and their probabilities given as array of arrays. Sets
         * any negative values to 0.0 and scales probabilities so that they add
         * up to 1.0.
         *
         * @param valueProb values and their probabilities given as array of
         *                  arrays, first element is the value second is
         *                  probability
         */
        public final void setData(Object[]... valueProb)
        {
            values = new ArrayList<>();
            probabilities = new ArrayList<>();
            if (valueProb == null)
            {
                return;
            }
            for (Object[] vp : valueProb)
            {
                if (vp != null && vp.length > 0)
                {
                    if (vp[0] != null)
                    {
                        values.add(vp[0]);
                        Double prob = 1.0 / valueProb.length;
                        if (vp.length > 1 && vp[1] != null)
                        {
                            Class c = vp[1].getClass();
                            if (c.equals(Float.class))
                            {
                                prob = ((Float) vp[1]).doubleValue();
                            }
                            else if (c.equals(Double.class))
                            {
                                prob = (Double) vp[1];
                            }
                            else if (c.equals(Short.class))
                            {
                                prob = ((Short) vp[1]).doubleValue();
                            }
                            else if (c.equals(Byte.class))
                            {
                                prob = ((Byte) vp[1]).doubleValue();
                            }
                            else if (c.equals(Integer.class))
                            {
                                prob = ((Integer) vp[1]).doubleValue();
                            }
                            else if (c.equals(Long.class))
                            {
                                prob = ((Long) vp[1]).doubleValue();
                            }
                            if (prob < 0.0)
                            {
                                prob = 1.0 / valueProb.length;
                            }
                        }
                        probabilities.add(prob);
                    }
                }
            }
            ensureProbability();
        }

        /**
         * Set the values and their probabilities given as map. Sets any
         * negative values to 0.0 and scales probabilities so that they add up
         * to 1.0.
         *
         * @param valueProb values and their probabilities given as map
         */
        public final void setData(Map<Object, Double> valueProb)
        {
            probabilities = new ArrayList<>();
            values = new ArrayList<>();

            for (Object key : valueProb.keySet())
            {
                values.add(key);
                probabilities.add(valueProb.get(key));
            }
            ensureProbability();
        }

        /**
         * Set the values wit uniform probabilities given as iterable container.
         * Sets any negative values to 0.0 and scales probabilities so that they
         * add up to 1.0.
         *
         * @param valueProb values as iterable container
         */
        public final void setData(Iterable valueProb)
        {
            probabilities = new ArrayList<>();
            values = new ArrayList<>();
            Iterator iter = valueProb.iterator();
            while (iter.hasNext())
            {
                Object o = iter.next();
                if (o != null)
                {
                    values.add(o);
                    probabilities.add(1.0);
                }
            }
            ensureProbability();
        }

        /**
         * Get another random object. The probability of an the occurrence of an
         * object is governed by the probabilities given at construction.
         *
         * @return the next random Object
         */
        public Object next()
        {
            Object reval = null;

            Double prob = nextDouble(0.0, 1.0);
            if (values.isEmpty())
            {
                return reval;
            }
            Double sum = 0.0;
            for (int i = 0; i < probabilities.size(); i++)
            {
                if (prob > sum && prob <= sum + probabilities.get(i))
                {
                    return values.get(i);
                }
                sum += probabilities.get(i);
            }
            return reval;
        }
    }
}
