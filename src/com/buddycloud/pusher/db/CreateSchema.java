/*
 * Copyright 2011 buddycloud
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.buddycloud.pusher.db;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.buddycloud.pusher.utils.ConfigurationUtils;

/**
 * Creates jdbc schema required by the crawler
 * and by the search engine.
 *
 */
public class CreateSchema {
	
	/**
	 * 
	 */
	private static final String SQL_DELIMITER = ";";
	
	private static final String SQL_CREATE_FILE = "resources/schema/create-schema.sql";
	
	public static void main(String[] args) throws PropertyVetoException, IOException, SQLException {
		DataSource channelDirectoryDataSource = new DataSource(
				ConfigurationUtils.loadConfiguration());
		
		runScript(channelDirectoryDataSource, SQL_CREATE_FILE);
	}

	private static void runScript(
			DataSource channelDirectoryDataSource, String sqlFile)
			throws IOException, FileNotFoundException, SQLException {
		List<String> readLines = IOUtils.readLines(
				new FileInputStream(sqlFile));
		
		Connection connection = channelDirectoryDataSource.getConnection();
		StringBuilder statementStr = new StringBuilder();
		
		for (String line : readLines) {
			statementStr.append(line);
			if (line.endsWith(SQL_DELIMITER)) {
				Statement statement = connection.createStatement();
				statement.execute(statementStr.toString());
				statement.close();
				statementStr.setLength(0);
			}
		}
		
		connection.close();
	}
	
}
