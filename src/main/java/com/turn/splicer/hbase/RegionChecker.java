/**
 * Copyright 2015-2016 The Splicer Query Engine Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.turn.splicer.hbase;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HRegionLocation;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegionChecker implements Closeable {

	private static final Logger LOG = LoggerFactory.getLogger(RegionChecker.class);

	public static int METRIC_WIDTH = 3;
	public static int TS_HOUR_WIDTH = 4;

	private final HTable table;

	public RegionChecker(Configuration config) {
		try {
			this.table = new HTable(config, "tsdb");
		} catch (IOException e) {
			LOG.error("Could not create connection", e);
			throw new RegionCheckException("Could not create connection", e);
		}
	}

	/**
	 * Find the best region server for a given row key range
	 *
	 * @param metric name of the metric
	 * @param startTime in seconds
	 * @param endTime in seconds
	 * @return the best region server to query for this metric, start, stop row key
	 */
	public String getBestRegionHost(String metric, long startTime, long endTime) {
		final byte[] startRowKey = new byte[METRIC_WIDTH + TS_HOUR_WIDTH];
		final byte[] endRowKey = new byte[METRIC_WIDTH + TS_HOUR_WIDTH];

		byte[] metricKey = MetricsCache.get().getMetricKey(metric);
		LOG.debug("{}",metricKey.length);
		LOG.debug(Arrays.toString(metricKey));

		System.arraycopy(metricKey, 0, startRowKey, 0, METRIC_WIDTH);
		System.arraycopy(metricKey, 0, endRowKey, 0, METRIC_WIDTH);

		Bytes.putInt(startRowKey, METRIC_WIDTH, (int) startTime);
		Bytes.putInt(endRowKey, METRIC_WIDTH, (int) endTime);

		return getBestRegionHost(startRowKey, endRowKey);
	}

	public String getBestRegionHost(byte[] startRowKey, byte[] endRowKey) {
		try {
			List<HRegionLocation> regions = table.getRegionsInRange(startRowKey, endRowKey);
			if (regions != null && regions.size() > 0) {
				HRegionLocation reg = regions.get(0);
				LOG.debug("Found region hostname: " + reg.getHostname());
				return reg.getHostname();
			} else {
				LOG.info("Regions is null");
				throw new RegionCheckException("Could not find a host");
			}
		} catch (IOException e) {
			throw new RegionCheckException("Could not handle region server lookup", e);
		}
	}

	public void close() {
		try {
			table.close();
		} catch (IOException e) {
			LOG.error("Could not close connection", e);
		}
	}
}
