package com.server2.sql.database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.server2.Constants;
import com.server2.Settings;
import com.server2.sql.database.SQLDatabaseConnection.DatabaseConnection.State;

/**
 * Handles a database connection.
 * 
 * @
 */

public class SQLDatabaseConnection implements Runnable {

	/**
	 * Represents a connection in the pool.
	 * 
	 * @
	 */
	public static class DatabaseConnection {

		/**
		 * The state of the connection.
		 * 
		 * @
		 */
		public static enum State {
			IDLE, INACTIVE, BUSY,
		}

		/**
		 * The database username.
		 */
		private final String username;

		/**
		 * The database password.
		 */
		private final String password;

		/**
		 * The url of the database.
		 */
		private final String url;

		/**
		 * The database connection.
		 */
		private Connection connection;

		/**
		 * The last time the connection was pinged.
		 */
		private long lastPing = System.currentTimeMillis();

		/**
		 * The sate of the connection.
		 */
		private State state = State.INACTIVE;

		/**
		 * Initialises the MySQL driver and database settings.
		 * 
		 * @param id
		 * @param username
		 * @param password
		 * @param url
		 */
		public DatabaseConnection(int id, String username, String password,
				String url) {
			this.username = username;
			this.password = password;
			this.url = "jdbc:mysql://" + url;
		}

		/**
		 * Closes the datbase connection.
		 */
		public void close() {
			if (connection != null) {
				try {
					connection.close();
				} catch (final SQLException e) {
					System.out.println("Error closing database connection");
				}
				setState(State.INACTIVE);
			}
		}

		/**
		 * Attempts to connect to the database.
		 * 
		 * @return
		 */
		public boolean connect() {
			setState(State.INACTIVE);
			try {
				connection = DriverManager.getConnection(url, username,
						password);
			} catch (final SQLException e) {
				System.out.println("Error connecting to MySQL database (" + url
						+ ") !");
				return false;
			}
			return true;
		}

		/**
		 * @return the connection.
		 */
		public Connection getConnection() {
			return connection;
		}

		/**
		 * @return the state of the connection.
		 */
		public State getState() {
			synchronized (this) {
				return state;
			}
		}

		/**
		 * @return true if the ping was successful.
		 */
		public boolean ping() {
			lastPing = System.currentTimeMillis();
			if (connection != null) {
				try {
					connection.prepareStatement("SELECT 1").executeQuery();
				} catch (final SQLException e) {
					return false;
				}
				return true;
			}
			return false;
		}

		public PreparedStatement prepStmt(String query) throws SQLException {
			return connection.prepareStatement(query);
		}

		/**
		 * Sets the state of the connection.
		 * 
		 * @param state
		 */
		public void setState(State state) {
			synchronized (this) {
				this.state = state;
			}
		}

	}

	/**
	 * Represents a database query.
	 * 
	 * @author Ultimate1
	 */
	public class Query implements Callable<ResultSet> {

		/**
		 * The query to be executed.
		 */
		private final String query;

		/**
		 * Initialise the query.
		 * 
		 * @param query
		 */
		private Query(String query) {
			this.query = query;
		}

		/**
		 * Executes the query.
		 * 
		 * @param dbc
		 * @return
		 */
		@Override
		public ResultSet call() {
			PreparedStatement statement = null;
			final boolean isUpdating = !query.toLowerCase()
					.startsWith("select");
			final DatabaseConnection pooledConnection = getPooledConnection();
			if (pooledConnection == null) {
				if (isUpdating)
					addFailedQuery(this);
				return null;
			}
			pooledConnection.setState(State.BUSY);
			try {
				statement = pooledConnection.getConnection().prepareStatement(
						query);
				if (isUpdating)
					statement.executeUpdate();
				else
					return statement.executeQuery();
			} catch (final SQLException e) {
				if (isUpdating)
					addFailedQuery(this);
			} finally {
				if (statement != null && isUpdating)
					try {
						statement.close();
					} catch (final SQLException e) {
						System.out.println("Error closing statement");
					}
				pooledConnection.setState(State.IDLE);
			}
			return null;
		}

		@Override
		public String toString() {
			return query;
		}

	}

	public static void write(String data) {
		if (Settings.getString("sv_name").contains("DEV"))
			return;
		final File file = new File(Constants.DATA_DIRECTORY + ""
				+ "queries.txt");
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file, true));
			bw.write(data);
			bw.newLine();
			bw.newLine();
			bw.flush();
		} catch (final IOException ioe) {

		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (final IOException ioe2) {

				}
		}
	}

	/**
	 * The database connection pool.
	 */
	public final DatabaseConnection[] pool;

	/**
	 * The work service.
	 */
	private final ExecutorService workService;

	/**
	 * Queries that failed execution.
	 */
	private final Queue<Query> failedQueries = new LinkedList<Query>();

	/**
	 * The interval at which connections are pinged.
	 */
	private long pingInterval = 300000;

	/**
	 * Determines if the <code>DatabaseManager</code> is running.
	 */
	private boolean isRunning = true;

	/**
	 * Initialise the database manager.
	 * 
	 * @param username
	 * @param password
	 * @param url
	 * @param poolSize
	 */
	public SQLDatabaseConnection(String username, String password, String url,
			int poolSize) {
		pool = new DatabaseConnection[poolSize];
		workService = Executors.newFixedThreadPool(pool.length);
		for (int id = 0; id < pool.length; id++)
			pool[id] = new DatabaseConnection(id, username, password, url);
		new Thread(this, "DatabaseConnection").start();
	}

	/**
	 * Adds a failed query to the list.
	 * 
	 * @param query
	 */
	public void addFailedQuery(Query query) {
		synchronized (this) {
			failedQueries.add(query);
		}
	}

	/**
	 * Executes a database query.
	 * 
	 * @param query
	 */
	public ResultSet executeQuery(String query) {
		ResultSet result = null;
		if (!query.toLowerCase().contains("select"))
			write(query);

		if (!query.toLowerCase().startsWith("select"))
			workService.submit(new Query(query));
		else {
			final Future<?> future = workService.submit(new Query(query));
			try {
				result = (ResultSet) future.get();
			} catch (final Exception e) {
				System.out.println("Error executing query!");
			}
		}
		return result;
	}

	/**
	 * Gets the first available connection from the pool.
	 * 
	 * @return
	 */
	public DatabaseConnection getPooledConnection() {
		for (final DatabaseConnection connection : pool)
			if (connection.getState().equals(State.IDLE))
				return connection;
		return null;
	}

	/**
	 * Connect all the connections.
	 * 
	 * @param alias
	 */
	public void initialise(String alias) {
		System.out.println("Connecting to " + alias + " database..");
		for (DatabaseConnection poolConnection : pool) {
			if (!poolConnection.connect())
				continue;
			poolConnection.setState(State.IDLE);
			System.out.println("Successfully connected to " + alias
					+ " database!");
		}
		try {
		 }catch (Exception e) {
			 e.printStackTrace();
		}
	}

	public PreparedStatement prepQuery(String query) {
		try {
			return pool[0].prepStmt(query);
		} catch (final Exception e) {
			return null;
		}
	}

	@Override
	public void run() {
		while (isRunning) {

			// Sleep a little..
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				continue;
			}

			// Ping the connections in the pool.
			for (final DatabaseConnection connection : pool) {
				if (System.currentTimeMillis() - connection.lastPing <= pingInterval)
					continue;
				if (!connection.ping()) {
					final boolean reconnected = connection.connect();
					if (reconnected)
						connection.setState(State.IDLE);
				}
			}

			// Execute queries that previously failed.
			synchronized (this) {
				if (!failedQueries.isEmpty()) {
					Query query;
					while ((query = failedQueries.poll()) != null)
						workService.submit(query);
				}
			}

		}

	}

	/**
	 * Sets the interval at which connections are pinged in minutes.
	 * 
	 * @param pingInterval
	 */
	public void setPingInterval(int pingInterval) {
		this.pingInterval = pingInterval * 60000;
	}

	/**
	 * Shuts down the database manager.
	 */
	public void shutdown() {
		isRunning = false;
		failedQueries.clear();
		if (workService != null)
			workService.shutdown();
		for (final DatabaseConnection connection : pool)
			connection.close();
	}

}