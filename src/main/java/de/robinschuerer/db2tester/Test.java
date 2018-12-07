package de.robinschuerer.db2tester;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Test {
	final static String URL_PREFIX = "jdbc:db2://";

	public static void main(String[] args) {

		if (args.length != 2) {
			System.err.println("Invalid value. First argument appended to " +
					"jdbc:db2: must specify a valid URL.");
			System.err.println("Second argument must be a valid user ID.");
			System.exit(1);
		}

		final String url = URL_PREFIX + args[0];
		final String user = args[1];

		System.out.println("Password: ");
		Scanner in = new Scanner(System.in);
		final String password = in.nextLine();
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			System.out.println("**** Loaded the JDBC driver");

			final Connection connection = DriverManager.getConnection(url, user, password);
			connection.setAutoCommit(false);
			System.out.println("**** Created a JDBC connection to the data source");

			final Statement statement = connection.createStatement();
			System.out.println("**** Created JDBC Statement object");

			statement.close();
			System.out.println("**** Closed JDBC Statement");
			connection.commit();
			connection.close();
			System.out.println("**** Disconnected from data source");
		} catch (ClassNotFoundException e) {
			System.err.println("Could not load JDBC driver");
			System.out.println("Exception: " + e);
			e.printStackTrace();
		} catch (SQLException ex) {
			System.err.println("SQLException information");
			while (ex != null) {
				System.err.println("Error msg: " + ex.getMessage());
				System.err.println("SQLSTATE: " + ex.getSQLState());
				System.err.println("Error code: " + ex.getErrorCode());
				ex.printStackTrace();
				ex = ex.getNextException(); // For drivers that support chained exceptions
			}
		}
	}
}
