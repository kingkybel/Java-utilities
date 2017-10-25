/*
 * Copyright (C) 2017 Dieter J Kybelksties
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @date: 2017-10-23
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.protocol;

import java.util.TreeMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Dieter J Kybelksties
 */
public class Protocol
{

    private static final Class CLAZZ = Protocol.class;
    private static final Log LOGGER = LogFactory.getLog(CLAZZ);
    TreeMap<Rule, Rule> rules = new TreeMap<>();

    public Protocol()
    {
    }

    public void addRule(Rule rule)
    {
        if (rule != null)
        {
            rules.put(rule, rule);
        }
    }

    public Rule getRule(Actor fromActor,
                        Actor toActor,
                        State currentState,
                        ProcessMessage.Type messageType) throws Exception
    {
        Rule key = new Rule(fromActor,
                            toActor,
                            currentState,
                            messageType,
                            null);
        return rules.get(key);
    }

}
