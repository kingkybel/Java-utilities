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
 * @date: 2016-10-20
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.general;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author Dieter J Kybelksties
 */
public class LogFormatter extends Formatter
{

    private static final Class CLAZZ = LogFormatter.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    @Override
    public String format(LogRecord record)
    {
        StringBuilder builder = new StringBuilder(1000);
        builder.append(DateUtils.LogDateFormat.format(
                new Date(record.getMillis()))).append(", ");
        builder.append(record.getLevel()).append(", ");
        builder.append(formatMessage(record)).append(", ");
        builder.append(record.getSourceClassName()).append(StringUtils.NEWLINE);
        return builder.toString();
    }

    @Override
    public String getHead(Handler h)
    {
        return super.getHead(h);
    }

    @Override
    public String getTail(Handler h)
    {
        return super.getTail(h);
    }
}
