/*
 * Copyright (C) 2018 Dieter J Kybelksties
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
 * @date: 2018-01-30
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.general;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.logging.Logger;

/**
 * BitSet-extension that can be constructed from arrays.
 *
 * @author Dieter J Kybelksties
 */
public class ExtBitSet extends BitSet
{

    private static final Class CLAZZ = ExtBitSet.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    /**
     * Convert a long value into an array of bytes.
     *
     * @param v the value to convert
     * @return the created byte-array
     */
    public static byte[] toByteArray(long v)
    {
        return ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(v).array();
    }

    /**
     * Convert a int value into an array of bytes.
     *
     * @param v the value to convert
     * @return the created byte-array
     */
    public static byte[] toByteArray(int v)
    {
        return ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(v).array();
    }

    /**
     * Convert a short value into an array of bytes.
     *
     * @param v the value to convert
     * @return the created byte-array
     */
    public static byte[] toByteArray(short v)
    {
        return ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putShort(v).array();
    }

    /**
     * Convert a char value into an array of bytes.
     *
     * @param v the value to convert
     * @return the created byte-array
     */
    public static byte[] toByteArray(char v)
    {
        return ByteBuffer.allocate(Character.SIZE / Byte.SIZE).putChar(v).
                array();
    }

    /**
     * Convert a byte value into an array of bytes.
     *
     * @param v the value to convert
     * @return the created byte-array
     */
    public static byte[] toByteArray(byte v)
    {
        return ByteBuffer.allocate(Byte.SIZE / Byte.SIZE).put(v).array();
    }

    /**
     * Convert a boolean value into an array of bytes.
     *
     * @param v the value to convert
     * @return the created byte-array
     */
    public static byte[] toByteArray(boolean v)
    {
        return ByteBuffer.allocate(1).putInt(v ? 1 : 0).array();
    }

    /**
     * Convert an Object-array value into an array of bytes.
     *
     * @param v the value to convert
     * @return the created byte-array
     * @throws IOException
     */
    public static byte[] toByteArray(Object[] v) throws IOException
    {
        byte[] bytes = null;
        // TODO
        return bytes;
    }

    /**
     * Construct from a general Object.
     *
     * @param initBits the object describing the initial bits
     */
    public ExtBitSet(Object initBits)
    {
        ArrayList<Object> bits = new ArrayList<>();
        if (initBits != null)
        {
            bits.add(initBits);
        }
        set(bits.toArray());
    }

    /**
     * Construct from a general Object-array.
     *
     * @param initBits the object-array describing the initial bits
     */
    public ExtBitSet(Object[] initBits)
    {
        set(initBits);
    }

    @Override
    public String toString()
    {
        String reval = length() + ":" + (length() == 0 ? "0" : "");
        for (int i = length() - 1; i >= 0; i--)
        {
            reval += get(i) ? "1" : "0";
        }
        return reval;
    }

    /**
     * Set bits from the given byte-array.
     *
     * @param initBits the byte-array describing the bits to set
     */
    public final void set(byte[] initBits)
    {
        if (initBits != null)
        {
            Byte[] tmp = new Byte[initBits.length];
            for (int i = 0; i < initBits.length; i++)
            {
                tmp[i] = initBits[i];
            }
            set(tmp);
        }
    }

    /**
     * Set bits from the given byte-array after clearing out pre-existing bits.
     *
     * @param initBits the byte-array describing the bits to set
     */
    public final void reset(byte[] initBits)
    {
        clear();
        set(initBits);
    }

    /**
     * Set bits from the given char-array.
     *
     * @param initBits the char-array describing the bits to set
     */
    public final void set(char[] initBits)
    {
        if (initBits != null)
        {
            Character[] tmp = new Character[initBits.length];
            for (int i = 0; i < initBits.length; i++)
            {
                tmp[i] = initBits[i];
            }
            set(tmp);
        }
    }

    /**
     * Set bits from the given char-array after clearing out pre-existing bits.
     *
     * @param initBits the char-array describing the bits to set
     */
    public final void reset(char[] initBits)
    {
        clear();
        set(initBits);
    }

    /**
     * Set bits from the given short-array.
     *
     * @param initBits the short-array describing the bits to set
     */
    public final void set(short[] initBits)
    {
        if (initBits != null)
        {
            Short[] tmp = new Short[initBits.length];
            for (int i = 0; i < initBits.length; i++)
            {
                tmp[i] = initBits[i];
            }
            set(tmp);
        }
    }

    /**
     * Set bits from the given short-array after clearing out pre-existing bits.
     *
     * @param initBits the short-array describing the bits to set
     */
    public final void reset(short[] initBits)
    {
        clear();
        set(initBits);
    }

    /**
     * Set bits from the given int-array.
     *
     * @param initBits the int-array describing the bits to set
     */
    public final void set(int[] initBits)
    {
        if (initBits != null)
        {
            Integer[] tmp = new Integer[initBits.length];
            for (int i = 0; i < initBits.length; i++)
            {
                tmp[i] = initBits[i];
            }
            set(tmp);
        }
    }

    /**
     * Set bits from the given int-array after clearing out pre-existing bits.
     *
     * @param initBits the int-array describing the bits to set
     */
    public final void reset(int[] initBits)
    {
        clear();
        set(initBits);
    }

    /**
     * Set bits from the given long-array.
     *
     * @param initBits the long-array describing the bits to set
     */
    public final void set(long[] initBits)
    {
        if (initBits != null)
        {
            Long[] tmp = new Long[initBits.length];
            for (int i = 0; i < initBits.length; i++)
            {
                tmp[i] = initBits[i];
            }
            set(tmp);
        }
    }

    /**
     * Set bits from the given long-array after clearing out pre-existing bits.
     *
     * @param initBits the long-array describing the bits to set
     */
    public final void reset(long[] initBits)
    {
        clear();
        set(initBits);
    }

    /**
     * Set bits from the given boolean-array.
     *
     * @param initBits the boolean-array describing the bits to set
     */
    public final void set(boolean[] initBits)
    {
        if (initBits != null)
        {
            Boolean[] tmp = new Boolean[initBits.length];
            for (int i = 0; i < initBits.length; i++)
            {
                tmp[i] = initBits[i];
            }
            set(tmp);
        }
    }

    /**
     * Set bits from the given boolean-array after clearing out pre-existing
     * bits.
     *
     * @param initBits the boolean-array describing the bits to set
     */
    public final void reset(boolean[] initBits)
    {
        clear();
        set(initBits);
    }

    /**
     * Set bits from the given Boolean-array.
     *
     * @param initBits the Boolean-array describing the bits to set
     */
    public final void set(Boolean[] initBits)
    {
        if (initBits != null)
        {
            for (int i = initBits.length - 1; i >= 0; i--)
            {
                set(initBits.length - 1 - i, initBits[i]);
            }
        }
    }

    /**
     * Set bits from the given Boolean-array after clearing out pre-existing
     * bits.
     *
     * @param initBits the Boolean-array describing the bits to set
     */
    public final void reset(Boolean[] initBits)
    {
        clear();
        set(initBits);
    }

    /**
     * Set bits from the given Object-array.
     *
     * @param initBits the Object-array describing the bits to set
     */
    public final void set(Object[] initBits)
    {
        if (initBits != null && initBits.length > 0)
        {
            final Class clazz = initBits[0].getClass();
            if (clazz.equals(Byte.class) ||
                clazz.equals(Character.class) ||
                clazz.equals(Short.class) ||
                clazz.equals(Integer.class) ||
                clazz.equals(Long.class))
            {
                byte[] data;
                int numClassBytes = clazz.equals(Byte.class) ? 1 :
                                    clazz.equals(Character.class) ? 2 :
                                    clazz.equals(Short.class) ? 2 :
                                    clazz.equals(Integer.class) ? 4 :
                                    clazz.equals(Long.class) ? 8 :
                                    0;
                int bitToSet = initBits.length * (8 * numClassBytes - 1) - 1;
                for (Object value : initBits)
                {
                    data = oneValueToByteArray(value);

                    for (int i = 0; i < data.length; i++)
                    {
                        byte b = data[i];
                        for (int bit = (i == 0 ? 7 : 8); bit > 0; bit--)
                        {
                            if ((b & (0x01 << (bit - 1))) > 0)
                            {
                                super.set(bitToSet);
                            }
                            bitToSet--;
                        }
                    }
                }
            }
        }
    }

    /**
     * Set bits from the given Object-array after clearing out pre-existing
     * bits.
     *
     * @param initBits the Object-array describing the bits to set
     */
    public final void reset(Object[] initBits)
    {
        clear();
        set(initBits);
    }

    /**
     * Convert one object to a byte-array.
     *
     * @param value the object to convert
     * @return the resulting byte-array
     */
    public byte[] oneValueToByteArray(Object value)
    {
        byte[] data;
        if (value.getClass().equals(byte.class) ||
            value.getClass().equals(Byte.class))
        {
            data = toByteArray((byte) value);
        }
        else if (value.getClass().equals(short.class) ||
                 value.getClass().equals(Short.class))
        {
            data = toByteArray((short) value);
        }
        else if (value.getClass().equals(char.class) ||
                 value.getClass().equals(Character.class))
        {
            data = toByteArray((char) value);
        }
        else if (value.getClass().equals(int.class) ||
                 value.getClass().equals(Integer.class))
        {
            data = toByteArray((int) value);
        }
        else if (value.getClass().equals(long.class) ||
                 value.getClass().equals(Long.class))
        {
            data = toByteArray((long) value);
        }
        else
        {
            data = new byte[0];
        }
        return data;
    }
}
