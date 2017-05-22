package test.hwms.service;

import java.util.Collection;
import java.util.List;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import test.hwms.model.IUserService;
import test.hwms.model.User;
import test.hwms.model.UserNotFoundException;

public class Sql2oUserService implements IUserService {
	public static final String DEFAULT_DB_HOSTNAME = "hwmsdb";
	public static final int DEFAULT_DB_PORT = 3306;
	public static final String DEFAULT_DB_USERNAME = "hwms_owner";
	public static final String DEFAULT_DB_PASSWORD = "helloworld";
	
	private Sql2o sql2o;
	
	public Sql2oUserService() {
		this(DEFAULT_DB_HOSTNAME, DEFAULT_DB_PORT, DEFAULT_DB_USERNAME, DEFAULT_DB_PASSWORD);
	}
	
	public Sql2oUserService(String hostname, int port, String username, String password) {
		this.sql2o = new Sql2o("jdbc:mysql://" + hostname + ":" + port + "/hwms", username, password);
	}

	@Override
	public Collection<User> getAllUsers() {
		List<User> users;
		try (Connection conn = sql2o.open()) {
			users = conn.createQuery("SELECT id, name, email FROM users").executeAndFetch(User.class);
		}
		return users;
	}

	@Override
	public User getUser(int id) throws UserNotFoundException {
		try (Connection conn = sql2o.open()) {
			return getUser(conn, id);
		}
	}
	
	private static User getUser(Connection conn, int id) {
		return conn.createQuery("SELECT id, name, email FROM users WHERE id=:id")
				.addParameter("id", id)
				.executeAndFetchFirst(User.class);
	}

	@Override
	public User createUser(String name, String email) {
		try (Connection conn = sql2o.open()) {
			Integer id = conn.createQuery("INSERT INTO users(name, email) values(:name, :email)", true)
					.addParameter("name", name)
					.addParameter("email", email)
					.executeUpdate()
					.getKey(Integer.class);
			return getUser(conn, id.intValue());
		}
	}

	@Override
	public User updateUser(int id, String name, String email) throws UserNotFoundException {
		try (Connection conn = sql2o.open()) {
			int result = conn.createQuery("UPDATE users SET name=:name, email=:email WHERE id=:id")
					.addParameter("id", id)
					.addParameter("name", name)
					.addParameter("email", email)
					.executeUpdate()
					.getResult();
			System.out.println("updateUser result: " + result);
			return getUser(conn, id);
		}
	}

	@Override
	public User deleteUser(int id) throws UserNotFoundException {
		try (Connection conn = sql2o.open()) {
			User user = getUser(conn, id);
			int result = conn.createQuery("DELETE FROM users WHERE id=:id")
				.addParameter("id", id)
				.executeUpdate()
				.getResult();
			System.out.println("deleteUser result: " + result);
			return user;
		}
	}
}
