/*
 * RHQ Management Platform
 * Copyright (C) 2005-2009 Red Hat, Inc.
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.rhq.enterprise.server.xmlschema;

/**
 * A schedule that triggers based on a cron expression.
 * 
 * @author John Mazzitelli
 */
public class CronScheduleType extends AbstractScheduleType {
    public static final String TYPE_NAME = "cron";

    private final String cronExpression;

    public CronScheduleType(boolean concurrent, boolean clustered, String cronExpression) {
        super(concurrent, clustered, TYPE_NAME);
        this.cronExpression = cronExpression;
    }

    public String getCronExpression() {
        return this.cronExpression;
    }

    @Override
    public String getScheduleTrigger() {
        return getCronExpression();
    }
}
