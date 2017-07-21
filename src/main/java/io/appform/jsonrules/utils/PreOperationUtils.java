/**
 * Copyright (c) 2017 Mohammed Irfanulla S <mohammed.irfanulla.s1@gmail.com>
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
 *
 */

package io.appform.jsonrules.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ext.DOMDeserializer;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import io.appform.jsonrules.expressions.preoperation.PreOperation;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.util.List;

public class PreOperationUtils {

	private PreOperationUtils() {
		// to prevent instantion of this utils class.
	}

	private static final String MONTH_OF_YEAR = "month_of_year";
	private static final String WEEK_OF_YEAR = "week_of_year";
	private static final String WEEK_OF_MONTH = "week_of_month";
	private static final String DAY_OF_YEAR = "day_of_year";
	private static final String DAY_OF_MONTH = "day_of_month";
	private static final String DAY_OF_WEEK = "day_of_week";
	private static final String HOUR_OF_DAY = "hour_of_day";
	private static final String MINUTE_OF_HOUR = "minute_of_hour";

	public static final int getFromDateTime(OffsetDateTime dateTime, String field) {
		switch (field) {
		case MINUTE_OF_HOUR:
			return dateTime.get(ChronoField.MINUTE_OF_HOUR);
		case HOUR_OF_DAY:
			return dateTime.get(ChronoField.HOUR_OF_DAY);
		case DAY_OF_WEEK:
			return dateTime.get(ChronoField.DAY_OF_WEEK);
		case DAY_OF_MONTH:
			return dateTime.get(ChronoField.DAY_OF_MONTH);
		case DAY_OF_YEAR:
			return dateTime.get(ChronoField.DAY_OF_YEAR);
		case WEEK_OF_MONTH:
			return dateTime.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
		case WEEK_OF_YEAR:
			return dateTime.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
		case MONTH_OF_YEAR:
			return dateTime.get(ChronoField.MONTH_OF_YEAR);
		default:
		}
		throw new IllegalArgumentException("Operand doesnot represent a valid field");
	}
	
	public static final OffsetDateTime getDateTime(long epoch, String zoneOffSet) {
		try {
			Instant instant;
			if (isEpochInMillis(epoch)) {
				instant = Instant.ofEpochMilli(epoch);
			} else {
				instant = Instant.ofEpochSecond(epoch);
			}
			if (zoneOffSet != null && !zoneOffSet.trim().isEmpty()) {
				return instant.atOffset(ZoneOffset.of(zoneOffSet));
			}
			return instant.atOffset(ZoneOffset.UTC);
		} catch(Exception e) {
			throw new IllegalArgumentException("Operand doesnot represent a valid date");
		}
	}
	
	public static final OffsetDateTime getDateTime(String dateTimeStr, String zoneOffSet) {
		try {
			Instant instant = Instant.parse(dateTimeStr);
			if (zoneOffSet != null && !zoneOffSet.trim().isEmpty()) {
				return instant.atOffset(ZoneOffset.of(zoneOffSet));
			}
			return instant.atOffset(ZoneOffset.UTC);
		} catch(Exception e) {
			throw new IllegalArgumentException("Operand doesnot represent a valid date");
		}
	
	}
	
	private static boolean isEpochInMillis(long epoch) {
		// Safe check to know if epoch is in milli seconds for all dates between 1973 to 5138
		if (String.valueOf(epoch).length() >= 12) {
			return true;
		}
		return false;
	}
	
	public static final LocalDateTime getLocalDateTime(long epoch, String timeZone) {
		try {
			long epochSecond = Long.divideUnsigned(epoch, 1000);
			long nanoOfSecond = Long.remainderUnsigned(epoch, 1000) * 1_000_000;
			final ZoneOffset zoneOffSet = (timeZone == null) ? ZoneOffset.UTC : ZoneOffset.of(timeZone);
			return LocalDateTime.ofEpochSecond(epochSecond, (int) nanoOfSecond, zoneOffSet);
		} catch (Exception e) {
			throw new IllegalArgumentException("Operand doesnot represent a valid date");
		}
	}

	public static final JsonNode evaluate(List<PreOperation<?>> preOperations, JsonNode jsonNode) {
		if (null == preOperations || preOperations.isEmpty()) {
			return jsonNode;
		}
		PreOperation operation = preOperations.get(0);
		return evaluate(preOperations.subList(1, preOperations.size()), (new ObjectMapper()).convertValue(operation.compute(jsonNode), JsonNode.class));
	}
	
}
